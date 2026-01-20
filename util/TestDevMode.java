package util;

import java.awt.FlowLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import models.User;
import ui.LoginFrame;
import ui.StudentPanel;
import ui.CoordinatorPanel;
import ui.EvaluatorPanel;

public class TestDevMode {
    

    public static JPanel createDevPanel(LoginFrame parent) {
        JPanel devPanel = new JPanel();
        
        // DEV MODE: Label
        JLabel devLabel = new JLabel("DEV MODE");
        devLabel.setFont(devLabel.getFont().deriveFont(java.awt.Font.ITALIC, 10f));
        
        // Button Panel
        JPanel devButtons = new JPanel(new FlowLayout());
        JButton studentBtn = new JButton("Test Student");
        JButton evaluatorBtn = new JButton("Test Evaluator");
        JButton coordinatorBtn = new JButton("Test Coordinator");
        
        // Test Student Button 
        studentBtn.addActionListener(e -> {
            User testUser = new User("STU001", "Test Student", "STUDENT", "");
            StudentPanel studentPanel = parent.getStudentPanel();
            if (studentPanel != null) {
                studentPanel.setUser(testUser);
            }
            if (parent instanceof LoginFrame) {
                ((LoginFrame) parent).showPanel("StudentPanel");
            }
        });
        
        // Test Evaluator Button 
        evaluatorBtn.addActionListener(e -> {
            User testUser = new User("EVA001", "Test Evaluator", "EVALUATOR", "");
            EvaluatorPanel.setUser(testUser);
            if (parent instanceof LoginFrame) {
                ((LoginFrame) parent).showPanel("EvaluatorPanel");
            }
        });
        
        // Test Coordinator Button
        coordinatorBtn.addActionListener(e -> {
            User testUser = new User("COO001", "Test Coordinator", "COORDINATOR", "");
            CoordinatorPanel.setUser(testUser);
            if (parent instanceof LoginFrame) {
                ((LoginFrame) parent).showPanel("CoordinatorPanel");
            }
        });
        
        devButtons.add(studentBtn);
        devButtons.add(evaluatorBtn);
        devButtons.add(coordinatorBtn);
        
        // Sets buttons to the pane
        devPanel.setLayout(new java.awt.BorderLayout());
        devPanel.add(devLabel, java.awt.BorderLayout.NORTH);
        devPanel.add(devButtons, java.awt.BorderLayout.CENTER);
        
        return devPanel;
     }
}
