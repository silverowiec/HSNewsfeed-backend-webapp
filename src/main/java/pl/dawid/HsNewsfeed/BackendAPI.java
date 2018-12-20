package pl.dawid.HsNewsfeed;


import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.Named;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.ObjectifyService;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;


@Api(name = "messagingService", version = "v2",
        scopes = {Constants.EMAIL_SCOPE},
        clientIds = {Constants.WEB_CLIENT_ID, Constants.API_EXPLORER_CLIENT_ID,
                Constants.ANDROID_CLIENT_ID, Constants.IOS_CLIENT_ID}, audiences = {Constants.ANDROID_AUDIENCE})

public class BackendAPI {

    public static final String GCM_KEY = "API KEY";


    @ApiMethod (name = "deleteHours")
    public LessonHour deleteHours(@Named("schoolCode") String code){
        Key<School> schoolKey = getSchool(code).schoolKey;
        List<LessonHour> lhs = listHours(code);
        for (LessonHour lh :lhs
             ) {
            ObjectifyService.ofy().delete().entity(lh);
        }
        return new LessonHour("x", -1, -1, -1);
    }



    @ApiMethod(name = "addSchool")
    public School addSchool(@Named("schoolCode") String code, @Named("name") String name, @Named("pupilsCount") int maxPupilsCount,
                            @Named("pass") String pass, @Named("email") String email) throws NoSuchAlgorithmException {
        MessageDigest encryptor = MessageDigest.getInstance("SHA-256");
        byte[] hash = encryptor.digest(pass.getBytes());
        StringBuilder hashString = new StringBuilder();
        for (byte b : hash)
            hashString.append(b);
        School school = new School(code, name, maxPupilsCount, hashString.toString(), email);
        ObjectifyService.ofy().save().entity(school).now();
        return school;
    }

    @ApiMethod(name = "getSchool")
    public School getSchool(@Named("schoolCode") String shoolCode) {
        Key<School> schoolKey = Key.create(School.class, shoolCode);
        return ObjectifyService.ofy().load().type(School.class)
                .ancestor(schoolKey).first().now();
    }

    @ApiMethod(name = "listSchools")
    public List<School> listSchools() {
        List<School> schools = ObjectifyService.ofy().load().type(School.class).limit(100).list();
        Collections.sort(schools, new Comparator<School>() {
            @Override
            public int compare(School o1, School o2) {
                            return o1.name.compareTo(o2.name);
                        }

        });
        return schools;
    }

    @ApiMethod(name = "addClass")
    public SchoolClass addClass(@Named("schoolCode") String schoolCode, @Named("classCode") String code) {
        SchoolClass schoolClass = new SchoolClass(schoolCode, code);
        ObjectifyService.ofy().save().entity(schoolClass).now();
        return schoolClass;
    }

    @ApiMethod(name = "getClass")
    public SchoolClass getClass(@Named("schoolCode") String schoolCode, @Named("classCode") String code) {
        Key<School> schoolKey = Key.create(School.class, schoolCode);
        List<SchoolClass> list = ObjectifyService.ofy().load().type(SchoolClass.class)
                .ancestor(schoolKey).limit(100).list();
        for (SchoolClass s : list) {
            if (Objects.equals(s.code, code))
                return s;
        }

        return null;

    }

    @ApiMethod(name = "deleteClass")
    public Message deleteClass(@Named("schoolCode") String schoolCode, @Named("classCode") String classCode) {
        SchoolClass schoolClass = getClass(schoolCode, classCode);
        try {
            ObjectifyService.ofy().delete().entity(schoolClass);
        } catch (Exception e) {
            Key<School> schoolKey = Key.create(getSchool(schoolCode));
            return new Message("Wystapil blad poczas usuwania klasy, skontaktuj sie z administratorem aplikacji.", "", "default", true);

        }
        Key<School> schoolKey = Key.create(getSchool(schoolCode));
        return new Message("Usuwanie klasy zakonczone sukcesem.", "", "default", true);
    }

