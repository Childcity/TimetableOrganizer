package TableModels;

import DAO.TeacherDisciplineDao;
import Data.Group;
import Data.TeacherDiscipline;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class TeacherDisciplineComboBoxCellEditor extends AbstractCellEditor
        implements TableCellEditor, ActionListener {

    private TeacherDiscipline currentItem;
    private TableModel groupTeacherDisciplineTableModel;

    public TeacherDisciplineComboBoxCellEditor(TableModel groupTeacherDisciplineTableModel) {
        this.groupTeacherDisciplineTableModel = groupTeacherDisciplineTableModel;
    }

    @Override
    public Object getCellEditorValue() {
        return this.currentItem;
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value,
                                                 boolean isSelected, int row, int column) {
        if (value instanceof TeacherDiscipline) {
            this.currentItem = (TeacherDiscipline) value;
        }

        Group group = (Group) groupTeacherDisciplineTableModel.getValueAt(row, column - 1);

        List<TeacherDiscipline> itemList = new TeacherDisciplineDao().getAllForGroup(group);
        JComboBox<TeacherDiscipline> comboItem = new JComboBox<>();
        itemList.forEach(comboItem::addItem);

        comboItem.setSelectedItem(currentItem);
        comboItem.addActionListener(this);


        return comboItem;
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        JComboBox<TeacherDiscipline> comboItem = (JComboBox<TeacherDiscipline>) event.getSource();
        this.currentItem = (TeacherDiscipline) comboItem.getSelectedItem();
    }

}