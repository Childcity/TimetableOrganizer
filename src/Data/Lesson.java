package Data;

import java.util.ArrayList;
import java.util.List;

public class Lesson {
    private int timetableId_;
    private int lessonNumber_;
    private String[] lessonTime_;
    private Auditorium auditorium_;
    private GroupTeacherDiscipline groupTeacherDiscipline_;

    public static List<String[]> GetLessonsTimes(){
        return new ArrayList<>(){{
            add(new String[]{"8:30", "10:05"});
            add(new String[]{"10.25", "12.00"});
            add(new String[]{"12.20", "13.55"});
            add(new String[]{"14.15", "15.50"});
            add(new String[]{"16.10", "17.45"});
        }};
    }

    public Lesson(int timetableId, int lessonNumber, Auditorium auditorium, GroupTeacherDiscipline groupTeacherDiscipline){
        timetableId_ = timetableId;
        lessonNumber_ = lessonNumber;
        auditorium_ = auditorium;
        groupTeacherDiscipline_ = groupTeacherDiscipline;
        if(lessonNumber < 0 || lessonNumber >= GetLessonsTimes().size()){
            lessonTime_ = new String[2];
        }else {
            lessonTime_ = GetLessonsTimes().get(lessonNumber);
        }
    }

    public Lesson(int lessonNumber){
        this(-1, lessonNumber, new Auditorium(), new GroupTeacherDiscipline());
    }

//    public Lesson(){
//        this(-1);
//    }

    public int getLessonNumber() {
        return lessonNumber_;
    }

    public void setLessonNumber(int lessonNumber) {
        this.lessonNumber_ = lessonNumber;
    }

    public Auditorium getAuditorium() {
        return auditorium_;
    }

    public void setAuditorium(Auditorium auditorium) {
        this.auditorium_ = auditorium;
    }

    public String[] getLessonTime() {
        return lessonTime_;
    }

    public GroupTeacherDiscipline getGroupTeacherDiscipline() {
        return groupTeacherDiscipline_;
    }

    public void setGroupTeacherDiscipline(GroupTeacherDiscipline groupTeacherDiscipline) {
        this.groupTeacherDiscipline_ = groupTeacherDiscipline;
    }

    public int getTimetableId() {
        return timetableId_;
    }

    public void setTimetableId(int timetableId) {
        this.timetableId_ = timetableId;
    }
}
