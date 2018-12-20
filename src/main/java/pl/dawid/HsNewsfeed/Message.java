package pl.dawid.HsNewsfeed;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.Parent;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by Dawid on 26/04/16.
 */
@Entity
public class Message {

    @Id
    public Long id;
    public String title;
    public String description;
    @Index
    public Date date;
    public String dates;
    @Parent Key<School> schoolKey;
    public boolean isForTeacher;

    public Message(){

    }

    public Message(String title, String description, String key, boolean isForTeacher){
        super();
        this.title=title;
        this.description=description;
        this.date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat();
        sdf.setTimeZone(TimeZone.getTimeZone("Europe/Warsaw"));
        this.dates = sdf.format(this.date);
        schoolKey = Key.create(School.class, key);
        this.isForTeacher = isForTeacher;

    }


    public Date getDate(){
        return date;
    }


}
