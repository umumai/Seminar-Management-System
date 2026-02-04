package ui;
import Database.DBHelper;
import java.awt.*;
import java.sql.SQLException;
import javax.swing.*;
import models.Coordinator;

//dalam coordinator frame ni kita just place the JTabbedPane. 
// and then we place the other frame (from other file) into each tabs
public class CoordinatorFrame extends JPanel {
    private JFrame frame;
    private Coordinator currentUser;
    // private JPanel cLayoutPanel;
    // private CardLayout cardLayout;
    public Color deepBlue = new Color(14,69,128);
    public Color deepRed = new Color(151, 32, 0);


    public CoordinatorFrame(JFrame frame, Coordinator coordinator) {
        this.frame = frame;
        this.currentUser = coordinator;
        setLayout(new BorderLayout());

        //=================COORDINATOR SCREEN==================

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBorder(BorderFactory.createEmptyBorder(5,15,5,5));
        JLabel coordinatorLabel = new JLabel("User " + (currentUser != null ? currentUser.getName() : "XXX")); //coordinator.getName
        JButton logoutButton = new JButton("logOut");
        logoutButton.setBackground(deepRed);
        logoutButton.setForeground(Color.WHITE);
        logoutButton.setFocusPainted(false);
        logoutButton.addActionListener(e -> {
            ((LoginFrame) frame).showPanel("LoginPanel");
        });
        topPanel.add(coordinatorLabel,BorderLayout.WEST);
        topPanel.add(logoutButton,BorderLayout.EAST);

        JPanel centerPanel = new JPanel(new GridLayout(0,3,3,3));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(110,50,140,50));
        centerPanel.setBackground(deepBlue);
        JButton newSeminarBtn = createSeminarActionBtn("new Seminar",Color.white, Color.black);
        JButton studMngmentBtn = new JButton("student management");
        studMngmentBtn.addActionListener(e -> {
            ((LoginFrame) frame).showPanel("studManagementPanel");
        });
        JButton scheduleMngmentBtn = new JButton("manage schedule");
        scheduleMngmentBtn.addActionListener(e -> {
            ((LoginFrame) frame).showPanel("editSchedulePanel");
        });
        centerPanel.add(newSeminarBtn);
        centerPanel.add(studMngmentBtn);
        centerPanel.add(scheduleMngmentBtn);
    
        add(topPanel,BorderLayout.NORTH);
        add(centerPanel,BorderLayout.CENTER);
    }
    
    public void updateUser(Coordinator user) {
        this.currentUser = user;
        // Rebuild the UI with updated user
        removeAll();
        setLayout(new BorderLayout());

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBorder(BorderFactory.createEmptyBorder(5,15,5,5));
        JLabel coordinatorLabel = new JLabel("User " + currentUser.getName());
        JButton logoutButton = new JButton("logOut");
        logoutButton.setBackground(deepRed);
        logoutButton.setForeground(Color.WHITE);
        logoutButton.setFocusPainted(false);
        logoutButton.addActionListener(e -> {
            ((LoginFrame) frame).showPanel("LoginPanel");
        });
        topPanel.add(coordinatorLabel,BorderLayout.WEST);
        topPanel.add(logoutButton,BorderLayout.EAST);

        JPanel centerPanel = new JPanel(new GridLayout(0,3,3,3));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(110,50,140,50));
        centerPanel.setBackground(deepBlue);
        JButton newSeminarBtn = createSeminarActionBtn("new Seminar",Color.white, Color.black);
        JButton studMngmentBtn = new JButton("student management");
        studMngmentBtn.addActionListener(e -> {
            ((LoginFrame) frame).showPanel("studManagementPanel");
        });
        JButton scheduleMngmentBtn = new JButton("manage schedule");
        scheduleMngmentBtn.addActionListener(e -> {
            ((LoginFrame) frame).showPanel("editSchedulePanel");
        });
        centerPanel.add(newSeminarBtn);
        centerPanel.add(studMngmentBtn);
        centerPanel.add(scheduleMngmentBtn);
    
        add(topPanel,BorderLayout.NORTH);
        add(centerPanel,BorderLayout.CENTER);
        revalidate();
        repaint();
    }
    
    //pass in button mana yg kita nk benda ni function at
    private void createNewSeminar(JButton btn) {
        //create panel for the dialog box
        JPanel panel = new JPanel(new GridLayout(2,2,10,10));
        JTextField dateField = new JTextField(15);
        JTextField venueField = new JTextField(15);
        panel.add(new JLabel("Date:"));
        panel.add(dateField);
        panel.add(new JLabel("Venue:"));
        panel.add(venueField);

        int result = JOptionPane.showConfirmDialog(
                this,
                panel,
                "Create new Seminar",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );

        if (result == JOptionPane.OK_OPTION) {
            String date = dateField.getText().trim();
            String venue = venueField.getText().trim();
            if (date.isEmpty() || venue.isEmpty()) {
                JOptionPane.showMessageDialog(panel, "Error: Invalid input.");
            } else {
                try {
                    int generatedId = DBHelper.createSession(date, venue);
                    DBHelper.insertSessionPresenter(generatedId);
                    if (generatedId > 0) {
                        String sessionIdStr = String.format("SEM%03d", generatedId);
                        JOptionPane.showMessageDialog(panel, "Seminar created: " + sessionIdStr);
                    } else {
                        JOptionPane.showMessageDialog(panel, "Error: Failed to create seminar.");
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(panel, "Error creating seminar: " + ex.getMessage());
                }
            }
        }
    }

    //GUI helper (reusable)
    public JButton createSeminarActionBtn(String text, Color bgColor, Color textColor){
        JButton btn = new JButton(text);
        btn.setBackground(bgColor);
        btn.setForeground(textColor);
        btn.setFocusPainted(false);
        // btn.setBorderPainted(true);
        btn.addActionListener(e -> {
            createNewSeminar(btn);
        });
        return btn;
    }

}
