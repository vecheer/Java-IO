package L1_BIO_simple;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class C2_BIO_Endpoint_B {
    public static void main(String[] args) throws IOException {

        // 1.服务端启一个socket，监听9999
        ServerSocket server = new ServerSocket(9999);
        // 2.接收来自9999的请求
        Socket socket = server.accept();
        // 3.读取请求流(默认字节流)
        InputStream is = socket.getInputStream();

        // 处理流(使用缓冲字符流)
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String msgReceived;
        while ((msgReceived= br.readLine())!=null){
            System.out.println("接收到A: " + msgReceived);

            OutputStream os = socket.getOutputStream();
            PrintWriter ps = new PrintWriter(os);
            ps.println("hello, I am B, I got your msg");
            ps.flush();
        }




        server.close();
    }
}
