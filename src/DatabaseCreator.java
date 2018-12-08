import DAO.Speciality;
import DAO.SpecialityDao;
import TableModels.SpecialityTableModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

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
    private JButton saveChangesButton;

    private JTable editableTable;
    private List<?> tableData;


    public DatabaseCreator() {
        initMainPanel();

        buttonOK.addActionListener(e -> onOK());

        buttonCancel.addActionListener(e -> onCancel());

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(e -> onCancel(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        saveChangesButton.addActionListener(e -> {
            if(tableData == null || tableData.isEmpty()){
                return;
            }

            if(tableData.get(0) instanceof Speciality){
                new SpecialityDao().saveAll((List<Speciality>) tableData);
            }
        });
        specialitiesButton.addActionListener(e -> {
            // create table
            tableData = new SpecialityDao().getAll(); //fill table data
            editableTable = new JTable(new SpecialityTableModel((List<Speciality>) tableData));
            tablePanel.removeAll();
            tablePanel.add(new JScrollPane(editableTable)); // add the table to the root panel
            revalidate();
        });
    }

    private void initMainPanel() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        setTitle("Database Creator"); // dialog title

        setLocationRelativeTo(null); // show dialog in center of screen

        setBounds(400, 200, WINDOW_WIDTH, WINDOW_HEIGHT);
        setPreferredSize(new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT));

        var timer = new Timer(100, e -> {
            specialitiesButton.doClick();
        });
        timer.setRepeats(false);
        timer.start();
    }

    private void onOK() {
        // add your code here
        dispose();
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

    private void createUIComponents() {
        tablePanel = new JPanel();
        tablePanel.setLayout(new BorderLayout(10,10));
        // TODO: place custom component creation code here
    }
}
