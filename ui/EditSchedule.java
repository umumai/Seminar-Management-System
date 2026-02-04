package ui;
import Database.DBHelper;
import java.awt.*;
import java.util.ArrayList;
import javax.swing.*;
import models.Session;

public class EditSchedule extends JPanel{
    private JFrame frame;
    String state = "edit";
    public Color deepBlue = new Color(14,69,128);
    //public Color beige = new Color(228, 219, 210);
    
    public EditSchedule(JFrame frame){
        this.frame=frame;
        setLayout(new BorderLayout());
        setBackground(deepBlue);

        JPanel topPanel = new JPanel(new BorderLayout());
        //topPanel.setBackground(beige);
        topPanel.setBorder(BorderFactory.createEmptyBorder(5,15,5,5));
        JLabel coordinatorLabel = new JLabel("Manage Schedule"); 
        JButton returnButton = new JButton("return");
        returnButton.addActionListener(e->{
            ((LoginFrame) frame).showPanel("CoordinatorPanel");
        });
        topPanel.add(coordinatorLabel,BorderLayout.WEST);
        topPanel.add(returnButton,BorderLayout.EAST);

        //datas
        ArrayList<String> dateList = new ArrayList<>();
        dateList.add("12-OCT-2025");
        dateList.add("28-JAN-2026");
        dateList.add("3-FEB-2026");
        // later you can add more dynamically
        dateList.add("date");

        //panels are stored inside tabbedPane
        JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.LEFT);
        //configure how many session exist
        int sessionList = DBHelper.getSessionCount();
        System.err.println("DEBUG : Total session exist = " + sessionList);
        for (int i = 1; i <= sessionList; i++) {
            JPanel mainPanel = new JPanel(new BorderLayout());
            //adjust the tab name here
            String tabsName = "Session " + i;
            Session session = DBHelper.getSession(i);
            //create stuffs here==========
            
        
        JPanel detailsPanel = new JPanel(new GridLayout(3, 1, 2, 2));
        detailsPanel.setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 10));
        detailsPanel.add(new JLabel("Date : " + session.getDate()));
        detailsPanel.add(new JLabel("Venue : " + session.getVenue()));
        detailsPanel.add(new JLabel("Presentation Type : " + session.getSessionType()));
        
        //table panel
        JPanel tablePanel = new JPanel(new GridLayout(9, 3, 1, 1));
        tablePanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        //header row
        JButton R1C1 = headerBox("Time", Color.BLACK, Color.white);
        JButton R1C2 = headerBox("Student Name", Color.BLACK, Color.white);
        JButton R1C3 = headerBox("Evaluator Name", Color.BLACK, Color.white);
        //9:00AM row
        JButton R2C1 = headerBox("9:00AM", Color.BLACK, Color.white);
        JButton R2C2 = studentBox("", Color.white, Color.black);
        JButton R2C3 = evaluatorBox("", Color.white, Color.black);
        //10:00AM row
        JButton R3C1 = headerBox("10:00AM", Color.BLACK, Color.white);
        JButton R3C2 = studentBox("", Color.white, Color.black);
        JButton R3C3 = evaluatorBox("", Color.white, Color.black);
        //11:00AM row
        JButton R4C1 = headerBox("11:00AM", Color.BLACK, Color.white);
        JButton R4C2 = studentBox("", Color.white, Color.black);
        JButton R4C3 = evaluatorBox("", Color.white, Color.black);
        //12:00PM row
        JButton R5C1 = headerBox("12:00PM", Color.BLACK, Color.white);
        JButton R5C2 = studentBox("", Color.white, Color.black);
        JButton R5C3 = evaluatorBox("", Color.white, Color.black);
        //1:00PM row
        JButton R6C1 = headerBox("1:00PM", Color.BLACK, Color.white);
        JButton R6C2 = studentBox("", Color.white, Color.black);
        JButton R6C3 = evaluatorBox("", Color.white, Color.black);
        //2:00PM row
        JButton R7C1 = headerBox("2:00PM", Color.BLACK, Color.white);
        JButton R7C2 = studentBox("", Color.white, Color.black);
        JButton R7C3 = evaluatorBox("", Color.white, Color.black);
        //3:00PM row
        JButton R8C1 = headerBox("3:00PM", Color.BLACK, Color.white);
        JButton R8C2 = studentBox("", Color.white, Color.black);
        JButton R8C3 = evaluatorBox("", Color.white, Color.black);
        //4:00PM row
        JButton R9C1 = headerBox("4:00PM", Color.BLACK, Color.white);
        JButton R9C2 = studentBox("", Color.white, Color.black);
        JButton R9C3 = evaluatorBox("", Color.white, Color.black);

        //add all the JButton into the table panel
        tablePanel.add(R1C1);
        tablePanel.add(R1C2);
        tablePanel.add(R1C3);

        tablePanel.add(R2C1);
        tablePanel.add(R2C2);
        tablePanel.add(R2C3);

        tablePanel.add(R3C1);
        tablePanel.add(R3C2);
        tablePanel.add(R3C3);

        tablePanel.add(R4C1);
        tablePanel.add(R4C2);
        tablePanel.add(R4C3);

        tablePanel.add(R5C1);
        tablePanel.add(R5C2);
        tablePanel.add(R5C3);

        tablePanel.add(R6C1);
        tablePanel.add(R6C2);
        tablePanel.add(R6C3);

        tablePanel.add(R7C1);
        tablePanel.add(R7C2);
        tablePanel.add(R7C3);

        tablePanel.add(R8C1);
        tablePanel.add(R8C2);
        tablePanel.add(R8C3);

        tablePanel.add(R9C1);
        tablePanel.add(R9C2);
        tablePanel.add(R9C3);


        JButton saveButton = buttonEdit();

        //bottom panel
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(0, 20, 10, 20));
        bottomPanel.add(saveButton,BorderLayout.EAST);        

            //add other panels to main Panel
            mainPanel.add(detailsPanel, BorderLayout.NORTH);
            mainPanel.add(tablePanel, BorderLayout.CENTER);
            mainPanel.add(bottomPanel, BorderLayout.SOUTH);
            //mainPanel.add(label);
            //===========
            //add each tabs
            tabbedPane.addTab(tabsName, mainPanel);
        }

        //final setup
        add(topPanel,BorderLayout.NORTH);
        add(tabbedPane, BorderLayout.CENTER);
    }

    //====================================================================================
    private void changeStudName(JButton btn) {

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

    private void changeEvalName(JButton btn) {

    //convert the string into a combobox
    ArrayList<String> evaluatorList = new ArrayList<>();
    evaluatorList.add("Ng Hu");
    evaluatorList.add("Willie Poh");
    evaluatorList.add("Farhah");
    // later you can add more dynamically
    evaluatorList.add("Khairil");

    // Convert ArrayList to array
    String[] evaluatorListArray = evaluatorList.toArray(new String[0]);
    JComboBox<String> roleCombo = new JComboBox<>(evaluatorListArray);

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

            for (String choice : evaluatorList) {
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


    public JButton headerBox(String text, Color bgColor, Color textColor){
        JButton btn = new JButton(text);
        btn.setBackground(bgColor);
        btn.setForeground(textColor);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setEnabled(false);
        return btn;
    }

    public JButton studentBox(String text, Color bgColor, Color textColor){
        JButton btn = new JButton(text);
        btn.setBackground(bgColor);
        btn.setForeground(textColor);
        btn.setFocusPainted(false);
        // btn.setBorderPainted(true);
        btn.addActionListener(e -> {
            if ("save".equals(state)) {
                changeStudName(btn);
            }
        });
        return btn;
    }

    public JButton evaluatorBox(String text, Color bgColor, Color textColor){
        JButton btn = new JButton(text);
        btn.setBackground(bgColor);
        btn.setForeground(textColor);
        btn.setFocusPainted(false);
        // btn.setBorderPainted(true);
        btn.addActionListener(e -> {
            if ("save".equals(state)) {
                changeEvalName(btn);
            }
        });
        return btn;
    }

    public JButton buttonEdit(){
        JButton btn = new JButton("edit");
        btn.setBackground(deepBlue);
        btn.setForeground(Color.white);
        btn.setFocusPainted(false);
        
        //when button is clicked :
        btn.addActionListener(e->{
            if("save".equals(state)){
            // when save button is clicked, button = edit. table = unenabled
            btn.setText("edit");
            btn.setBackground(deepBlue);
            state = "edit";
            System.out.println("state = "+state);
        } else if ("edit".equals(state)) {
                btn.setText("save");
                btn.setBackground(Color.green);
                state = "save";
                System.out.println("state = "+state); }
        });
        
        // when save button is clicked, switch to other screen
        //btn.addActionListener(e -> frame.showScreen("VIEW-SCHEDULE")); 
        return btn;
    }
}
