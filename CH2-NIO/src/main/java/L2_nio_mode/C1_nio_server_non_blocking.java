package L2_nio_mode;

import xheimaUtil.ByteBufferUtil;
import lombok.extern.log4j.Log4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.LinkedList;
import java.util.List;

@Log4j
public class C1_nio_server_non_blocking {

    static List<SocketChannel> clients = new LinkedList<>();
    static ByteBuffer buffer = ByteBuffer.allocateDirect(32);

    public static void main(String[] args) throws IOException {

        ServerSocketChannel server = ServerSocketChannel.open();
        server.bind(new InetSocketAddress(8080));
        server.configureBlocking(false);

        while (true) {
            SocketChannel channel = server.accept();
            if (channel!=null) {
                channel.configureBlocking(false);
                clients.add(channel);
            }
            for (SocketChannel client : clients) {
                int len = client.read(buffer);
                if (len>0){
                    buffer.flip();
                    // 6.业务处理
                    ByteBufferUtil.debugAll(buffer);
                    buffer.clear();
                }
            }
        }
    }

}
