package ThreadPool.chapter4;

import java.util.Date;
import java.util.concurrent.Callable;

/**
* @author: Lansg
* @date: 2022/11/18 16:58
* @Description: 任务类
*/
public class Task implements Callable<String> {

    private final String name;

    public Task(String name){
        this.name=name;
    }

    @Override
    public String call() throws Exception {
        System.out.printf("%s: Starting at : %s\n",name,new Date());
        return "hello,world";
    }
}
