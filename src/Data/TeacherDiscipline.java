package Data;

public class TeacherDiscipline {
    private int id_;
    private Teacher teacher_;
    private Discipline discipline_;

    public TeacherDiscipline(int id, Teacher teacher, Discipline discipline){
        id_ = id;
        teacher_ = teacher;
        discipline_ = discipline;
    }

    public TeacherDiscipline(Teacher teacher, Discipline discipline){
        this(-1, teacher, discipline);
    }

    public TeacherDiscipline(){
        this(-1, new Teacher(), new Discipline());
    }

    public void setTeacher(Teacher teacher) {
        this.teacher_ = teacher;
    }

    public Teacher getTeacher() {
        return teacher_;
    }

    public void setDiscipline(Discipline discipline) {
        this.discipline_ = discipline;
    }

    public Discipline getDiscipline() {
        return discipline_;
    }

    public String toString(){
        String firstName, lastName, discipline;
        firstName = teacher_.getFirstName();
        lastName = teacher_.getLastName();
        discipline = discipline_.toString();

//        if(firstName.length() >= 4){
//            firstName = firstName.substring(0, 3);
//        }
//        System.out.println(firstName);
//        if(lastName.length() >= 4){
//            lastName = lastName.substring(0, 3);
//        }
//        System.out.println(lastName);
//        if(teacher_.getFirstName().length() >= 4){
//            discipline = discipline.substring(0, 3);
//        }
//        System.out.println(discipline);

        return firstName + lastName + " : " + discipline;
    }

    public int getId() {
        return id_;
    }

    public void setId(int id) {
        this.id_ = id;
    }
}