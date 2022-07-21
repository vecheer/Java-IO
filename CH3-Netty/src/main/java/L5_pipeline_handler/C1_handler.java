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
public class C1_handler {
    public static void main(String[] args) {
        new ServerBootstrap()
                .group(new NioEventLoopGroup())
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) throws Exception {
                        ChannelPipeline pipeline = ch.pipeline();
                        // 往pipeline中添加入站handler
                        // 1.name 处理（由客户端传入）
                        pipeline.addLast("in-1-name_assembly", new ChannelInboundHandlerAdapter() {
                            @Override
                            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                log.info("in-1-name_assembly processing");
                                String name = ((ByteBuf) msg).toString(StandardCharsets.UTF_8);
                                Student student = new Student(null, name);
                                super.channelRead(ctx, student);
                            }
                        });
                        // 2.ID 处理（由原子类自增）
                        pipeline.addLast("in-2-setting_Id", new ChannelInboundHandlerAdapter() {
                            private final YqAtomicInt count =  new YqAtomicInt(0);

                            @Override
                            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                log.info("in-2-setting_Id processing");
                                Student student = (Student) msg;
                                student.setId(count.getAndIncrement());
                                super.channelRead(ctx, msg);
                            }
                        });
                        // 3.打印信息
                        pipeline.addLast("in-3-print_info", new ChannelInboundHandlerAdapter() {
                            @Override
                            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                log.info("in-3-print_info processing");
                                Student student = (Student) msg;
                                log.info("得到的结果是: " + msg);
                                ch.writeAndFlush(msg);
                            }
                        });
                        // 往pipeline中添加出站handler
                        pipeline.addLast("out-1",new ChannelOutboundHandlerAdapter() {
                            @Override
                            public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
                               log.info("out-1");
                                super.write(ctx, msg, promise);
                            }
                        });
                        pipeline.addLast("out-2",new ChannelOutboundHandlerAdapter() {
                            @Override
                            public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
                                log.info("out-2");
                                super.write(ctx, msg, promise);
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
