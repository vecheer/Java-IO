package L6_ByteBuf;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import lombok.extern.log4j.Log4j;
import utils.data.Generator;

import static io.netty.buffer.ByteBufUtil.appendPrettyHexDump;
import static io.netty.util.internal.StringUtil.NEWLINE;

public class C1_ByteBuf {
    public static void main(String[] args) {
        ByteBuf buf = ByteBufAllocator.DEFAULT.buffer();
        buf.getClass();
        log(buf);

        StringBuilder builder = new StringBuilder(500);
        for (int i = 0; i < 500; i++) {
            builder.append(Generator.getRandomInt(10));
        }

        buf.writeBytes(builder.toString().getBytes());
        log(buf);


//        ByteBuf buf = ByteBufAllocator.DEFAULT.heapBuffer();
//        ByteBuf buf = ByteBufAllocator.DEFAULT.directBuffer();

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
