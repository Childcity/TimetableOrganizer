import DAO.AuditoriumDao;
import DAO.GroupDao;
import DAO.GroupTeacherDisciplineDao;
import DAO.TimetableDao;
import Data.*;
import TableModels.ComboBoxCellRenderer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Vector;

public class TimetableEditor extends JDialog {
    private static final int WINDOW_WIDTH = 1500;
    private static final int WINDOW_HEIGHT = 800;
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JButton openDbCreatorButton;
    private JButton specialitiesButton;
    private JButton saveChangesButton;
    private JPanel timetablePanel;
    private JComboBox weekComboBox;
    private JComboBox groupComboBox;

    public TimetableEditor() {
        setBounds(400, 200, WINDOW_WIDTH, WINDOW_HEIGHT);
        setPreferredSize(new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT));
        setTitle("Timetable Editor"); // dialog title
        setLocationRelativeTo(null); // show dialog in center of screen
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);
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


        openDbCreatorButton.addActionListener(e -> {
            DatabaseCreator dbCreator = new DatabaseCreator();
            dbCreator.pack();
            dbCreator.setVisible(true);
            updateGroupComboBox();
        });

        groupComboBox.addActionListener(e -> {
            initTimetable();
        });

        weekComboBox.addActionListener(e -> {
            initTimetable();
        });

        updateGroupComboBox();
    }

    private void onOK() {
        dispose();
    }

    private void onCancel() {
        dispose();
    }

    private void updateGroupComboBox(){
        List<Group> groups = new GroupDao().getAll();
        String[] groupNames = new String[groups.size()];
        for (int i = 0; i < groupNames.length; i++) {
            groupNames[i] = groups.get(i).getGroupName();
        }
        DefaultComboBoxModel model = new DefaultComboBoxModel(groupNames);
        groupComboBox.setModel(model);
        if(groupNames.length > 0)
            groupComboBox.setSelectedIndex(0);
        initTimetable();
    }

    private void createUIComponents() {
        timetablePanel = new JPanel(new GridLayout(2, 6));
    }

    private void initTimetable(){
        timetablePanel.removeAll();

        List<Group> groups = new GroupDao().getAll();
        Group selectedGroup = null;
        for (Group gr : groups){
            if(gr.getGroupName().equals(groupComboBox.getSelectedItem())){
                selectedGroup = gr;
                break;
            }
        }

        List<Day> dayList = new TimetableDao().getAllDays(weekComboBox.getSelectedIndex()+1, selectedGroup);

        JPanel dayPanel = null;
        JPanel lessonPanel = null;
        for (int day = 0; day < 6; day++){
            dayPanel = new JPanel();
            dayPanel.setLayout(new BoxLayout(dayPanel, BoxLayout.Y_AXIS));
            dayPanel.setBorder(BorderFactory.createTitledBorder("<html><h2>"+dayList.get(day).getDayName() + "</h2></html>"));
            for (int less = 0; less < 5; less++){
                Lesson lesson = dayList.get(day).getLessons().get(less);
                lessonPanel = new JPanel(new GridLayout(1, 2));
                lessonPanel.setBorder(BorderFactory.createTitledBorder(
                        "<html>Lesson: " + (lesson.getLessonNumber() + 1) + "<br/>"
                        + lesson.getLessonTime()[0] + "-" + lesson.getLessonTime()[1] + "</html>"));

                JComboBox auditJComboBox = buildAuditoriumComboBox(lesson.getAuditorium());
                JComboBox grTchDisComboBox = buildGrTchDisComboBox(lesson.getGroupTeacherDiscipline());
                lessonPanel.add(auditJComboBox, 0);
                lessonPanel.add(grTchDisComboBox, 1);

                dayPanel.add(lessonPanel);
            }
            timetablePanel.add(dayPanel);
        }
    }

    private JComboBox buildAuditoriumComboBox(Auditorium existedAudit){
        List<Auditorium> auditoriums = new AuditoriumDao().getAll();
        auditoriums.add(new Auditorium()); //Empty audit

        Object[] auditoriumObjects= new Object[auditoriums.size()];
        int indexToSelect = auditoriumObjects.length - 1;

        for (int i = 0; i < auditoriumObjects.length; i++) {
            auditoriumObjects[i] = auditoriums.get(i);
            if(existedAudit.getId() != -1 && auditoriums.get(i).getId() == existedAudit.getId()){
                indexToSelect = i;
            }
        }

        JComboBox auditJComboBox = new JComboBox(auditoriumObjects);
        auditJComboBox.setSelectedIndex(indexToSelect);

        return auditJComboBox;
    }

    private JComboBox buildGrTchDisComboBox(GroupTeacherDiscipline existed){
        List<GroupTeacherDiscipline> grTchDis = new GroupTeacherDisciplineDao().getAll();
        grTchDis.add(new GroupTeacherDiscipline()); //Empty audit

        Object[] auditoriumObjects= new Object[grTchDis.size()];
        int indexToSelect = auditoriumObjects.length - 1;

        for (int i = 0; i < auditoriumObjects.length; i++) {
            auditoriumObjects[i] = grTchDis.get(i);
            if(existed.getId() != -1 && grTchDis.get(i).getId() == existed.getId()){
                indexToSelect = i;
            }
        }

        JComboBox buildGrTchDis = new JComboBox(auditoriumObjects);
        buildGrTchDis.setSelectedIndex(indexToSelect);

        return buildGrTchDis;
    }
}
