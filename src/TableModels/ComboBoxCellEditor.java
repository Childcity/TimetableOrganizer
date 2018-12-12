package TableModels;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class ComboBoxCellEditor<T> extends AbstractCellEditor
        implements TableCellEditor, ActionListener {

    private T currentItem;
    private List<T> itemList;

    public ComboBoxCellEditor(List<T> itemList) {
        this.itemList = itemList;
    }

    @Override
    public Object getCellEditorValue() {
        return this.currentItem;
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value,
                                                 boolean isSelected, int row, int column) {
        Class type = ((T) new Object()).getClass();
        if (type.isInstance(value)) {
            this.currentItem = (T) value;
        }

        JComboBox<T> comboItem = new JComboBox<>();
        itemList.forEach(comboItem::addItem);

        comboItem.setSelectedItem(currentItem);
        comboItem.addActionListener(this);

//        if (isSelected) comboGroup.setBackground(table.getSelectionBackground());
//        else comboGroup.setBackground(table.getSelectionForeground());

        return comboItem;
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        JComboBox<T> comboItem = (JComboBox<T>) event.getSource();
        this.currentItem = (T) comboItem.getSelectedItem();
    }

}