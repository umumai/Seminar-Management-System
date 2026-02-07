package ui;

import Database.DBHelper;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.sql.SQLException;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class StudentRegisterPanel {
    public static JPanel createPanel(JFrame parent) {
        JTextField nameField = new JTextField(15);
        JPasswordField passField = new JPasswordField(15);

        JPanel p = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(4,4,4,4);
        c.gridx = 0; c.gridy = 0; p.add(new JLabel("Name:"), c);
        c.gridx = 1; p.add(nameField, c);
        c.gridx = 0; c.gridy = 1; p.add(new JLabel("Password:"), c);
        c.gridx = 1; p.add(passField, c);

        JButton createBtn = new JButton("Create Account");
        JButton backBtn = new JButton("Back to Login");
        c.gridx = 0; c.gridy = 2; c.gridwidth = 2; p.add(createBtn, c);
        c.gridy = 3; p.add(backBtn, c);

        createBtn.addActionListener(e -> {
            String name = nameField.getText().trim();
            String pass = new String(passField.getPassword());
            if (name.isEmpty() || pass.isEmpty()) {
                JOptionPane.showMessageDialog(parent, "Enter name and password");
                return;
            }
            try {
                String newId = DBHelper.createStudent(name, pass);
                DBHelper.insertStudentProfile(newId);
                JOptionPane.showMessageDialog(parent, "Account created. Your ID: " + newId);
                nameField.setText("");
                passField.setText("");
                if (parent instanceof LoginFrame) {
                    ((LoginFrame) parent).showLoginPanel();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(parent, "Error creating account: " + ex.getMessage());
            }
        });

        backBtn.addActionListener(e -> {
            if (parent instanceof LoginFrame) {
                ((LoginFrame) parent).showLoginPanel();
            }
        });

        return p; 
    }
}