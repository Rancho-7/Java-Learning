package ThreadPool.chapter3;

/**
* @author: Lansg
* @date: 2022/11/18 11:10
* @Description: 用来存储执行并发任务后的返回结果
*/
public class Result {
    private String name;
    private int value;

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
