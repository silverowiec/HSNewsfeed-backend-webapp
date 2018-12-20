package pl.dawid.HsNewsfeed;

/**
 * Created by dawid on 30/05/16.
 */
public class Counter {

    public int count;
    public String countS;



    public Counter(){

    }
    public Counter(int count){
        super();
        this.count = count;
        this.countS = String.valueOf(count);

    }
    public String getCountS(){
        return countS;

    }

    public void setCount(int count){
        this.count=count;
    }

    public int getCount(){
        return count;
    }
}
