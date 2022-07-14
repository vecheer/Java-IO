package L1_bytebuffer;

import xheimaUtil.ByteBufferUtil;

import java.nio.ByteBuffer;

public class C8_ByteBuffer_flip {
    public static void main(String[] args) {
        ByteBuffer buffer = ByteBuffer.allocate(16);
        buffer.put((byte)'a');
        buffer.put((byte)'b');
        buffer.put((byte)'c');
        buffer.put(new byte[]{'1','2'});

        System.out.println("buffer大小 16     放入字符 a b c 1 2");
        ByteBufferUtil.debugAll(buffer);

        System.out.println("切换为读模式");
        buffer.flip();

        System.out.println("读取下一个postion: " + (char)buffer.get());
        System.out.println("读取下一个postion: " + (char)buffer.get());

        System.out.println("切换为写模式 放入 x y");
        buffer.flip();
        buffer.put((byte)'x');
        buffer.put((byte)'y');
        System.out.println("此时buffer是: ");
        ByteBufferUtil.debugAll(buffer);


        System.out.println("切换为读模式");
        buffer.flip();

        System.out.println("读取下一个postion: " + (char)buffer.get());
        System.out.println("读取下一个postion: " + (char)buffer.get());

    }
}
