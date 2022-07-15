package L3_selector;

import com.google.common.base.Charsets;
import lombok.extern.log4j.Log4j;
import utils.time.Timer;
import xheimaUtil.ByteBufferUtil;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Set;

@Log4j
public class C1_selector {
    public static void main(String[] args) throws IOException {


        Selector selector = Selector.open();

        ServerSocketChannel server = ServerSocketChannel.open();
        server.bind(new InetSocketAddress(8080));
        server.configureBlocking(false);


        // 将server注册到selector上（由selector来管理我们的server下的客户端连接）
        // 返回一个selectionKey，可以用此来感知哪个客户端有事件
        SelectionKey myServerKey = server.register(selector, /*关注的事件*/0, null);
        // myServerKey(server channel)关注的事件 —— accept事件
        myServerKey.interestOps(SelectionKey.OP_ACCEPT);

        while (true) {
            // 阻塞监听
            selector.select();
            // 遍历监听到的事件
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = selectionKeys.iterator();
            while (iterator.hasNext()) {
                SelectionKey key = iterator.next();
                iterator.remove();
                System.out.println(key.channel());
                System.out.println(key.interestOps());
                // accept 事件
                if (key.isAcceptable()) {
                    log.info("key isAcceptable");
                    ServerSocketChannel serverChannel = (ServerSocketChannel) key.channel();
                    SocketChannel channel = serverChannel.accept();
                    // do after accepted the socket channel
                    channel.configureBlocking(false);
                    // register the channel received
                    SelectionKey channelKey = channel.register(selector, 0, null);
                    channelKey.interestOps(SelectionKey.OP_READ);
                }
                // read 事件
                else if (key.isReadable()) {
                    log.info("key isReadable");
                    try{
                        SocketChannel channel = (SocketChannel) key.channel();
                        ByteBuffer buffer = ByteBuffer.allocate(5);
                        int read = channel.read(buffer);
                        if (read == -1){
                            key.cancel();
                        } else {
                            // handle buffer
                            System.out.println(Charsets.UTF_8.decode(buffer));
                        }
                    }catch (IOException e) {
                        log.error(e);
                        key.cancel();
                    }

                }
            }
            Timer.sleep(2000);
        }
    }
}
