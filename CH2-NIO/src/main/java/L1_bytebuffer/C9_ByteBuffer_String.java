package L1_bytebuffer;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class C9_ByteBuffer_String {
    public static void main(String[] args) {

        /*ByteBuffer buffer = ByteBuffer.allocate(16);
        buffer.put("hello".getBytes());*/



        /*ByteBuffer buffer = StandardCharsets.UTF_8.encode("hello");*/


        ByteBuffer buffer = ByteBuffer.wrap("hello".getBytes());
        String s = StandardCharsets.UTF_8.decode(buffer).toString();


    }
}
