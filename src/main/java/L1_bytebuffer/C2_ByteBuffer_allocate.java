package L1_bytebuffer;

import java.nio.ByteBuffer;

public class C2_ByteBuffer_allocate {
    public static void main(String[] args) {


        System.out.println(    ByteBuffer.allocate(1024)          );
        System.out.println(    ByteBuffer.allocateDirect(1024)    );


    }
}
