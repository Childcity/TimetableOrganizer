import DAO.*;
import Data.*;
import TableModels.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;


public class DatabaseCreator extends JDialog implements ActionListener{
    private final int WINDOW_WIDTH = 600, WINDOW_HEIGHT = 450;

    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;

    private JButton coursesButton;
    private JButton specialitiesButton;
    private JButton teachersButton;
    private JButton disciplinesButton;
    private JButton groupsButton;
    private JPanel tablePanel;
    private JButton saveChangesButton;
    private JButton teacherDisciplinesButton;
    private JButton auditoriesButton;
    private JButton groupDisciplineTeacherButton;

    private enum  Table {Courses, Specialities, Groups
        , Teachers, Disciplines, TeacherDisciplines
        , Auditoriums, GroupDisciplineTeacher
    }

    private JTable editableTable;
    private List<?> tableData;
    private Table currTableState;

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

        specialitiesButton.addActionListener(this);
        coursesButton.addActionListener(this);
        groupsButton.addActionListener(this);
        teachersButton.addActionListener(this);
        disciplinesButton.addActionListener(this);
        auditoriesButton.addActionListener(this);
        groupDisciplineTeacherButton.addActionListener(this);

        saveChangesButton.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String clickedButton = e.getActionCommand();

        if(clickedButton.equals("Save changes")) {
            if(tableData == null){
                return;
            }

            if(currTableState == Table.Courses){
                new CourseDao().saveAll((List<Course>) tableData);

            }else if(currTableState == Table.Specialities){
                new SpecialityDao().saveAll((List<Speciality>) tableData);

            }else if(currTableState == Table.Groups){
                new GroupDao().saveAll((List<Group>) tableData);

            }else if(currTableState == Table.Teachers){
                new TeacherDao().saveAll((List<Teacher>) tableData);

            }else if(currTableState == Table.Disciplines){
                new DisciplineDao().saveAll((List<Discipline>) tableData);

            }else if(currTableState == Table.TeacherDisciplines){

            }else if(currTableState == Table.Auditoriums){

            }else if(currTableState == Table.GroupDisciplineTeacher){

            }
            return;
        }

        if(clickedButton.equals("Courses")){
            currTableState = Table.Courses;
            tableData = new CourseDao().getAll(); //fill table data
            editableTable.setModel(new CourseTableModel((List<Course>) tableData));
            tablePanel.setBorder(BorderFactory.createTitledBorder("Courses"));

        }else if(clickedButton.equals("Specialities")){
            currTableState = Table.Specialities;
            tableData = new SpecialityDao().getAll(); //fill table data
            editableTable.setModel(new SpecialityTableModel((List<Speciality>) tableData));
            tablePanel.setBorder(BorderFactory.createTitledBorder("Specialities"));

        }else if(clickedButton.equals("Groups")){
            currTableState = Table.Groups;
            tablePanel.setBorder(BorderFactory.createTitledBorder("Groups"));
            tableData = new GroupDao().getAll(); //fill table data
            editableTable.setModel(new GroupTableModel((List<Group>) tableData));
            editableTable.setDefaultRenderer(Course.class, new ComboBoxCellRenderer<>());
            editableTable.setDefaultEditor(Course.class, new ComboBoxCellEditor<>(new CourseDao().getAll()));
            editableTable.setDefaultRenderer(Speciality.class, new ComboBoxCellRenderer<>());
            editableTable.setDefaultEditor(Speciality.class, new ComboBoxCellEditor<>(new SpecialityDao().getAll()));

        }else if(clickedButton.equals("Teachers")){
            currTableState = Table.Teachers;
            tablePanel.setBorder(BorderFactory.createTitledBorder("Teachers"));
            tableData = new TeacherDao().getAll(); //fill table data
            editableTable.setModel(new TeacherTableModel((List<Teacher>) tableData));
        }else if(clickedButton.equals("Disciplines")){
            currTableState = Table.Disciplines;
            tablePanel.setBorder(BorderFactory.createTitledBorder("Disciplines"));
            tableData = new DisciplineDao().getAll(); //fill table data
            editableTable.setModel(new DisciplineTableModel((List<Discipline>) tableData));
            editableTable.setDefaultRenderer(Speciality.class, new ComboBoxCellRenderer<>());
            editableTable.setDefaultEditor(Speciality.class, new ComboBoxCellEditor<>(new SpecialityDao().getAll()));
            editableTable.setDefaultRenderer(DisciplineType.class, new ComboBoxCellRenderer<>());
            editableTable.setDefaultEditor(DisciplineType.class, new ComboBoxCellEditor<>(DisciplineType.GetDisciplines()));

        }else if(clickedButton.equals("Teacher-Disciplines")){

        }else if(clickedButton.equals("Auditoriums")){

        }else if(clickedButton.equals("Group-Discipline-Teacher")){


        }

        if(tablePanel.getComponentCount() >= 1)
            tablePanel.remove(0);
        tablePanel.add(new JScrollPane(editableTable)); // add the table to the root panel
        revalidate();
    }

    private void initMainPanel() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        setTitle("Database Creator"); // dialog title

        setLocationRelativeTo(null); // show dialog in center of screen

        setBounds(400, 200, WINDOW_WIDTH, WINDOW_HEIGHT);
        setPreferredSize(new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT));

        // add button to create possibility add new rows in table
//        JButton addRowButton = new JButton("Add new raw");
//        addRowButton.setFocusPainted(false);
//        addRowButton.setBorder(new LineBorder(Color.BLACK));
//        tablePanel.add(addRowButton, BorderLayout.PAGE_END);

        editableTable = new JTable();
        editableTable.setRowHeight(20);
        editableTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(SwingUtilities.isRightMouseButton(e)){
                    //TODO: Popup menu
//                    JPopupMenu popup;
//                    popup = new JPopupMenu();
//                    JMenuItem item;
//                    popup.add(item = new JMenuItem("Left"));
//                    item.setHorizontalTextPosition(JMenuItem.RIGHT);
//                    popup.show(DatabaseCreator.this, e.getX(), e.getY());
                    System.out.println("Popup menu " + e.getPoint());
                }
                super.mouseClicked(e);
            }
        });

        // click on Specialities after form loads
        var timer = new Timer(100, e -> {
            coursesButton.doClick();
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
        tablePanel.setLayout(new GridLayout());
//        tablePanel.setLayout(new BorderLayout(10,10));
    }
}
