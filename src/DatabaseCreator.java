import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class DatabaseCreator extends JDialog {
    private final int WINDOW_WIDTH = 600, WINDOW_HEIGHT = 450;

    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JButton coursesButton;
    private JButton specialitiesButton;
    private JButton teachersButton;
    private JButton classesButton;
    private JButton groupsButton;
    private JPanel tablePanel;

    public DatabaseCreator() {
        initRootPanel();

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void initRootPanel() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        setTitle("Database Creator"); // dialog title

        setLocationRelativeTo(null); // show dialog in center of screen

        setBounds(400, 200, WINDOW_WIDTH, WINDOW_HEIGHT);
        setPreferredSize(new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT));
    }

    private void onOK() {
        // add your code here
        dispose();
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }
}
