package ThreadPool.chapter1;

import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
* @author: Lansg
* @date: 2022/11/11 16:09
* @Description: 线程池类，通过一个ThreadPoolExecutor类型的实例对象来执行任务
*/
public class Server {

    private final ThreadPoolExecutor executor;

    public Server(){
        executor = new ThreadPoolExecutor(
                5,//  int corePoolSize  核心线程数量为5
                10,//  int maximumPoolSize  最大线程数量为10
                3,// long keepAliveTime  超时3秒未调用就会释放关闭
                TimeUnit.SECONDS,//  TimeUnit unit  超时单位
                new LinkedBlockingDeque<>(4),//  任务等待队列
                new RejectedTaskController()//  传入自定义的拒绝策略
        );
    }

    //执行任务方法
    public void executeTask(Task task){
        System.out.printf("Server: A new task has arrived\n");

        executor.execute(task);

        //获取线程池的线程数量
        System.out.printf("Server: Pool Size: %d\n",executor.getPoolSize());
        //获取活动的线程数
        System.out.printf("Server: Active Count: %d\n",executor.getActiveCount());
        //获取线程池需要执行的任务数量
        System.out.printf("Server: Task Count: %d\n",executor.getTaskCount());
        //获取线程池已完成的任务数量
        System.out.printf("Server: Completed Tasks: %d\n",executor.getCompletedTaskCount());
    }

    public void endServer(){
        executor.shutdown();
    }
}
