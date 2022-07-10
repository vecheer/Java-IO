package L1_BIO_simple;

import utils.time.Timer;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * @author yq
 * @version 1.0
 * @date 2022/7/10 15:41
 */
public class C1_BIO_Client {
    public static void main(String[] args) throws IOException {

        // 建联
        Socket socket = new Socket("localhost",9999);

        // 获取流
        OutputStream os = socket.getOutputStream();
        // 使用输出字符流来处理
        PrintWriter ps = new PrintWriter(os);
        // 写内容
        ps.println("hello, I am Client");
        ps.println("How are you");
        // 发射内容
        ps.flush();

        Timer.sleepForever();
        socket.close();
    }
}
