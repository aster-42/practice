package wheel;

import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.function.Consumer;
import java.util.function.Function;

import static java.util.Objects.isNull;

public class MultiThreadTaskExecutor<T, R> {
    private static final BlockingQueue<Runnable> BLOCKING_QUEUE = new ArrayBlockingQueue<>(1000);
    private static final ThreadPoolExecutor THREAD_POOL = new ThreadPoolExecutor(30, 100, 60L,
            TimeUnit.SECONDS, BLOCKING_QUEUE, new ThreadPoolExecutor.CallerRunsPolicy());

    private final List<T> tasks;
    private final Function<T, R> computation;
    private final Consumer<R> resultCollector;
    private final boolean useCurrentThreadForFirstTask = true;

    public static<T, R> void execute(List<T> tasks, Function<T, R> computation, Consumer<R> resultCollector){
        new MultiThreadTaskExecutor<T, R>(tasks, computation, resultCollector).doExecute();
    }

    private MultiThreadTaskExecutor(List<T> tasks, Function<T, R> computation, Consumer<R> resultCollector) {
        this.tasks = tasks;
        this.computation = computation;
        this.resultCollector = resultCollector;
    }

    void doExecute(){
        if (isEmpty(tasks) || isNull(computation) || isNull(resultCollector)) {
            return;
        }
        List<Future<R>> futures = new ArrayList<>(tasks.size());
        tasks.stream().skip(skipFirstTaskIfNecessary()).map(task -> THREAD_POOL.submit(() -> computation.apply(task))).forEach(futures::add);
        executeFirstTaskInCurrentThreadIfNecessary();
        futures.stream().map(this::getResultFromFuture).forEach(resultCollector);
    }

    boolean isEmpty(List<T> tasks){
        return isNull(tasks) || tasks.size() == 0;
    }

    void executeFirstTaskInCurrentThreadIfNecessary(){
        if (useCurrentThreadForFirstTask) {
            resultCollector.accept(computation.apply(tasks.get(0)));
        }
    }

    int skipFirstTaskIfNecessary(){
        if (useCurrentThreadForFirstTask) {
            return 1;
        }
        return 0;
    }

    R getResultFromFuture(Future<R> future){
        try {
            return future.get();
        } catch (InterruptedException e) {
            throw new RuntimeException("future task is interrupted", e);
        } catch (ExecutionException e) {
            throw new RuntimeException("future task execution error", e);
        }
    }

    public static void main(String[] args) {
        List<Integer> resultList = new ArrayList<>();
        List<Integer> numberList = Lists.newArrayList(1,2,3,4,5);
        MultiThreadTaskExecutor.execute(
                numberList,
                e -> {
                    System.out.println("invoke number:" + e);
                    return e;
                },
                resultList::add
        );
        System.out.println(resultList);
        THREAD_POOL.shutdown();
    }
}