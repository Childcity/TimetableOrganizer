package TableModels;

import Data.Speciality;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

public class SpecialityCellRenderer extends DefaultTableCellRenderer {

    public Component getTableCellRendererComponent(JTable table, Object value,
                                                   boolean isSelected, boolean hasFocus, int row, int column) {
        if (value instanceof Speciality) {
            Speciality speciality = (Speciality) value;
            setText(speciality.getSpecName());
        }

        if (isSelected) setBackground(table.getSelectionBackground());
        else setBackground(table.getBackground());

        return this;
    }

}
