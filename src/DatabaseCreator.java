import DAO.*;
import Data.*;
import TableModels.*;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.List;


public class DatabaseCreator extends JDialog implements ActionListener{
    private static final int WINDOW_WIDTH = 800, WINDOW_HEIGHT = 500;

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
    private JButton auditoriumsButton;
    private JButton groupDisciplineTeacherButton;

    private enum  Table {Courses, Specialities, Groups
        , Teachers, Disciplines, TeacherDisciplines
        , Auditoriums, GroupTeacherDiscipline
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
        teacherDisciplinesButton.addActionListener(this);
        auditoriumsButton.addActionListener(this);
        groupDisciplineTeacherButton.addActionListener(this);

        saveChangesButton.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        editableTable.setRowHeight(20);
        String clickedButton = e.getActionCommand();

        if(clickedButton.equals("Save changes")) {
            if(tableData == null){
                return;
            }

            TableData tableDataRef = (TableData) editableTable.getModel();
            List tableDataList = tableDataRef.getTableData();
            tableDataRef.getDao().saveAll(tableDataList);


//            if(currTableState == Table.Courses){
//                new CourseDao().saveAll((List<Course>) tableData);
//                coursesButton.doClick();
//
//            }else if(currTableState == Table.Specialities){
//                new SpecialityDao().saveAll((List<Speciality>) tableData);
//
//            }else if(currTableState == Table.Groups){
//                new GroupDao().saveAll((List<Group>) tableData);
//
//            }else if(currTableState == Table.Teachers){
//                new TeacherDao().saveAll((List<Teacher>) tableData);
//
//            }else if(currTableState == Table.Disciplines){
//
//                new DisciplineDao().saveAll((List<Discipline>) tableData);
//
//            }else if(currTableState == Table.TeacherDisciplines){
//                new TeacherDisciplineDao().saveAll((List<TeacherDiscipline>) tableData);
//
//            }else if(currTableState == Table.Auditoriums){
//                new AuditoriumDao().saveAll((List<Auditorium>) tableData);
//
//            }else if(currTableState == Table.GroupTeacherDiscipline){
//                new GroupTeacherDisciplineDao().saveAll((List<GroupTeacherDiscipline>) tableData);
//            }
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
            currTableState = Table.TeacherDisciplines;
            tablePanel.setBorder(BorderFactory.createTitledBorder("Teacher-Disciplines"));
            tableData = new TeacherDisciplineDao().getAll(); //fill table data
            editableTable.setModel(new TeacherDisciplineTableModel((List<TeacherDiscipline>) tableData));
            editableTable.setDefaultRenderer(Teacher.class, new ComboBoxCellRenderer<>());
            editableTable.setDefaultEditor(Teacher.class, new ComboBoxCellEditor<>(new TeacherDao().getAll()));
            editableTable.setDefaultRenderer(Discipline.class, new ComboBoxCellRenderer<>());
            editableTable.setDefaultEditor(Discipline.class, new ComboBoxCellEditor<>(new DisciplineDao().getAll()));

        }else if(clickedButton.equals("Auditoriums")){
            currTableState = Table.Auditoriums;
            tablePanel.setBorder(BorderFactory.createTitledBorder("Auditoriums"));
            tableData = new AuditoriumDao().getAll(); //fill table data
            editableTable.setModel(new AuditoriumTableModel((List<Auditorium>) tableData));
            editableTable.setDefaultRenderer(AuditoriumType.class, new ComboBoxCellRenderer<>());
            editableTable.setDefaultEditor(AuditoriumType.class, new ComboBoxCellEditor<>(AuditoriumType.GetAuditoriums()));

        }else if(clickedButton.equals("Group-Teacher-Discipline")){
            currTableState = Table.GroupTeacherDiscipline;
            tablePanel.setBorder(BorderFactory.createTitledBorder("Group-Teacher-Discipline"));
            tableData = new GroupTeacherDisciplineDao().getAll(); //fill table data
            editableTable.setModel(new GroupTeacherDisciplineTableModel((List<GroupTeacherDiscipline>) tableData));
            editableTable.setDefaultRenderer(Group.class, new ComboBoxCellRenderer<>());
            editableTable.setDefaultEditor(Group.class, new ComboBoxCellEditor<>(new GroupDao().getAll()));
            editableTable.setDefaultRenderer(TeacherDiscipline.class, new ComboBoxCellRenderer<>());
            editableTable.setDefaultEditor(TeacherDiscipline.class, new ComboBoxCellEditor<>(new TeacherDisciplineDao().getAll()));
            editableTable.getColumnModel().getColumn(2).setPreferredWidth(130);
        }

        if(tablePanel.getComponentCount() >= 2)
            tablePanel.remove(1);
        tablePanel.add(new JScrollPane(editableTable), BorderLayout.CENTER); // add the table to the root panel
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

         //add button to create possibility add new rows in table
        JButton addRowButton = new JButton("Add new raw");
        addRowButton.setFocusPainted(false);
        addRowButton.setBorder(new LineBorder(Color.BLACK));
        addRowButton.addActionListener(e -> {
            List tableData = ((TableData) editableTable.getModel()).getTableData();
            tableData.add(((TableData) editableTable.getModel()).getNewRow());
            editableTable.revalidate();
        });
        tablePanel.add(addRowButton, BorderLayout.PAGE_END);

        editableTable = new JTable();
        editableTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evt) {
                if(SwingUtilities.isRightMouseButton(evt)){
                    showPopupMenu(evt);
                }
                super.mouseClicked(evt);
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
        tablePanel.setLayout(new BorderLayout(10,10));
    }

    private void showPopupMenu(MouseEvent ev){
        int row = editableTable.rowAtPoint(ev.getPoint());
        int col = editableTable.columnAtPoint(ev.getPoint());

        JPopupMenu popup = new JPopupMenu();
        PopupMenuActionListener actionListener = new PopupMenuActionListener(editableTable, row, col);
        JMenuItem itemAddRow = new JMenuItem("Add new row");
        JMenuItem itemDeleteRow = new JMenuItem("Delete row");

        itemAddRow.addActionListener(actionListener);
        itemDeleteRow.addActionListener(actionListener);

        popup.add(itemAddRow);
        popup.add(itemDeleteRow);

        itemAddRow.setHorizontalTextPosition(JMenuItem.LEFT);
        popup.setBorder(new BevelBorder(BevelBorder.RAISED));
        popup.show(DatabaseCreator.this, ev.getX(), ev.getY() + 80);
        //System.out.println("Popup menu " + ev.getPoint());
    }
}

class PopupMenuActionListener implements ActionListener{
    private JTable editableTable;
    private int rowIndex, columnIndex;

    PopupMenuActionListener(JTable editableTable_, int rowIndex_, int columnIndex_){
        editableTable = editableTable_;
        rowIndex = rowIndex_;
        columnIndex = columnIndex_;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String clickedItem = e.getActionCommand();
        TableData tableDataRef = (TableData) editableTable.getModel();
        List tableDataList = tableDataRef.getTableData();

        if(clickedItem.equals("Add new row")){
            tableDataList.add(tableDataRef.getNewRow());

        }else if(clickedItem.equals("Delete row")){
            tableDataRef.getDao().delete(tableDataList.get(rowIndex));
        }

        editableTable.revalidate();
        editableTable.repaint();
    }
}