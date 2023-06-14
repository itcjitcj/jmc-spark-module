package org.example.juc;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;

public class Mylock {
    // AQS的子类，用于实现锁的获取和释放逻辑
    private static class Sync extends AbstractQueuedSynchronizer {

        // 是否被当前线程占用
        protected boolean isHeldExclusively() {
            return getState() == 1;
        }

        // 计数器为0时获取锁
        public boolean tryAcquire(int acquires) {
            if (compareAndSetState(0, 1)) {
                setExclusiveOwnerThread(Thread.currentThread());
                return true;
            }
            return false;
        }

        // 释放锁，设置状态值为0
        protected boolean tryRelease(int releases) {
            if (getState() == 0) throw new IllegalMonitorStateException();
            setExclusiveOwnerThread(null);
            setState(0);
            return true;
        }
    }

    // AQS同步器
    private final Sync sync = new Sync();

    // 获取锁
    public void lock() {
        sync.acquire(1);
    }

    // 释放锁
    public void unlock() {
        sync.release(1);
    }
}
