package pl.dawid.HsNewsfeed;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Parent;

/**
 * Created by dawid on 04/08/16.
 */
@Entity
public class LessonHour {
    @Parent
    Key<School> schoolKey;
    @Id
    public Long id;
    public int number;
    public int startHour;
    public int endHour;
    public int startMinute;
    public int endMinute;

    public LessonHour(){}
    public LessonHour(String key, int number, int startHour, int startMinute){
        schoolKey = Key.create(School.class, key);
        this.number=number;
        this.startHour = startHour;
        this.startMinute = startMinute;
        this.endHour=startHour;
        this.endMinute=startMinute+45;
        if(this.endMinute>=60){
            this.endHour+=1;
            this.endMinute-=60;
        }


    }

}