    @ApiMethod(name = "deleteLesson")
    public Message deleteLesson(@Named("schoolCode") String schoolCode, @Named("classCode") String classCode,
                                @Named("count") int count) {
        try {
            SchoolClass sc = getClass(schoolCode, classCode);
            Key<SchoolClass> schoolClassKey = Key.create(SchoolClass.class, sc.id);
            List<Lesson> lessons = ObjectifyService.ofy().load().type(Lesson.class).ancestor(schoolClassKey).limit(1000).list();
            for (Lesson l : lessons) {
                if (l.count == count) {

                    ObjectifyService.ofy().delete().entity(l);
                    break;
                }
            }


        } catch (Exception e) {
            Key<School> schoolKey = Key.create(getSchool(schoolCode));
            return new Message("Wystapil blad poczas usuwania lekcji, skontaktuj sie z administratorem aplikacji.", "", "default", true);

        }
        Key<School> schoolKey = Key.create(getSchool(schoolCode));
        return new Message("Usuwanie lekcji zakonczone sukcesem.", "", "default", true);

    }

    @ApiMethod(name = "deleteTimeTable")
    public Message deleteTimeTable(@Named("schoolCode") String schoolCode) {
        Key<School> schoolKey = getSchool(schoolCode).schoolKey;
        try {
            List<SchoolClass> schoolClasses = listClasses(schoolCode);
            for (SchoolClass sc : schoolClasses) {
                Key<SchoolClass> sck = Key.create(SchoolClass.class, sc.id);
                List<Lesson> lessons = ObjectifyService.ofy().load().type(Lesson.class).ancestor(sck).limit(1000).list();
                ObjectifyService.ofy().delete().entities(lessons);
            }

        } catch (Exception e) {
            return new Message("Wystapil blad poczas usuwania planu lekcji, skontaktuj sie z administratorem aplikacji.", "", "default", true);

        }
        return new Message("Usuwanie planu lekcji zakonczone sukcesem.", "", "default", true);
    }

    @ApiMethod(name = "addHour")
    public LessonHour addHour(@Named("schoolCode") String schoolCode,
                              @Named("number") int number,
                              @Named("startHour") int startHour,
                              @Named("startMinute") int startMinute) {
        LessonHour lessonHour = new LessonHour(schoolCode, number, startHour, startMinute);
        ObjectifyService.ofy().save().entity(lessonHour);
        return lessonHour;
    }

    @ApiMethod(name = "listHours")
    public List<LessonHour> listHours(@Named("schoolCode") String schoolCode) {
        Key<School> schoolKey = getSchool(schoolCode).schoolKey;
        List<LessonHour> lessonHours = ObjectifyService.ofy().load().type(LessonHour.class).ancestor(schoolKey)
                .limit(10).list();
        Comparator<LessonHour> comparator = new Comparator<LessonHour>() {
            @Override
            public int compare(LessonHour o1, LessonHour o2) {
                if (o1.number < o2.number) return 0;
                else return 1;
            }
        };
        Collections.sort(lessonHours, comparator);

        return lessonHours;
    }

    @ApiMethod(name = "addLesson")
    public Lesson addLesson(@Named("schoolCode") String schoolCode, @Named("classCode") String classCode,
                            @Named("name") String name,
                            @Named("classRoom") String classRoom,
                            @Named("hour") int hour,
                            @Named("minute") int minute,
                            @Named("count") int count,
                            @Named("weekDay") Lesson.WeekDay weekDay) {
        SchoolClass schoolClass = getClass(schoolCode, classCode);
        Lesson lesson = new Lesson(schoolClass, name, classRoom, hour, minute, count, weekDay);
        List<Lesson> lessons = listLessons(schoolCode, classCode);
        for (Lesson l : lessons) {
            if (l.count == count && l.weekDay == weekDay) {
                ObjectifyService.ofy().delete().entity(l);

            }
        }
        ObjectifyService.ofy().save().entity(lesson);
        return lesson;
    }

    @ApiMethod(name = "listLessons")
    public List<Lesson> listLessons(@Named("schoolCode") String schoolCode,
                                    @Named("classCode") String classCode) {
        SchoolClass schoolClass = getClass(schoolCode, classCode);
        Key<SchoolClass> schoolClassKey = Key.create(SchoolClass.class, schoolClass.id);
        List<Lesson> lessons = ObjectifyService.ofy()
                .load().type(Lesson.class)
                .ancestor(schoolClassKey)
                .limit(100).list();

        Comparator<Lesson> comparator = new Comparator<Lesson>() {
            @Override
            public int compare(Lesson o1, Lesson o2) {
                if (o1.count < o2.count) return 0;
                else return 1;
            }
        };

        Collections.sort(lessons, comparator);


        return lessons;
    }


