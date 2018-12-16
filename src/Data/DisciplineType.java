package Data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DisciplineType {
    private String type_;

    public static List<DisciplineType> GetDisciplines(){
        var disTypes = new ArrayList<DisciplineType>();
        //disTypes.add(new DisciplineType(""));
        disTypes.add(new DisciplineType("Lection"));
        disTypes.add(new DisciplineType("Practice"));
        disTypes.add(new DisciplineType("Laboratory"));
        return disTypes;
    }

    public DisciplineType(String disType){
        type_ = disType;
    }

    public DisciplineType(){
        this("");
    }

    public DisciplineType(DisciplineType other){
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
        if (! (obj instanceof DisciplineType))
            return false;
        if (obj == this)
            return true;
        return this.getType().equals(((DisciplineType) obj).getType());
    }
}
