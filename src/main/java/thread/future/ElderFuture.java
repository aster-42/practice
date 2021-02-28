package thread.future;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;
import java.util.function.Consumer;

public class ElderFuture {

    public static void main(String[] args) {
        String url = "www";
        // callback 回调式
        // thread -> consumer
        ElderFuture.downloadAsync(url, ElderFuture::render);

        // call 呼叫式, 阻塞的 api 不方便组合
        // future
        ExecutorService service = Executors.newCachedThreadPool();
        Future<File> future = service.submit(
            // new Callable<File>(){ public File call() throws Exception {} }
            () -> download(url)
        );
        try {
            File file = future.get();
            render(file);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }


    }

    /**
     * thread -> callback
     * */
    public static void downloadAsync(String url, Consumer<File> consumer) {
        new Thread(() -> {
            File file = download(url);
            consumer.accept(file);
        }).start();
    }

    /**
     * mock method
     * */
    private static File download(String url) {
        try {
            Thread.sleep(20);
            System.out.println("download-file");
        } catch (InterruptedException e) {
            return null;
        }
        return null;
    }

    /**
     * render mock method
     * */
    public static void render(File file) {
        try {
            Thread.sleep(10);
            System.out.println("render-file");
        } catch (InterruptedException ignore) { }
    }

}
