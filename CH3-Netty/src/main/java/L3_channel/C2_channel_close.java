package L3_channel;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringEncoder;
import lombok.extern.log4j.Log4j;

import java.net.InetSocketAddress;

/*
// 同步
        channel.close();

        ChannelFuture closedFuture = channel.closeFuture();
        closedFuture.sync();
        tidyUp();
*/
@Log4j
public class C2_channel_close {
    public static void main(String[] args) throws InterruptedException {

        NioEventLoopGroup eventLoopGroup = new NioEventLoopGroup();
        ChannelFuture channelFuture = new Bootstrap()
                .group(eventLoopGroup)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) {
                        ch.pipeline().addLast(new StringEncoder());
                    }
                })
                .connect(new InetSocketAddress("localhost", 8080));

        channelFuture.sync();
        Channel channel = channelFuture.channel();

        channel.closeFuture().addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) {
                eventLoopGroup.shutdownGracefully();
                tidyUp();
            }
        });






    }

    public static void tidyUp(){
        log.info("正在执行close后的善后工作...");
    }
}
