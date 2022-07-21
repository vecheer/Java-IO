package L4_Future_Promise;

import io.netty.channel.EventLoop;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.util.concurrent.DefaultPromise;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.log4j.Log4j;
import utils.time.Timer;

import java.util.concurrent.ExecutionException;

@Log4j
public class C3_Netty_Promise {
    public static void main(String[] args) throws ExecutionException, InterruptedException {

        EventLoop eventLoop = new NioEventLoopGroup(2).next();
        DefaultPromise<User> promise = new DefaultPromise<>(eventLoop);// 构造器需要1个EventLoop

        // 线程执行完毕后，向promise中填充结果
        new Thread(()->{
            try{
                log.info("worker running...");
                Timer.sleep(2000);
                int a = 1/0;
                // 成功时设置值
                promise.setSuccess(new User(121,"Davy"));
                log.info("worker done!");
            }catch (Exception e) {
                log.error(e.getMessage());
                // 失败时塞入异常
                promise.setFailure(e);
            }
        }).start();

        log.info("result is: " + promise.get());


    }


    @Data
    @AllArgsConstructor
    static class User {
        private Integer uid;
        private String uname;
    }
}
