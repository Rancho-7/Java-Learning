package ThreadPool.chapter2;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;

public class Main {
    public static void main(String[] args) {
        //通过Executors的newFixedThreadPool()方法创建一个ThreadPoolExecutor对象
        ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(2);
        //创建一个Future对象列表来接收返回值
        List<Future<Integer>> resultList = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < 10; i++) {
            Integer number = random.nextInt(10);
            FactorialCalculator calculator = new FactorialCalculator(number);
            Future<Integer> result = executor.submit(calculator);
            resultList.add(result);
        }
        //创建一个do循环来监控执行器状态
        do {
            System.out.printf("Main:Number of Completed Tasks: %d\n",executor.getCompletedTaskCount());
            for (int i = 0; i < resultList.size(); i++) {
                Future<Integer> result = resultList.get(i);
                //通过isDone()方法查看任务是否完成
                System.out.printf("Main:Task %d: %s\n",i,result.isDone());
                try {
                    TimeUnit.MILLISECONDS.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }while (executor.getCompletedTaskCount()<resultList.size());
        System.out.printf("Main: Results\n");
        for (int i = 0; i < resultList.size(); i++) {
            Future<Integer> result = resultList.get(i);
            Integer number = null;
            try {
                number=result.get();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //控制台打印记录每个任务的返回结果
            System.out.printf("Main: Task %d: %d\n",i,number);
        }
        executor.shutdown();
    }
}
