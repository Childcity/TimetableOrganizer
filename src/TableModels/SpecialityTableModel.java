package TableModels;

import Data.Speciality;

import javax.swing.table.AbstractTableModel;
import java.util.List;

public class SpecialityTableModel extends AbstractTableModel implements TableData<Speciality>
{
    private final List<Speciality> specialities;

    private final String[] columnNames = new String[] {
            "Id", "Name", "Numeric Name"
    };
    private final Class[] columnClass = new Class[] {
            Integer.class, String.class, Integer.class
    };

    public SpecialityTableModel(List<Speciality> specialities) {
        this.specialities = specialities;
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
        return specialities.size();
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex)
    {
        //Speciality row = specialityDao.getById(rowIndex + 1).orElse(new Speciality());
        Speciality row = specialities.get(rowIndex);

        if(0 == columnIndex) {
            return row.getId();
        }
        else if(1 == columnIndex) {
            return row.getSpecName();
        }
        else if(2 == columnIndex) {
            return row.getNumericName();
        }
        return null;
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex)
    {
        Speciality row = specialities.get(rowIndex);

        if(0 == columnIndex) {
            row.setId((Integer)aValue);
        }
        else if(1 == columnIndex) {
            row.setSpecName((String) aValue);
        }
        else if(2 == columnIndex) {
            row.setNumericName((Integer)aValue);
        }
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex)
    {
        return columnIndex > 0;
    }

    @Override
    public List<Speciality> getTableData() {
        return specialities;
    }
}