package org.example.juc;

import lombok.SneakyThrows;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicStampedReference;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.atomic.AtomicInteger;

public class LockTest {

    //测试自定义锁
    public static void testMyLock() {
        Mylock mylock = new Mylock();
        //创建10个线程
        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                //获取锁
                mylock.lock();
                try {
                    System.out.println(Thread.currentThread().getName() + " is running");
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    //释放锁
                    mylock.unlock();
                }
            }).start();
        }
    }

    //测试可重入锁
    public static void testReentrantLock() {
        ReentrantLock reentrantLock = new ReentrantLock();
        //创建10个线程
        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                //获取锁
                reentrantLock.lock();
                try {
                    System.out.println(Thread.currentThread().getName() + " is running");
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    //释放锁
                    reentrantLock.unlock();
                }
            }).start();
        }
    }

    //测试synchronized
    public static void testSynchronized() {
        //创建10个线程
        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                //获取锁
                synchronized (LockTest.class) {
                    try {
                        System.out.println(Thread.currentThread().getName() + " is running");
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }

    //测试死锁
    public static void testDeadLock() {
        Object lock1 = new Object();
        Object lock2 = new Object();
        //创建线程1
        new Thread(() -> {
            synchronized (lock1) {
                try {
                    System.out.println(Thread.currentThread().getName() + " is running");
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                synchronized (lock2) {
                    System.out.println(Thread.currentThread().getName() + " is running");
                }
            }
        }).start();
        //创建线程2
        new Thread(() -> {
            synchronized (lock2) {
                try {
                    System.out.println(Thread.currentThread().getName() + " is running");
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                synchronized (lock1) {
                    System.out.println(Thread.currentThread().getName() + " is running");
                }
            }
        }).start();
    }

    //测试读写可重入锁
    public static void testReadWriteLock() {
        ReentrantReadWriteLock readWriteLockDemo = new ReentrantReadWriteLock();
        //创建list用于读写测试
        ArrayList<Integer> list = new ArrayList<>();
        //创建10个线程
        for (int i = 0; i < 10; i++) {
            //创建读线程
            new Thread(() -> {
                readWriteLockDemo.readLock().lock();
                try {
                    Thread.sleep(1000);
                    System.out.println(Thread.currentThread().getName() + " is running "+list);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    readWriteLockDemo.readLock().unlock();
                }
            }).start();
            //创建写线程
            new Thread(() -> {
                readWriteLockDemo.writeLock().lock();
                try {
                    Thread.sleep(1000);
                    list.add(1);
                    System.out.println(Thread.currentThread().getName() + " is running "+list);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    readWriteLockDemo.writeLock().unlock();
                }
            }).start();
        }
    }

    //测试cas
    private static final int THREAD_NUM = 10;
    private static AtomicInteger counter = new AtomicInteger(0);

    /**
     * 在上面的代码中，使用AtomicInteger类来实现一个线程安全的计数器。在主线程中创建了10个子线程，每一个线程都会不断地尝试获取counter的当前值，然后将其加1并通过CAS操作来尝试更新它的值。如果CAS操作成功，打印输出当前线程的名称和更新后的计数值，并退出循环；否则说明CAS操作失败，需要继续循环尝试操作，直到成功。
     * 从输出结果可以看出，10个线程在竞争计数器资源时能够实现正确的计数，最终计数器的值为10。而且，由于CAS操作是一条原子指令，因此多线程环境下仍然可以保持线程安全性，避免了传统的加锁操作可能带来的性能与可伸缩性问题。
     */
    @SneakyThrows
    public static void testCas() {
        Thread[] threads = new Thread[THREAD_NUM];
        for (int i = 0; i < THREAD_NUM; i++) {
            threads[i] = new Thread(() -> {
                while (true) {
                    int oldValue = counter.get();
                    int newValue = oldValue + 1;
                    if (counter.compareAndSet(oldValue, newValue)) {
                        System.out.println(Thread.currentThread().getName() + " : " + counter.get());
                        break;
                    }
                }
            });
            threads[i].start();
        }
        for (int i = 0; i < THREAD_NUM; i++) {
            threads[i].join();
        }
        System.out.println("Final count: " + counter.get());
    }

    /**
     * ABA问题是使用CAS操作时可能会遇到的一种情况，它指的是一个变量在CAS操作时被修改了两次，中间还有其他线程修改过。简单来说，就是在某个线程A执行CAS操作时，先把变量的值改为B，然后又改回A，但是在这之间有其他线程B修改过该变量的值。
     *
     * 为了解决ABA问题，Java的java.util.concurrent包中提供了一个AtomicStampedReference类，这个类是针对ABA问题的解决方案之一。这个类不仅包括原子变量的引用，还有一个时间戳的整数值，这个时间戳可以用来记录该变量被修改过的次数，避免了ABA问题的发生。
     *
     * 在上面的代码中，线程A会尝试对变量进行ABA操作，线程B在线程A之前修改了这个值，导致线程A进行ABA操作时发生了问题。我们创建了一个AtomicStampedReference类型的实例asr来包含一个整数变量的引用和一个整数时间戳。
     *
     * 当线程A执行CAS操作时，首先调用 asr.getStamp() 获取当前版本号，再获取当前值 asr.getReference()，然后依次将该值修改为1，再修改回0。这个过程中，版本号随着修改依次递增，asr.compareAndSet() 方法中的两个版本号需要一一对应才能保证CAS操作成功。而线程B在线程A执行之前修改了这个值，导致线程A进行ABA操作时发生了问题。这时线程A中第二个CAS操作就会失败，因为版本号已经与之前获取的不同了。
     *
     * 从输出结果可以看出，线程A进行了ABA操作，但是由于版本号被线程B修改过，导致第二个CAS操作失败，变量的值最终保持为1。而线程B成功修改了变量的值，并将版本号恢复为0。这样，通过使用AtomicStampedReference类，我们就能够解决ABA问题并保证原子性。
     */
    public static void casAba(){
        // 初始化时设置初始值和版本号
        Integer initialRef = 0, initialStamp = 0;
        AtomicStampedReference<Integer> asr = new AtomicStampedReference<>(initialRef, initialStamp);

        // 线程A不停尝试将值由0更改为1，然后再更改回0
        new Thread(() -> {
            int expectedStamp = asr.getStamp();
            Integer oldReference = asr.getReference();
            Integer newReference1 = 1;
            Integer newReference2 = 0;
            boolean c1 = asr.compareAndSet(oldReference, newReference1, expectedStamp, expectedStamp + 1);
            System.out.println(Thread.currentThread().getName() + " : c1 = " + c1 + ", reference = " + asr.getReference() + ", stamp = " + asr.getStamp());
            boolean c2 = asr.compareAndSet(newReference1, newReference2, expectedStamp + 1, expectedStamp + 2);
            System.out.println(Thread.currentThread().getName() + " : c2 = " + c2 + ", reference = " + asr.getReference() + ", stamp = " + asr.getStamp());
        }, "Thread-A").start();

        // 线程B在线程A执行之前修改了这个值
        new Thread(() -> {
            try {
                Thread.sleep(100); // 等待线程A执行完
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Integer initialRef1 = 2, initialStamp1 = 0;
            boolean c3 = asr.compareAndSet(initialRef, initialRef1, initialStamp, initialStamp1);
            System.out.println(Thread.currentThread().getName() + " : c3 = " + c3 + ", reference = " + asr.getReference() + ", stamp = " + asr.getStamp());
        }, "Thread-B").start();
    }

    public static void main(String[] args) {
        casAba();
    }
}
