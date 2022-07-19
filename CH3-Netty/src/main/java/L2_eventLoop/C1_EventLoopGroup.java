package L2_eventLoop;

import io.netty.channel.EventLoop;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import lombok.extern.log4j.Log4j;
import utils.time.Timer;

import java.util.Date;
import java.util.concurrent.TimeUnit;


@Log4j
public class C1_EventLoopGroup {
    public static void main(String[] args) {

        // 新建EventLoopGroup
        EventLoopGroup group = new NioEventLoopGroup(2);

        // 获取EventLoop
        EventLoop worker = group.next();

        // 提交事件
        group.next().submit(()->{
            Timer.sleep(2000);
            log.info("ok!");
        });
        group.next().execute(()->{
            Timer.sleep(2000);
            log.info("ok!");
        });
        group.next().scheduleAtFixedRate(()->{
            log.info(new Date());
        },0,1, TimeUnit.SECONDS);

    }
}
