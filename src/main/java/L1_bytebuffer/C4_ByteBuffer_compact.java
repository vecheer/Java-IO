package L1_bytebuffer;

import heimaUtil.ByteBufferUtil;

import java.nio.ByteBuffer;

public class C4_ByteBuffer_compact {
    public static void main(String[] args) {
        ByteBuffer buffer = ByteBuffer.allocate(7);
        buffer.put((byte)'y');
        buffer.put((byte)'x');
        buffer.put((byte)'y');
        buffer.put(new byte[]{'1','2'});

        System.out.println("buffer大小 7     放入字符 y x y 1 2");
        ByteBufferUtil.debugAll(buffer);

        System.out.println("下面切换为读模式，并读取3个字符");
        buffer.flip();
        for (int i = 1; i <= 3; i++) {
            System.out.println("第" + i + "次读取字符:  " + (char)buffer.get());
        }

        System.out.println("当前buffer: ");
        ByteBufferUtil.debugAll(buffer);

        buffer.compact();

        System.out.println("\n下面开始compact！compact之后buffer: ");
        ByteBufferUtil.debugAll(buffer);

        buffer.put((byte)'$');
        buffer.put((byte)'&');
        System.out.println("\n再放入两个字符后的buffer ");
        ByteBufferUtil.debugAll(buffer);

    }
}
