package TableModels;

import Data.Discipline;
import Data.DisciplineType;
import Data.Speciality;

import javax.swing.table.AbstractTableModel;
import java.util.List;

public class DisciplineTableModel extends AbstractTableModel implements TableData<Discipline>
{
    private final List<Discipline> disciplines;

    private final String[] columnNames = new String[] {
            "Id", "Discipline Name", "Total Hours", "Discipline Type", "Speciality"
    };

    public DisciplineTableModel(List<Discipline> disciplines) {
        this.disciplines = disciplines;
    }

    @Override
    public String getColumnName(int column)
    {
        return columnNames[column];
    }

    @Override
    public Class<?> getColumnClass(int columnIndex)
    {
        return getValueAt(0, columnIndex).getClass();
    }

    @Override
    public int getColumnCount()
    {
        return columnNames.length;
    }

    @Override
    public int getRowCount()
    {
        return disciplines.size();
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex)
    {
        //Speciality row = specialityDao.getById(rowIndex + 1).orElse(new Speciality());
        Discipline row = disciplines.get(rowIndex);

        if(0 == columnIndex) {
            return row.getId();
        }
        else if(1 == columnIndex) {
            return row.getDisName();
        }
        else if(2 == columnIndex) {
            return row.getHoursTotal();
        }
        else if(3 == columnIndex) {
            return row.getDisType();
        }
        else if(4 == columnIndex) {
            return row.getSpeciality();
        }

        return null;
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex)
    {
        Discipline row = disciplines.get(rowIndex);

        if(0 == columnIndex) {
            row.setId((Integer)aValue);
        }
        else if(1 == columnIndex) {
            row.setDisName((String) aValue);
        }
        else if(2 == columnIndex) {
            row.setHoursTotal((Integer) aValue);
        }
        else if(3 == columnIndex) {
            row.setDisType((DisciplineType) aValue);
        }
        else if(4 == columnIndex) {
            row.setSpeciality((Speciality) aValue);
        }
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex)
    {
        return columnIndex > 0;
    }

    @Override
    public List<Discipline> getTableData() {
        return disciplines;
    }
}