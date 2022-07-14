package L2_nio_mode;

import lombok.extern.log4j.Log4j;
import utils.time.Timer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

@Log4j
public class C1_nio_client {
    public static void main(String[] args) throws IOException {
        new Thread(() -> {
            try {
            SocketChannel channel =SocketChannel.open();
            channel.connect(new InetSocketAddress("localhost",8090));
            channel.write(StandardCharsets.UTF_8.encode("Hello,dmd----------11111"));
            Timer.sleep(3000);
                channel.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
        new Thread(() -> {
            try {
                SocketChannel channel =SocketChannel.open();
                channel.connect(new InetSocketAddress("localhost",8090));
                channel.write(StandardCharsets.UTF_8.encode("Hello,dmd----------22222"));
                Timer.sleep(3000);
                channel.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }
}
