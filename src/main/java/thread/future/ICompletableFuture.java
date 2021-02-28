package thread.future;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ICompletableFuture {

    public static void main(String[] args) {
        String url = "completableFuture-url";
        ExecutorService service = Executors.newCachedThreadPool();

        // another future
        CompletableFuture<File> cf = CompletableFuture.supplyAsync(()-> download(url));
        // 获取数据方法1: 会丢 exception
        try {
            File getFile = cf.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        // 获取数据方法2: 会阻塞卡主
        File joinFile = cf.join();

        // 呼叫者设定 flow
        cf.thenApply((file) -> {
            System.out.println("file then apply");
            return file;
        });
        cf.thenApplyAsync((file) -> {
            System.out.println("file then apply async");
            return file;
        });
        // 做完呼叫 thread, 便于流式处理
        cf.thenAccept(ElderFuture::render);

        // future 包住 job的回传值, 可以塞 ExecutorService
        CompletableFuture.supplyAsync(() -> download(url), service);

        //
        service.submit(() -> {
            try {
                File file = download(url);
                // promise 的部分, 可以写值
                cf.complete(file);
            } catch (Exception e) {
                // 传递异常
                cf.completeExceptionally(e);
            }
        });

        // 传递异常并处理
        cf.whenComplete(((file, throwable) -> {
            if (file != null) {
                System.out.println("info");
            }else {
                System.out.println("error");
            }
        }));

        //
        CompletableFuture<File> otherFile = CompletableFuture
                .supplyAsync(()-> download(url))
                // thenCompose 可以确保返回的只有一层 completableFuture, 保证 job 可以串联
                .thenCompose(
                    (file) -> {
                        render(file);
                        return CompletableFuture.completedFuture(file);
                    }
                )
                .thenCompose(
                    (file -> {
                        System.out.println("fin");
                        return CompletableFuture.completedFuture(file);
                    })
                );
                // thenCombine

        // allOf 等待所有值回来
        String url1 = "1", url2 = "2", url3 = "3";
        CompletableFuture<File> api1 = CompletableFuture.supplyAsync(()-> download(url1));
        CompletableFuture<File> api2 = CompletableFuture.supplyAsync(()-> download(url2));
        CompletableFuture<File> api3 =  CompletableFuture.supplyAsync(()-> download(url3));
        CompletableFuture<Void> all = CompletableFuture.allOf(
                api1, api2, api3
        );
        // 但是返回值是 void, 需要自己 get
        CompletableFuture<List<File>> allOfResult = all.thenApply(
                v -> {
                    File file1 = null, file2 = null, file3 = null;
                    try {
                        file1 = api1.get();
                    } catch (InterruptedException | ExecutionException e) {
                        api1.completeExceptionally(e);
                    }

                    try {
                        file2 = api2.get();
                    } catch (InterruptedException | ExecutionException e) {
                        api2.completeExceptionally(e);
                    }

                    try {
                        file3 = api3.get();
                    } catch (InterruptedException | ExecutionException e) {
                        api3.completeExceptionally(e);
                    }

                    return Arrays.asList(file1, file2, file3);
                }
        );

        // anyOf
        // 尽量用 async 结尾的 api
        // 缺点: 不容易 debug
        // 优点: 反应式 reactive
    }

    private static File download(String url) {
        try {
            Thread.sleep(20);
            System.out.println("download-file");
        } catch (InterruptedException e) {
            return null;
        }
        return null;
    }

    public static void render(File file) {
        try {
            Thread.sleep(10);
            System.out.println("render-file");
        } catch (InterruptedException ignore) { }
    }
}
