package ui;

import Database.DBHelper;
import java.awt.CardLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import models.User;
import models.Coordinator;
import util.TestDevMode;

public class LoginFrame extends JFrame {
    private final JTextField nameField = new JTextField(12);
    private final JPasswordField passField = new JPasswordField(12);
    private final CardLayout cardLayout;
    private final JPanel mainPanel;
    private StudentPanel studentPanelInstance; // for multi-user support instead of static methods. Also better isolation code
    private User currentUser; // Store authenticated user
    private CoordinatorFrame coordinatorPanelInstance; // Cache for coordinator panel

    public LoginFrame() {
        setTitle("Seminar Management System");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(600, 400);
        setLocationRelativeTo(null);


        // Init CardLayout
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // login panel
        JPanel loginPanel = createLoginPanel();
        
        // other panels (Coordinator, Student, Evaluator, Register)
        coordinatorPanelInstance = new CoordinatorFrame(this, new Coordinator("COO000", "Coordinator")); // Create with dummy user
        JPanel coordinatorPanel = coordinatorPanelInstance;
        studentPanelInstance = new StudentPanel(this);
        JPanel studentPanel = studentPanelInstance.getPanel();
        JPanel evaluatorPanel = EvaluatorPanel.createPanel(this);
        JPanel registerPanel = StudentRegisterPanel.createPanel(this);
        JPanel scheduleFrame = new ScheduleFrame(this);
        JPanel editSchedulePanel = new EditSchedule(this);
        JPanel reportPanel = new ReportFrame(this);
        JPanel studManagementPanel = new StudMngmentFrame(this);


        // Add all panels to CardLayout
        mainPanel.add(loginPanel, "LoginPanel");
        mainPanel.add(coordinatorPanel, "CoordinatorPanel");
        mainPanel.add(studentPanel, "StudentPanel");
        mainPanel.add(evaluatorPanel, "EvaluatorPanel");
        mainPanel.add(registerPanel, "RegisterPanel");
        mainPanel.add(scheduleFrame, "schedulePanel");
        mainPanel.add(editSchedulePanel, "editSchedulePanel");
        mainPanel.add(reportPanel, "reportPanel");
        mainPanel.add(studManagementPanel, "studManagementPanel");


        add(mainPanel);

        // Show login panel when frame is open
        cardLayout.show(mainPanel, "LoginPanel");
    }

    private JPanel createLoginPanel() {
        JPanel p = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(4,4,4,4);
        c.gridx = 0; c.gridy = 0; p.add(new JLabel("ID:"), c);
        c.gridx = 1; p.add(nameField, c);
        c.gridx = 0; c.gridy = 1; p.add(new JLabel("Password:"), c);
        c.gridx = 1; p.add(passField, c);

        JButton loginBtn = new JButton("Login");
        JButton regBtn = new JButton("Register (Students)");

        c.gridx = 0; c.gridy = 2; c.gridwidth = 2; p.add(loginBtn, c);
        c.gridy = 3; p.add(regBtn, c);

        // DEV MODE: Quick access buttons (remove later)
        JPanel devPanel = TestDevMode.createDevPanel(this);
        c.gridy = 4; c.gridwidth = 2;
        p.add(devPanel, c);
        // DEV MODE: end



        // getRootPane().setDefaultButton(loginBtn); // press "Enter" to login

        loginBtn.addActionListener(e -> doLogin());

        regBtn.addActionListener(e -> {
            // StudentRegisterFrame rf = new StudentRegisterFrame(this);
            // rf.setVisible(true);
            cardLayout.show(mainPanel, "RegisterPanel");
        });
        return p;
    }

    private void doLogin() {
        String id = nameField.getText().trim();
        String pass = new String(passField.getPassword());
        if (id.isEmpty() || pass.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Enter ID and password");
            return;
        }

        // User u = DBHelper.authenticate(id, pass);
        // Try to authenticate (will return null if database not set up yet)
        User u = null;
        try { //error handling
            u = DBHelper.authenticate(id, pass);
        } catch (Exception e) {
            // Database not ready - show error message and allow test access (so it doesnt crash)
            JOptionPane.showMessageDialog(this, 
                "Database not configured yet.\nUse 'Test' buttons below for development access.");
            return;
        }
     

        if (u == null) {
            JOptionPane.showMessageDialog(this, "Invalid credentials");
            return;
        }

        String role = u.getRole();
        if (role != null) {
            role = role.trim().toUpperCase(); // Normalize role to uppercase
        }
        System.out.println("DEBUG: Logged in user - ID: " + u.getId() + ", Name: " + u.getName() + ", Role: '" + role + "'");
        this.currentUser = u; // Store the authenticated user
        // int code = 0;
        switch (role) {

            case "COORDINATOR":
                // Update coordinator panel with authenticated user - convert to Coordinator
                System.out.println("DEBUG: Switching to COORDINATOR panel"); // Debug
                Coordinator coordinator = new Coordinator(u.getId(), u.getName());
                coordinatorPanelInstance.updateUser(coordinator);
                cardLayout.show(mainPanel, "CoordinatorPanel");
                return;

            case "STUDENT":
                if (studentPanelInstance != null) {
                    studentPanelInstance.setUser(u);
                }
                cardLayout.show(mainPanel, "StudentPanel");
                return;
            case "EVALUATOR":
                EvaluatorPanel.setUser(u);
                cardLayout.show(mainPanel, "EvaluatorPanel");
                return;
            
            default:
                System.out.println("DEBUG: No matching role found. Role was: '" + role + "'"); // Debug
                JOptionPane.showMessageDialog(this, "Unknown role: " + role);
                return;
            }
    }

    public void showLoginPanel() {
        cardLayout.show(mainPanel, "LoginPanel");
    }

    public void showPanel(String panelName) {
        cardLayout.show(mainPanel, panelName);
    }
    
    public StudentPanel getStudentPanel() {
        return studentPanelInstance;
    }
    
    public User getCurrentUser() {
        return currentUser;
    }
}
