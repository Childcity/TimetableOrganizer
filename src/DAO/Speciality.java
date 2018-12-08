package DAO;

public class Speciality {
    private int id_;
    private String specName_;
    private int numericName_;

    public Speciality(){
        specName_= "";
        id_ = numericName_ = -1;
    }

    public Speciality(int id, String specName, int numericName){
        id_ = id;
        specName_ = specName;
        numericName_ = numericName;
    }

    public Speciality(String specName, int numericName){
        id_ = -1;
        specName_ = specName;
        numericName_ = numericName;
    }

    public void setId(int id) {
        id_ = id;
    }

    public int getId() {
        return id_;
    }

    public String getSpecName() {
        return specName_;
    }

    public void setSpecName(String specName_) {
        this.specName_ = specName_;
    }

    public int getNumericName() {
        return numericName_;
    }

    public void setNumericName(int numericName_) {
        this.numericName_ = numericName_;
    }
}
