package L2_BIO_threadpool;

import utils.concurrent.pool.YqThreadPool;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class C1_BIO_Server_Pool {

    static YqThreadPool pool = new YqThreadPool(3, 4);


    public static void main(String[] args) throws IOException {

        // 1.服务端启一个socket，监听9999
        ServerSocket server = new ServerSocket(9999);

        while(true){
            // 2.接收来自9999的请求
            Socket socket = server.accept();

            pool.execute(() -> {
                try {
                    // 3.读取请求流(默认字节流)
                    InputStream is = socket.getInputStream();
                    // 处理流(使用缓冲字符流)
                    BufferedReader br = new BufferedReader(new InputStreamReader(is));
                    String msgReceived;
                    while ((msgReceived = br.readLine()) != null) {
                        System.out.println("接收到请求: " + msgReceived);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }

    }
}
