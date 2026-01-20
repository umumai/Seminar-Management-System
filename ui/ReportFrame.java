package ui;
import java.awt.*;
import javax.swing.*;

public class ReportFrame extends JPanel {
    private JFrame frame;
    public ReportFrame(JFrame frame) {
        this.frame = frame;
        setLayout(new BorderLayout());
        
        //top Panel
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBorder(BorderFactory.createEmptyBorder(5,15,5,5));
        JLabel coordinatorLabel = new JLabel("Stud xxx report"); 
        JButton returnButton = new JButton("return");
        returnButton.addActionListener(e->((LoginFrame) frame).showPanel("studManagementPanel"));
        topPanel.add(coordinatorLabel,BorderLayout.WEST);
        topPanel.add(returnButton,BorderLayout.EAST);

        add(topPanel,BorderLayout.NORTH);
    }

    
}
