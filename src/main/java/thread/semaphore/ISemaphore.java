package thread.semaphore;

import java.util.Random;
import java.util.concurrent.Semaphore;

/**
 * 信号量 Semaphore
 *
 * acquire()
 * 获取一个令牌，在获取到令牌、或者被其他线程调用中断之前线程一直处于阻塞状态
 * acquire(int permits)
 * 获取一个令牌，在获取到令牌、或者被其他线程调用中断、或超时之前线程一直处于阻塞状态
 * acquireUninterruptibly()
 * 获取一个令牌，在获取到令牌之前线程一直处于阻塞状态（忽略中断）
 * tryAcquire()
 * 尝试获得令牌，返回获取令牌成功或失败，不阻塞线程
 * tryAcquire(long timeout, TimeUnit unit)
 * 尝试获得令牌，在超时时间内循环尝试获取，直到尝试获取成功或超时返回，不阻塞线程
 * release()
 * 释放一个令牌，唤醒一个获取令牌不成功的阻塞线程
 * hasQueuedThreads()
 * 等待队列里是否还存在等待线程
 * getQueueLength()
 * 获取等待队列里阻塞的线程数
 * drainPermits()
 * 清空令牌把可用令牌数置为0，返回清空令牌的数量
 * availablePermits()
 * 返回可用的令牌数量
 * */
public class ISemaphore {

    public static void main(String[] args) {
        // 停车场只有 10 个位置
        Semaphore semaphore = new Semaphore(10);
        for (int i = 0; i < 100; i++) {
            Thread thread = new Thread(() -> {
                try {
                    System.out.println("===="+Thread.currentThread().getName()+"来到停车场");
                    if(semaphore.availablePermits()==0){
                        System.out.println("车位不足，请耐心等待");
                    }
                    //获取令牌尝试进入停车场
                    semaphore.acquire();
                    System.out.println(Thread.currentThread().getName()+"成功进入停车场");
                    //模拟车辆在停车场停留的时间
                    long time = new Random().nextInt(10000);
                    Thread.sleep(time);
                    System.out.println(Thread.currentThread().getName()+"驶出停车场" + ", 停车时长:" + time);
                    // 释放令牌, 腾出停车场车位
                    semaphore.release();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            },i + "号车");
            thread.start();
        }
    }
}
