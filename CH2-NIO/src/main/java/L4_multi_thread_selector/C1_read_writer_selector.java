package L4_multi_thread_selector;

import lombok.extern.log4j.Log4j;
import utils.data.Generator;
import utils.time.Timer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * @author yq
 * @version 1.0
 * @date 2022/7/17 13:36
 */
@Log4j
public class C1_read_writer_selector {




    public static void main(String[] args) throws IOException {


        ServerSocketChannel server = ServerSocketChannel.open();
        server.bind(new InetSocketAddress(8080));
        server.configureBlocking(false);
        // boss selector
        Selector boss = Selector.open();
        server.register(boss, /*关注的事件*/SelectionKey.OP_ACCEPT);

        Worker[] workers = new Worker[4];
        for (int i = 0; i < 4; i++) {
            workers[i] = new Worker(String.format("reader-writer-worker-%s",i));
        }

        while (true) {
            // 阻塞监听
            boss.select();
            // 遍历监听到的事件
            Iterator<SelectionKey> iterator = boss.selectedKeys().iterator();
            while (iterator.hasNext()) {
                SelectionKey key = iterator.next();
                iterator.remove();
                // accept 事件
                if (key.isAcceptable()) {
                    log.info("key isAcceptable");
                    ServerSocketChannel serverChannel = (ServerSocketChannel) key.channel();
                    SocketChannel channel = serverChannel.accept();
                    channel.configureBlocking(false);

                    // boss线程accept的channel交给【worker的selector】监控
                    workers[Generator.getRandomIndex(4)].register(channel,SelectionKey.OP_READ);
                    log.info("boss的工作结束! 准备继续select监听accept事件!");
                }
            }
        }
    }



    static class Worker implements Runnable {

        private Thread thread;

        private String name;

        private Selector selector;

        private boolean initiated;

        // 由于worker的selector.select()阻塞时，主线程再往worker的selector上注册监听事件时也会跟着阻塞，从而导致主线程卡住
        // 所以这里就减少耦合，将注册监听的操作，放到worker中执行
        // 使用一个任务队列来解耦
        private final ConcurrentLinkedQueue<Runnable> taskQueue = new ConcurrentLinkedQueue<Runnable>();

        public Worker(String name){
            this.name = name;
            this.thread = new Thread(this,name);
            try {
                this.selector = Selector.open();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // 启动线程
        public void register(SocketChannel channel,int Ops){
            if (!initiated){
                this.thread.start();
                initiated = true;
            }
            // time --- 1
            // 将注册事件的操作放到队列中
            // worker在select过程中，会来队列中取出操作（注册对客户端channel读写事件），从而将客户端channel，注册到自己的选择器中
            taskQueue.add(()->{
                try {
                    channel.register(selector, Ops, null);
                } catch (ClosedChannelException e) {
                    e.printStackTrace();
                }
            });
            // time --- 2
            // 如果此时worker的selector正处于select状态，但是此时又没有事件发生
            // 导致此时worker会阻塞在select处，从而导致
            selector.wakeup();
        }


        @Override
        public void run() {
            while (true) {
                try {
                    // 阻塞选择，但是一旦boss告知有新的客户端要监听，则会打断选择
                    this.selector.select();

                    // time --- 3
                    // 注册boss传来的监听事件
                    Runnable task = taskQueue.poll();
                    if (task!=null)
                        task.run();

                    Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                    while (iterator.hasNext()){
                        SelectionKey key = iterator.next();
                        iterator.remove();

                        if (key.isReadable()){
                            try{
                                SocketChannel channel = (SocketChannel) key.channel();
                                ByteBuffer buffer = ByteBuffer.allocate(16);
                                int len = channel.read(buffer);
                                if (len == -1){
                                    key.cancel();
                                } else {
                                    buffer.flip();
                                    log.info("接收到信息: " + Charset.defaultCharset().decode(buffer));
                                }
                            }catch (IOException e) {
                                log.error("error occurred while handling events!");
                                log.error(e.getMessage());
                            }

                        }

                    }


                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }
    }
}
