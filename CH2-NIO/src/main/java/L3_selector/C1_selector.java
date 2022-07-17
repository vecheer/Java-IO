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
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
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
                log.info("本次接收到的channel是: " + key.channel());
                log.info("本channel感兴趣的事件是: " + key.interestOps());
                // accept 事件
                if (key.isAcceptable()) {
                    log.info("key isAcceptable");
                    ServerSocketChannel serverChannel = (ServerSocketChannel) key.channel();
                    SocketChannel channel = serverChannel.accept();
                    // do after accepted the socket channel
                    channel.configureBlocking(false);
                    // register the channel received
                    // 为该channel指定1个附件，每个channel都有自己的附件，防止多个channel公用一个buffer引发的数据混乱
                    ByteBuffer buffer = ByteBuffer.allocate(5);
                    SelectionKey channelKey = channel.register(selector, 0, buffer);
                    channelKey.interestOps(SelectionKey.OP_READ);
                }
                // read 事件
                else if (key.isReadable()) {
                    log.info("key isReadable");
                    try{
                        SocketChannel channel = (SocketChannel) key.channel();
                        ByteBuffer buffer = (ByteBuffer) key.attachment(); // 取出该channel的buffer
                        int read = channel.read(buffer);
                        log.info("当前从channel读取到的字节数为: " + read);
                        if (read == -1){
                            key.cancel();
                        } else {
                            // handle buffer
                            List<ByteBuffer> bufferList = bufferParse(buffer, '\n');
                            log.info("本次buffer被拆分为" + bufferList.size() + "个list");
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

    // 解析收到的buffer，并按照粘包和拆包解析
    public static List<ByteBuffer> bufferParse(ByteBuffer source, char splitChar){
        final List<ByteBuffer> receivedCache = new LinkedList<>();
        // 置为读
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
        // 切回写，没读的部分不动，留到下一次继续处理
        source.compact();
        return receivedCache;
    }

}
