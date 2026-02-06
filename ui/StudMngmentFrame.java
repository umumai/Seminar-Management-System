package ui;
import Database.DBHelper;
import java.awt.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.swing.*;
import models.*;

//dalam coordinator frame ni kita just place the JTabbedPane. 
// and then we place the other frame (from other file) into each tabs
public class StudMngmentFrame extends JPanel {
    private JFrame frame;
    private String students;
    private String evaluators;
    static String state = "edit";
    public Color deepBlue = new Color(14,69,128);
    private List<Appointment> apptList;
    
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


        //panels are stored inside tabbedPane
        JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.LEFT);

        //check how many session exist
        int sessionList = DBHelper.getSessionCount();
        System.err.println("DEBUG : Total session exist in studmanagement = " + sessionList);
        for (int i = 1; i <= sessionList; i++) {
            JPanel mainPanel = new JPanel(new BorderLayout());
            // Create Appointment objects for each appointment_id in the database
            apptList = DBHelper.getAppointmentsbySession(i);
            //DEBUG
            // System.err.println("DEBUG : Total appointments exist in session " + i + " = " + apptList.size());
            // for (Appointment appointment : apptList) {
            //     System.err.println("DEBUG : Appointment - SessionID: " + appointment.getSessionID() + 
            //     ", StudentID: " + appointment.getStudentID() + 
            //     ", EvaluatorID: " + appointment.getEvaluatorID() + 
            //     ", TimeSlot: " + appointment.getTimeSlot());
            // }
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
        JButton R1C1 = box("Student ID", deepBlue, Color.white);
        JButton R1C2 = box("Student Name", deepBlue, Color.white);
        JButton R1C3 = box("Evaluator Name", deepBlue, Color.white);
        JButton R1C4 = box("Time", deepBlue, Color.white);
        JButton R1C5 = box("Status", deepBlue, Color.white);

        //1st student
        JButton R2C1 = box("", deepBlue, Color.white);
        JButton R2C2 = studentBox("");
        JButton R2C3 = studentBox("");
        JButton R2C4 = statusBox("", Color.white, deepBlue);
        JButton R2C5 = studentBox("");
        if (apptList.size() > 0 && apptList.get(0) != null){
            Appointment currentAppt = apptList.get(0);
            R2C1.setText(currentAppt.getStudentID());
            R2C2.setText(DBHelper.getNameByID(currentAppt.getStudentID()));
            R2C3.addActionListener(e -> {
                if ("save".equals(state)) {
                    changeEvalName(R2C3, currentAppt);
                }
            });
            R2C4.addActionListener(e -> {
                if ("save".equals(state)) {
                    changeTime(R2C4, currentAppt, apptList);
                }
            });
            if ((currentAppt.getEvaluatorID()) != null){
                R2C3.setText(DBHelper.getNameByID(currentAppt.getEvaluatorID()));
            } else {
                R2C3.setText("Edit to assign.");
                R2C3.setForeground(Color.gray);
            }
            if (("0".equals(currentAppt.getTimeSlot())))
            {
                R2C4.setText("Edit to assign.");
                R2C4.setForeground(Color.gray);
            } else {
                R2C4.setText(currentAppt.getTimeSlot()+":00");
            }
            
            R2C5.setText(currentAppt.getStatus());
        }

        JButton R3C1 = box("", deepBlue, Color.white);
        JButton R3C2 = studentBox("");
        JButton R3C3 = studentBox("");
        JButton R3C4 = statusBox("", Color.white, deepBlue);
        JButton R3C5 = studentBox("");
        if (apptList.size() > 1 && apptList.get(1) != null){
            Appointment currentAppt = apptList.get(1);
            R3C1.setText(currentAppt.getStudentID());
            R3C2.setText(DBHelper.getNameByID(currentAppt.getStudentID()));
            R3C3.addActionListener(e -> {
                if ("save".equals(state)) {
                    changeEvalName(R3C3, currentAppt);
                }
            });
            R3C4.addActionListener(e -> {
                if ("save".equals(state)) {
                    changeTime(R3C4, currentAppt, apptList);
                }
            });
            if ((currentAppt.getEvaluatorID()) != null){
                R3C3.setText(DBHelper.getNameByID(currentAppt.getEvaluatorID()));
            } else {
                R3C3.setText("Edit to assign.");
                R3C3.setForeground(Color.gray);
            }
            if (("0".equals(currentAppt.getTimeSlot())))
            {
                R3C4.setText("Edit to assign.");
                R3C4.setForeground(Color.gray);
            } else {
                R3C4.setText(currentAppt.getTimeSlot()+":00");
            }
            R3C5.setText(currentAppt.getStatus());
        }

        JButton R4C1 = box("", deepBlue, Color.white);
        JButton R4C2 = studentBox("");
        JButton R4C3 = studentBox("");
        JButton R4C4 = statusBox("", Color.white, deepBlue);
        JButton R4C5 = studentBox("");
        if (apptList.size() > 2 && apptList.get(2) != null){
            Appointment currentAppt = apptList.get(2);
            R4C1.setText(currentAppt.getStudentID());
            R4C2.setText(DBHelper.getNameByID(currentAppt.getStudentID()));
            R4C3.addActionListener(e -> {
                if ("save".equals(state)) {
                    changeEvalName(R4C3, currentAppt);
                }
            });
            R4C4.addActionListener(e -> {
                if ("save".equals(state)) {
                    changeTime(R4C4, currentAppt, apptList);
                }
            });
            if ((currentAppt.getEvaluatorID()) != null){
                R4C3.setText(DBHelper.getNameByID(currentAppt.getEvaluatorID()));
            } else {
                R4C3.setText("Edit to assign.");
                R4C3.setForeground(Color.gray);
            }
            if (("0".equals(currentAppt.getTimeSlot())))
            {
                R4C4.setText("Edit to assign.");
                R4C4.setForeground(Color.gray);
            } else {
                R4C4.setText(currentAppt.getTimeSlot()+":00");
            }
            R4C5.setText(currentAppt.getStatus());
        }

        //12:00PM row
        JButton R5C1 = box("", deepBlue, Color.white);
        JButton R5C2 = studentBox("");
        JButton R5C3 = studentBox("");
        JButton R5C4 = statusBox("", Color.white, deepBlue);
        JButton R5C5 = studentBox("");
        if (apptList.size() > 3 && apptList.get(3) != null){
            Appointment currentAppt = apptList.get(3);
            R5C1.setText(currentAppt.getStudentID());
            R5C2.setText(DBHelper.getNameByID(currentAppt.getStudentID()));
            R5C3.addActionListener(e -> {
                if ("save".equals(state)) {
                    changeEvalName(R5C3, currentAppt);
                }
            });
            R5C4.addActionListener(e -> {
                if ("save".equals(state)) {
                    changeTime(R5C4, currentAppt, apptList);
                }
            });
            if ((currentAppt.getEvaluatorID()) != null){
                R5C3.setText(DBHelper.getNameByID(currentAppt.getEvaluatorID()));
            } else {
                R5C3.setText("Edit to assign.");
                R5C3.setForeground(Color.gray);
            }
            if (("0".equals(currentAppt.getTimeSlot())))
            {
                R5C4.setText("Edit to assign.");
                R5C4.setForeground(Color.gray);
            } else {
                R5C4.setText(currentAppt.getTimeSlot()+":00");
            }
            R5C5.setText(currentAppt.getStatus());
        }

        //1:00PM row
        JButton R6C1 = box("", deepBlue, Color.white);
        JButton R6C2 = studentBox("");
        JButton R6C3 = studentBox("");
        JButton R6C4 = statusBox("", Color.white, deepBlue);
        JButton R6C5 = studentBox("");
        if (apptList.size() > 4 && apptList.get(4) != null){
            Appointment currentAppt = apptList.get(4);
            R6C1.setText(currentAppt.getStudentID());
            R6C2.setText(DBHelper.getNameByID(currentAppt.getStudentID()));
            R6C3.addActionListener(e -> {
                if ("save".equals(state)) {
                    changeEvalName(R6C3, currentAppt);
                }
            });
            R6C4.addActionListener(e -> {
                if ("save".equals(state)) {
                    changeTime(R6C4, currentAppt, apptList);
                }
            });
            if ((currentAppt.getEvaluatorID()) != null){
                R6C3.setText(DBHelper.getNameByID(currentAppt.getEvaluatorID()));
            } else {
                R6C3.setText("Edit to assign.");
                R6C3.setForeground(Color.gray);
            }
            if (("0".equals(currentAppt.getTimeSlot())))
            {
                R6C4.setText("Edit to assign.");
                R6C4.setForeground(Color.gray);
            } else {
                R6C4.setText(currentAppt.getTimeSlot()+":00");
            }
            R6C5.setText(currentAppt.getStatus());
        }

        //2:00PM row
        JButton R7C1 = box("", deepBlue, Color.white);
        JButton R7C2 = studentBox("");
        JButton R7C3 = studentBox("");
        JButton R7C4 = statusBox("", Color.white, deepBlue);
        JButton R7C5 = studentBox("");
        if (apptList.size() > 5 && apptList.get(5) != null){
            Appointment currentAppt = apptList.get(5);
            R7C1.setText(currentAppt.getStudentID());
            R7C2.setText(DBHelper.getNameByID(currentAppt.getStudentID()));
            R7C3.addActionListener(e -> {
                if ("save".equals(state)) {
                    changeEvalName(R7C3, currentAppt);
                }
            });
            R7C4.addActionListener(e -> {
                if ("save".equals(state)) {
                    changeTime(R7C4, currentAppt, apptList);
                }
            });
            if ((currentAppt.getEvaluatorID()) != null){
                R7C3.setText(DBHelper.getNameByID(currentAppt.getEvaluatorID()));
            } else {
                R7C3.setText("Edit to assign.");
                R7C3.setForeground(Color.gray);
            }
            if (("0".equals(currentAppt.getTimeSlot())))
            {
                R7C4.setText("Edit to assign.");
                R7C4.setForeground(Color.gray);
            } else {
                R7C4.setText(currentAppt.getTimeSlot()+":00");
            }
            R7C5.setText(currentAppt.getStatus());
        }

        //3:00PM row
        JButton R8C1 = box("", deepBlue, Color.white);
        JButton R8C2 = studentBox("");
        JButton R8C3 = studentBox("");
        JButton R8C4 = statusBox("", Color.white, deepBlue);
        JButton R8C5 = studentBox("");
        if (apptList.size() > 6 && apptList.get(6) != null){
            Appointment currentAppt = apptList.get(6);
            R8C1.setText(currentAppt.getStudentID());
            R8C2.setText(DBHelper.getNameByID(currentAppt.getStudentID()));
            R8C3.addActionListener(e -> {
                if ("save".equals(state)) {
                    changeEvalName(R8C3, currentAppt);
                }
            });
            R8C4.addActionListener(e -> {
                if ("save".equals(state)) {
                    changeTime(R8C4, currentAppt, apptList);
                }
            });
            if ((currentAppt.getEvaluatorID()) != null){
                R8C3.setText(DBHelper.getNameByID(currentAppt.getEvaluatorID()));
            } else {
                R8C3.setText("Edit to assign.");
                R8C3.setForeground(Color.gray);
            }
            if (("0".equals(currentAppt.getTimeSlot())))
            {
                R8C4.setText("Edit to assign.");
                R8C4.setForeground(Color.gray);
            } else {
                R8C4.setText(currentAppt.getTimeSlot()+":00");
            }
            R8C5.setText(currentAppt.getStatus());
        }

        //4:00PM row
        JButton R9C1 = box("", deepBlue, Color.white);
        JButton R9C2 = studentBox("");
        JButton R9C3 = studentBox("");
        JButton R9C4 = statusBox("", Color.white, deepBlue);
        JButton R9C5 = studentBox("");
        if (apptList.size() > 7 && apptList.get(7) != null){
            Appointment currentAppt = apptList.get(7);
            R9C1.setText(currentAppt.getStudentID());
            R9C2.setText(DBHelper.getNameByID(currentAppt.getStudentID()));
            R9C3.addActionListener(e -> {
                if ("save".equals(state)) {
                    changeEvalName(R9C3, currentAppt);
                }
            });
            R9C4.addActionListener(e -> {
                if ("save".equals(state)) {
                    changeTime(R9C4, currentAppt, apptList);
                }
            });
            if ((currentAppt.getEvaluatorID()) != null){
                R9C3.setText(DBHelper.getNameByID(currentAppt.getEvaluatorID()));
            } else {
                R9C3.setText("Edit to assign.");
                R9C3.setForeground(Color.gray);
            }
            if (("0".equals(currentAppt.getTimeSlot())))
            {
                R9C4.setText("Edit to assign.");
                R9C4.setForeground(Color.gray);
            } else {
                R9C4.setText(currentAppt.getTimeSlot()+":00");
            }
            R9C5.setText(currentAppt.getStatus());
        }


        if ((DBHelper.getStudentIdFromAppointment(i, 0)) != null){

        //add all the JButton into the table panel
        tablePanel.add(R1C1);
        tablePanel.add(R1C2);
        tablePanel.add(R1C3);
        tablePanel.add(R1C4);
        tablePanel.add(R1C5);

        tablePanel.add(R2C1);
        tablePanel.add(R2C2);
        tablePanel.add(R2C3);
        tablePanel.add(R2C4);
        tablePanel.add(R2C5);

        tablePanel.add(R3C1);
        tablePanel.add(R3C2);
        tablePanel.add(R3C3);
        tablePanel.add(R3C4);
        tablePanel.add(R3C5);

        tablePanel.add(R4C1);
        tablePanel.add(R4C2);
        tablePanel.add(R4C3);
        tablePanel.add(R4C4);
        tablePanel.add(R4C5);

        tablePanel.add(R5C1);
        tablePanel.add(R5C2);
        tablePanel.add(R5C3);
        tablePanel.add(R5C4);
        tablePanel.add(R5C5);

        tablePanel.add(R6C1);
        tablePanel.add(R6C2);
        tablePanel.add(R6C3);
        tablePanel.add(R6C4);
        tablePanel.add(R6C5);

        tablePanel.add(R7C1);
        tablePanel.add(R7C2);
        tablePanel.add(R7C3);
        tablePanel.add(R7C4);
        tablePanel.add(R7C5);

        tablePanel.add(R8C1);
        tablePanel.add(R8C2);
        tablePanel.add(R8C3);
        tablePanel.add(R8C4);
        tablePanel.add(R8C5);

        tablePanel.add(R9C1);
        tablePanel.add(R9C2);
        tablePanel.add(R9C3);
        tablePanel.add(R9C4);
        tablePanel.add(R9C5);

        JButton saveButton = buttonEdit();
        JLabel instruction = new JLabel("Click edit to assign evaluator & time.");
        instruction.setForeground(Color.GRAY);
        //bottom panel
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(0, 20, 10, 20));
        bottomPanel.add(saveButton,BorderLayout.EAST);        
        bottomPanel.add(instruction,BorderLayout.WEST); 
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

    //===============================ActionListener=======================================
    private void changeEvalName(JButton btn, Appointment appt) {

    //convert the string into a combobox
    ArrayList<String> evaluatorList = new ArrayList<>();
    for (int x = 1; x < DBHelper.getSessionCount(); x++) {
        evaluatorList.add(DBHelper.getUserByRole("Evaluator",x ));
    }

    // List<Appointment> apptList = DBHelper.getAppointmentsbySession(i);
    // Set<String> bookedTimes = new HashSet<>();
    // for (Appointment apt : apptList) {
    //     bookedTimes.add(apt.getTimeSlot());
    // }

    // availTime.removeIf(bookedTimes::contains);
    // Convert ArrayList to array
    String[] evaluatorListArray = evaluatorList.toArray(new String[0]);
    JComboBox<String> roleCombo = new JComboBox<>(evaluatorListArray);

    JPanel panel = new JPanel(new GridLayout(0, 1));
    panel.add(new JLabel("Choose evaluator to assign :"));
    panel.add(roleCombo);

        int result = JOptionPane.showConfirmDialog(
                this,
                panel,
                "Assign evaluator",
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
                    btn.setForeground(deepBlue);
                    appt.setEvaluatorID(DBHelper.getIDbyName(choice));
                    System.err.println("DEBUG : Updating evaluator for " + appt.getStudentID() +
                " -> EvaluatorID: " + appt.getEvaluatorID());
                    try {
                        DBHelper.updateAppointment(appt.getStudentID(), appt.getEvaluatorID(), appt.getTimeSlot(), appt.getStatus());
                    } catch (SQLException ex) {
                        System.getLogger(StudMngmentFrame.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
                    }
                    //result = choice [choice = "Fatimah"]
                    // openRoleScreen(role); // call function dynamically
                    break;
                }
            }
        }
    }

    private void changeTime(JButton btn, Appointment appt, List<Appointment> sessionAppointments) {

    //convert the string into a combobox
    ArrayList<String> availTime = new ArrayList<>();
    for (int i = 9; i <= 17; i++) {
        availTime.add(String.valueOf(i));
    }

    Set<String> bookedTimes = new HashSet<>();
    for (Appointment apt : sessionAppointments) {
        if (apt != null && apt.getTimeSlot() != null && !"0".equals(apt.getTimeSlot())) {
            bookedTimes.add(apt.getTimeSlot());
        }
    }

    availTime.removeIf(bookedTimes::contains);
    
    // Convert ArrayList to array
    String[] availTimeArray = availTime.toArray(new String[0]);
    JComboBox<String> roleCombo = new JComboBox<>(availTimeArray);

    JPanel panel = new JPanel(new GridLayout(0, 1));
    panel.add(new JLabel("Choose time :"));
    panel.add(roleCombo);

        int result = JOptionPane.showConfirmDialog(
                this,
                panel,
                "Assign evaluator",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );

        if (result == JOptionPane.OK_OPTION) {
            //item that are chosen from the combobox stored inside role
            String selectedChoice = (String) roleCombo.getSelectedItem();

            for (String choice : availTimeArray) {
                if (selectedChoice.equals(choice)) {
                    System.out.println(choice + " selected");
                    btn.setText(choice + ":00");
                    btn.setForeground(deepBlue);
                    appt.setEvaluatorID(DBHelper.getIDbyName(choice));
                    appt.setTimeSlot(choice);
                    System.err.println("DEBUG : Updating time for " + appt.getStudentID() +
                " -> Time: " + appt.getTimeSlot());
                    try {
                        DBHelper.updateAppointment(appt.getStudentID(), appt.getEvaluatorID(), appt.getTimeSlot(), appt.getStatus());
                    } catch (SQLException ex) {
                        System.getLogger(StudMngmentFrame.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
                    }
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

    public JButton studentBox(String text){
        JButton btn = new JButton(text);
        btn.setBackground(Color.white);
        btn.setForeground(deepBlue);
        btn.setFocusPainted(false);
        // btn.setBorderPainted(true);
        return btn;
    }

    // public JButton evaluatorBox(String text, Color bgColor, Color textColor){
    //     JButton btn = new JButton(text);
    //     btn.setBackground(bgColor);
    //     btn.setForeground(textColor);
    //     btn.setFocusPainted(false);
    //     // btn.setBorderPainted(true);
    //     btn.addActionListener(e -> {
    //         if ("save".equals(state)) {
    //             changeEvalName(btn);
    //         }
    //     });
    //     return btn;
    // }

    // String status = "view Report";
    public JButton statusBox(String text, Color bgColor, Color textColor){
        JButton btn = new JButton(text);
        btn.setBackground(bgColor);
        btn.setForeground(textColor);
        btn.setFocusPainted(false);
        // btn.setBorderPainted(true);
        // btn.addActionListener(e -> {
        //     if("view Report".equals(status)) {
        //         ((LoginFrame) frame).showPanel("reportFrame");
        //         btn.setText("view Report");
        //     }
                // } else {
            //     //status = "view Report";
            //     btn.setText("generate Report");
            // } 

        // });
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
            System.out.println("state = "+ state);
        } else if ("edit".equals(state)) {
                btn.setText("stop editing");
                btn.setBackground(Color.green);
                state = "save";
                System.out.println("state = "+ state); 
                
            }
        });
        
        // when save button is clicked, switch to other screen
        //btn.addActionListener(e -> frame.showScreen("VIEW-SCHEDULE")); 
        return btn;
    }
    
}