package TableModels;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

public class ComboBoxListRenderer<T> extends JLabel implements ListCellRenderer {
    @Override
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {

        Class type = ((T) new Object()).getClass();
        if (type.isInstance(value)) {
            T item = (T) value;
            setText(item.toString());
        }

        if (isSelected) setBackground(list.getSelectionBackground());
        else setBackground(list.getBackground());

        return this;
    }
}
