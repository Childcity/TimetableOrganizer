package Data;

public class Course {
    private int id_;
    private String courseName_;

    public Course(){
        courseName_ = "";
        id_ = -1;
    }

    public Course(int id, String courseName){
        id_ = id;
        courseName_ = courseName;
    }

    public Course(String courseName){
        id_ = -1;
        courseName_ = courseName;
    }

    public void setId(int id) {
        id_ = id;
    }

    public int getId() {
        return id_;
    }

    public String getCourseName() {
        return courseName_;
    }

    public void setCourseName(String specName_) {
        this.courseName_ = specName_;
    }

    public String toString(){
        return getCourseName();
    }
}
