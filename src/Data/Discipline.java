package Data;

public class Discipline {
    private int id_;
    private String disName_;
    private int hoursTotal_;
    private DisciplineType disType_;
    private Speciality speciality_;


    public Discipline(int id, String disName, int hoursTotal, DisciplineType disType, Speciality speciality){
        id_ = id;
        disName_ = disName;
        hoursTotal_ = hoursTotal;
        disType_ = disType;
        speciality_ = speciality;
    }

    public Discipline(String disName, int hoursTotal, DisciplineType disType, Speciality speciality){
        this(-1, disName, hoursTotal, disType, speciality);
    }

    public Discipline(){
        this(-1, "", -1, new DisciplineType(), new Speciality());
    }

    public void setId(int id) {
        id_ = id;
    }

    public int getId() {
        return id_;
    }

    public String getDisName() {
        return disName_;
    }

    public void setDisName(String disName) {
        this.disName_ = disName;
    }

    public int getHoursTotal() {
        return hoursTotal_;
    }

    public void setHoursTotal(int hoursTotal) {
        this.hoursTotal_ = hoursTotal;
    }

    public DisciplineType getDisType() {
        return disType_;
    }

    public void setDisType(DisciplineType disType) {
        this.disType_ = disType;
    }

    public Speciality getSpeciality() {
        return speciality_;
    }

    public void setSpeciality(Speciality speciality) {
        this.speciality_ = speciality;
    }

    @Override
    public String toString(){
        if(disName_.equals("") || disType_.getType().equals("")){
            return "";
        }
        return getDisName() + "-" + getDisType();
    }
}
