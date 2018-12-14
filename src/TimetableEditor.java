import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TimetableEditor extends JDialog implements ActionListener {
    private static final int WINDOW_WIDTH = 1000, WINDOW_HEIGHT = 600;
    private JPanel mainPanel;
    private JButton button1;
    private JButton openDataEditorButton;
    private JTable table1;
    private JPanel eastPanel;

    TimetableEditor(){
        setTitle("Timetable Editor"); // dialog title

        setModal(true);
        setLocationRelativeTo(null); // show dialog in center of screen

        setBounds(400, 200, WINDOW_WIDTH, WINDOW_HEIGHT);
        setPreferredSize(new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT));
        eastPanel.setLayout(new BoxLayout(eastPanel, BoxLayout.Y_AXIS));
System.out.println(eastPanel.getComponents().length);
        openDataEditorButton.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String clickedButton = e.getActionCommand();

        if(clickedButton.equals("Open Data Editor")) {
            DatabaseCreator dbCreator = new DatabaseCreator();
            dbCreator.pack();
            dbCreator.setVisible(true);
        }

    }
}
