package Data;

public class GroupTeacherDiscipline {
    private int id_;
    private Group group_;
    private TeacherDiscipline teacherDiscipline_;

    public GroupTeacherDiscipline(int id, Group group, TeacherDiscipline teacherDiscipline){
        id_ = id;
        group_ = group;
        teacherDiscipline_ = teacherDiscipline;
    }

    public GroupTeacherDiscipline(Group group, TeacherDiscipline teacherDiscipline){
        this(-1, group, teacherDiscipline);
    }

    public GroupTeacherDiscipline(){
        this(-1, new Group(), new TeacherDiscipline());
    }

    public void setId(int id) {
        this.id_ = id;
    }

    public int getId() {
        return id_;
    }

    public Group getGroup() {
        return group_;
    }

    public void setGroup(Group group) {
        this.group_ = group;
    }

    public TeacherDiscipline getTeacherDiscipline() {
        return teacherDiscipline_;
    }

    public void setTeacherDiscipline(TeacherDiscipline teacherDiscipline) {
        this.teacherDiscipline_ = teacherDiscipline;
    }
}