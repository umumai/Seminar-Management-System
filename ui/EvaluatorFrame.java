package ui;
import Database.DBHelper;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.swing.*;
import models.*;

//dalam coordinator frame ni kita just place the JTabbedPane. 
// and then we place the other frame (from other file) into each tabs
public class EvaluatorFrame extends JPanel {
    private JFrame frame;
    private Evaluator currentUser;
    private final CardLayout contentLayout = new CardLayout();
    private final JPanel contentPanel = new JPanel(contentLayout);
    public Color deepBlue = new Color(14,69,128);
    public Color deepRed = new Color(151, 32, 0);
    
    public EvaluatorFrame(JFrame frame, Evaluator evaluator) {
        this.frame = frame;
        this.currentUser = evaluator;
        buildUI();
    }

    public void updateUser(Evaluator user) {
        this.currentUser = user;
        buildUI();
    }

    private void buildUI() {
        removeAll();
        setLayout(new BorderLayout());

        //============top Panel
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBorder(
        BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, Color.GRAY), // line
            BorderFactory.createEmptyBorder(5, 15, 5, 5)            // padding
            )
        );
        String evaluatorName = currentUser != null ? currentUser.getName() : "Evaluator";
        JLabel evaluatorLabel = new JLabel("Evaluator " + evaluatorName);
        JButton logoutButton = new JButton("Logout");
        logoutButton.setBackground(deepRed);
        logoutButton.setForeground(Color.WHITE);
        logoutButton.setFocusPainted(false);
        logoutButton.addActionListener(e -> {
            if (frame instanceof LoginFrame) {
                ((LoginFrame) frame).showPanel("LoginPanel");
            }
        });
        topPanel.add(evaluatorLabel, BorderLayout.WEST);
        topPanel.add(logoutButton, BorderLayout.EAST);

        //===========center Panel
        //panels are stored inside tabbedPane
        JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.LEFT);

        //show schedule for session connected to this evaluator only
        List<Appointment> allAppt = DBHelper.getAllAppointments(); //list all appt
        List<Appointment> evalApptList = new ArrayList<>(); //list appt only connected to this eval
        List<Session> evalSessionList = new ArrayList<>(); //list session connected to this eval only
        String evaluatorId = currentUser != null ? currentUser.getId() : null;

        //store appt connected to eval
        for (Appointment apt : allAppt) {
            if (evaluatorId != null && evaluatorId.equals(apt.getEvaluatorID())) {
                evalApptList.add(apt);
            }
        }
        Set<Integer> seenSessionIds = new HashSet<>();
        for (Appointment apt : evalApptList) {
            int sessionId = apt.getSessionID();
            if (seenSessionIds.add(sessionId)) {
                evalSessionList.add(DBHelper.getSession(sessionId));
            }
        }

        for (Session session : evalSessionList) {
        JPanel mainPanel = new JPanel(new BorderLayout());
            
        String sessionID = session.getSessionID();
        String displaySessionId = sessionID != null ? sessionID.replaceFirst("^SEM00", "") : "";
        String tabsName = "Session " + displaySessionId;
        List<Appointment> apptList = DBHelper.getAppointmentsbySession(Integer.valueOf(displaySessionId));

        //=======display details n schedule inside corresponding session
        
        JPanel detailsPanel = new JPanel(new GridLayout(3, 1, 2, 2));
        detailsPanel.setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 10));
        detailsPanel.add(new JLabel("Date : " + session.getDate()));
        detailsPanel.add(new JLabel("Venue : " + session.getVenue()));
        
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
        JButton R2C3 = box("");        //10:00AM row
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
                String studentId = appt.getStudentID();
                boolean isAssignedToMe = appt.getEvaluatorID() == null ? 
                    currentUser.getId() == null : appt.getEvaluatorID().equals(currentUser.getId());
                // green highlight = evaluated (that evaluator only)
                boolean isEvaluated = isAssignedToMe && currentUser != null &&
                    DBHelper.isStudentEvaluated(studentId, currentUser.getId());
                
                switch (time) {
                    case "9":
                        R2C2.setText(DBHelper.getNameByID(studentId));
                        R2C3.setText(DBHelper.getNameByID(appt.getEvaluatorID()));
                        if (isAssignedToMe) {
                            if (isEvaluated) {
                                R2C2.setBackground(new Color(144, 238, 144));
                            }
                            R2C2.addActionListener(e -> showEvaluationPanel(studentId));
                        } else {
                            R2C2.setForeground(Color.gray);
                        }
                        break;
                    case "10":
                        R3C2.setText(DBHelper.getNameByID(studentId));
                        R3C3.setText(DBHelper.getNameByID(appt.getEvaluatorID()));
                        if (isAssignedToMe) {
                            if (isEvaluated) {
                                R3C2.setBackground(new Color(144, 238, 144));
                            }
                            R3C2.addActionListener(e -> showEvaluationPanel(studentId));
                        } else {
                            R3C2.setForeground(Color.gray);
                        }
                        break;
                    case "11":
                        R4C2.setText(DBHelper.getNameByID(studentId));
                        R4C3.setText(DBHelper.getNameByID(appt.getEvaluatorID()));
                        if (isAssignedToMe) {
                            if (isEvaluated) {
                                R4C2.setBackground(new Color(144, 238, 144));
                            }
                            R4C2.addActionListener(e -> showEvaluationPanel(studentId));
                        } else {
                            R4C2.setForeground(Color.gray);
                        }
                        break;
                    case "12":
                        R5C2.setText(DBHelper.getNameByID(studentId));
                        R5C3.setText(DBHelper.getNameByID(appt.getEvaluatorID()));
                        if (isAssignedToMe) {
                            if (isEvaluated) {
                                R5C2.setBackground(new Color(144, 238, 144));
                            }
                            R5C2.addActionListener(e -> showEvaluationPanel(studentId));
                        } else {
                            R5C2.setForeground(Color.gray);
                        }
                        break;
                    case "13":
                        R6C2.setText(DBHelper.getNameByID(studentId));
                        R6C3.setText(DBHelper.getNameByID(appt.getEvaluatorID()));
                        if (isAssignedToMe) {
                            if (isEvaluated) {
                                R6C2.setBackground(new Color(144, 238, 144));
                            }
                            R6C2.addActionListener(e -> showEvaluationPanel(studentId));
                        } else {
                            R6C2.setForeground(Color.gray);
                        }
                        break;
                    case "14":
                        R7C2.setText(DBHelper.getNameByID(studentId));
                        R7C3.setText(DBHelper.getNameByID(appt.getEvaluatorID()));
                        if (isAssignedToMe) {
                            if (isEvaluated) {
                                R7C2.setBackground(new Color(144, 238, 144));
                            }
                            R7C2.addActionListener(e -> showEvaluationPanel(studentId));
                        } else {
                            R7C2.setForeground(Color.gray);
                        }
                        break;
                    case "15":
                        R8C2.setText(DBHelper.getNameByID(studentId));
                        R8C3.setText(DBHelper.getNameByID(appt.getEvaluatorID()));
                        if (isAssignedToMe) {
                            if (isEvaluated) {
                                R8C2.setBackground(new Color(144, 238, 144));
                            }
                            R8C2.addActionListener(e -> showEvaluationPanel(studentId));
                        } else {
                            R8C2.setForeground(Color.gray);
                        }
                        break;
                    case "16":
                        R9C2.setText(DBHelper.getNameByID(studentId));
                        R9C3.setText(DBHelper.getNameByID(appt.getEvaluatorID()));
                        if (isAssignedToMe) {
                            if (isEvaluated) {
                                R9C2.setBackground(new Color(144, 238, 144));
                            }
                            R9C2.addActionListener(e -> showEvaluationPanel(studentId));
                        } else {
                            R9C2.setForeground(Color.gray);
                        }
                        break;
                    default:
                        // do nothing for unrecognized time slots.
                        break;
                }
            }

        //add all the JButton into the table panel (display schedule)
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

        //add other panels to main Panel
        mainPanel.add(detailsPanel, BorderLayout.NORTH);
        mainPanel.add(tablePanel, BorderLayout.CENTER);
        //===========
        //add each tabs
        tabbedPane.addTab(tabsName, mainPanel);
        }

        //==========bottom panel
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10,15,5,5));
        JLabel instruction = new JLabel("Click name of students assigned to you to give evaluation.");
        instruction.setForeground(Color.gray);
        bottomPanel.add(instruction,BorderLayout.WEST);

        //=========add all to this panel
        JPanel schedulePanel = new JPanel(new BorderLayout());
        schedulePanel.add(tabbedPane, BorderLayout.CENTER);
        schedulePanel.add(bottomPanel, BorderLayout.SOUTH);

        JPanel evaluationPanel = buildEvaluationPanel();
        contentPanel.removeAll();
        contentPanel.add(schedulePanel, "schedule");
        contentPanel.add(evaluationPanel, "evaluation");
        contentLayout.show(contentPanel, "schedule");

        add(topPanel, BorderLayout.NORTH);
        add(contentPanel, BorderLayout.CENTER);
        revalidate();
        repaint();
    }

    
    public JButton box(String text){
        JButton btn = new JButton(text);
        btn.setBackground(Color.white);
        btn.setForeground(deepBlue);
        return btn;
    }
    
    public JButton evaluatedBox(String text){
        JButton btn = new JButton(text);
        btn.setBackground(new Color(144, 238, 144)); // Light green
        btn.setForeground(deepBlue);
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

    private EvaluationFrame evaluationFrameInstance;
    
    private JPanel buildEvaluationPanel() {
        evaluationFrameInstance = new EvaluationFrame(frame);
        evaluationFrameInstance.setEvaluator(currentUser);
        evaluationFrameInstance.setOnReturnToDashboard(this::showScheduleAndRefresh);
        return evaluationFrameInstance;
    }

    private void showScheduleAndRefresh() {
        buildUI();
    }

    private void showEvaluationPanel(String studentId) { 
        // umu prevent evaluator from evaluating the same student again
        if (studentId != null && DBHelper.isStudentEvaluatedByAny(studentId)) {
            JOptionPane.showMessageDialog(this, "This Application has been evaluated");
            System.out.println(studentId + " application has been evaluated.");
            return;
        }

        if (evaluationFrameInstance != null) {
            evaluationFrameInstance.setEvaluator(currentUser);
            evaluationFrameInstance.setCurrentStud(studentId);
        }
        contentLayout.show(contentPanel, "evaluation");
    }

    
}
