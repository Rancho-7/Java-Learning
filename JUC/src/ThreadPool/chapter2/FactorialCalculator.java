package ThreadPool.chapter2;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

/**
* @author: Lansg
* @date: 2022/11/16 15:44
* @Description:任务类
*/
public class FactorialCalculator implements Callable<Integer> {

    private final Integer number;

    public FactorialCalculator(Integer number){
        this.number=number;
    }

    @Override
    public Integer call() throws Exception {
        int result = 1;
        if ((number==0)|(number==1)){
            result=1;
        }else{
            for (int i=2;i<number;i++){
                result*=i;
                //为了便于观察，在每两次乘法间让任务休眠20ms
                TimeUnit.MILLISECONDS.sleep(20);
            }
        }
        System.out.printf("%s: %d\n",Thread.currentThread().getName(),result);
        return result;
    }
}
