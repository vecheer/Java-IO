package L2_eventLoop;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.log4j.Log4j;

import java.nio.charset.StandardCharsets;

@Log4j
public class C3_boss_and_worker {
    public static void main(String[] args) {

        new ServerBootstrap()
                // 使用两个EventLoopGroup，分别代表boss和worker
                .group(/*boss*/new NioEventLoopGroup(),/*worker*/new NioEventLoopGroup())
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch)  {
                        ch.pipeline().addLast(new ChannelInboundHandlerAdapter(){
                            // 没有使用decoder的情况下，传来的msg就是netty的ByteBuf
                            @Override
                            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                ByteBuf buf = (ByteBuf) msg;
                                log.info(buf.toString(StandardCharsets.UTF_8));
                            }
                        });
                    }
                })
                .bind(8080);
    }
}
