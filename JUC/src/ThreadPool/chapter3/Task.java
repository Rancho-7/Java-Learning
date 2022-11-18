package ThreadPool.chapter3;

import javax.sql.rowset.CachedRowSet;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

/**
* @author: Lansg
* @date: 2022/11/18 11:12
* @Description: 任务类
*/
public class Task implements Callable<Result> {

    private final String name;

    public Task(String name){
        this.name=name;
    }

    @Override
    public Result call() throws Exception {
        System.out.printf("%s: Starting\n",this.name);
        long duration=(long) (Math.random()*10);
        System.out.printf("%s: Waiting %d seconds for results.\n",this.name,duration);
        TimeUnit.SECONDS.sleep(duration);
        //计算5个随机数字之和
        int value=0;
        for (int i=0; i<5; i++){
            value+=(int) (Math.random()*100);
        }
        Result result = new Result();
        result.setName(this.name);
        result.setValue(value);
        System.out.println(this.name+":Ends");
        return result;
    }
}
