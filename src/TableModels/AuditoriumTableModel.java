package TableModels;

import DAO.AuditoriumDao;
import DAO.Dao;
import Data.Auditorium;
import Data.AuditoriumType;

import javax.swing.table.AbstractTableModel;
import java.util.List;

public class AuditoriumTableModel extends AbstractTableModel implements TableData<Auditorium>
{
    private final List<Auditorium> auditoriums;

    private final String[] columnNames = new String[] {
            "Id", "Auditorium Number", "Capacity", "Auditorium Type"
    };

    public AuditoriumTableModel(List<Auditorium> auditoriums) {
        this.auditoriums = auditoriums;
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
        return auditoriums.size();
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex)
    {
        //Speciality row = specialityDao.getById(rowIndex + 1).orElse(new Speciality());
        Auditorium row = auditoriums.get(rowIndex);

        if(0 == columnIndex) {
            return row.getId();

        }else if(1 == columnIndex) {
            return row.getAuditNumber();

        }else if(2 == columnIndex) {
            return row.getCapacity();

        }else if(3 == columnIndex) {
            return row.getAuditType();
        }

        return null;
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex)
    {
        Auditorium row = auditoriums.get(rowIndex);

        if(0 == columnIndex) {
            row.setId((Integer)aValue);

        }else if(1 == columnIndex) {
            row.setAuditNumber((Integer) aValue);

        }else if(2 == columnIndex) {
            row.setCapacity((Integer) aValue);

        }else if(3 == columnIndex) {
            row.setAuditType((AuditoriumType) aValue);
        }

    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex)
    {
        return columnIndex > 0;
    }

    @Override
    public List<Auditorium> getTableData() {
        return auditoriums;
    }

    @Override
    public Auditorium getNewRow() {
        return new Auditorium();
    }

    @Override
    public Dao<Auditorium> getDao() {
        return new AuditoriumDao();
    }
}