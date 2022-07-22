package L6_ByteBuf;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import org.openjdk.jol.vm.VM;

import static io.netty.buffer.ByteBufUtil.appendPrettyHexDump;
import static io.netty.util.internal.StringUtil.NEWLINE;

public class C3_slice {
    public static void main(String[] args) {
        ByteBuf buf = ByteBufAllocator.DEFAULT.buffer();
        buf.writeBytes("12345678".getBytes());
        ByteBuf subBuf1 = buf.slice(0,5);
        ByteBuf subBuf2 = buf.slice(6,2);

        subBuf1.setByte(4,'x');
        // 可以看到，对切片后的buf进行修改，原始buf的字符也会变动
        //log(buf);

        // 不可扩容!  —— IndexOutOfBoundsException
        //subBuf1.setByte(6,'x');


        // 原buf
        log(buf);

        // release前先给subBuf retain 一下
        subBuf1.retain();

        buf.release();
        log(subBuf1);
        log(buf);

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
