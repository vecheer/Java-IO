package L4_Future_Promise;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.log4j.Log4j;
import utils.concurrent.atomic.YqAtomicInt;
import utils.time.Timer;

import java.util.concurrent.*;

@Log4j
public class C1_JDK_Future {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ThreadPoolExecutor pool = new ThreadPoolExecutor(
                2, 2,
                60L, TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(),
                new ThreadFactory() {
                    private final YqAtomicInt count = new YqAtomicInt(0);
                    @Override
                    public Thread newThread(Runnable r) {
                        String name = "My-Pool-Thread-" + count.getAndIncrement();
                        return new Thread(r,name);
                    }
                }
        );

        Future<User> task1 = pool.submit(() -> {
            User jack = new User(111, "Jack");
            log.info(jack + "   was created");

            Timer.sleep(3000);

            return jack;
        });

        log.info(task1.get());
    }


    @Data
    @AllArgsConstructor
    static class User {
        private Integer uid;
        private String uname;
    }
}
