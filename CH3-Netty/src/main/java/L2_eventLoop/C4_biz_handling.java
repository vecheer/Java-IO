package L2_eventLoop;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.log4j.Log4j;

import java.nio.charset.StandardCharsets;

@Log4j
public class C4_biz_handling {
    public static void main(String[] args) {

        EventLoopGroup bizGroup = new DefaultEventLoopGroup();
        new ServerBootstrap()
                // 使用两个EventLoopGroup，分别代表boss和worker
                .group(/*boss*/new NioEventLoopGroup(),/*worker*/new NioEventLoopGroup(2))
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) {
                        ch.pipeline()
                                .addLast("IO_handler", new ChannelInboundHandlerAdapter() {
                                    @Override
                                    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                        ByteBuf buf = (ByteBuf) msg;
                                        log.info(buf.toString(StandardCharsets.UTF_8));
                                        ctx.fireChannelRead(msg);
                                    }
                                })
                                .addLast(bizGroup,"biz_handler",new ChannelInboundHandlerAdapter() {
                                    @Override
                                    public void channelRead(ChannelHandlerContext ctx, Object msg)  {
                                        log.info("handling");
                                    }
                                });
                    }
                })
                .bind(8080);
    }
}
