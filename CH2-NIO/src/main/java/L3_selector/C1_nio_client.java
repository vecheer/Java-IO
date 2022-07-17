package L3_selector;

import lombok.extern.log4j.Log4j;
import utils.time.Timer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;

@Log4j
public class C1_nio_client {
    public static void main(String[] args) throws IOException {
        try {
            SocketChannel channel = SocketChannel.open();

            channel.connect(new InetSocketAddress("localhost", 8080));
            //channel.write(Charset.defaultCharset().encode("我准备发信息了\n这是wuyuqi吴宇奇发的消息"));
            channel.write(Charset.defaultCharset().encode("abc4abc8abc12abc16abc\n这是wuyuqi吴宇奇发的消息\n")); // 结尾得加一个\n不然服务端以为是半包
            //channel.write(Charset.defaultCharset().encode("abc\n1233\n"));
            Timer.sleepForever();
            channel.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
