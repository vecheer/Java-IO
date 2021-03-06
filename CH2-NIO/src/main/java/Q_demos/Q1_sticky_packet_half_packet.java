package Q_demos;

import xheimaUtil.ByteBufferUtil;

import java.nio.ByteBuffer;
import java.util.LinkedList;
import java.util.List;

public class Q1_sticky_packet_half_packet {

    static byte[] input1 = "Hello,world\nI’m YQ\nHo".getBytes();
    static byte[] input2 = "w are you?\n".getBytes();

    public static void main(String[] args) {
        /*
        p1: Hello,world\nI’m YQ\nHo
        p2:w are you?\n
        Q：将两个ByteBuffer包还原
         */

        ByteBuffer buffer = ByteBuffer.allocate(100);

        // 接收第1个包
        buffer.put(input1);
        List<ByteBuffer> list1 = bufferParse(buffer);
        list1.forEach(ByteBufferUtil::debugAll);

        // 接收第2个包
        buffer.put(input2);
        List<ByteBuffer> list2 = bufferParse(buffer);
        list2.forEach(ByteBufferUtil::debugAll);

    }


    // 解析收到的buffer，并按照粘包和拆包解析
    public static List<ByteBuffer> bufferParse(ByteBuffer source){
        final List<ByteBuffer> receivedCache = new LinkedList<>();
        // 置为读
        source.flip();
        int mark = 0;
        for (int i = 0; i < source.limit(); i++) {
            if ((char)source.get(i) == '\n') {
                ByteBuffer temp = ByteBuffer.allocate(i-mark+1);
                for (int j = mark; j <= i; j++) {
                    // 这个地方只能先get(i)，再在这里get()，如果compact那边就没有内容可以整理了
                    temp.put(source.get());
                }
                mark = i+1; // 下次从分隔符的下一个符号开始
                temp.flip(); // 先转为读模式，方便后续直接读取
                receivedCache.add(temp);
            }
        }
        // 切回写，没读的部分不动，留到下一次继续处理
        source.compact();
        return receivedCache;
    }
}
