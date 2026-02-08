package ui;
import Database.DBHelper;
import java.awt.*;
import java.io.File;
import javax.swing.*;
import models.User;
import models.Evaluator;

public class EvaluationFrame extends JPanel {
    private JFrame frame;
    private Evaluator currentEvaluator;
    private String currentStudentId;
    private Runnable onReturnToDashboard;
    private final JLabel reportLabel = new JLabel("Student evaluation");
    
    // Left panel components
    private JLabel researchTitleLabel;
    private JTextArea abstractArea;
    private JLabel supervisorLabel;
    private JLabel presentationTypeLabel;
    private JLabel materialLabel;
    private JButton downloadButton;
    
    // Right panel components
    private JSpinner claritySpinner;
    private JSpinner methodologySpinner;
    private JSpinner resultsSpinner;
    private JSpinner presentationSpinner;
    private JTextArea commentArea;
    private JButton submitButton;
    
    public EvaluationFrame(JFrame frame) {
        this.frame = frame;
        initializeUI();
    }
    
    public void setEvaluator(Evaluator evaluator) {
        this.currentEvaluator = evaluator;
    }

    /** Callback to run when Return is clicked or after successful submit (go back to evaluator dashboard). */
    public void setOnReturnToDashboard(Runnable runnable) {
        this.onReturnToDashboard = runnable;
    }
    
