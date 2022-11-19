package ThreadPool.chapter4;

import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
* @author: Lansg
* @date: 2022/11/18 17:01
* @Description: 主类
*/
public class Main {
    public static void main(String[] args) {
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        System.out.printf("Main: Starting at: %s\n",new Date());
        //初始化并启动5个任务
        for (int i = 0; i < 5; i++) {
            Task task = new Task("Task "+i);
//            executor.schedule(task,i+1, TimeUnit.SECONDS);
            executor.submit(task);
        }
        executor.shutdown();
        try {
            //等待全部任务结束
            executor.awaitTermination(1,TimeUnit.DAYS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //打印结束信息
        System.out.printf("Main: Ends at: %s\n",new Date());
    }
}
