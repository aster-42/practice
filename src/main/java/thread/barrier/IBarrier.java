package thread.barrier;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;

/**
 * CyclicBarrier
 * CountdownLatch
 * */
public class IBarrier {

    /** output:
     * invoke...
     * 3 > thread sleep:135
     * 0 > thread sleep:134
     * 4 > thread sleep:418
     * 2 > thread sleep:477
     * 1 > thread sleep:931
     * all finished
     * */
    public static void cyclic() {
        int parties = 5;
        CyclicBarrier cyclicBarrier = new CyclicBarrier(parties, () -> {
            System.out.println("all finished");
        });

        for (int i = 0; i < parties; i++) {
            int finalIndex = i;
            new Thread(()->{
                try {
                    int sleep = (int) (Math.random() * 1000);
                    Thread.sleep(sleep);
                    System.out.println(finalIndex + " > thread sleep:" + sleep + "|number waiting:" + cyclicBarrier.getNumberWaiting());
                    cyclicBarrier.await();
                } catch (InterruptedException | BrokenBarrierException e) {
                    e.printStackTrace();
                }
            }).start();
        }
        System.out.println("invoke...");
    }

    /**
     * invoke...
     * 1 > thread sleep:35|count num:5
     * 3 > thread sleep:64|count num:5
     * 2 > thread sleep:384|count num:3
     * 4 > thread sleep:714|count num:2
     * 0 > thread sleep:994|count num:1
     * */
    public static void countDown() {
        int parties = 5;
        CountDownLatch countDownLatch = new CountDownLatch(parties);

        for (int i = 0; i < parties; i++) {
            int finalIndex = i;
            new Thread(()->{
                try {
                    int sleep = (int) (Math.random() * 1000);
                    Thread.sleep(sleep);
                    System.out.println(finalIndex + " > thread sleep:" + sleep + "|count num:" + countDownLatch.getCount());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    countDownLatch.countDown();
                }
            }).start();
        }
        System.out.println("invoke...");
    }

    public static void main(String[] args) {
        cyclic();
        countDown();
    }
}
