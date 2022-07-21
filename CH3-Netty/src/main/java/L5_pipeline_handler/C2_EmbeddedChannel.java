package L5_pipeline_handler;

import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import io.netty.channel.embedded.EmbeddedChannel;
import lombok.extern.log4j.Log4j;

@Log4j
public class C2_EmbeddedChannel {
    public static void main(String[] args) {
        ChannelInboundHandlerAdapter in_1 = new ChannelInboundHandlerAdapter(){
            @Override
            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                log.info("in_1");
                super.channelRead(ctx, msg);
            }
        };
        ChannelInboundHandlerAdapter in_2 = new ChannelInboundHandlerAdapter(){
            @Override
            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                log.info("in_2");
                super.channelRead(ctx, msg);
            }
        };
        ChannelOutboundHandlerAdapter out_1 = new ChannelOutboundHandlerAdapter(){
            @Override
            public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
                log.info("out_1");
                super.write(ctx, msg, promise);
            }
        };

        // 模拟来自客户端的channel
        EmbeddedChannel channel = new EmbeddedChannel(in_1,in_2,out_1);



        // 模拟入栈操作
        channel.writeInbound(ByteBufAllocator.DEFAULT.buffer().writeBytes("get into pipeline".getBytes()));

        // 模拟出栈操作
        channel.writeOutbound(ByteBufAllocator.DEFAULT.buffer().writeBytes("get out from pipeline".getBytes()));
    }
}