    private void initializeUI() {
        setLayout(new BorderLayout());
        
        // Top panel
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 5));
        String evaluatorName = currentEvaluator != null ? currentEvaluator.getName() : "Evaluator";
        reportLabel.setText("EVALUATOR " + evaluatorName);
        reportLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        JButton returnButton = new JButton("Return");
        returnButton.addActionListener(e -> returnToDashboard());
        topPanel.add(reportLabel, BorderLayout.WEST);
        topPanel.add(returnButton, BorderLayout.EAST);
        
        // Center panel with two sections
        JPanel centerPanel = new JPanel(new GridLayout(1, 2, 20, 0));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        centerPanel.setBackground(new Color(240, 240, 240));
        
        // Left panel - Presenter details
        JPanel leftPanel = createPresenterDetailsPanel();
        
        // Right panel - Evaluation form
        JPanel rightPanel = createEvaluationFormPanel();
        
        centerPanel.add(leftPanel);
        centerPanel.add(rightPanel);
        
        add(topPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
    }
    
    private JPanel createPresenterDetailsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.GRAY, 1),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(Color.WHITE);
        
        // Research Title
        JLabel titleLabel = new JLabel("Research Title:");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 12));
        researchTitleLabel = new JLabel("(RESEARCH TITLE)");
        researchTitleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        
        // Abstract
        JLabel abstractLabel = new JLabel("Abstract:");
        abstractLabel.setFont(new Font("SansSerif", Font.BOLD, 12));
        abstractArea = new JTextArea(5, 30);
        abstractArea.setEditable(false);
        abstractArea.setLineWrap(true);
        abstractArea.setWrapStyleWord(true);
        abstractArea.setText("(ABSTRACT)");
        abstractArea.setBackground(Color.WHITE);
        JScrollPane abstractScroll = new JScrollPane(abstractArea);
        abstractScroll.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        
        // Supervisor Name
        JLabel supervisorTitleLabel = new JLabel("Supervisor Name:");
        supervisorTitleLabel.setFont(new Font("SansSerif", Font.BOLD, 12));
        supervisorLabel = new JLabel("(SUPERVISOR NAME)");
        supervisorLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        
        // Presentation Type
        JLabel typeTitleLabel = new JLabel("Presentation Type:");
        typeTitleLabel.setFont(new Font("SansSerif", Font.BOLD, 12));
        presentationTypeLabel = new JLabel("(PRESENTATION TYPE)");
        presentationTypeLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        
        // Presentation Material
        JLabel materialTitleLabel = new JLabel("Presentation Material:");
        materialTitleLabel.setFont(new Font("SansSerif", Font.BOLD, 12));
        JPanel materialPanel = new JPanel(new BorderLayout());
        materialLabel = new JLabel("presentationfile.pdf");
        materialLabel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.RED, 1),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        downloadButton = new JButton("Download Material");
        downloadButton.addActionListener(e -> downloadMaterial());
        materialPanel.add(materialLabel, BorderLayout.CENTER);
        materialPanel.add(downloadButton, BorderLayout.EAST);
        materialPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        
        contentPanel.add(titleLabel);
        contentPanel.add(researchTitleLabel);
        contentPanel.add(Box.createVerticalStrut(10));
        contentPanel.add(abstractLabel);
        contentPanel.add(abstractScroll);
        contentPanel.add(Box.createVerticalStrut(10));
        contentPanel.add(supervisorTitleLabel);
        contentPanel.add(supervisorLabel);
        contentPanel.add(Box.createVerticalStrut(10));
        contentPanel.add(typeTitleLabel);
        contentPanel.add(presentationTypeLabel);
        contentPanel.add(Box.createVerticalStrut(10));
        contentPanel.add(materialTitleLabel);
        contentPanel.add(materialPanel);
        
        panel.add(contentPanel, BorderLayout.NORTH);
        return panel;
    }
    
    private JPanel createEvaluationFormPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.GRAY, 1),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBackground(Color.WHITE);
        
        String studentIdDisplay = currentStudentId != null ? currentStudentId : "Student";
        JLabel evalTitleLabel = new JLabel("Evaluation: " + studentIdDisplay);
        evalTitleLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        evalTitleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        
        // Score inputs
        JPanel scoresPanel = new JPanel(new GridBagLayout());
        scoresPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        
        // Problem clarity
        gbc.gridx = 0; gbc.gridy = 0;
        scoresPanel.add(new JLabel("Problem clarity:"), gbc);
        claritySpinner = new JSpinner(new SpinnerNumberModel(1, 1, 5, 1));
        gbc.gridx = 1;
        scoresPanel.add(claritySpinner, gbc);
        
        // Methodology
        gbc.gridx = 0; gbc.gridy = 1;
        scoresPanel.add(new JLabel("Methodology:"), gbc);
        methodologySpinner = new JSpinner(new SpinnerNumberModel(1, 1, 5, 1));
        gbc.gridx = 1;
        scoresPanel.add(methodologySpinner, gbc);
        
        // Results
        gbc.gridx = 0; gbc.gridy = 2;
        scoresPanel.add(new JLabel("Results:"), gbc);
        resultsSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 5, 1));
        gbc.gridx = 1;
        scoresPanel.add(resultsSpinner, gbc);
        
        // Presentation
        gbc.gridx = 0; gbc.gridy = 3;
        scoresPanel.add(new JLabel("Presentation:"), gbc);
        presentationSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 5, 1));
        gbc.gridx = 1;
        scoresPanel.add(presentationSpinner, gbc);
        
        // Comment
        gbc.gridx = 0; gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.BOTH;
        scoresPanel.add(new JLabel("Comment:"), gbc);
        commentArea = new JTextArea(4, 25);
        commentArea.setLineWrap(true);
        commentArea.setWrapStyleWord(true);
        commentArea.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        JScrollPane commentScroll = new JScrollPane(commentArea);
        gbc.gridy = 5;
        scoresPanel.add(commentScroll, gbc);
        
        // Submit button
        submitButton = new JButton("Submit");
        submitButton.setBackground(new Color(14, 69, 128));
        submitButton.setForeground(Color.WHITE);
        submitButton.setFocusPainted(false);
        submitButton.addActionListener(e -> submitEvaluation());
        submitButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        formPanel.add(evalTitleLabel);
        formPanel.add(scoresPanel);
        formPanel.add(Box.createVerticalStrut(20));
        formPanel.add(submitButton);
        
        panel.add(formPanel, BorderLayout.NORTH);
        return panel;
    }
    
    private void downloadMaterial() {
        if (currentStudentId == null) {
            JOptionPane.showMessageDialog(this, "No student selected.");
            return;
        }
        
        DBHelper.StudentProfileData profile = DBHelper.getStudentProfile(currentStudentId);
        if (profile == null || profile.filepath == null || profile.filepath.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No presentation material available.");
            return;
        }
        
        File file = new File(profile.filepath);
        if (file.exists()) {
            try {
                Desktop.getDesktop().open(file.getParentFile());
                JOptionPane.showMessageDialog(this, "Opening file location: " + file.getAbsolutePath());
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Unable to open file: " + e.getMessage());
            }
        } else {
            JOptionPane.showMessageDialog(this, "File not found: " + profile.filepath);
        }
    }
    
    private void submitEvaluation() {
        if (currentStudentId == null || currentEvaluator == null) {
            JOptionPane.showMessageDialog(this, "Missing student or evaluator information.");
            return;
        }
        
        int clarity = (Integer) claritySpinner.getValue();
        int methodology = (Integer) methodologySpinner.getValue();
        int results = (Integer) resultsSpinner.getValue();
        int presentation = (Integer) presentationSpinner.getValue();
        String comments = commentArea.getText().trim();
        
        // Validate scores (already constrained by spinner, but double-check)
        if (clarity < 1 || clarity > 5 || methodology < 1 || methodology > 5 ||
            results < 1 || results > 5 || presentation < 1 || presentation > 5) {
            JOptionPane.showMessageDialog(this, "All scores must be between 1 and 5.");
            return;
        }
        
        boolean success = DBHelper.saveEvaluation(
            currentStudentId,
            currentEvaluator.getId(),
            clarity,
            methodology,
            results,
            presentation,
            comments
        );
        
        if (success) { //message to confirmkan success 
            JOptionPane.showMessageDialog(this, "Evaluation submitted successfully!");
            System.out.println("Application for " + currentStudentId + " has been evaluated! Status = Evaluated");
            returnToDashboard();
        } else {
            JOptionPane.showMessageDialog(this, "Failed to submit evaluation. Please try again.");
        }
    }

    private void returnToDashboard() {
        if (onReturnToDashboard != null) {
            onReturnToDashboard.run();
        } else if (frame instanceof LoginFrame) {
            ((LoginFrame) frame).showPanel("EvaluatorPanel");
        }
    }
    
    // Adjust according to the student User object
    public void setCurrentStud(String studentId) {
        this.currentStudentId = studentId == null ? "" : studentId;
        
        if (currentStudentId.isEmpty()) {
            reportLabel.setText("Student evaluation");
            return;
        }
        
        User student = DBHelper.getUserbyID(currentStudentId);
        if (student != null) {
            reportLabel.setText("EVALUATOR " + (currentEvaluator != null ? currentEvaluator.getName() : "Evaluator"));
        }
        
        // Load student profile data
        DBHelper.StudentProfileData profile = DBHelper.getStudentProfile(currentStudentId);
        if (profile != null) {
            researchTitleLabel.setText(profile.researchTitle != null ? profile.researchTitle : "(No title)");
            abstractArea.setText(profile.abstractText != null ? profile.abstractText : "(No abstract)");
            supervisorLabel.setText(profile.supervisorName != null ? profile.supervisorName : "(No supervisor)");
            presentationTypeLabel.setText(profile.presentationType != null ? profile.presentationType : "(No type)");
            
            if (profile.filepath != null && !profile.filepath.isEmpty()) {
                File file = new File(profile.filepath);
                materialLabel.setText(file.getName());
            } else {
                materialLabel.setText("No file available");
            }
        } else {
            researchTitleLabel.setText("(RESEARCH TITLE)");
            abstractArea.setText("(ABSTRACT)");
            supervisorLabel.setText("(SUPERVISOR NAME)");
            presentationTypeLabel.setText("(PRESENTATION TYPE)");
            materialLabel.setText("No file available");
        }
        
        // Update evaluation form title
        if (submitButton != null) {
            Component[] components = submitButton.getParent().getComponents();
            for (Component comp : components) {
                if (comp instanceof JLabel && ((JLabel) comp).getText().startsWith("Evaluation:")) {
                    ((JLabel) comp).setText("Evaluation: " + currentStudentId);
                    break;
                }
            }
        }
        
        revalidate();
        repaint();
    }
}

