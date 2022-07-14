package L1_bytebuffer;

import xheimaUtil.ByteBufferUtil;

import java.nio.ByteBuffer;

public class C6_ByteBuffer_mark_reset {
    public static void main(String[] args) {
        ByteBuffer buffer = ByteBuffer.allocate(7);
        buffer.put((byte)'a');
        buffer.put((byte)'b');
        buffer.put((byte)'c');
        buffer.put(new byte[]{'1','2'});

        System.out.println("buffer大小 7     放入字符 a b c 1 2");
        ByteBufferUtil.debugAll(buffer);

        System.out.println("切换为读模式");
        buffer.flip();

        System.out.println("读取下一个postion: " + (char)buffer.get());
        System.out.println("读取下一个postion: " + (char)buffer.get());

        System.out.println("mark! ");
        buffer.mark();

        System.out.println("读取下一个postion: " + (char)buffer.get());
        System.out.println("读取下一个postion: " + (char)buffer.get());


        System.out.println("回到存档点:  ");
        buffer.reset();
        System.out.println("读取下一个postion: " + (char)buffer.get());
        System.out.println("读取下一个postion: " + (char)buffer.get());

    }
}
