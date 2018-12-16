package Data;

public class Group {
    private int id_;
    private String groupName_;
    private int studentsNumber_;
    private Course course_;
    private Speciality speciality_;


    public Group(){
        id_ = studentsNumber_ = -1;
        groupName_ = "";
        course_ = new Course();
        speciality_ = new Speciality();
    }

    public Group(int id, String groupName, int studentsNumber, Course course, Speciality speciality){
        id_ = id;
        groupName_ = groupName;
        studentsNumber_ = studentsNumber;
        course_ = course;
        speciality_ = speciality;
    }

    public Group(String groupName, int studentsNumber, Course course, Speciality speciality){
        this(-1, groupName, studentsNumber, course, speciality);
    }

    public void setId(int id) {
        this.id_ = id;
    }

    public int getId() {
        return id_;
    }

    public String getGroupName() {
        return groupName_;
    }

    public void setGroupName(String groupName) {
        this.groupName_ = groupName;
    }

    public int getStudentsNumber() {
        return studentsNumber_;
    }

    public void setStudentsNumber(int studentsNumber) {
        this.studentsNumber_ = studentsNumber;
    }

    public Course getCourse() {
        return course_;
    }

    public void setCourse(Course course) {
        this.course_ = course;
    }

    public Speciality getSpeciality() {
        return speciality_;
    }

    public void setSpeciality(Speciality speciality) {
        this.speciality_ = speciality;
    }

    @Override
    public String toString(){
        return getGroupName();
    }
}