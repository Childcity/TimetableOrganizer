package TableModels;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

public class ComboBoxCellRenderer<T> extends DefaultTableCellRenderer {
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
                                                   boolean isSelected, boolean hasFocus,
                                                   int row, int column) {
        Class type = ((T) new Object()).getClass();
        if (type.isInstance(value)) {
            T item = (T) value;
            setText(item.toString());
        }

        if (isSelected) setBackground(table.getSelectionBackground());
        else setBackground(table.getBackground());

        return this;
    }
}
