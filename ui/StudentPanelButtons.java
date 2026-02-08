package ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.JButton;

 // Util class for creating and styling buttons used in StudentPanel

public class StudentPanelButtons {
    
    
     // logout button
     
    public static JButton createLogoutButton() {
        JButton button = new JButton("Logout");
        button.setFocusPainted(false);
        return button;
    }
    
    
     // tab button (Register/Status/Schedule tabs)
     
    public static JButton createTabButton(String text) {
        JButton button = new JButton(text);
        button.setFocusPainted(false);
        return button;
    }
    
    
     // Styles a tab button based on selection state
     
    public static void styleTabButton(JButton button, boolean isSelected) {
        if (isSelected) {
            button.setBackground(new Color(220, 220, 220));
            button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 2, 0, Color.BLUE),
                BorderFactory.createEmptyBorder(8, 20, 8, 20)
            ));
        } else {
            button.setBackground(new Color(245, 245, 245));
            button.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));
        }
    }
    
    
     // register button for seminar cards
     
    public static JButton createSeminarRegisterButton() {
        JButton button = new JButton("Register");
        button.setPreferredSize(new Dimension(100, 35));
        button.setMinimumSize(new Dimension(100, 35));
        button.setMaximumSize(new Dimension(100, 35));
        // button.setEnabled(true); [redundant]  
        button.setFocusPainted(false);
        return button;
    }
    
    
     // "Go back" button
     
    public static JButton createGoBackButton() {
        JButton button = new JButton("Go back");
        button.setFocusPainted(false);
        return button;
    }
    
    
     //"Attach Material" button
     
    public static JButton createAttachMaterialButton() {
        JButton button = new JButton("Attach Material");
        button.setFocusPainted(false);
        return button;
    }
    
    
     // styled submit button
     
    public static JButton createSubmitButton() {
        JButton button = new JButton("SUBMIT");
        button.setBackground(Color.BLUE);
        button.setForeground(Color.WHITE);
        button.setOpaque(true);
        button.setBorderPainted(false);
        button.setFont(new Font("SansSerif", Font.BOLD, 14));
        button.setPreferredSize(new Dimension(120, 35));
        button.setFocusPainted(false);
        return button;
    }
}

