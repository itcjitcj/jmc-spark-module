package org.example.juc;

import lombok.SneakyThrows;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ExecutorTest {
    public static void main(String[] args) {

    }

    //测试线程池
    public static void testThreadPool() {
      //创建线程池
        //1 创建固定大小的线程池
        ExecutorService threadPool = Executors.newFixedThreadPool(5);
        //2 创建单个线程的线程池
        ExecutorService threadPool2 = Executors.newSingleThreadExecutor();
        //3 创建可缓存的线程池
        ExecutorService threadPool3 = Executors.newCachedThreadPool();
        //4 创建固定大小的线程池，可以延迟或定时的执行任务
        ExecutorService threadPool4 = Executors.newScheduledThreadPool(5);
        //5 创建单个线程的线程池，可以延迟或定时的执行任务
        ExecutorService threadPool5 = Executors.newSingleThreadScheduledExecutor();
        //6 创建一个线程池，该线程池可以根据需要创建新线程，但在以前构造的线程可用时将重用它们
        ExecutorService threadPool6 = Executors.newWorkStealingPool();
        //7 使用threadpoolexecutor创建线程池
        // 1. corePoolSize 核心线程数
        // 2. maximumPoolSize 最大线程数
        // 3. keepAliveTime 线程空闲时间
        // 4. unit 时间单位
        // 5. workQueue 任务队列 用于存储等待执行的任务 有四种队列 1. ArrayBlockingQueue 2. LinkedBlockingQueue 3. SynchronousQueue 4. PriorityBlockingQueue 默认是LinkedBlockingQueue 无界队列 一般不会使用 一般使用ArrayBlockingQueue 有界队列 有界队列可以防止资源耗尽 也可以防止任务过多导致的OOM 也可以使用SynchronousQueue 同步队列 一般不会使用 一般使用PriorityBlockingQueue 优先级队列 优先级队列可以让优先级高的任务先执行 也可以让优先级高的任务先被取出执行 也可以使用DelayQueue 延迟队列
        // 6. threadFactory 线程工厂 用于创建线程
        // 7. handler 拒绝策略 当任务太多来不及处理时如何拒绝任务 有四种策略 1. AbortPolicy 2. CallerRunsPolicy 3. DiscardOldestPolicy 4. DiscardPolicy 默认是AbortPolicy 会抛出异常 一般不会使用 一般使用CallerRunsPolicy 会将任务回退给调用者 由调用者执行 也就是main线程执行
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(
                5, 10, 60, TimeUnit.MILLISECONDS,new java.util.concurrent.LinkedBlockingDeque<Runnable>(1000));

        //创建10个任务给线程池
        for (int i = 0; i < 10; i++) {
            threadPool.execute(new Runnable() {
                @SneakyThrows
                public void run() {
                    System.out.println(Thread.currentThread().getName() + " is running");
                    Thread.sleep(1000);
                }
            });
        }
        //关闭线程池 优雅的关闭线程池 等待线程池中的任务执行完毕
        threadPool.shutdown();
    }
}
