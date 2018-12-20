package pl.dawid.HsNewsfeed;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Parent;

/**
 * Created by dawid on 02/08/16.
 */
@Entity
public class SchoolClass {
    @Id public Long id;
    public String code;
    @Parent
    Key<School> schoolKey;

    public SchoolClass(){}
    public SchoolClass(String schoolCode, String code){
        super();
        this.schoolKey = Key.create(School.class, schoolCode);
        this.code = code;
    }
}
