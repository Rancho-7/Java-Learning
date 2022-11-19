package ThreadPool.chapter5;

import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) {
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        //打印记录当前时间
        System.out.printf("Main: Starting at: %s\n",new Date());
        Task task = new Task("Task");
        //提交一个任务给执行器，并可以从返回的对象中获取任务的执行状态
        ScheduledFuture<?> result = executor.scheduleAtFixedRate(task, 1, 2, TimeUnit.SECONDS);
        for (int i = 0; i < 10; i++) {
            //打印至下次任务的剩余时间，通过getDelay()方法获取下次任务的时间
            System.out.printf("Main: Delay: %d\n", result.getDelay(TimeUnit.MILLISECONDS));
            try {
                TimeUnit.MILLISECONDS.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        executor.shutdown();
        try {
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.printf("Main: Finished at: %s\n",new Date());
    }
}
