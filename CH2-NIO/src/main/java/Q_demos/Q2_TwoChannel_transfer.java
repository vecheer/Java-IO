package Q_demos;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

public class Q2_TwoChannel_transfer {
    public static void main(String[] args) {

        // 从from写到to
        // 获取from的写入流，to的输出流
        try (
                FileChannel FROM = new FileInputStream("./被读文件.txt").getChannel();
                FileChannel TO = new FileOutputStream("./被写文件.txt").getChannel();
        ) {
            long left = FROM.size(); //left size to move
            while (left > 0) {
                // 每次转移的大小
                long moved = FROM.transferTo(FROM.size()-left, left, TO);
                left = left - moved;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
