package ui;

import java.awt.BorderLayout;
import java.awt.Font;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import models.User;

public class StudentPanel {
    private static User currentUser;
    private static JPanel studentPanel;

    public static JPanel createPanel(JFrame parent) {
        studentPanel = new JPanel(new BorderLayout());
        
        // Return button
        JPanel bottomPanel = new JPanel(new BorderLayout());
        JButton returnButton = new JButton("Logout");
        returnButton.addActionListener(e -> {
            if (parent instanceof LoginFrame) {
                ((LoginFrame) parent).showLoginPanel();
            }
        });
        bottomPanel.add(returnButton, BorderLayout.EAST);
        studentPanel.add(bottomPanel, BorderLayout.SOUTH);

        // Will be updated when user logs in
        updateContent();
        
        return studentPanel;
    }

    public static void setUser(User user) {
        currentUser = user;
        updateContent();
    }

    private static void updateContent() {
        if (studentPanel == null) return;
        
        // Remove existing content (except bottom panel)
        if (studentPanel.getComponentCount() > 1) {
            studentPanel.remove(1);
        }
        if (studentPanel.getComponentCount() > 1) {
            studentPanel.remove(1);
        }

        if (currentUser != null) {
            JLabel welcome = new JLabel("Welcome, " + currentUser.getName() + " (" + currentUser.getId() + ")", SwingConstants.CENTER);
            welcome.setFont(new Font("SansSerif", Font.PLAIN, 16));
            studentPanel.add(welcome, BorderLayout.CENTER);

        }

        studentPanel.revalidate();
        studentPanel.repaint();
    }
}

