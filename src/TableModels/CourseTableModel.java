package TableModels;

import DAO.Course;
import DAO.Speciality;

import javax.swing.table.AbstractTableModel;
import java.util.List;

public class CourseTableModel extends AbstractTableModel implements TableData<Course>
{
    private final List<Course> courses;

    private final String[] columnNames = new String[] {
            "Id", "Course Name"
    };
    private final Class[] columnClass = new Class[] {
            Integer.class, String.class
    };

    public CourseTableModel(List<Course> courses) {
        this.courses = courses;
    }

    @Override
    public String getColumnName(int column)
    {
        return columnNames[column];
    }

    @Override
    public Class<?> getColumnClass(int columnIndex)
    {
        return columnClass[columnIndex];
    }

    @Override
    public int getColumnCount()
    {
        return columnNames.length;
    }

    @Override
    public int getRowCount()
    {
        return courses.size();
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex)
    {
        //Speciality row = specialityDao.getById(rowIndex + 1).orElse(new Speciality());
        Course row = courses.get(rowIndex);

        if(0 == columnIndex) {
            return row.getId();
        }
        else if(1 == columnIndex) {
            return row.getCourseName();
        }

        return null;
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex)
    {
        Course row = courses.get(rowIndex);

        if(0 == columnIndex) {
            //row.setId((Integer)aValue);
        }
        else if(1 == columnIndex) {
            row.setCourseName((String) aValue);
        }
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex)
    {
        return true;
    }

    @Override
    public List<Course> getTableData() {
        return courses;
    }
}