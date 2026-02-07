package ui;
import Database.DBHelper;
import java.awt.*;
import javax.swing.*;
import models.User;

public class ReportFrame extends JPanel {
    private JFrame frame;
    private final JLabel reportLabel = new JLabel("Student report");
    private User currentStud;
    public ReportFrame(JFrame frame) {
        this.frame = frame;
        setLayout(new BorderLayout());
        
        //top Panel
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBorder(BorderFactory.createEmptyBorder(5,15,5,5));
        reportLabel.setText("Student report");
        JButton returnButton = new JButton("return");
        returnButton.addActionListener(e->((LoginFrame) frame).showPanel("EvaluatorPanel"));
        topPanel.add(reportLabel,BorderLayout.WEST);
        topPanel.add(returnButton,BorderLayout.EAST);

        add(topPanel,BorderLayout.NORTH);
    }

    public void setCurrentStud(String studentId) {
        String ID = studentId == null ? "" : studentId;
        currentStud = DBHelper.getUserbyID(ID);
        reportLabel.setText("Student " + currentStud.getId() + " report");
        revalidate();
        repaint();
    }

    
}