    @ApiMethod(name = "listLessonsByDay")
    public List<Lesson> listLessonsByDay(@Named("schoolCode") String schoolCode,
                                         @Named("classCode") String classCode,
                                         @Named("Day") Lesson.WeekDay weekDay) {
        SchoolClass schoolClass = getClass(schoolCode, classCode);
        Key<SchoolClass> schoolClassKey = Key.create(SchoolClass.class, schoolClass.id);
        List<Lesson> lessons = ObjectifyService.ofy()
                .load().type(Lesson.class)
                .ancestor(schoolClassKey)
                .limit(100).list();

        ArrayList<Lesson> lessonList = new ArrayList<>();

        for (Lesson l : lessons) {
            if (l.weekDay == weekDay)
                lessonList.add(l);
        }

        Comparator<Lesson> comparator = new Comparator<Lesson>() {
            @Override
            public int compare(Lesson o1, Lesson o2) {
                if (o1.count < o2.count) return -1;
                else if (o1.count == o2.count) return 0;
                else return 1;
            }
        };

        Collections.sort(lessonList, comparator);


        return lessonList;
    }


    @ApiMethod(name = "listClasses")
    public List<SchoolClass> listClasses(@Named("schoolCode") String schoolCode) {
        School school = getSchool(schoolCode);
       List<SchoolClass> classes = ObjectifyService.ofy().load().type(SchoolClass.class)
                .ancestor(school.schoolKey).limit(50).list();
        Collections.sort(classes, new Comparator<SchoolClass>() {
            @Override
            public int compare(SchoolClass o1, SchoolClass o2) {
                  return o1.code.compareTo(o2.code);

            }
        });
            return classes;
    }


    @ApiMethod(name = "createMessage")
    public Message createMessage(@Named("schoolKey") String key, @Named("title") String title,
                                 @Named("description") String description, @Named("isForTeacher") boolean isForTeacher) {

        Message m = new Message(title, description, key, isForTeacher);
        ObjectifyService.ofy().save().entity(m).now();


        //GCM
        GCMSender gcmSender = new GCMSender();
        if(!isForTeacher) {
            GCMSender.sendPush(key, title);
        }else{
            GCMSender.sendPush(key+"N", title);
        }

        return m;
    }

    @ApiMethod(name = "listMessages")
    @SuppressWarnings("unused")
    /**
     * Return messages about something.
     */
    public List<Message> listMessages(@Named("schoolKey") String key) {
        Key<School> schoolKey = Key.create(School.class, key);
        List<Message> messages = ObjectifyService.ofy()
                .load()
                .type(Message.class)
                .order("-date")
                .ancestor(schoolKey)
                .limit(20)
                .list();
        return messages;
    }


    @ApiMethod(name = "getValid")
    public Validate getValid(@Named("schoolKey") String schoolKey, @Named("password") String password) throws NoSuchAlgorithmException {
        School school = getSchool(schoolKey);
        return new Validate(school.getPass(), password);
    }

    @ApiMethod(name = "getLucky")
    public Lucky getLucky(@Named("schoolKey") String schoolCode) {
        Key<Catalog> luckiKey = Key.create(Catalog.class, schoolCode);
        School s = getSchool(schoolCode);
        Lucky luckyNumber = ObjectifyService.ofy().load().type(Lucky.class).order("-date").ancestor(luckiKey)
                .first().now();
        Calendar c1 = Calendar.getInstance();
        try {
            c1.setTime(luckyNumber.getDate());
        } catch (Exception e) {
            c1.set(1999, Calendar.JANUARY, 1);
        }
        Date d = new Date();
        Calendar c2 = Calendar.getInstance();
        c2.setTime(d);

        if (c1.get(Calendar.DAY_OF_YEAR) != c2.get(Calendar.DAY_OF_YEAR)) {
            Lucky l = new Lucky(schoolCode, s.maxPupilsCount);
            if (!(c2.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) && !(c2.get(Calendar.DAY_OF_WEEK) ==
                    Calendar.SUNDAY)) {
                GCMSender.sendPush(schoolCode, "Szczczesliwy numerek na dzis to: " + l.getNumber());
            }
            ObjectifyService.ofy().save().entity(l).now();
            return l;
        } else {
            return luckyNumber;
        }


    }


}
