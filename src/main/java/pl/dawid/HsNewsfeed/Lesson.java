package pl.dawid.HsNewsfeed;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.Parent;

/**
 * Created by dawid on 02/08/16.
 */
@Entity
public class Lesson {
    @Id
    public Long id;
    public String name;
    public String classRoom;
    public int hour;
    public int minute;
    @Index public int count;
    public WeekDay weekDay;
   public enum WeekDay {Poniedzialek, Wtorek, Sroda, Czwartek, Piatek};

    @Parent
    Key<SchoolClass> schoolClassKey;

    public Lesson(){}
    public Lesson(SchoolClass schoolClass, String name, String classRoom, int hour, int minute,
                  int count, WeekDay weekDay){
        schoolClassKey = Key.create(SchoolClass.class, schoolClass.id);
        this.name = name;
        this.classRoom=classRoom;
        this.hour=hour;
        this.minute=minute;
        this.count = count;
        this.weekDay = weekDay;
    }

}
