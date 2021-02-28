package thread.executor;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class IExecutorService {
    public static void main(String[] args) {
        AtomicInteger versionCount = new AtomicInteger(0);
        AtomicInteger sq = new AtomicInteger(200);

        ThreadFactory factory = new ThreadFactoryBuilder().setNameFormat("damon-poop-%d").build();
        int maxPoolSize = 300;
        ExecutorService pool = new ThreadPoolExecutor(100, maxPoolSize, 0L,
                TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(1024),
                factory,
                new ThreadPoolExecutor.AbortPolicy());

        Collection<Callable<Integer>> tasks = new ArrayList<>();

        for (int i = 0; i < maxPoolSize; i++) {
            final int ret = i;
            tasks.add(() -> {
                versionCount.incrementAndGet();
                sq.decrementAndGet();
                System.out.printf("sq:%s, version:%s\n", Math.max(sq.intValue(), 0), versionCount.intValue());
                return ret;
            });
        }

        try {
            pool.invokeAll(tasks);
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        pool.shutdown();

    }
}
