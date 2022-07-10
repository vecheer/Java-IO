package L1_BIO_simple;

import utils.time.Timer;

import java.io.*;
import java.net.Socket;

/**
 * @author yq
 * @version 1.0
 * @date 2022/7/10 15:41
 */
public class C2_BIO_Endpoint_A {
    public static void main(String[] args) throws IOException {

        // 建联
        Socket socket = new Socket("localhost",9999);

        // 获取流
        OutputStream os = socket.getOutputStream();
        // 使用输出字符流来处理
        PrintWriter ps = new PrintWriter(os);
        // 写内容
        ps.println("hello, I am A");
        ps.println("How are you");
        // 发射内容
        ps.flush();


        // 获取B的响应
        InputStream is = socket.getInputStream();
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String msgReceived;
        while ((msgReceived= br.readLine())!=null){
            System.out.println("接收到B: " + msgReceived);
        }


        Timer.sleepForever();
        socket.close();
    }
}
