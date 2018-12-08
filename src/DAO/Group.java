package DAO;

public class Group {
    private int id;
    private String groupName;
    private int studentsNumber;
    private int courseId;
    private int specialityId;

    public int getId() {
        return id;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public int getStudentsNumber() {
        return studentsNumber;
    }

    public void setStudentsNumber(int studentsNumber) {
        this.studentsNumber = studentsNumber;
    }

    public int getCourseId() {
        return courseId;
    }

    public int getSpecialityId() {
        return specialityId;
    }
}
