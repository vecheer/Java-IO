package L4_Future_Promise;

import io.netty.channel.EventLoop;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.log4j.Log4j;
import utils.time.Timer;

import java.util.concurrent.ExecutionException;


@Log4j
public class C2_Netty_Future {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        NioEventLoopGroup group = new NioEventLoopGroup(2);

        EventLoop eventLoop = group.next();

        Future<User> futureTask = eventLoop.submit(() -> {
            User jack = new User(111, "Jack");
            log.info(jack + "   was created");
            Timer.sleep(3000);
            return jack;
        });

        // 同步的方式
        log.info("已获取结果: " + futureTask.get());
        // 异步的方式
        futureTask.addListener(future -> log.info("回调获取结果: " + future.get()));


    }



    @Data
    @AllArgsConstructor
    static class User {
        private Integer uid;
        private String uname;
    }
}
