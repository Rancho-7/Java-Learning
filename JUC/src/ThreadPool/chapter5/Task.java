package ThreadPool.chapter5;

import java.util.Date;

public class Task implements Runnable{

    private final String name;

    public Task(String name){
        this.name=name;
    }

    @Override
    public void run() {
        //输出当前时间来确认任务是否按照给定周期进行调度
        System.out.printf("%s: Executed at: %s\n",name,new Date());
    }
}
