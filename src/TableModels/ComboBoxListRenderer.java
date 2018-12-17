package TableModels;

import Data.Lesson;

import javax.swing.*;
import java.awt.*;

public class ComboBoxListRenderer<T> extends JLabel implements ListCellRenderer, LessonData {
    private final Font font1 = new Font(getFont().getName(),getFont().getStyle(),getFont().getSize() - 2);
    private final Font font2 = new Font(getFont().getName(),getFont().getStyle(),getFont().getSize());
    private Lesson connectedLesson_;

    public ComboBoxListRenderer(Lesson lesson){
        connectedLesson_ = lesson;
    }

    @Override
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        setPreferredSize(new Dimension(50, 20));

        Class type = ((T) new Object()).getClass();
        if (type.isInstance(value)) {
            T item = (T) value;
            setText(item.toString());
        }

        setOpaque(true);

        if (isSelected) {
            setFont(font2);
            setForeground(Color.BLUE);
            setBackground(list.getSelectionBackground());
        }else {
            setFont(font1);
            setForeground(list.getForeground());
            setBackground(list.getBackground());
        }

        return this;
    }

    @Override
    public Lesson getConnectedLesson() {
        return connectedLesson_;
    }
}
