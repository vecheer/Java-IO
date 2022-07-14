package L1_bytebuffer;

import xheimaUtil.ByteBufferUtil;

import java.nio.ByteBuffer;

public class C5_ByteBuffer_rewind {
    public static void main(String[] args) {
        ByteBuffer buffer = ByteBuffer.allocate(7);
        buffer.put((byte)'y');
        buffer.put((byte)'x');
        buffer.put((byte)'y');
        buffer.put(new byte[]{'1','2'});

        System.out.println("buffer大小 7     放入字符 y x y 1 2");
        ByteBufferUtil.debugAll(buffer);

        System.out.println("切换为读模式");
        buffer.flip();

        System.out.println("读取下一个postion: " + (char)buffer.get());
        System.out.println("读取下一个postion: " + (char)buffer.get());

        System.out.println("读完两个字符后, buffer现状: ");
        ByteBufferUtil.debugAll(buffer);

        System.out.println("rewind后, buffer现状: ");
        buffer.rewind();
        ByteBufferUtil.debugAll(buffer);
        System.out.println("读取下一个postion: " + (char)buffer.get());
        System.out.println("读取下一个postion: " + (char)buffer.get());

    }
}
