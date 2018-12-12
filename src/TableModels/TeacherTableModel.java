package TableModels;

import Data.Teacher;

import javax.swing.table.AbstractTableModel;
import java.util.List;

public class TeacherTableModel extends AbstractTableModel implements TableData<Teacher>
{
    private final List<Teacher> teachers;

    private final String[] columnNames = new String[] {
            "Id", "First Name", "Last Name"
    };

    public TeacherTableModel(List<Teacher> teachers) {
        this.teachers = teachers;
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
        return teachers.size();
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex)
    {
        //Speciality row = specialityDao.getById(rowIndex + 1).orElse(new Speciality());
        Teacher row = teachers.get(rowIndex);

        if(0 == columnIndex) {
            return row.getId();

        }else if(1 == columnIndex) {
            return row.getFirstName();

        }else if(2 == columnIndex) {
            return row.getLastName();
        }

        return null;
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex)
    {
        Teacher row = teachers.get(rowIndex);

        if(0 == columnIndex) {
            row.setId((Integer)aValue);

        }else if(1 == columnIndex) {
            row.setFirstName((String) aValue);

        }else if(2 == columnIndex) {
            row.setLastName((String) aValue);
        }
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex)
    {
        return columnIndex > 0;
    }

    @Override
    public List<Teacher> getTableData() {
        return teachers;
    }
}