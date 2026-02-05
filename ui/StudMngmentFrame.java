package ui;
import Database.DBHelper;
import java.awt.*;
import java.util.ArrayList;
import javax.swing.*;
import models.Session;

//dalam coordinator frame ni kita just place the JTabbedPane. 
// and then we place the other frame (from other file) into each tabs
public class StudMngmentFrame extends JPanel {
    private JFrame frame;
    private String students;
    private String evaluators;
    String state = "edit";
    public Color deepBlue = new Color(14,69,128);
    
    
    public StudMngmentFrame(JFrame frame) {
        this.frame=frame;
        setLayout(new BorderLayout());

        //top Panel
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBorder(BorderFactory.createEmptyBorder(5,15,5,5));
        JLabel coordinatorLabel = new JLabel("Student Management"); 
        JButton returnButton = new JButton("return");
        returnButton.addActionListener(e->{((LoginFrame) frame).showPanel("CoordinatorPanel");});
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

        int counter = 1;
        //header row
        JButton R1C1 = box("Student ID", deepBlue, Color.white);
        JButton R1C2 = box("Student Name", deepBlue, Color.white);
        JButton R1C3 = box("Evaluator Name", deepBlue, Color.white);
        JButton R1C4 = box("Status", deepBlue, Color.white);

        //1st student
        
        JButton R2C1 = box(DBHelper.getStudentIdFromAppointment(i,counter), deepBlue, Color.white);
        JButton R2C2 = studentBox(DBHelper.getStudentNameById(DBHelper.getStudentIdFromAppointment(i,counter)), Color.white, deepBlue);
        JButton R2C3 = evaluatorBox("", Color.white, deepBlue);
        JButton R2C4 = statusBox("", Color.white, deepBlue);
        if ((DBHelper.getStudentIdFromAppointment(i,counter)) != null ){
            R2C4.setText("pending");
        }
        counter++;

        //2nd student
        JButton R3C1 = box(DBHelper.getStudentIdFromAppointment(i,counter), deepBlue, Color.white);
        JButton R3C2 = studentBox(DBHelper.getStudentNameById(DBHelper.getStudentIdFromAppointment(i,counter)), Color.white, deepBlue);
        JButton R3C3 = evaluatorBox("", Color.white, deepBlue);
        JButton R3C4 = statusBox("", Color.white, deepBlue);
        if ((DBHelper.getStudentIdFromAppointment(i,counter++)) != null ){
            R3C4.setText("pending");
        }
        counter++;

        //3rd student
        JButton R4C1 = box(DBHelper.getStudentIdFromAppointment(i,counter), deepBlue, Color.white);
        JButton R4C2 = studentBox(DBHelper.getStudentNameById(DBHelper.getStudentIdFromAppointment(i,counter)), Color.white, deepBlue);
        JButton R4C3 = evaluatorBox("", Color.white, deepBlue);
        JButton R4C4 = statusBox("", Color.white, deepBlue);
        if ((DBHelper.getStudentIdFromAppointment(i,counter)) != null ){
            R4C4.setText("pending");
        }
        counter++;

        //4th student
        JButton R5C1 = box(DBHelper.getStudentIdFromAppointment(i,counter), deepBlue, Color.white);
        JButton R5C2 = studentBox(DBHelper.getStudentNameById(DBHelper.getStudentIdFromAppointment(i,counter)), Color.white, deepBlue);
        JButton R5C3 = evaluatorBox("", Color.white, deepBlue);
        JButton R5C4 = statusBox("", Color.white, deepBlue);
        if ((DBHelper.getStudentIdFromAppointment(i,counter)) != null ){
            R5C4.setText("pending");
        }
        counter++;

        //5th student
        JButton R6C1 = box(DBHelper.getStudentIdFromAppointment(i,counter), deepBlue, Color.white);
        JButton R6C2 = studentBox(DBHelper.getStudentNameById(DBHelper.getStudentIdFromAppointment(i,counter)), Color.white, deepBlue);
        JButton R6C3 = evaluatorBox("", Color.white, deepBlue);
        JButton R6C4 = statusBox("", Color.white, deepBlue);
        if ((DBHelper.getStudentIdFromAppointment(i,counter)) != null ){
            R6C4.setText("pending");
        }
        counter++;

        //6th student
        JButton R7C1 = box(DBHelper.getStudentIdFromAppointment(i,counter), deepBlue, Color.white);
        JButton R7C2 = studentBox(DBHelper.getStudentNameById(DBHelper.getStudentIdFromAppointment(i,counter)), Color.white, deepBlue);
        JButton R7C3 = evaluatorBox("", Color.white, deepBlue);
        JButton R7C4 = statusBox("", Color.white, deepBlue);
        if ((DBHelper.getStudentIdFromAppointment(i,counter)) != null ){
            R7C4.setText("pending");
        }
        counter++;

        //7th student
        JButton R8C1 = box(DBHelper.getStudentIdFromAppointment(i,counter), deepBlue, Color.white);
        JButton R8C2 = studentBox(DBHelper.getStudentNameById(DBHelper.getStudentIdFromAppointment(i,counter)), Color.white, deepBlue);
        JButton R8C3 = evaluatorBox("", Color.white, deepBlue);
        JButton R8C4 = statusBox("", Color.white, deepBlue);
        if ((DBHelper.getStudentIdFromAppointment(i,counter)) != null ){
            R8C4.setText("pending");
        }
        counter++;

        //8th student
        JButton R9C1 = box(DBHelper.getStudentIdFromAppointment(i,counter), deepBlue, Color.white);
        JButton R9C2 = studentBox(DBHelper.getStudentNameById(DBHelper.getStudentIdFromAppointment(i,counter)), Color.white, deepBlue);
        JButton R9C3 = evaluatorBox("", Color.white, deepBlue);
        JButton R9C4 = statusBox("", Color.white, deepBlue);
        if ((DBHelper.getStudentIdFromAppointment(i,counter)) != null ){
            R5C4.setText("pending");
        }


        if ((DBHelper.getStudentIdFromAppointment(i, 0)) != null){

        //add all the JButton into the table panel
        tablePanel.add(R1C1);
        tablePanel.add(R1C2);
        tablePanel.add(R1C3);
        tablePanel.add(R1C4);

        tablePanel.add(R2C1);
        tablePanel.add(R2C2);
        tablePanel.add(R2C3);
        tablePanel.add(R2C4);

        tablePanel.add(R3C1);
        tablePanel.add(R3C2);
        tablePanel.add(R3C3);
        tablePanel.add(R3C4);

        tablePanel.add(R4C1);
        tablePanel.add(R4C2);
        tablePanel.add(R4C3);
        tablePanel.add(R4C4);

        tablePanel.add(R5C1);
        tablePanel.add(R5C2);
        tablePanel.add(R5C3);
        tablePanel.add(R5C4);

        tablePanel.add(R6C1);
        tablePanel.add(R6C2);
        tablePanel.add(R6C3);
        tablePanel.add(R6C4);

        tablePanel.add(R7C1);
        tablePanel.add(R7C2);
        tablePanel.add(R7C3);
        tablePanel.add(R7C4);

        tablePanel.add(R8C1);
        tablePanel.add(R8C2);
        tablePanel.add(R8C3);
        tablePanel.add(R8C4);

        tablePanel.add(R9C1);
        tablePanel.add(R9C2);
        tablePanel.add(R9C3);
        tablePanel.add(R9C4);
        JButton saveButton = buttonEdit();
        //bottom panel
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(0, 20, 10, 20));
        bottomPanel.add(saveButton,BorderLayout.EAST);        
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);
        } else {
            JLabel text = new JLabel("Information unavailable as student not registered to this seminar yet.");
            tablePanel.add(text);
        }



            //add other panels to main Panel
            mainPanel.add(detailsPanel, BorderLayout.NORTH);
            mainPanel.add(tablePanel, BorderLayout.CENTER);
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
    private void changeEvalName(JButton btn) {

    //convert the string into a combobox
    ArrayList<String> evaluatorList = new ArrayList<>();
   for (int x = 1; x < DBHelper.getSessionCount(); x++) {
        evaluatorList.add(DBHelper.getUserByRole("Evaluator",x ));
    }
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


    public JButton box(String text, Color bgColor, Color textColor){
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

    String status = "view Report";
    public JButton statusBox(String text, Color bgColor, Color textColor){
        JButton btn = new JButton(text);
        btn.setBackground(bgColor);
        btn.setForeground(textColor);
        btn.setFocusPainted(false);
        // btn.setBorderPainted(true);
        btn.addActionListener(e -> {
            if("view Report".equals(status)) {
                ((LoginFrame) frame).showPanel("reportFrame");
                btn.setText("view Report");
            }
                // } else {
            //     //status = "view Report";
            //     btn.setText("generate Report");
            // } 

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