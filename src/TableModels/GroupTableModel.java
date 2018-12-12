package TableModels;

import Data.Course;
import Data.Group;
import Data.Speciality;

import javax.swing.table.AbstractTableModel;
import java.util.List;

public class GroupTableModel extends AbstractTableModel implements TableData<Group>
{
    private final List<Group> groups;

    private final String[] columnNames = new String[] {
            "Id", "Group Name", "Students Number", "Course", "Speciality"
    };

    public GroupTableModel(List<Group> groups) {
        this.groups = groups;
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
        return groups.size();
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex)
    {
        //Speciality row = specialityDao.getById(rowIndex + 1).orElse(new Speciality());
        Group row = groups.get(rowIndex);

        if(0 == columnIndex) {
            return row.getId();
        }
        else if(1 == columnIndex) {
            return row.getGroupName();
        }
        else if(2 == columnIndex) {
            return row.getStudentsNumber();
        }
        else if(3 == columnIndex) {
            return row.getCourse();
        }
        else if(4 == columnIndex) {
            return row.getSpeciality();
        }

        return null;
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex)
    {
        Group row = groups.get(rowIndex);

        if(0 == columnIndex) {
            row.setId((Integer)aValue);
        }
        else if(1 == columnIndex) {
            row.setGroupName((String) aValue);
        }
        else if(2 == columnIndex) {
            row.setStudentsNumber((Integer) aValue);
        }
        else if(3 == columnIndex) {
            row.setCourse((Course) aValue);
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
    public List<Group> getTableData() {
        return groups;
    }
}