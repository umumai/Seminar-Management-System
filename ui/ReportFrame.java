package ui;
import Database.DBHelper;
import java.awt.*;
import javax.swing.*;
import models.*;

public class ReportFrame extends JPanel {
    private JFrame frame;
    private final JLabel reportLabel = new JLabel("Student report");
    private final JLabel studentNameLabel;
    private final JLabel evaluatorNameLabel;
    private final JLabel presentationTypeLabel;
    private final JLabel sessionLabel;
    private final JLabel awardLabel;
    private final JLabel clarityLabel;
    private final JLabel methodologyLabel;
    private final JLabel resultsLabel;
    private final JLabel presentationLabel;
    private final JTextArea commentsArea;

    private User currentStud;
    private Evaluation scores;
    public ReportFrame(JFrame frame) {
        this.frame = frame;
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(5,15,15,15));
        
        //=====top Panel
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBorder(BorderFactory.createEmptyBorder(5,15,5,5));
        reportLabel.setText("Student report");
        JButton returnButton = new JButton("return");
        returnButton.addActionListener(e->((LoginFrame) frame).showPanel("appointmentPanel"));
        topPanel.add(reportLabel,BorderLayout.WEST);
        topPanel.add(returnButton,BorderLayout.EAST);

        // ===== Center container =====
        JPanel centerPanel = new JPanel();
        centerPanel.setBorder(BorderFactory.createEmptyBorder(5,15,5,50));
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));

        // ===== Header Info Panel =====
        JPanel infoPanel = new JPanel(new GridLayout(5, 2, 10, 8));
        infoPanel.setBorder(BorderFactory.createTitledBorder("Presentation Details"));

        infoPanel.add(new JLabel("Student Name:"));
        studentNameLabel = new JLabel("-");
        infoPanel.add(studentNameLabel);

        infoPanel.add(new JLabel("Evaluator Name:"));
        evaluatorNameLabel = new JLabel("-");
        infoPanel.add(evaluatorNameLabel);

        infoPanel.add(new JLabel("Presentation Type:"));
        presentationTypeLabel = new JLabel("-");
        infoPanel.add(presentationTypeLabel);

        infoPanel.add(new JLabel("Session:"));
        sessionLabel = new JLabel("-");
        infoPanel.add(sessionLabel);

        infoPanel.add(new JLabel("Award:"));
        awardLabel = new JLabel("-");
        infoPanel.add(awardLabel);

        // ===== Scores Panel =====
        JPanel scorePanel = new JPanel(new GridLayout(4, 2, 10, 8));
        scorePanel.setBorder(BorderFactory.createTitledBorder("Evaluation Scores"));

        scorePanel.add(new JLabel("Clarity:"));
        clarityLabel = new JLabel("-");
        scorePanel.add(clarityLabel);

        scorePanel.add(new JLabel("Methodology:"));
        methodologyLabel = new JLabel("-");
        scorePanel.add(methodologyLabel);

        scorePanel.add(new JLabel("Results:"));
        resultsLabel = new JLabel("-");
        scorePanel.add(resultsLabel);

        scorePanel.add(new JLabel("Presentation:"));
        presentationLabel = new JLabel("-");
        scorePanel.add(presentationLabel);

        centerPanel.add(infoPanel);
        centerPanel.add(Box.createVerticalStrut(10));
        centerPanel.add(scorePanel);

        add(centerPanel, BorderLayout.CENTER);

        // ===== Comments Panel =====
        JPanel commentPanel = new JPanel(new BorderLayout(5, 5));
        commentPanel.setBorder(BorderFactory.createTitledBorder("Comments"));

        JTextArea commentsArea = new JTextArea(5, 30);
        commentsArea.setEditable(false);
        commentsArea.setLineWrap(true);
        commentsArea.setWrapStyleWord(true);
        this.commentsArea = commentsArea;

        commentPanel.add(new JScrollPane(commentsArea), BorderLayout.CENTER);
        add(commentPanel, BorderLayout.SOUTH);
    

        add(topPanel,BorderLayout.NORTH);
    }

    public void setCurrentStud(String studentId) {
        String ID = studentId == null ? "" : studentId;
        currentStud = DBHelper.getUserbyID(ID);
        scores = DBHelper.getEvaluationByStudentId(ID);
        reportLabel.setText("Student " + currentStud.getId() + " report");
        studentNameLabel.setText(currentStud.getName());
        // evaluatorNameLabel.setText();
        //presentationTypeLabel.setText();
        //sessionLabel.setText(sessionName);

        clarityLabel.setText(String.valueOf(scores.getClarityScore()));
        methodologyLabel.setText(String.valueOf(scores.getMethodologyScore()));
        resultsLabel.setText(String.valueOf(scores.getResultsScore()));
        presentationLabel.setText(String.valueOf(scores.getPresentationScore()));

        commentsArea.setText(scores.getComments());
        awardLabel.setText("Award: " + DBHelper.getStudentAwardResult(ID));
        Session s = DBHelper.getStudentRegisteredSeminar(ID);
        sessionLabel.setText(s != null ? s.getSessionID() : "-");
        revalidate();
        repaint();
    }

    public void setCurrentStud(String studentId, int sessionId) {
        String ID = studentId == null ? "" : studentId;
        currentStud = DBHelper.getUserbyID(ID);
        scores = DBHelper.getEvaluationByStudentId(ID, sessionId);

        reportLabel.setText("Student " + ID + " report");
        studentNameLabel.setText(currentStud != null ? currentStud.getName() : "-");

        Session s = DBHelper.getSession(sessionId);
        sessionLabel.setText(s != null ? s.getSessionID() : ("SEM" + String.format("%03d", sessionId)));

        String award = DBHelper.getStudentAwardResult(ID, sessionId);
        awardLabel.setText(award != null ? award : "-");

        if (scores != null) {
            clarityLabel.setText(String.valueOf(scores.getClarityScore()));
            methodologyLabel.setText(String.valueOf(scores.getMethodologyScore()));
            resultsLabel.setText(String.valueOf(scores.getResultsScore()));
            presentationLabel.setText(String.valueOf(scores.getPresentationScore()));
            commentsArea.setText(scores.getComments());
        } else {
            clarityLabel.setText("-");
            methodologyLabel.setText("-");
            resultsLabel.setText("-");
            presentationLabel.setText("-");
            commentsArea.setText("No evaluation found for this session.");
        }

        System.out.println("ReportView: loaded (student=" + ID + ", session=SEM" + String.format("%03d", sessionId) + ", award=" + award + ")");
        revalidate();
        repaint();
    }

    
}
