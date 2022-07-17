package L4_multi_thread_selector;

import utils.time.Timer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;

/**
 * @author yq
 * @version 1.0
 * @date 2022/7/17 18:38
 */
public class TestClient {
    public static void main(String[] args) {
        try {
            SocketChannel channel = SocketChannel.open();
            channel.connect(new InetSocketAddress("localhost", 8080));
            channel.write(Charset.defaultCharset().encode("hello wyq"));
            channel.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
