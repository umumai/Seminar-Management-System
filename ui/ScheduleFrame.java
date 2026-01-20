package ui;
import java.awt.*;
import java.util.ArrayList;
import javax.swing.*;

public class ScheduleFrame extends JPanel {
    private JFrame frame;
    public Color deepBlue = new Color(14,69,128);
    private void changeName(JButton btn) {

    //convert the string into a combobox
    ArrayList<String> studentList = new ArrayList<>();
    studentList.add("Fatimah");
    studentList.add("Ummu");
    studentList.add("Nisah");
    // later you can add more dynamically
    studentList.add("Busyra");

    // Convert ArrayList to array
    String[] studentListArray = studentList.toArray(new String[0]);
    JComboBox<String> roleCombo = new JComboBox<>(studentListArray);

    JPanel panel = new JPanel(new GridLayout(0, 1));
    panel.add(new JLabel("Choose role:"));
    panel.add(roleCombo);

    int result = JOptionPane.showConfirmDialog(
            this,
            panel,
            "Select Role",
            JOptionPane.OK_CANCEL_OPTION,
            JOptionPane.PLAIN_MESSAGE
    );

    if (result == JOptionPane.OK_OPTION) {
        //item that are chosen from the combobox stored inside role
        String selectedChoice = (String) roleCombo.getSelectedItem();

        for (String choice : studentList) {
            if (selectedChoice.equals(choice)) {
                System.out.println(choice + " selected");
                btn.setText(choice);
                //result = choice [choice = "Fatimah"]
                // openRoleScreen(role); // call function dynamically
                break;
            }
        }
    }
}

    public ScheduleFrame(JFrame frame) {
        this.frame = frame;
        // super("Postgraduate Academic Research Seminar");
        // setSize(600, 400);
        // setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        //main panel
        JPanel mainPanel = new JPanel(new BorderLayout());
        ArrayList<String> dateList = new ArrayList<>();
        dateList.add("12-OCT-2025");
        dateList.add("28-JAN-2026");
        dateList.add("3-FEB-2026");
        // later you can add more dynamically
        dateList.add("Busyra");
        //top panel
        JPanel topPanel = new JPanel(new GridLayout(3, 1, 2, 2));
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 10));
        topPanel.add(new JLabel("Date : " + dateList.get(0)));
        topPanel.add(new JLabel("Venue : "));
        topPanel.add(new JLabel("Presentation Type : "));
        
        //table panel
        JPanel tablePanel = new JPanel(new GridLayout(9, 3, 1, 1));
        tablePanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        //header row
        createBox("Time", Color.BLACK, Color.white, tablePanel);
        createBox("Student Name", Color.BLACK, Color.white, tablePanel);
        createBox("Evaluator Name", Color.BLACK, Color.white, tablePanel);
        //9:00AM row
        createBox("9:00AM", Color.BLACK, Color.white, tablePanel);
        JButton btn1 = createButton("tea", Color.white, Color.black, tablePanel);
        createButton("", Color.white, Color.black, tablePanel);
        //10:00AM row
        createBox("10:00AM", Color.BLACK, Color.white, tablePanel);
        createButton("", Color.white, Color.black, tablePanel);
        createButton("", Color.white, Color.black, tablePanel);
        //11:00AM row
        createBox("11:00AM", Color.BLACK, Color.white, tablePanel);
        createButton("", Color.white, Color.black, tablePanel);
        createButton("", Color.white, Color.black, tablePanel);
        //12:00PM row
        createBox("12:00PM", Color.BLACK, Color.white, tablePanel);
        createButton("", Color.white, Color.black, tablePanel);
        createButton("", Color.white, Color.black, tablePanel);
        //1:00PM row
        createBox("1:00PM", Color.BLACK, Color.white, tablePanel);
        createButton("", Color.white, Color.black, tablePanel);
        createButton("", Color.white, Color.black, tablePanel);
        //2:00PM row
        createBox("2:00PM", Color.BLACK, Color.white, tablePanel);
        createButton("", Color.white, Color.black, tablePanel);
        createButton("", Color.white, Color.black, tablePanel);
        //3:00PM row
        createBox("3:00PM", Color.BLACK, Color.white, tablePanel);
        createButton("", Color.white, Color.black, tablePanel);
        createButton("", Color.white, Color.black, tablePanel);
        //4:00PM row
        createBox("4:00PM", Color.BLACK, Color.white, tablePanel);
        createButton("", Color.white, Color.black, tablePanel);
        createButton("", Color.white, Color.black, tablePanel);

        //bottom panel - might delete later kalau xguna lgsung
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(0, 20, 10, 20));
        // bottomPanel.add(createButtonSave("save", Color.green, Color.white, bottomPanel),BorderLayout.EAST);        
     
        //add other panels to main Panel
        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(tablePanel, BorderLayout.CENTER);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        //final setup
        add(mainPanel);
        setVisible(true);
    }

    public JButton createBox(String text, Color bgColor, Color textColor, JPanel panel){
        JButton btn = new JButton(text);
        btn.setBackground(bgColor);
        btn.setForeground(textColor);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setEnabled(false);
        panel.add(btn);
        return btn;
    }

    public JButton createButton(String text, Color bgColor, Color textColor, JPanel panel){
        JButton btn = new JButton(text);
        btn.setBackground(bgColor);
        btn.setForeground(textColor);
        btn.setEnabled(false);
        // btn.setBorderPainted(true);
        panel.add(btn);
        return btn;
    }

    public JButton createButtonSave(String text, Color bgColor, Color textColor, JPanel panel){
        JButton btn = new JButton(text);
        btn.setBackground(bgColor);
        btn.setForeground(textColor);
        btn.setFocusPainted(false);
        panel.add(btn);
        return btn;
    }

    // public static void main(String[] args) {
    //     new ScheduleFrame();
    // }
}