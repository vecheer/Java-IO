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

/**
 *   // 方式1 同步等待 channel future 准备完毕
 *         channelFuture.sync();
 *
 *         Channel channel = channelFuture.channel();
 *         channel.writeAndFlush("hello channel future test");
 */
@Log4j
public class C1_ChannelFuture {
    public static void main(String[] args) throws InterruptedException {

        ChannelFuture channelFuture = new Bootstrap()
                .group(new NioEventLoopGroup())
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) {
                        ch.pipeline().addLast(new StringEncoder());
                    }
                })
                .connect(new InetSocketAddress("localhost", 8080));


        channelFuture.addListener((ChannelFutureListener) future -> {
            Channel channel = future.channel();
            log.info("call back now!");
            channel.writeAndFlush("hello channel future test listener");
        });


    }
}
