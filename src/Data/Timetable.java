package Data;

public class Timetable {
    private int id_;
    private int day_;
    private int week_;
    private int lesson_;
    private Auditorium auditorium_;
    private GroupTeacherDiscipline groupTeacherDiscipline_;

    public Timetable(int id, int week, int day, int lesson, Auditorium auditorium, GroupTeacherDiscipline groupTeacherDiscipline){
        id_ = id;
        day_ = day;
        week_ = week;
        lesson_ = lesson;
        auditorium_ = auditorium;
        groupTeacherDiscipline_ = groupTeacherDiscipline;
    }

    public Timetable(int week, int day, int lesson, Auditorium auditorium, GroupTeacherDiscipline groupTeacherDiscipline){
        this(-1, week, day, lesson, auditorium, groupTeacherDiscipline);
    }

    public Timetable(){
        this(-1, -1, -1, new Auditorium(), new GroupTeacherDiscipline());
    }

    public void setId(int id) {
        id_ = id;
    }

    public int getId() {
        return id_;
    }

    public int getDay() {
        return day_;
    }

    public void setDay(int day) {
        this.day_ = day;
    }

    public int getLesson() {
        return lesson_;
    }

    public void setLesson(int lesson) {
        this.lesson_ = lesson;
    }

    public Auditorium getAuditorium() {
        return auditorium_;
    }

    public void setAuditorium(Auditorium auditorium) {
        this.auditorium_ = auditorium;
    }

    public GroupTeacherDiscipline getGroupTeacherDiscipline() {
        return groupTeacherDiscipline_;
    }

    public void setGroupTeacherDiscipline(GroupTeacherDiscipline groupTeacherDiscipline) {
        this.groupTeacherDiscipline_ = groupTeacherDiscipline;
    }

    public void setWeek(int week) {
        week_ = week;
    }

    public int getWeek() {
        return week_;
    }
}
