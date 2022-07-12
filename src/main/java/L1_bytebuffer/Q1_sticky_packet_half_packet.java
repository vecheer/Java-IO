package L1_bytebuffer;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class Q1_sticky_packet_half_packet {

    static ByteBuffer p1 = StandardCharsets.UTF_8.encode("Hello,world\nI’m YQ\nHo");
    static ByteBuffer p2 = StandardCharsets.UTF_8.encode("w are you?\n");

    public static void main(String[] args) {
        /*
        p1: Hello,world\nI’m YQ\nHo
        p2:w are you?\n
        Q：将两个ByteBuffer包还原
         */

        String sum = new StringBuilder()
                .append(StandardCharsets.UTF_8.decode(p1))
                .append(StandardCharsets.UTF_8.decode(p2))
                .toString();

        String[] strs = sum.split("\n");
        System.out.println(Arrays.toString(strs));


    }
}
