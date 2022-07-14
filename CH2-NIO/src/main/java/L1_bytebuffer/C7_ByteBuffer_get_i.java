package L1_bytebuffer;

import xheimaUtil.ByteBufferUtil;

import java.nio.ByteBuffer;

public class C7_ByteBuffer_get_i {
    public static void main(String[] args) {
        ByteBuffer buffer = ByteBuffer.allocate(7);
        buffer.put((byte)'a');
        buffer.put((byte)'b');
        buffer.put((byte)'c');
        buffer.put(new byte[]{'1','2'});

        System.out.println("buffer大小 7     放入字符 a b c 1 2");
        ByteBufferUtil.debugAll(buffer);



        System.out.println("读取 postion 2: " + (char)buffer.get(2));

    }
}
