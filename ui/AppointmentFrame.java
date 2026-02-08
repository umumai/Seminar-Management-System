package ui;
import Database.DBHelper;
import java.awt.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import models.*;

//dalam coordinator frame ni kita just place the JTabbedPane. 
// and then we place the other frame (from other file) into each tabs
public class AppointmentFrame extends JPanel {
    private JFrame frame;
    private String students;
    private String evaluators;
    static String state = "edit";
    public Color deepBlue = new Color(14,69,128);
    private List<Appointment> apptList;

    // ----- adjustd umu -

    private String deriveAppointmentStatus(Appointment appt) {
        // Keep evaluated as-is (set by evaluator submission flow)
        if (appt != null && appt.getStatus() != null && "evaluated".equalsIgnoreCase(appt.getStatus())) {
            return "Evaluated";
        }

        boolean evaluatorAssigned =
            appt != null &&
            appt.getEvaluatorID() != null &&
            !appt.getEvaluatorID().trim().isEmpty();

        boolean timeAssigned =
            appt != null &&
            appt.getTimeSlot() != null &&
            !appt.getTimeSlot().trim().isEmpty() &&
            !"0".equals(appt.getTimeSlot());

        if (evaluatorAssigned && timeAssigned) {
            return "Under Evaluation";
        }
        return "Unassigned";
    }
    // ----end 

    public AppointmentFrame(JFrame frame) {
        this.frame=frame;
        setLayout(new BorderLayout());

        

        //top Panel
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBorder(BorderFactory.createEmptyBorder(5,15,5,5));
        JLabel coordinatorLabel = new JLabel("Appointment Management"); 
        JButton returnButton = new JButton("return");
        returnButton.addActionListener(e->{((LoginFrame) frame).showPanel("CoordinatorPanel");});
        topPanel.add(coordinatorLabel,BorderLayout.WEST);
        topPanel.add(returnButton,BorderLayout.EAST);


        //panels are stored inside tabbedPane
        JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.LEFT);

