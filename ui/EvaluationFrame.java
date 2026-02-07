package ui;
import Database.DBHelper;
import java.awt.*;
import javax.swing.*;
import models.User;

public class EvaluationFrame extends JPanel {
    private JFrame frame;
    private final JLabel reportLabel = new JLabel("Student evaluation");
    private User currentStud;
    public EvaluationFrame(JFrame frame) {
        this.frame = frame;
        setLayout(new BorderLayout());
        
        //============top Panel
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBorder(BorderFactory.createEmptyBorder(5,15,5,5));
        reportLabel.setText("Student evaluation"); //default text if no student id is passed
        JButton returnButton = new JButton("return");
        returnButton.addActionListener(e->((LoginFrame) frame).showPanel("EvaluatorPanel"));
        topPanel.add(reportLabel,BorderLayout.WEST);
        topPanel.add(returnButton,BorderLayout.EAST);

        //===========center panel
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBorder(BorderFactory.createEmptyBorder(5,15,5,5));

        add(topPanel,BorderLayout.NORTH);
    }

    //adjust according to the student User object
    public void setCurrentStud(String studentId) {
        String ID = studentId == null ? "" : studentId;
        currentStud = DBHelper.getUserbyID(ID);
        reportLabel.setText("Student " + currentStud.getId() + " evaluation");
        revalidate();
        repaint();
    }

    
}

