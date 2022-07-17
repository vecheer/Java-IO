package L4_multi_thread_selector;

import lombok.extern.log4j.Log4j;
import utils.time.Timer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.charset.Charset;
import java.util.Iterator;

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

        Worker worker0 = new Worker("reader-writer-worker-0");
        worker0.register();

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
                    channel.register(worker0.selector,SelectionKey.OP_READ);
                }
            }
            Timer.sleep(2000);
        }
    }



    static class Worker implements Runnable {

        private Thread thread;

        private String name;

        private Selector selector;

        private boolean initiated;


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
        public void register(){
            if (!initiated){
                this.thread.start();
                initiated = true;
            }
        }


        @Override
        public void run() {
            while (true) {
                try {
                    this.selector.select();

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
                                    key.channel();
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
