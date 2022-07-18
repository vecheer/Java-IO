package L1_netty_intro;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;

public class C1_Server {
    public static void main(String[] args) {
        // 1.netty 服务器启动器
        //     负责组装netty组件，启动服务器
        new ServerBootstrap()
                // 2.BossEventLoop, WorkerEventLoop (需要包含1个selector + 1个thread)
                //     就是我们之前自定义的boss和worker，他们是selector的装饰器，并且绑定到一个线程上，可以很好地支持并发
                //     eventLoop group：就是 select + thread 的组合
                .group(new NioEventLoopGroup())
                // 3.设置服务器serverSocketchannel的实现，支持BIO、NIO
                .channel(NioServerSocketChannel.class)
                // 4. 设置通道建连后的处理器，来处理这些通道
                .childHandler(
                        // 5. 添加了一个通道初始化器
                        new ChannelInitializer<NioSocketChannel>() {
                            @Override
                            protected void initChannel(NioSocketChannel channel) throws Exception {
                                // 往初始化器中添加具体的子处理器（每个handler可以包含多个子处理器，协同处理）
                                channel.pipeline().addLast(new StringDecoder()); // 子处理器1：解码器
                                channel.pipeline().addLast(new ChannelInboundHandlerAdapter() { // 子处理器2：打印结果
                                    @Override
                                    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                        System.out.println(msg);
                                    }
                                });
                            }
                        })
                // 监听端口
                .bind(8080);
    }
}
