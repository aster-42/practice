package thread.lock;

import lombok.Synchronized;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 可重入锁
 * lock() 自旋等锁
 * tryLock() 获取锁
 *
 * 比 synchronized 粒度更细, synchronized 是抢占式的非公平锁
 * */
public class IReentrantLock {
    public static void main(String[] args) {
        ExecutorService threadPool = Executors.newCachedThreadPool();
        // 公平锁, 获取资源的顺序是按照申请锁的先后顺序的
        ReentrantLock fairLock = new ReentrantLock(true);
        // 非公平锁
        ReentrantLock unFairLock = new ReentrantLock();
        for (int i = 0; i < 10; i++) {
            threadPool.submit(new TestThread(fairLock, i, " fairLock"));
//            threadPool.submit(new TestThread(unFairLock, i, "unFairLock"));
        }
    }

    static class TestThread implements Runnable {
        Lock lock;
        int index;
        String tag;

        public TestThread(Lock lock, int index, String tag) {
            this.lock = lock;
            this.index = index;
            this.tag = tag;
        }
        @Override
        public void run() {
            System.out.println(Thread.currentThread().getId() + " Thread Start " + tag);
            invoke();
        }
        private void invoke() {
            lock.lock();
            try {
                long time = new Random().nextInt(100);
                Thread.sleep(time);
                System.out.println(
                    Thread.currentThread().getId()
                        + " Thread get Lock --->" + tag
                        + " index:" + index
                        + " time:" + time
                );
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        }

    }
}
