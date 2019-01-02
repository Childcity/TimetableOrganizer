import DAO.*;
import Data.*;
import TableModels.ComboBoxListRenderer;
import TableModels.LessonData;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class TimetableEditor extends JDialog {
    private static final int WINDOW_WIDTH = 1500;
    private static final int WINDOW_HEIGHT = 800;
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JButton openDbCreatorButton;
    private JButton saveChangesButton;
    private JPanel timetableBasePanel;
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
            initTimetable();
        });

        groupComboBox.addActionListener(e -> {
            initTimetable();
        });

        weekComboBox.addActionListener(e -> {
            initTimetable();
        });

        saveChangesButton.addActionListener(e -> saveTimetable());

        updateGroupComboBox();
        //initTimetable();
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
    }

    private void createUIComponents() {
        timetablePanel = new JPanel(new GridLayout(2, 6));
        JScrollPane scrollPane = new JScrollPane(timetablePanel);
        timetableBasePanel = new JPanel(new GridLayout());
        timetableBasePanel.add(scrollPane);
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

        JPanel dayPanel;
        JPanel lessonPanel;
        for (int day = 0; day < 6; day++){
            dayPanel = new JPanel();
            dayPanel.setLayout(new BoxLayout(dayPanel, BoxLayout.Y_AXIS));
            dayPanel.setBorder(BorderFactory.createTitledBorder("<html><h2>"+dayList.get(day).getDayName() + "</h2></html>"));
            for (int less = 0; less < 5; less++){
                Lesson lesson = dayList.get(day).getLessons().get(less);
                lessonPanel = new JPanel(new GridLayout(1, 2));
                lessonPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(),
                        "<html>Lesson: " + (lesson.getLessonNumber() + 1) + "<br/>"
                        + lesson.getLessonTime()[0] + "-" + lesson.getLessonTime()[1] + "</html>"));

                JComboBox auditJComboBox = buildAuditoriumComboBox(lesson);
                JComboBox grTchDisComboBox = buildGrTchDisComboBox(lesson, selectedGroup);
                lessonPanel.add(auditJComboBox, 0);
                lessonPanel.add(grTchDisComboBox, 1);

                dayPanel.add(lessonPanel);
            }
            timetablePanel.add(dayPanel);
        }

        timetablePanel.revalidate();
    }

    private JComboBox buildAuditoriumComboBox(Lesson lesson){
        Auditorium existedAudit = lesson.getAuditorium();
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
        auditJComboBox.setRenderer(new ComboBoxListRenderer(lesson));

        return auditJComboBox;
    }

    private JComboBox buildGrTchDisComboBox(Lesson lesson, Group selectedGroup){
        GroupTeacherDiscipline existed = lesson.getGroupTeacherDiscipline();
        List<GroupTeacherDiscipline> grTchDis = new GroupTeacherDisciplineDao().getAllForGroup(selectedGroup);
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
        buildGrTchDis.setRenderer(new ComboBoxListRenderer(lesson));

        return buildGrTchDis;
    }

    private void saveTimetable(){
        int week = weekComboBox.getSelectedIndex() + 1;

        TimetableDao timetableDao = new TimetableDao();
        Timetable timetable;
        JPanel dayPanel;
        JPanel lessonPanel;
        for (int day = 0; day < timetablePanel.getComponentCount(); day++){
            dayPanel = (JPanel) timetablePanel.getComponent(day);
            for (int less = 0; less < dayPanel.getComponentCount(); less++){
                lessonPanel = (JPanel) dayPanel.getComponent(less);

                JComboBox auditComboBox = (JComboBox) lessonPanel.getComponent(0);
                JComboBox grTchDisComboBox = (JComboBox) lessonPanel.getComponent(1);

                Lesson lesson = ((LessonData)auditComboBox.getRenderer()).getConnectedLesson();
                lesson.setAuditorium((Auditorium)auditComboBox.getSelectedItem());
                lesson.setGroupTeacherDiscipline((GroupTeacherDiscipline) grTchDisComboBox.getSelectedItem());

                timetable = new Timetable(lesson.getTimetableId(), week, day, less, lesson.getAuditorium(), lesson.getGroupTeacherDiscipline() );
                if(timetable.getId() != -1 ){
                    if(timetable.getAuditorium().toString().equals("") || timetable.getGroupTeacherDiscipline().toString().equals("")){
                        timetableDao.delete(timetable);
                        continue;
                    }
                }

                if(! (timetable.getAuditorium().toString().equals("") || timetable.getGroupTeacherDiscipline().toString().equals(""))){
                    if(! checkInputData(timetable)){
                        return;
                    }

                    timetableDao.save(timetable);
                }

                //System.out.println(timetable.getId() + " " + timetable.getWeek() + " " + timetable.getDay() + " " + timetable.getLesson() + " " + timetable.getAuditorium() + timetable.getGroupTeacherDiscipline());
            }
        }
    }

    private boolean checkInputData(Timetable newTimetable){
        List<Group> groups = new GroupDao().getAll();

        for (Group group : groups) {
            if (group.getId() != newTimetable.getGroupTeacherDiscipline().getGroup().getId()) {
                List<Day> allDays = new TimetableDao().getAllDays(newTimetable.getWeek(), group);
                Day otherGroupDay = allDays.get(newTimetable.getDay());
                Lesson otherGroupLesson = otherGroupDay.getLessons().get(newTimetable.getLesson());
                if(otherGroupLesson.getTimetableId() != -1){
                    //if auditoriums the same and lesson is not a Lection -> show message
                    if(otherGroupLesson.getAuditorium().getId() == newTimetable.getAuditorium().getId()
                            && (! otherGroupLesson.getAuditorium().getAuditType().equals(AuditoriumType.GetAuditoriums().get(0)))){
                        JOptionPane.showMessageDialog(null,
                                "<html>Conflict with " + newTimetable.getAuditorium() + ": <br/>"
                                + "group: " + newTimetable.getGroupTeacherDiscipline().getGroup().getGroupName() + " - " +group.getGroupName()
                                + "<br/>day: " + Day.GetDayNames().get(newTimetable.getDay())
                                + "<br/>lesson: " + (newTimetable.getLesson() + 1) + "</html>");
                        return false;
                    }
                }
            }
        }

        return true;
    }
}
