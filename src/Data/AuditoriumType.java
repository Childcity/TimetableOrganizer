package Data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AuditoriumType {
    private String type_;

    public static List<AuditoriumType> GetAuditoriums(){
        var auditoriumTypes = new ArrayList<AuditoriumType>();
        //auditoriumTypes.add(new AuditoriumType(""));
        auditoriumTypes.add(new AuditoriumType("LectionRoom"));
        auditoriumTypes.add(new AuditoriumType("LaboratoryRoom"));
        return auditoriumTypes;
    }

    public AuditoriumType(String auditType){
        type_ = auditType;
    }

    public AuditoriumType(){
        this("");
    }

    public AuditoriumType(AuditoriumType other){
        this(other.getType());
    }

    public String getType() {
        return type_;
    }

    public void setType_(String type) {
        this.type_ = type;
    }

    @Override
    public String toString() {
        return getType();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (! (obj instanceof AuditoriumType))
            return false;
        if (obj == this)
            return true;
        return this.getType().equals(((AuditoriumType) obj).getType());
    }
}
