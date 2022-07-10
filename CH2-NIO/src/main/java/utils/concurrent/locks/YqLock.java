package utils.concurrent.locks;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import java.util.concurrent.locks.Condition;

// 自定义锁
// 轮子
// 是真正【锁实现类】的装饰器
class YqLock {

    // =================================================
    // 内部同步器实现   extends_AQS
    // =================================================
    class MyLockImpl extends AbstractQueuedSynchronizer {
        // tryLock
        // AQS强制子类必须实现，因为AQS内部没有对此进行实现
        @Override
        protected boolean tryAcquire(int heldLockFlag) {
            // 未持有锁:0   持有锁:heldLockFlag
            if (compareAndSetState(0, heldLockFlag)) {
                setExclusiveOwnerThread(Thread.currentThread());
                return true;
            }
            return false;
        }

        // tryRelease
        // AQS强制子类必须实现，因为AQS内部没有对此进行实现
        @Override
        protected boolean tryRelease(int arg) {
            setExclusiveOwnerThread(null);
            setState(0); // 由于state字段是volatile，所以设置owner为null应在state设置之前，保证happens-before
            return true;
        }

        // 是否持有独占锁
        @Override
        protected boolean isHeldExclusively() {
            return getState()==1;
        }

        protected Condition newCondition() {
            return new ConditionObject();
        }
    }

    private MyLockImpl lockImpl = new MyLockImpl();

    // 阻塞获取锁，会进入等待队列
    public void lock() {
        lockImpl.acquire(1);
    }

    public void lockInterruptibly() throws InterruptedException {
        lockImpl.acquireInterruptibly(1);
    }

    // 如果第一时间不能获取锁，就返回false，获取成功就获得锁，并返回true
    public boolean tryLock() {
        return lockImpl.tryAcquire(1);
    }

    // 指定时间内不能获取锁，就返回false，获取成功就获得锁，并返回true
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        return lockImpl.tryAcquireNanos(1, unit.toNanos(time));
    }

    public void unlock() {
        lockImpl.release(1);
    }

    public Condition newCondition() {
        return lockImpl.newCondition();
    }
}