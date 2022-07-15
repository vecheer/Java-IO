package L3_selector;

import lombok.extern.log4j.Log4j;
import utils.time.Timer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

@Log4j
public class C1_selector2 {
    public static void main(String[] args) throws IOException {

        Selector selector = Selector.open();
        ServerSocketChannel server = ServerSocketChannel.open();
        server.bind(new InetSocketAddress(8090));
        server.configureBlocking(false);
        server.register(selector, /*关注的事件*/SelectionKey.OP_ACCEPT);
        while (true) {
            selector.select();
            Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
            while (iterator.hasNext()) {
                SelectionKey key = iterator.next();
                iterator.remove();
                if (key.isAcceptable()){
                    ServerSocketChannel serverChannel = (ServerSocketChannel) key.channel();
                    SocketChannel channel = serverChannel.accept();
                    channel.configureBlocking(false);
                    channel.register(selector, SelectionKey.OP_READ);
                }
                else if (key.isReadable()){
                    SocketChannel channel = (SocketChannel) key.channel();
                    ByteBuffer buffer = ByteBuffer.allocate(32);
                    channel.read(buffer);
                    buffer.flip();
                    buffer.clear();
                    // handle buffer
                }
            }
        }
    }
}