        //check how many session exist
        int sessionList = DBHelper.getSessionCount();
        System.err.println("DEBUG : Total session exist in appointment management = " + sessionList);
        for (int i = 1; i <= sessionList; i++) {
            final int sessionId = i; // umu added sessionId to pass to other functions
            JPanel mainPanel = new JPanel(new BorderLayout());
            // Create Appointment objects for each appointment_id in the database
            apptList = DBHelper.getAppointmentsbySession(i);
            List<Appointment> sessionAppointments = apptList;
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
        
        // table panel (5 columns including Status) ---umu
        // JPanel tablePanel = new JPanel(new GridLayout(9, 3, 1, 1));
        JPanel tablePanel = new JPanel(new GridLayout(9, 5, 1, 1));
        tablePanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        //header row
        JButton R1C1 = box("Student ID", deepBlue, Color.white);
        JButton R1C2 = box("Student Name", deepBlue, Color.white);
        JButton R1C3 = box("Evaluator Name", deepBlue, Color.white);
        JButton R1C4 = box("Time", deepBlue, Color.white);
        JButton R1C5 = box("Status", deepBlue, Color.white);
        JButton R1C6 = box("Report", deepBlue, Color.white);

        //1st student
        JButton R2C1 = box("", deepBlue, Color.white);
        JButton R2C2 = Box("");
        JButton R2C3 = Box("");
        JButton R2C4 = Box("");
        JButton R2C5 = Box("");
        JButton R2C6 = Box("");
        if (apptList.size() > 0 && apptList.get(0) != null){
            Appointment currentAppt = apptList.get(0);
            R2C1.setText(currentAppt.getStudentID());
            R2C2.setText(DBHelper.getNameByID(currentAppt.getStudentID()));
            R2C3.addActionListener(e -> {
                if ("save".equals(state)) {
                    // changeEvalName(R2C3, currentAppt); --umu
                    changeEvalName(R2C3, R2C5, currentAppt);
                }
            });
            R2C4.addActionListener(e -> {
                if ("save".equals(state)) {
                    changeTime(R2C4, R2C5, currentAppt, sessionAppointments);
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
            R2C6.setText("Unavailable");
            if ("Evaluated".equalsIgnoreCase(currentAppt.getStatus())) {
                R2C6.setText("View Report");
                R2C6.addActionListener(e -> ((LoginFrame) frame).showReportPanel(currentAppt.getStudentID(), sessionId)); // umu added sessionId 
            }
        }

        JButton R3C1 = box("", deepBlue, Color.white);
        JButton R3C2 = Box("");
        JButton R3C3 = Box("");
        JButton R3C4 = Box("");
        JButton R3C5 = Box("");
        JButton R3C6 = Box("");
        if (apptList.size() > 1 && apptList.get(1) != null){
            Appointment currentAppt = apptList.get(1);
            R3C1.setText(currentAppt.getStudentID());
            R3C2.setText(DBHelper.getNameByID(currentAppt.getStudentID()));
            R3C3.addActionListener(e -> {
                if ("save".equals(state)) {
                    changeEvalName(R3C3, R3C5, currentAppt); // so it updates status column, bila the first mentioned row dah filled the status row update also
                }
            });
            R3C4.addActionListener(e -> {
                if ("save".equals(state)) {
                    changeTime(R3C4, R3C5, currentAppt, sessionAppointments);
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
            R3C6.setText("Unavailable");
            if ("Evaluated".equalsIgnoreCase(currentAppt.getStatus())) {
                R3C6.setText("View Report");
                R3C6.addActionListener(e -> ((LoginFrame) frame).showReportPanel(currentAppt.getStudentID(), sessionId));
            }
        }

        JButton R4C1 = box("", deepBlue, Color.white);
        JButton R4C2 = Box("");
        JButton R4C3 = Box("");
        JButton R4C4 = Box("");
        JButton R4C5 = Box("");
        JButton R4C6 = Box("");
        if (apptList.size() > 2 && apptList.get(2) != null){
            Appointment currentAppt = apptList.get(2);
            R4C1.setText(currentAppt.getStudentID());
            R4C2.setText(DBHelper.getNameByID(currentAppt.getStudentID()));
            R4C3.addActionListener(e -> {
                if ("save".equals(state)) {
                    changeEvalName(R4C3, R4C5, currentAppt);
                }
            });
            R4C4.addActionListener(e -> {
                if ("save".equals(state)) {
                    changeTime(R4C4, R4C5, currentAppt, sessionAppointments);
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
            R4C6.setText("Unavailable");
            if ("Evaluated".equalsIgnoreCase(currentAppt.getStatus())) {
                R4C6.setText("View Report");
                R4C6.addActionListener(e -> ((LoginFrame) frame).showReportPanel(currentAppt.getStudentID(), sessionId));
            }
        }

        //12:00PM row
        JButton R5C1 = box("", deepBlue, Color.white);
        JButton R5C2 = Box("");
        JButton R5C3 = Box("");
        JButton R5C4 = Box("");
        JButton R5C5 = Box("");
        JButton R5C6 = Box("");
        if (apptList.size() > 3 && apptList.get(3) != null){
            Appointment currentAppt = apptList.get(3);
            R5C1.setText(currentAppt.getStudentID());
            R5C2.setText(DBHelper.getNameByID(currentAppt.getStudentID()));
            R5C3.addActionListener(e -> {
                if ("save".equals(state)) {
                    changeEvalName(R5C3, R5C5, currentAppt);
                }
            });
            R5C4.addActionListener(e -> {
                if ("save".equals(state)) {
                    changeTime(R5C4, R5C5, currentAppt, sessionAppointments);
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
            R5C6.setText("Unavailable");
            if ("Evaluated".equalsIgnoreCase(currentAppt.getStatus())) {
                R5C6.setText("View Report");
                R5C6.addActionListener(e -> ((LoginFrame) frame).showReportPanel(currentAppt.getStudentID(), sessionId));
            }
        }

        //1:00PM row
        JButton R6C1 = box("", deepBlue, Color.white);
        JButton R6C2 = Box("");
        JButton R6C3 = Box("");
        JButton R6C4 = Box("");
        JButton R6C5 = Box("");
        JButton R6C6 = Box("");
        if (apptList.size() > 4 && apptList.get(4) != null){
            Appointment currentAppt = apptList.get(4);
            R6C1.setText(currentAppt.getStudentID());
            R6C2.setText(DBHelper.getNameByID(currentAppt.getStudentID()));
            R6C3.addActionListener(e -> {
                if ("save".equals(state)) {
                    changeEvalName(R6C3, R6C5, currentAppt);
                }
            });
            R6C4.addActionListener(e -> {
                if ("save".equals(state)) {
                    changeTime(R6C4, R6C5, currentAppt, sessionAppointments);
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
            R6C6.setText("Unavailable");
            if ("Evaluated".equalsIgnoreCase(currentAppt.getStatus())) {
                R6C6.setText("View Report");
                R6C6.addActionListener(e -> ((LoginFrame) frame).showReportPanel(currentAppt.getStudentID(), sessionId));
            }
        }

        //2:00PM row
        JButton R7C1 = box("", deepBlue, Color.white);
        JButton R7C2 = Box("");
        JButton R7C3 = Box("");
        JButton R7C4 = Box("");
        JButton R7C5 = Box("");
        JButton R7C6 = Box("");
        if (apptList.size() > 5 && apptList.get(5) != null){
            Appointment currentAppt = apptList.get(5);
            R7C1.setText(currentAppt.getStudentID());
            R7C2.setText(DBHelper.getNameByID(currentAppt.getStudentID()));
            R7C3.addActionListener(e -> {
                if ("save".equals(state)) {
                    changeEvalName(R7C3, R7C5, currentAppt);
                }
            });
            R7C4.addActionListener(e -> {
                if ("save".equals(state)) {
                    changeTime(R7C4, R7C5, currentAppt, sessionAppointments);
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
            R7C6.setText("Unavailable");
            if ("Evaluated".equalsIgnoreCase(currentAppt.getStatus())) {
                R7C6.setText("View Report");
                R7C6.addActionListener(e -> ((LoginFrame) frame).showReportPanel(currentAppt.getStudentID(), sessionId));
            }
        }

        //3:00PM row
        JButton R8C1 = box("", deepBlue, Color.white);
        JButton R8C2 = Box("");
        JButton R8C3 = Box("");
        JButton R8C4 = Box("");
        JButton R8C5 = Box("");
        JButton R8C6 = Box("");
        if (apptList.size() > 6 && apptList.get(6) != null){
            Appointment currentAppt = apptList.get(6);
            R8C1.setText(currentAppt.getStudentID());
            R8C2.setText(DBHelper.getNameByID(currentAppt.getStudentID()));
            R8C3.addActionListener(e -> {
                if ("save".equals(state)) {
                    changeEvalName(R8C3, R8C5, currentAppt);
                }
            });
            R8C4.addActionListener(e -> {
                if ("save".equals(state)) {
                    changeTime(R8C4, R8C5, currentAppt, sessionAppointments);
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
            R8C6.setText("Unavailable");
            if ("Evaluated".equalsIgnoreCase(currentAppt.getStatus())) {
                R8C6.setText("View Report");
                R8C6.addActionListener(e -> ((LoginFrame) frame).showReportPanel(currentAppt.getStudentID(), sessionId));
            }
        }

        //4:00PM row
        JButton R9C1 = box("", deepBlue, Color.white);
        JButton R9C2 = Box("");
        JButton R9C3 = Box("");
        JButton R9C4 = Box("");
        JButton R9C5 = Box("");
        JButton R9C6 = Box("");
        if (apptList.size() > 7 && apptList.get(7) != null){
            Appointment currentAppt = apptList.get(7);
            R9C1.setText(currentAppt.getStudentID());
            R9C2.setText(DBHelper.getNameByID(currentAppt.getStudentID()));
            R9C3.addActionListener(e -> {
                if ("save".equals(state)) {
                    changeEvalName(R9C3, R9C5, currentAppt);
                }
            });
            R9C4.addActionListener(e -> {
                if ("save".equals(state)) {
                    changeTime(R9C4, R9C5, currentAppt, sessionAppointments);
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
            R9C6.setText("Unavailable");
            if ("Evaluated".equalsIgnoreCase(currentAppt.getStatus())) {
                R9C6.setText("View Report");
                R9C6.addActionListener(e -> ((LoginFrame) frame).showReportPanel(currentAppt.getStudentID(), sessionId));
            }
        }


        if ((DBHelper.getStudentIdFromAppointment(i, 0)) != null){

        //add all the JButton into the table panel
        tablePanel.add(R1C1);
        tablePanel.add(R1C2);
        tablePanel.add(R1C3);
        tablePanel.add(R1C4);
        tablePanel.add(R1C5);
        tablePanel.add(R1C6);

        tablePanel.add(R2C1);
        tablePanel.add(R2C2);
        tablePanel.add(R2C3);
        tablePanel.add(R2C4);
        tablePanel.add(R2C5);
        tablePanel.add(R2C6);

        tablePanel.add(R3C1);
        tablePanel.add(R3C2);
        tablePanel.add(R3C3);
        tablePanel.add(R3C4);
        tablePanel.add(R3C5);
        tablePanel.add(R3C6);

        tablePanel.add(R4C1);
        tablePanel.add(R4C2);
        tablePanel.add(R4C3);
        tablePanel.add(R4C4);
        tablePanel.add(R4C5);
        tablePanel.add(R4C6);

        tablePanel.add(R5C1);
        tablePanel.add(R5C2);
        tablePanel.add(R5C3);
        tablePanel.add(R5C4);
        tablePanel.add(R5C5);
        tablePanel.add(R5C6);

        tablePanel.add(R6C1);
        tablePanel.add(R6C2);
        tablePanel.add(R6C3);
        tablePanel.add(R6C4);
        tablePanel.add(R6C5);
        tablePanel.add(R6C6);

        tablePanel.add(R7C1);
        tablePanel.add(R7C2);
        tablePanel.add(R7C3);
        tablePanel.add(R7C4);
        tablePanel.add(R7C5);
        tablePanel.add(R7C6);

        tablePanel.add(R8C1);
        tablePanel.add(R8C2);
        tablePanel.add(R8C3);
        tablePanel.add(R8C4);
        tablePanel.add(R8C5);
        tablePanel.add(R8C6);

        tablePanel.add(R9C1);
        tablePanel.add(R9C2);
        tablePanel.add(R9C3);
        tablePanel.add(R9C4);
        tablePanel.add(R9C5);
        tablePanel.add(R9C6);

        JButton saveButton = buttonEdit();
        JLabel instruction = new JLabel("Click edit to assign evaluator & time.");
        instruction.setForeground(Color.GRAY);
        
        // Finalise Report button
        JButton finaliseButton = new JButton("Finalise Report");
        finaliseButton.setBackground(new Color(0, 150, 0));
        finaliseButton.setForeground(Color.WHITE);
        finaliseButton.setFocusPainted(false);
        // Finalise Report button action listener -- umu
        finaliseButton.addActionListener(e -> {
            // Finalise Session Report
            boolean success = DBHelper.finaliseSessionReport(sessionId);
            if (!success) {
                JOptionPane.showMessageDialog(this, "All presenters must be evaluated before finalising.", "Unable to finalise", JOptionPane.WARNING_MESSAGE);
                return;
            }
            JOptionPane.showMessageDialog(
                this,
                "Session finalised. Awards published and results unlocked.",
                "Finalised",
                JOptionPane.INFORMATION_MESSAGE
            );
            finaliseButton.setEnabled(false);
            finaliseButton.setText("Finalised");
        });
        
        //bottom panel
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(0, 20, 10, 20));
        JPanel rightButtonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        rightButtonsPanel.add(finaliseButton);
        rightButtonsPanel.add(saveButton);
        bottomPanel.add(instruction, BorderLayout.WEST);
        bottomPanel.add(rightButtonsPanel, BorderLayout.EAST);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);
        } else {
            JLabel text = new JLabel("Information  as student not registered to this seminar yet.", SwingConstants.CENTER);
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
    private void changeEvalName(JButton btn, JButton statusBtn, Appointment appt) { //umu added statusBtn to update status column

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
                        String derivedStatus = deriveAppointmentStatus(appt); //umu added derivedStatus to make sure update status column
                        appt.setStatus(derivedStatus);
                        statusBtn.setText(derivedStatus);
                        DBHelper.updateAppointment(appt.getStudentID(), appt.getEvaluatorID(), appt.getTimeSlot(), appt.getStatus());
                    } catch (SQLException ex) {
                        System.getLogger(AppointmentFrame.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
                    }
                    //result = choice [choice = "Fatimah"]
                    // openRoleScreen(role); // call function dynamically
                    break;
                }
            }
        }
    }

    private void changeTime(JButton btn, JButton statusBtn, Appointment appt, List<Appointment> sessionAppointments) { //umu added statusBtn 

    //convert the string into a combobox
    ArrayList<String> availTime = new ArrayList<>();
    for (int i = 9; i <= 16; i++) {
        availTime.add(String.valueOf(i));
    }

    ArrayList<String> bookedTime = new ArrayList<>();
    for (Appointment apt : sessionAppointments) {
        System.out.println("DEBUG : " + apt.getTimeSlot()  + ":00 is taken by " + DBHelper.getNameByID(apt.getStudentID()));
        if (apt != null && apt.getTimeSlot() != null && !"0".equals(apt.getTimeSlot())) {
            bookedTime.add(apt.getTimeSlot());
            // System.out.println("DEBUG : ");
        }
    }

    System.out.println("DEBUG changeTime: availTime initial 9-17, bookedTimes=" + bookedTime + ", availTimeRemaining=" + availTime);
    availTime.removeIf(bookedTime::contains);
    
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
                    // appt.setEvaluatorID(DBHelper.getIDbyName(choice)); --umu try out to avoid error
                    appt.setTimeSlot(choice);
                    System.err.println("DEBUG : Updating time for " + appt.getStudentID() +
                " -> Time: " + appt.getTimeSlot());
                    try {
                        String derivedStatus = deriveAppointmentStatus(appt); //umu
                        appt.setStatus(derivedStatus);
                        statusBtn.setText(derivedStatus);
                        DBHelper.updateAppointment(appt.getStudentID(), appt.getEvaluatorID(), appt.getTimeSlot(), appt.getStatus());
                    } catch (SQLException ex) {
                        System.getLogger(AppointmentFrame.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
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

    public JButton Box(String text){
        JButton btn = new JButton(text);
        btn.setBackground(Color.white);
        btn.setForeground(deepBlue);
        btn.setFocusPainted(false);
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