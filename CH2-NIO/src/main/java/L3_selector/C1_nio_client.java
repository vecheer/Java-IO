package L3_selector;

import com.google.common.base.Charsets;
import lombok.extern.log4j.Log4j;
import utils.time.Timer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

@Log4j
public class C1_nio_client {
    public static void main(String[] args) throws IOException {
        try {
            SocketChannel channel = SocketChannel.open();

            channel.connect(new InetSocketAddress("localhost", 8080));
            channel.write(Charsets.UTF_8.encode("wyq"));
            Timer.sleep(3000);
            channel.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
