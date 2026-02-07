package ui;

import java.awt.BorderLayout; //for container layout
import java.awt.Color;
import java.awt.Font;
import javax.swing.JButton;
import javax.swing.JFrame; //for font
import javax.swing.JLabel;
import javax.swing.JPanel; //for button
import javax.swing.SwingConstants;
import models.User;

public class EvaluatorPanel {
    private static User currentUser;
    private static JPanel evaluatorPanel;
    public static Color deepBlue = new Color(14,69,128);
    public static Color deepRed = new Color(151, 32, 0);

    public static JPanel createPanel(JFrame parent) {
        //main panel
        evaluatorPanel = new JPanel(new BorderLayout());
        
        // Return button
        JPanel bottomPanel = new JPanel(new BorderLayout());
        JButton returnButton = new JButton("Logout");
        returnButton.addActionListener(e -> {
            if (parent instanceof LoginFrame) {
                ((LoginFrame) parent).showLoginPanel();
            }
        });

        bottomPanel.add(returnButton, BorderLayout.EAST); //add return button to bottom panel
        evaluatorPanel.add(bottomPanel, BorderLayout.SOUTH);

        // Will be updated when user logs in
        updateContent();
        
        return evaluatorPanel;
    }

    public static void setUser(User user) {
        currentUser = user;
        updateContent(); //update content when user logs in
    }

    private static void updateContent() {
        if (evaluatorPanel == null) return;
        
        // Remove existing content (except bottom panel)
        if (evaluatorPanel.getComponentCount() > 1) {
            evaluatorPanel.remove(1);
        }
        if (evaluatorPanel.getComponentCount() > 1) {
            evaluatorPanel.remove(1);
        }

        //setup screen here
        if (currentUser != null) {
            JLabel welcome = new JLabel("Welcome, " + currentUser.getName() + " (" + currentUser.getId() + ")", SwingConstants.CENTER);
            welcome.setFont(new Font("SansSerif", Font.PLAIN, 16));
            

            //add to main panel
            evaluatorPanel.add(welcome, BorderLayout.CENTER);


        }

        //refresh main panel
        evaluatorPanel.revalidate(); //recalculate the layout after changes
        evaluatorPanel.repaint(); //repaint the panel to reflect the changes
    }
}

