import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class StartDlg extends JDialog {
    private final int WINDOW_WIDTH = 350, WINDOW_HEIGHT = 250;

    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JButton createNew;
    private JButton useExistingButton;
    private JLabel dbFilePathLabel;
    private JPanel dbFileNamePanel;

    private JFileChooser dbPathChooser;
    private String dbPath;

    public StartDlg() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        setTitle("Timetable Organizer"); // dialog title

        setBounds(500, 300, WINDOW_WIDTH, WINDOW_HEIGHT); // show dialog in center of screen
        setPreferredSize(new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT));

        // insert label in panel. Set preferred size to panel
        dbFileNamePanel.setLayout(new BorderLayout());
        dbFileNamePanel.setPreferredSize(new Dimension(WINDOW_WIDTH, 20));
        dbFilePathLabel = new JLabel("Database file: ");
        dbFileNamePanel.add(dbFilePathLabel, BorderLayout.CENTER);

        // setup fileChooser
        dbPathChooser = new JFileChooser(); // Create file dialog for user to select file name for new db
        dbPathChooser.setCurrentDirectory(new File(".")); // start at application current directory
        dbPathChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

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

        createNew.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String currentDateTime = new SimpleDateFormat("dd-MM-yyyy_HH-mm-ss").format(new Date()); // get current date and time
                dbPathChooser.setSelectedFile(new File(".\\timetable_" + currentDateTime + ".sqlite3")); // set default name for db
                // show file dialog and result (what  user passed) save to 'result'
                int result = dbPathChooser.showSaveDialog(null);
                if(result == JFileChooser.APPROVE_OPTION){ // if user pressed OK and entered path to new db
                    updateFilePath();
                    DatabaseCreator dbCreator = new DatabaseCreator();
                    dbCreator.pack();
                    dbCreator.setVisible(true);
                }
            }

        });
        useExistingButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // show file dialog and result (what  user passed) save to 'result'
                int result = dbPathChooser.showSaveDialog(null);
                if(result == JFileChooser.APPROVE_OPTION){ // if user pressed OK and entered path to new db
                    updateFilePath();
                }
            }
        });
    }

    private void onOK() {

    }

    private void onCancel() {
        dispose(); // just close Dialog
    }

    public static void main(String[] args) {
        StartDlg dialog = new StartDlg();
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }

    private void updateFilePath() {
        dbPath = dbPathChooser.getSelectedFile().getPath();
        dbFilePathLabel.setText(String.format(
                "<html><p width=%d>" +
                        "Database&nbsp;file:&nbsp;" + dbPath +
                        "</p></html>",(WINDOW_WIDTH - 70))
        );
    }
}
