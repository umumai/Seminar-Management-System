import java.awt.*;
import javax.swing.*;

//dalam coordinator frame ni kita just place the JTabbedPane. 
// and then we place the other frame (from other file) into each tabs
public class CoordinatorFrame extends JPanel {
    private MainFrame frame;
    // private JPanel cLayoutPanel;
    // private CardLayout cardLayout;
    public Color deepBlue = new Color(14,69,128);
    public Color deepRed = new Color(151, 32, 0);


    public CoordinatorFrame(MainFrame frame) {
        this.frame = frame;
        setLayout(new BorderLayout());

        //=================COORDINATOR SCREEN==================

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBorder(BorderFactory.createEmptyBorder(5,15,5,5));
        JLabel coordinatorLabel = new JLabel("User xxxx"); //coordinator.getName
        JButton logoutButton = new JButton("logOut");
        logoutButton.setBackground(deepRed);
        logoutButton.setForeground(Color.WHITE);
        logoutButton.setFocusPainted(false);
        topPanel.add(coordinatorLabel,BorderLayout.WEST);
        topPanel.add(logoutButton,BorderLayout.EAST);

        JPanel centerPanel = new JPanel(new GridLayout(0,3,3,3));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(110,50,140,50));
        centerPanel.setBackground(deepBlue);
        JButton newSeminarBtn = createSeminarActionBtn("new Seminar",Color.white, Color.black);
        JButton studMngmentBtn = new JButton("student management");
        studMngmentBtn.addActionListener(e->frame.showScreen("studMngmentFrame"));
        JButton scheduleMngmentBtn = new JButton("manage schedule");
        scheduleMngmentBtn.addActionListener(e->frame.showScreen("EDIT-SCHEDULE"));
        centerPanel.add(newSeminarBtn);
        centerPanel.add(studMngmentBtn);
        centerPanel.add(scheduleMngmentBtn);
    
        add(topPanel,BorderLayout.NORTH);
        add(centerPanel,BorderLayout.CENTER);
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
            String date = dateField.getText();
            String venue = venueField.getText();
            if (date.isEmpty() && venue.isEmpty()) {
            JOptionPane.showMessageDialog(panel, "Error : Invalid input.");
            }else{
            //TO ADJUST : diplay "Seminar 002 created" (getSessionID)
            JOptionPane.showMessageDialog(panel, "Seminar created!");
            }
            // if (!date.isEmpty() && !venue.isEmpty()) {
            //     return new Session(date, venue);
            // }
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
