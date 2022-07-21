package L5_pipeline_handler;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.log4j.Log4j;
import utils.concurrent.atomic.YqAtomicInt;

import java.nio.charset.StandardCharsets;

@Log4j
public class C1_handler_data_passing {
    public static void main(String[] args) {
        new ServerBootstrap()
                .group(new NioEventLoopGroup())
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) throws Exception {
                        ChannelPipeline pipeline = ch.pipeline();
                        pipeline.addLast("h-1", new ChannelInboundHandlerAdapter() {
                            @Override
                            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                log.info("h-1 processing");
                                // 法1: 委托父类 super.channelRead(ctx, msg);
                                super.channelRead(ctx, msg);
                            }
                        });
                        pipeline.addLast("h-2", new ChannelInboundHandlerAdapter() {
                            @Override
                            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                log.info("h-2 processing");
                                // 法2: 直接调用 ctx.fireChannelRead(msg);
                                ctx.fireChannelRead(msg);
                            }
                        });
                    }
                })
                .bind(8080);
    }



    @Data
    @AllArgsConstructor
    static class Student{
        private Integer id;
        private String name;
    }
}
