package utils.concurrent.pool;


import java.util.HashSet;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * @author yq
 * @version 1.0
 * @date 2022/7/10 16:58
 */
public class YqThreadPool {
    public static void main(String[] args) {
        YqThreadPool yqThreadPool = new YqThreadPool(2,3);
        for (int i = 0; i < 4; i++) {
            yqThreadPool.execute(()->{
                System.out.println("hello");
            });
        }
    }

    private volatile int coreSize;
    private volatile int maxSize;
    private long timeout = 60L;
    private TimeUnit unit = TimeUnit.SECONDS;
    private final LinkedBlockingQueue<Runnable> taskQueue = new LinkedBlockingQueue<>();

    private final HashSet<Worker> threadSet = new HashSet<>();

    private final RejectionHandler handler = new DefaultRejectionHandler();

    public YqThreadPool(int coreSize, int maxSize, long timeout, TimeUnit unit) {
        this.coreSize = coreSize;
        this.maxSize = maxSize;
        this.timeout = timeout;
        this.unit = unit;
    }

    public YqThreadPool(int coreSize, int maxSize){
        this.coreSize = coreSize;
        this.maxSize = maxSize;
    }




    public void execute(Runnable task) {
        synchronized (this) {
            if (threadSet.size() < coreSize) {
                Worker worker = new Worker(task);
                threadSet.add(worker);
                worker.start();
            } else if (threadSet.size() < maxSize) {
                Worker worker = new Worker(task, timeout, unit);
                threadSet.add(worker);
                worker.start();
            } else {
                boolean offered = taskQueue.offer(task);
                if (!offered)
                    handler.reject(taskQueue, task);
            }
        }

    }

    //=========================================================================================================
    // data structure
    //=========================================================================================================
    // never stop trying to take task to execute
    class Worker extends Thread {

        private Runnable task;

        private boolean tempThread = false;

        private long timeout;
        private TimeUnit unitUnit;

        public Worker(Runnable task) {
            this.task = task;
        }

        public Worker(Runnable task, long timeout, TimeUnit unit) {
            this.task = task;
            this.timeout = timeout;
            this.unitUnit = unit;

            this.tempThread = true;
        }

        @Override
        public void run() {
            while (task != null) {
                // exec task
                try {
                    task.run();
                } catch (Throwable e) {
                    // global exception catch
                    e.printStackTrace();
                }
                // take next task
                // 临时线程取任务的时候会等待，超时就返回null，退出循环被回收
                try {
                    if (tempThread)
                        task = taskQueue.poll(timeout, unit);
                    else
                        task = taskQueue.take();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            threadSet.remove(this);
        }
    }

    interface RejectionHandler {
        void reject(BlockingQueue<?> taskQueue, Runnable task);
    }

    static class DefaultRejectionHandler implements RejectionHandler {
        @Override
        public void reject(BlockingQueue<?> taskQueue, Runnable task) {
            throw new RuntimeException(task + "cannot be executing cuz the pool is busy");
        }
    }
}


