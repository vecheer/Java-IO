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
public class C1_server_write {


    public static void main(String[] args) throws IOException {


        Selector mySelector = Selector.open();

        ServerSocketChannel server = ServerSocketChannel.open();
        server.bind(new InetSocketAddress(8080));
        server.configureBlocking(false);
        server.register(mySelector, /*关注的事件*/SelectionKey.OP_ACCEPT);

        while (true) {
            mySelector.select();

            Iterator<SelectionKey> iterator = mySelector.selectedKeys().iterator();
            while (iterator.hasNext()) {
                SelectionKey key = iterator.next();
                iterator.remove();

                if (key.isAcceptable()) {
                    SocketChannel channel = server.accept(); // accept事件一定是serverSocket

                    // 准备写一个大buffer
                    StringBuilder builder = new StringBuilder();
                    for(int i = 0; i < 500000000; i++) {
                        builder.append("a");
                    }
                    ByteBuffer buffer = StandardCharsets.UTF_8.encode(builder.toString());
                    channel.write(buffer);

                    if (buffer.hasRemaining()){ // position < limit ---> 没写完
                        // SelectionKey.OP_WRITE + SelectionKey.OP_READ  等效于 electionKey.OP_WRITE | SelectionKey.OP_READ
                       channel.register(mySelector, SelectionKey.OP_WRITE + SelectionKey.OP_READ, buffer);
                    }
                }

                else if (key.isWritable()) {
                    try{
                        SocketChannel channel = (SocketChannel) key.channel();
                        ByteBuffer buffer = (ByteBuffer) key.attachment();

                        channel.write(buffer);

                        if (!buffer.hasRemaining()){ // 写完了
                            key.attach(null);
                            // 摘除写事件的关注
                            key.interestOps(key.interestOps() - SelectionKey.OP_WRITE);
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
