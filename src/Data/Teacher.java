package Data;

public class Teacher {
    private int id_;
    private String firstName_;
    private String lastName_;

    public Teacher(int id, String firstName, String lastName){
        id_ = id;
        firstName_ = firstName;
        lastName_ = lastName;
    }

    public Teacher(String firstName, String lastName){
        this(-1, firstName, lastName);
    }

    public Teacher(){
        this("", "");
    }

    public void setId(int id) {
        id_ = id;
    }

    public int getId() {
        return id_;
    }

    public String getFirstName() {
        return firstName_;
    }

    public void setFirstName(String firstName) {
        this.firstName_ = firstName;
    }

    public String getLastName() {
        return lastName_;
    }

    public void setLastName(String lastName) {
        this.lastName_ = lastName;
    }

    public String toString(){
        return getFirstName() + getLastName();
    }
}
