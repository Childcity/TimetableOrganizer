package TableModels;

import DAO.Dao;
import DAO.TeacherDisciplineDao;
import Data.*;
import Data.TeacherDiscipline;

import javax.swing.table.AbstractTableModel;
import java.util.List;

public class TeacherDisciplineTableModel extends AbstractTableModel implements TableData<TeacherDiscipline>
{
    private final List<TeacherDiscipline> teachersDisciplines;

    private final String[] columnNames = new String[] {
            "Id", "Teacher", "Discipline"
    };

    public TeacherDisciplineTableModel(List<TeacherDiscipline> teachersDisciplines) {
        this.teachersDisciplines = teachersDisciplines;
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
        return teachersDisciplines.size();
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex)
    {
        //Speciality row = specialityDao.getById(rowIndex + 1).orElse(new Speciality());
        TeacherDiscipline row = teachersDisciplines.get(rowIndex);

        if(0 == columnIndex) {
            return row.getId();
        }
        else if(1 == columnIndex) {
            return row.getTeacher();
        }
        else if(2 == columnIndex) {
            return row.getDiscipline();
        }

        return null;
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex)
    {
        TeacherDiscipline row = teachersDisciplines.get(rowIndex);

        if(0 == columnIndex) {
            row.setId((Integer)aValue);
        }
        else if(1 == columnIndex) {
            row.setTeacher((Teacher) aValue);
        }
        else if(2 == columnIndex) {
            row.setDiscipline((Discipline) aValue);
        }
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex)
    {
        return columnIndex > 0;
    }

    @Override
    public List<TeacherDiscipline> getTableData() {
        return teachersDisciplines;
    }

    @Override
    public TeacherDiscipline getNewRow() {
        return new TeacherDiscipline();
    }

    @Override
    public Dao<TeacherDiscipline> getDao() {
        return new TeacherDisciplineDao();
    }
}