package TableModels;

import DAO.Dao;
import DAO.GroupTeacherDisciplineDao;
import Data.*;

import javax.swing.table.AbstractTableModel;
import java.util.List;

public class GroupTeacherDisciplineTableModel extends AbstractTableModel implements TableData<GroupTeacherDiscipline>
{
    private final List<GroupTeacherDiscipline> groupTeacherDisciplines;

    private final String[] columnNames = new String[] {
            "Id", "Group", "Teacher-Discipline"
    };

    public GroupTeacherDisciplineTableModel(List<GroupTeacherDiscipline> groupTeacherDisciplines) {
        this.groupTeacherDisciplines = groupTeacherDisciplines;
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
        return groupTeacherDisciplines.size();
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex)
    {
        //Speciality row = specialityDao.getById(rowIndex + 1).orElse(new Speciality());
        GroupTeacherDiscipline row = groupTeacherDisciplines.get(rowIndex);

        if(0 == columnIndex) {
            return row.getId();
        }
        else if(1 == columnIndex) {
            return row.getGroup();
        }
        else if(2 == columnIndex) {
            return row.getTeacherDiscipline();
        }

        return null;
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex)
    {
        GroupTeacherDiscipline row = groupTeacherDisciplines.get(rowIndex);

        if(0 == columnIndex) {
            row.setId((Integer)aValue);
        }
        else if(1 == columnIndex) {
            row.setGroup((Group) aValue);
        }
        else if(2 == columnIndex) {
            row.setTeacherDiscipline((TeacherDiscipline) aValue);
        }
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex)
    {
        return columnIndex > 0;
    }

    @Override
    public List<GroupTeacherDiscipline> getTableData() {
        return groupTeacherDisciplines;
    }

    @Override
    public GroupTeacherDiscipline getNewRow() {
        return new GroupTeacherDiscipline();
    }

    @Override
    public Dao<GroupTeacherDiscipline> getDao() {
        return new GroupTeacherDisciplineDao();
    }
}