package L3_selector.sample;

import lombok.extern.log4j.Log4j;
import utils.time.Timer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

@Log4j
public class C1_server_read {


    public static void main(String[] args) throws IOException {


        Selector mySelector = Selector.open();

        ServerSocketChannel server = ServerSocketChannel.open();
        server.bind(new InetSocketAddress(8080));
        server.configureBlocking(false);

        SelectionKey myServerKey = server.register(mySelector, /*关注的事件*/SelectionKey.OP_ACCEPT);

        while (true) {
            mySelector.select();

            Iterator<SelectionKey> iterator = mySelector.selectedKeys().iterator();
            while (iterator.hasNext()) {
                SelectionKey key = iterator.next();
                iterator.remove();

                if (key.isAcceptable()) {
                    ServerSocketChannel serverChannel = (ServerSocketChannel) key.channel();
                    SocketChannel channel = serverChannel.accept();
                    channel.configureBlocking(false);

                    ByteBuffer buffer = ByteBuffer.allocate(5);
                    SelectionKey channelKey = channel.register(mySelector, SelectionKey.OP_READ, buffer);
                }

                else if (key.isReadable()) {
                    log.info("key isReadable");
                    try{
                        SocketChannel channel = (SocketChannel) key.channel();
                        ByteBuffer buffer = (ByteBuffer) key.attachment();
                        int read = channel.read(buffer);
                        if (read == -1){
                            key.cancel();
                        } else {
                            List<ByteBuffer> bufferList = bufferParse(buffer, '\n');
                            // 读取完buffer的compact操作之后，如果position和limit相等，则说明整个包都没有遇到换行符
                            // 进一步可以判断出是包太大，buffer放不下
                            // 这种情况下就需要对buffer进行扩容
                            if (buffer.position() == buffer.limit()){
                                ByteBuffer newBuffer = ByteBuffer.allocate(buffer.capacity() * 2);
                                buffer.flip();
                                newBuffer.put(buffer);
                                key.attach(newBuffer); //使用扩容后的buffer，替换老buffer
                            }
                            bufferList.forEach(one-> log.info("本次读取的内容是: " + StandardCharsets.UTF_8.decode(one)));
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

    public static List<ByteBuffer> bufferParse(ByteBuffer source, char splitChar){
        final List<ByteBuffer> receivedCache = new LinkedList<>();
        source.flip();
        int mark = 0;
        for (int i = 0; i < source.limit(); i++) {
            if ((char)source.get(i) == splitChar) {
                ByteBuffer temp = ByteBuffer.allocate(i-mark+1);
                for (int j = mark; j <= i; j++) {
                    temp.put(source.get());
                }
                mark = i+1;
                temp.flip();
                receivedCache.add(temp);
            }
        }
        source.compact();
        return receivedCache;
    }

}
