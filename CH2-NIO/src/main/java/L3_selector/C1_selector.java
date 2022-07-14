package L3_selector;

import lombok.extern.log4j.Log4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

@Log4j
public class C1_selector {
    public static void main(String[] args) throws IOException {


        Selector selector = Selector.open();

        ServerSocketChannel server = ServerSocketChannel.open();
        server.bind(new InetSocketAddress(8090));
        server.configureBlocking(false);


        // 将server注册到selector上（由selector来管理我们的server下的客户端连接）
        // 返回一个selectionKey，可以用此来感知哪个客户端有事件
        SelectionKey myServerKey = server.register(selector, /*关注的事件*/0, null);

        // myServerKey(server channel)关注的事件 —— accept事件
        myServerKey.interestOps(SelectionKey.OP_ACCEPT);
        System.err.println(myServerKey);

        while (true) {
            // 阻塞监听
            selector.select();

            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = selectionKeys.iterator();
            while (iterator.hasNext()) {
                SelectionKey key = iterator.next();

                if (key.isAcceptable()){
                    ServerSocketChannel channel = (ServerSocketChannel) key.channel();
                    SocketChannel accept = channel.accept();
                    // do after accepted
                }
            }

        }


        while (true) {
            // 监听事件
            selector.select();  //没有任何事件发生，则会在此等待（阻塞，防止一直空转）
            // 监听到的事件
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            // 处理监听到的事件
            Iterator<SelectionKey> iterator = selectionKeys.iterator();
            while (iterator.hasNext()) {
                SelectionKey key = iterator.next();
                System.err.println(key);
                // 是accept事件
                if (key.isAcceptable()){
                    ServerSocketChannel channel = (ServerSocketChannel) key.channel();
                    SocketChannel accept = channel.accept();
                    log.info(accept);
                }
            }

        }


    }
}
