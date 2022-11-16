如果我们需要接收任务的返回结果，则需要通过以下两个接口：

- **Callable**：该接口包含call（）方法，在此方法内部实现业务逻辑。由于该接口是一个泛型接口，因此必须指定call（）方法的返回值。
- **Future**：该接口提供了一些方法来获取Callable对象的返回结果和管理Callable对象的状况。

**1.创建一个任务类实现`Callable`接口**

```java
public class FactorialCalculator implements Callable<Integer> {
    
    private final Integer number;

    @Override
    //如果number不为0或1，则计算其阶乘
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
```

**2.实现Main类中的main（）方法**

通过`Executors`的newFixedThreadPool（）方法创建一个ThreadPoolExecutor对象

```java
//固定数量的线程池，大小为2
ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(2);
```

创建一个Future对象列表来接收返回值

```java
List<Future<Integer>> resultList = new ArrayList<>();
```

创建一个任务对象，通过submit（）方法提交，并将结果加入到`resultList`

```java
FactorialCalculator calculator = new FactorialCalculator(number);
Future<Integer> result = executor.submit(calculator);
resultList.add(result);
```

返回的Future对象可以实现以下两个目的：

- 获取任务状态。可以通过isDone（）方法来判断任务是否已经执行完毕，返回一个布尔值。

```java
for (int i = 0; i < resultList.size(); i++) {
    Future<Integer> result = resultList.get(i);
    System.out.printf("Main:Task %d: %s\n",i,result.isDone());
}
```

![chp2-1.png](E:../img/chp2/1.png)

- 通过get（）方法获取call（）方法的返回结果。该方法会等待直到call（）方法执行完毕并返回结果，如果等待时执行线程发生了中断，则它会抛出一个`InterruptedException`异常，如果call（）方法抛出异常，则它会抛出一个`ExecutionException`异常。

```java
for (int i = 0; i < resultList.size(); i++) {
    Future<Integer> result = resultList.get(i);
    Integer number = null;
    number=result.get();
    System.out.printf("Main: Task %d: %d\n",i,number);
}
```

![chp2-2.png](../img/chp2/2.png)