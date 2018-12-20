package pl.dawid.HsNewsfeed;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.Parent;

import java.util.Date;
import java.util.Random;

/**
 * Created by Dawid on 10/05/16.
 */
@Entity
public class Lucky {

    @Id
    public Long id;
    public int number;
    @Index
    public Date date;
    @Parent
    Key<Catalog> school;
    public String luckyS;

    public Date getDate() {
        return date;
    }

    public int getNumber() {
        return number;
    }

    public Lucky(){}

    public Lucky(String key, int max) {
        Random r = new Random();
        this.number = r.nextInt(max-1) + 1;
        this.luckyS = String.valueOf(number);
        this.date = new Date();
        school = Key.create(Catalog.class, key);
    }

}
