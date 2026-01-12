import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import java.awt.FlowLayout;

public class TestDevMode {
    

    public static JPanel createDevPanel(LoginFrame parent) {
        JPanel devPanel = new JPanel();
        
        // DEV MODE: Label
        JLabel devLabel = new JLabel("Development Mode");
        devLabel.setFont(devLabel.getFont().deriveFont(java.awt.Font.ITALIC, 10f));
        
        // Button Panel
        JPanel devButtons = new JPanel(new FlowLayout());
        JButton studentBtn = new JButton("Test Student");
        JButton evaluatorBtn = new JButton("Test Evaluator");
        JButton coordinatorBtn = new JButton("Test Coordinator");
        
        // Test Student Button 
        studentBtn.addActionListener(e -> {
            User testUser = new User("STU001", "Test Student", "STUDENT", "student1");
            DashboardFrame df = new DashboardFrame(testUser, 1);
            df.setVisible(true);
            parent.dispose();
        });
        
        // Test Evaluator Button 
        evaluatorBtn.addActionListener(e -> {
            User testUser = new User("EVA001", "Test Evaluator", "EVALUATOR", "evaluator1");
            DashboardFrame df = new DashboardFrame(testUser, 2);
            df.setVisible(true);
            parent.dispose();
        });
        
        // Test Coordinator Button
        coordinatorBtn.addActionListener(e -> {
            CoordinatorFrame cf = new CoordinatorFrame();
            cf.setVisible(true);
            parent.dispose();
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
