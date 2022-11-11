package ThreadPool.chapter1;

import sun.awt.windows.ThemeReader;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
* @author: Lansg
* @date: 2022/11/11 11:11
* @Description: 任务类
*/
public class Task implements Runnable{

    //存储任务的创建时间
    private final Date initDate;

    //存储任务的名称
    private final String name;

    public Task(String name) {
        initDate = new Date();
        this.name = name;
    }

    @Override
    public void run() {
        //打印创建时间的值
        System.out.printf("%s: Task %s Created on: %s\n",Thread.currentThread().getName(),name,initDate);
        //打印执行当前任务时的真正时间
        System.out.printf("%s: Task %s Created on: %s\n",Thread.currentThread().getName(),name,new Date());
        try {
            long duration =(long)(Math.random()*10);
            System.out.printf("%s: Task %s Doing a task during %d seconds\n",Thread.currentThread().getName(),name,duration);
            //任务休眠一段时间
            TimeUnit.SECONDS.sleep(duration);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //打印任务的完成时间
        System.out.printf("%s: Task %s Finished on: %s\n",Thread.currentThread().getName(),name,new Date());
    }
}
