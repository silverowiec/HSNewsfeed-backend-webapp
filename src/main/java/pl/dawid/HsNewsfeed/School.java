package pl.dawid.HsNewsfeed;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Parent;

/**
 * Created by dawid on 02/08/16.
 */
@Entity
public class School  {
    @Id
    public Long id;
    @Parent
    Key<School> schoolKey;
    public String code;
    public String name;
    public int maxPupilsCount;
    private String pass;
    public String email;

    public School(){}
    public School(String code, String name, int maxPupilsCount, String pass, String email){
        this.schoolKey = Key.create(School.class, code);
        this.code = code;
        this.name = name;
        this.maxPupilsCount = maxPupilsCount;
        this.pass=pass;
        this.email = email;
    }

    public String getPass(){
        return pass;
    }


}
