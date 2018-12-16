package Data;

public class Auditorium {
    private int id_;
    private int auditNumber_;
    private int capacity_;
    private AuditoriumType auditType_;

    public Auditorium(int id, int auditNumber, int capacity, AuditoriumType auditType){
        id_ = id;
        auditNumber_ = auditNumber;
        capacity_ = capacity;
        auditType_ = auditType;
    }

    public Auditorium(int auditNumber, int capacity, AuditoriumType auditType){
        this(-1, auditNumber, capacity, auditType);
    }

    public Auditorium(){
        this(-1, -1, new AuditoriumType());
    }

    public void setId(int id) {
        id_ = id;
    }

    public int getId() {
        return id_;
    }

    public int getAuditNumber() {
        return auditNumber_;
    }

    public void setAuditNumber(int auditNumber) {
        this.auditNumber_ = auditNumber;
    }

    public int getCapacity() {
        return capacity_;
    }

    public void setCapacity(int capacity) {
        this.capacity_ = capacity;
    }

    public AuditoriumType getAuditType() {
        return auditType_;
    }

    public void setAuditType(AuditoriumType auditType) {
        this.auditType_ = auditType;
    }

    @Override
    public String toString() {
        if(auditNumber_ == -1 || auditType_.getType().equals("")){
            return "";
        }

        return getAuditNumber() + " [" + getAuditType() + "]";
    }
}
