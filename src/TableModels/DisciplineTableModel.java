package TableModels;

import Data.Discipline;
import Data.DisciplineType;
import Data.Speciality;

import javax.swing.*;
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
            String  newDisciplineName = ((String) aValue).replaceAll("^ +| +$|( )+", "$1");
            if(checkDisciplineName(newDisciplineName, rowIndex, columnIndex)){
                row.setDisName(newDisciplineName);
            }
        }
        else if(2 == columnIndex) {
            row.setHoursTotal((Integer) aValue);
        }
        else if(3 == columnIndex) {
            DisciplineType newDisciplineType = (DisciplineType) aValue;
            if(checkDisciplineType(newDisciplineType, rowIndex, columnIndex))
            row.setDisType(newDisciplineType);
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

    private boolean checkDisciplineName(String newDiscipline, int rowIndex, int columnIndex){
        for (int i = 0; i < disciplines.size(); i++){
            if(i != rowIndex) {
                if (disciplines.get(i).getDisName().equals(newDiscipline)) {
                    //JOptionPane.showMessageDialog(null, disciplines.get(i).getDisType().getType() + " " + disciplines.get(rowIndex).getDisType().getType());
                    if(disciplines.get(i).getDisType().equals(disciplines.get(rowIndex).getDisType())){
                        JOptionPane.showMessageDialog(null, "Two Disciplines have the same Type!");
                        return false;
                    }
                }
            }
        }
        return true;
    }

    private boolean checkDisciplineType(DisciplineType newDisciplineType, int rowIndex, int columnIndex){
        for (int i = 0; i < disciplines.size(); i++){
            if(i != rowIndex) {
                if(disciplines.get(i).getDisType().equals(newDisciplineType)){
                    if (disciplines.get(i).getDisName().equals(disciplines.get(rowIndex).getDisName())) {
                        //JOptionPane.showMessageDialog(null, disciplines.get(i).getDisType().getType() + " " + disciplines.get(rowIndex).getDisType().getType());
                        JOptionPane.showMessageDialog(null, "Two Types have the same Discipline!");
                        return false;
                    }
                }
            }
        }
        return true;
    }
}