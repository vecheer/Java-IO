package L1_bytebuffer;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class C1_ByteBuffer {
    public static void main(String[] args) {

        // 以通道的形式读取文件
        try (FileChannel channel = new FileInputStream("a.txt").getChannel()) {
            // 准备缓冲区
            ByteBuffer buffer = ByteBuffer.allocate(3);

            while (true) {

                // 读取channel数据，写入buffer
                int len = channel.read(buffer); // len表示读取字节数
                if (len == -1) // -1表示读取完毕，没有剩余
                    break;

                buffer.flip(); // 切换buffer的模式到读取模式
                // 开始读取
                while (buffer.hasRemaining()) {
                    byte b = buffer.get();
                    System.out.print((char) b);
                }
                buffer.clear(); // 切换buffer的模式到写取模式
            }


        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
