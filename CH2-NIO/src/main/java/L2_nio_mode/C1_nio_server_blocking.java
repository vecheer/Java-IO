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
public class C1_nio_server_blocking {

    static List<SocketChannel> clients = new LinkedList<>();
    static ByteBuffer buffer = ByteBuffer.allocateDirect(32);

    public static void main(String[] args) throws IOException {
        // 1.创建服务端
        ServerSocketChannel server = ServerSocketChannel.open();
        // 2.端口绑定
        server.bind(new InetSocketAddress(8080));
        while (true) {
            // 3.循环不停接收连接
            SocketChannel channel = server.accept();
            // 4.对建立的连接，进行数据接收
            channel.read(buffer);
            buffer.flip();
            // 6.业务处理
            ByteBufferUtil.debugAll(buffer);
            buffer.clear();
        }
    }

}
