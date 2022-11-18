package ThreadPool.chapter3;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
* @author: Lansg
* @date: 2022/11/18 11:26
* @Description: 主方法类
*/
public class Main {
    public static void main(String[] args) {
        ExecutorService executor = Executors.newCachedThreadPool();
        List<Task> taskList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Task task = new Task("Task-" + i);
            taskList.add(task);
        }
        List<Future<Result>> resultList=null;
        Result firstResult=new Result();
        try {
            //通过firstResult变量接收第一个返回的结果
//            firstResult=executor.invokeAny(taskList);
            //使用一个resultList来接收返回的所有任务
            resultList=executor.invokeAll(taskList);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        executor.shutdown();
        System.out.println("Main: Printing the results");
//        System.out.println("The first result is: "+firstResult.getName());
        for (int i = 0; i < resultList.size(); i++) {
            Future<Result> future=resultList.get(i);
            Result result= null;
            try {
                result = future.get();
                //打印出每一个返回的结果
                System.out.println(result.getName()+":"+result.getValue());
            } catch (InterruptedException|ExecutionException e) {
                e.printStackTrace();
            }
        }
    }
}
