### Fork/Join框架概述

在java7中引入了一种新的fork/join线程池，它采用**分而治之**的思想，**将一个大任务拆分成多个小任务来执行**。

举个例子：如果要计算一个超大数组的和，最简单的做法是用一个循环在一个线程内完成；

还有一种方法，可以把数组拆成两部分，分别计算，最后加起来就是最终结果，这样可以用两个线程并行执行；

如果拆成两部分还是很大，我们还可以继续拆，用4个线程并行执行；

这就是Fork/Join任务的原理：**判断一个任务是否足够小，如果是，直接计算，否则就先分拆成几个子任务分别计算，最后将子任务的结果合并得到最终的结果，也就是说要先执行子任务**。

可能你会有疑问，直接用带线程池的Executor框架多起几个线程不就行了？

fork/join框架和Executor框架最主要的**区别**有以下两点：

- 对于ThreadPoolExecutor，使用分治法分解任务会存在一定问题。因为ThreadPoolExecutor中的线程无法向任务队列中再添加一个任务（子任务）并等待该任务完成之后再继续执行。而ForkJoinPool可以在创建子任务后挂起当前任务，从任务队列中先选择子任务执行。
- 工作窃取算法。当fork/join框架中的工作线程完成自己的子任务后，会寻找其他未执行完的子任务并执行他们，这样就可以最大程度的利用其执行时间，提高性能。

> **工作窃取算法**：
>
> 假如我们需要做一个比较大的任务，我们可以把这个任务分割为若干互不依赖的子任务，为了减少线程间的竞争，于是把这些子任务分别放到不同的队列里，并为每个队列创建一个单独的线程来执行队列里的任务，线程和队列一一对应。但是有的线程会先把自己队列里的任务干完，而其他线程对应的队列里还有任务等待处理。干完活的线程与其等着，不如去帮其他线程干活，于是它就去其他线程的队列里窃取一个任务来执行。而在这时它们会访问同一个队列，所以为了减少窃取任务线程和被窃取任务线程之间的竞争，通常会使用双端队列，**被窃取任务线程永远从双端队列的头部拿任务执行，而窃取任务的线程永远从双端队列的尾部拿任务执行**。



### Fork/Join框架主要类图

fork/join的核心由以下两个类组成：

**ForkJoinPool**：实现了工作窃取算法，管理工作线程并提供任务本身和任务执行过程的信息。

![fork1.png](../img/fork(join)/1.png)

**ForkJoinTask**：是ForkJoinPool内所执行任务的基类，它提供了以下两种操作：

- fork（）：将任务分割为更小的任务并执行；
- join（）：当一个任务等待它创建的所有任务结束时，该操作用来组合这些任务的执行结果；

一般来说，我们创建任务时需要实现以下三个子类之一：

- `RecursiveAction`：任务不返回结果；
- `RecursiveTask`：任务返回一个执行结果；
- `CountedCompleter`：所有子任务执行完毕后启动一个完成方法；

![fork2.png](../img/fork(join)/2.png)



### Fork/Join框架的局限性

- 任务只能使用fork（）和join（）操作进行同步，如果使用了其他同步机制，则在同步操作时，工作线程就不能执行其他任务了；
- 在fork/join框架中，被拆分的任务不应该去执行IO操作；
- 任务不能抛出受查异常，因为ForkJoinTask类的compute（）方法的实现声明不包含抛出异常，所以必须在内部实现处理异常的功能；



### 一个实例

我们来看如何使用Fork/Join对大数据进行并行求和。

核心就是任务类的实现：

```java
class SumTask extends RecursiveTask<Long> {
    protected Long compute() {
        // “分裂”子任务:
        SumTask subtask1 = new SumTask(...);
        SumTask subtask2 = new SumTask(...);
        // invokeAll会并行运行两个子任务:
        invokeAll(subtask1, subtask2);
        // 获得子任务的结果:
        Long subresult1 = subtask1.join();
        Long subresult2 = subtask2.join();
        // 汇总结果:
        return subresult1 + subresult2;
    }
}
```

其中的`invokeAll()`方法是通过join（）来实现的：

```java
public static void invokeAll(ForkJoinTask<?> t1, ForkJoinTask<?> t2) {
    int s1, s2;
    t2.fork();
    if ((s1 = t1.doInvoke() & DONE_MASK) != NORMAL)
        t1.reportException(s1);
    if ((s2 = t2.doJoin() & DONE_MASK) != NORMAL)
        t2.reportException(s2);
}
```

运行结果如下：

![fork3.png](../img/fork(join)/3.png)