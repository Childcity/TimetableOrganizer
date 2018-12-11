package TableModels;

import Data.Speciality;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class SpecialityCellEditor extends AbstractCellEditor
        implements TableCellEditor, ActionListener {

    private Speciality speciality;
    private List<Speciality> specialityList;

    public SpecialityCellEditor(List<Speciality> specialityList) {
        this.specialityList = specialityList;
    }

    @Override
    public Object getCellEditorValue() {
        return this.speciality;
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value,
                                                 boolean isSelected, int row, int column) {
        if (value instanceof Speciality) {
            this.speciality = (Speciality) value;
        }

        JComboBox<Speciality> comboSpeciality = new JComboBox<>();
        specialityList.forEach(comboSpeciality::addItem);

        comboSpeciality.setSelectedItem(speciality);
        comboSpeciality.addActionListener(this);

//        if (isSelected) comboSpeciality.setBackground(table.getSelectionBackground());
//        else comboSpeciality.setBackground(table.getSelectionForeground());

        return comboSpeciality;
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        JComboBox<Speciality> comboSpeciality = (JComboBox<Speciality>) event.getSource();
        this.speciality = (Speciality) comboSpeciality.getSelectedItem();
    }

}