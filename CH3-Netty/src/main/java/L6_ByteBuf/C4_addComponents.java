package L6_ByteBuf;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.CompositeByteBuf;

import static io.netty.buffer.ByteBufUtil.appendPrettyHexDump;
import static io.netty.util.internal.StringUtil.NEWLINE;

public class C4_addComponents {
    public static void main(String[] args) {

        CompositeByteBuf buf1 = ByteBufAllocator.DEFAULT.compositeBuffer();
        buf1.writeBytes("12345".getBytes());


        ByteBuf buf2 = ByteBufAllocator.DEFAULT.buffer();
        buf2.writeBytes("abcedfg".getBytes());

        log(buf1);
        log(buf2);

        buf1.addComponents(true,buf2);

        System.out.print((char)buf1.getByte(6));
        System.out.print((char)buf1.getByte(7));
        System.out.print((char)buf1.getByte(8));

    }

    private static void log(ByteBuf buffer) {
        int length = buffer.readableBytes();
        int rows = length / 16 + (length % 15 == 0 ? 0 : 1) + 4;
        StringBuilder buf = new StringBuilder(rows * 80 * 2)
                .append("read index:").append(buffer.readerIndex())
                .append(" write index:").append(buffer.writerIndex())
                .append(" capacity:").append(buffer.capacity())
                .append(NEWLINE);
        appendPrettyHexDump(buf, buffer);
        System.out.println(buf.toString());
    }
}
