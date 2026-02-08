package ui;
import Database.DBHelper;
import java.awt.*;
import java.util.List;
import javax.swing.*;
import models.Appointment;
import models.Session;

public class EditSchedule extends JPanel{
    private JFrame frame;
    String state = "edit";
    public Color deepBlue = new Color(14,69,128);
    //public Color beige = new Color(228, 219, 210);
    
    public EditSchedule(JFrame frame){
        this.frame=frame;
        setLayout(new BorderLayout());

        JPanel topPanel = new JPanel(new BorderLayout());
        //topPanel.setBackground(beige);
        topPanel.setBorder(BorderFactory.createEmptyBorder(5,15,5,5));
        JLabel coordinatorLabel = new JLabel("View Schedule"); 
        JButton returnButton = new JButton("return");
        returnButton.addActionListener(e->{
            ((LoginFrame) frame).showPanel("CoordinatorPanel");
        });
        topPanel.add(coordinatorLabel,BorderLayout.WEST);
        topPanel.add(returnButton,BorderLayout.EAST);

        //panels are stored inside tabbedPane
        JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.LEFT);
        //configure how many session exist
        int sessionList = DBHelper.getSessionCount();
        for (int i = 1; i <= sessionList; i++) {
            JPanel mainPanel = new JPanel(new BorderLayout());
            // Create Appointment objects for each appointment_id in the database
            List<Appointment> apptList = DBHelper.getAppointmentsbySession(i);
            //adjust the tab name here
            String tabsName = "Session " + i;
            Session session = DBHelper.getSession(i);
            //create stuffs here==========
            
        
        JPanel detailsPanel = new JPanel(new GridLayout(3, 1, 2, 2));
        detailsPanel.setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 10));
        detailsPanel.add(new JLabel("Date : " + session.getDate()));
        detailsPanel.add(new JLabel("Venue : " + session.getVenue()));
        // detailsPanel.add(new JLabel("Presentation Type : " + session.getSessionType()));
        
        //table panel
        JPanel tablePanel = new JPanel(new GridLayout(9, 3, 1, 1));
        tablePanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        //header row
        JButton R1C1 = headerBox("Time");
        JButton R1C2 = headerBox("Student Name");
        JButton R1C3 = headerBox("Evaluator Name");
        //9:00AM row
        JButton R2C1 = box("9:00AM");
        JButton R2C2 = box("");
        JButton R2C3 = box("");
        
        // R2C2.addActionListener(changeStudName(R2C2, sessionList));

        //10:00AM row
        JButton R3C1 = box("10:00AM");
        JButton R3C2 = box("");
        JButton R3C3 = box("");
        //11:00AM row
        JButton R4C1 = box("11:00AM");
        JButton R4C2 = box("");
        JButton R4C3 = box("");
        //12:00PM row
        JButton R5C1 = box("12:00PM");
        JButton R5C2 = box("");
        JButton R5C3 = box("");
        //1:00PM row
        JButton R6C1 = box("1:00PM");
        JButton R6C2 = box("");
        JButton R6C3 = box("");
        //2:00PM row
        JButton R7C1 = box("2:00PM");
        JButton R7C2 = box("");
        JButton R7C3 = box("");
        //3:00PM row
        JButton R8C1 = box("3:00PM");
        JButton R8C2 = box("");
        JButton R8C3 = box("");
        //4:00PM row
        JButton R9C1 = box("4:00PM");
        JButton R9C2 = box("");
        JButton R9C3 = box("");

        for (Appointment appt : apptList) {
            String time = appt.getTimeSlot();
            switch (time) {
                case "9":
                    R2C2.setText(DBHelper.getNameByID(appt.getStudentID()));
                    R2C3.setText(DBHelper.getNameByID(appt.getEvaluatorID()));
                    break;
                case "10":
                    R3C2.setText(DBHelper.getNameByID(appt.getStudentID()));
                    R3C3.setText(DBHelper.getNameByID(appt.getEvaluatorID()));
                    break;
                case "11":
                    R4C2.setText(DBHelper.getNameByID(appt.getStudentID()));
                    R4C3.setText(DBHelper.getNameByID(appt.getEvaluatorID()));
                    break;
                case "12":
                    R5C2.setText(DBHelper.getNameByID(appt.getStudentID()));
                    R5C3.setText(DBHelper.getNameByID(appt.getEvaluatorID()));
                    break;
                case "13":
                    R6C2.setText(DBHelper.getNameByID(appt.getStudentID()));
                    R6C3.setText(DBHelper.getNameByID(appt.getEvaluatorID()));
                    break;
                case "14":
                    R7C2.setText(DBHelper.getNameByID(appt.getStudentID()));
                    R7C3.setText(DBHelper.getNameByID(appt.getEvaluatorID()));
                    break;
                case "15":
                    R8C2.setText(DBHelper.getNameByID(appt.getStudentID()));
                    R8C3.setText(DBHelper.getNameByID(appt.getEvaluatorID()));
                    break;
                case "16":
                    R9C2.setText(DBHelper.getNameByID(appt.getStudentID()));
                    R9C3.setText(DBHelper.getNameByID(appt.getEvaluatorID()));
                    break;
                default:
                    // do nothing for unrecognized time slots.
                    break;
            }
        }

        if ((DBHelper.getStudentIdFromAppointment(i, 0)) != null){

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

        //bottom panel
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(0, 20, 10, 20));
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);
        } else {
            JLabel text = new JLabel("Timetable unavailable as student not registered to this seminar yet.");
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

    public JButton box(String text){
        JButton btn = new JButton(text);
        btn.setBackground(Color.white);
        btn.setForeground(deepBlue);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setFocusable(false);
        // btn.setEnabled(false);
        return btn;
    }

    public JButton headerBox(String text){
        JButton btn = new JButton(text);
        btn.setBackground(deepBlue);
        btn.setForeground(Color.white);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setFocusable(false);
        return btn;
    }
}
