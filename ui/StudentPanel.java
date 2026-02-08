package ui;

import Database.DBHelper;
import java.awt.*;
import java.io.File;
import java.util.List;
import java.util.logging.Logger;
import javax.swing.*; //sub-package for file chooser (canot remove)
import javax.swing.filechooser.FileNameExtensionFilter;
import models.Session;
import models.User;

public class StudentPanel {
    private static final Logger logger = Logger.getLogger(StudentPanel.class.getName());
    private User currentUser;
    private JPanel studentPanel;
    private CardLayout cardLayout;
    private JPanel cardContainer;
    private JButton registerTabButton;
    private JButton statusTabButton;
    // private JButton scheduleTabButton; (to be added later)
    
    // State management (penting: tracks student's data)
    private Session registeredSeminar = null;
    // (TEMPORARY) Selected seminar (student currently registering but doesn't submit yet)
    private Session selectedSeminarTemp = null; 
    private String submissionStatus = null; // "Submitted", "Under Evaluation", "Completed"
    private String awardResult = "Pending"; // 
    
    // (INPUT) Registration Form Components
    private JTextField researchTitleField;
    private JTextArea abstractField;
    private JTextField supervisorNameField;
    private JComboBox<String> presentationTypeField; // "", "Oral", "Poster"
    private JTextField materialPathField; // File path 
    private JLabel errorLabel; // Shows validation errors
    
    private final JFrame parent;
    
    public StudentPanel(JFrame parent) {
        this.parent = parent;
        initializePanel();
    }
    
    public JPanel getPanel() {
        return studentPanel;
    }
    
    private void initializePanel() {
        studentPanel = new JPanel(new BorderLayout());
        
        studentPanel = new JPanel(new BorderLayout());
        
        // Top header panel (Welcome + Logout)
        JPanel topPanel = createTopPanel();
        studentPanel.add(topPanel, BorderLayout.NORTH);
        
        // Main content area with CardLayout - CREATE THIS FIRST
        cardLayout = new CardLayout();
        cardContainer = new JPanel(cardLayout);
        
        // Create Register, Status, and Registration Form panels
        JPanel registerPanel = createRegisterPanel();
        JPanel statusPanel = createStatusPanel();
        JPanel registrationFormPanel = createRegistrationFormPanel();
        
        cardContainer.add(registerPanel, "Register");
        cardContainer.add(statusPanel, "Status");
        cardContainer.add(registrationFormPanel, "RegistrationForm");
        
        // Tab navigation buttons panel - CREATE AFTER cardContainer is initialized
        JPanel tabPanel = createTabPanel();
        
        // Create a wrapper panel that contains tabs and content
        JPanel mainContentPanel = new JPanel(new BorderLayout());
        mainContentPanel.add(tabPanel, BorderLayout.NORTH);
        mainContentPanel.add(cardContainer, BorderLayout.CENTER);
        
        studentPanel.add(mainContentPanel, BorderLayout.CENTER);
        
        // Show Register panel by default
        cardLayout.show(cardContainer, "Register");
        registerTabButton.setEnabled(false);
    }
    
    private JPanel createTopPanel() {
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        topPanel.setBackground(new Color(240, 240, 240));
        
        // Welcome label
        String welcomeText = currentUser != null 
            ? "WELCOME, " + currentUser.getName() + " (" + currentUser.getId() + ")" 
            : "WELCOME, Student";
        JLabel welcomeLabel = new JLabel(welcomeText);
        welcomeLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        topPanel.add(welcomeLabel, BorderLayout.WEST);
        
        // Logout button
        JButton logoutButton = StudentPanelButtons.createLogoutButton();
        logoutButton.addActionListener(e -> {
            if (this.parent instanceof LoginFrame) {
                ((LoginFrame) this.parent).showLoginPanel();
            }
        });
        topPanel.add(logoutButton, BorderLayout.EAST);
        
        return topPanel;
    }
    
    private JPanel createTabPanel() {
        JPanel tabPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        tabPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.GRAY));
        
        registerTabButton = StudentPanelButtons.createTabButton("Register");
        statusTabButton = StudentPanelButtons.createTabButton("Status");
        
        // Style buttons as tabs
        StudentPanelButtons.styleTabButton(registerTabButton, true);
        StudentPanelButtons.styleTabButton(statusTabButton, false);
        
        registerTabButton.addActionListener(e -> {
            cardLayout.show(cardContainer, "Register");
            registerTabButton.setEnabled(false);
            statusTabButton.setEnabled(true);
            StudentPanelButtons.styleTabButton(registerTabButton, true);
            StudentPanelButtons.styleTabButton(statusTabButton, false);
            cardContainer.revalidate();
            cardContainer.repaint();
        });
        
        statusTabButton.addActionListener(e -> {
            refreshStatusPanel(); // Refresh status panel to show student's data
            cardLayout.show(cardContainer, "Status");
            statusTabButton.setEnabled(false);
            registerTabButton.setEnabled(true);
            StudentPanelButtons.styleTabButton(statusTabButton, true);
            StudentPanelButtons.styleTabButton(registerTabButton, false);
            // Force layout update
            cardContainer.revalidate();
            cardContainer.repaint();
            studentPanel.revalidate();
            studentPanel.repaint();
        });
        
        tabPanel.add(registerTabButton);
        tabPanel.add(statusTabButton);
        
        return tabPanel;
    }
    
    
    private JPanel createRegisterPanel() {
        JPanel registerPanel = new JPanel(new BorderLayout());
        registerPanel.setName("Register");
        registerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Check if student already registered
        if (registeredSeminar != null) {
            JLabel message = new JLabel("You have already registered for a seminar.", SwingConstants.CENTER);
            message.setFont(new Font("SansSerif", Font.PLAIN, 14));
            registerPanel.add(message, BorderLayout.CENTER);
            return registerPanel;
        }
        
        // Get available seminars (mock - replace with database call)
        List<Session> seminars = getAvailableSeminars();
        
        if (seminars.isEmpty()) {
            JLabel noSeminarLabel = new JLabel("No available seminar at the moment", SwingConstants.CENTER);
            noSeminarLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
            registerPanel.add(noSeminarLabel, BorderLayout.CENTER);
            return registerPanel;
        }
        
        // Create scrollable panel for seminar cards
        JPanel seminarsContainer = new JPanel();
        seminarsContainer.setLayout(new BoxLayout(seminarsContainer, BoxLayout.Y_AXIS));
        seminarsContainer.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        for (Session seminar : seminars) {
            JPanel seminarCard = createSeminarCard(seminar);
            seminarsContainer.add(seminarCard);
            seminarsContainer.add(Box.createVerticalStrut(15));
        }
        
        JScrollPane scrollPane = new JScrollPane(seminarsContainer);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        registerPanel.add(scrollPane, BorderLayout.CENTER);
        
        return registerPanel;
    }
    
    private JPanel createSeminarCard(Session seminar) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.GRAY, 1),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        card.setBackground(Color.WHITE);
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 150));
        
        // Left side: Seminar info
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBackground(Color.WHITE);
        
        JLabel seminarIdLabel = new JLabel("Seminar ID: " + seminar.getSessionID());
        seminarIdLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        
        JLabel detailsLabel = new JLabel(seminar.getDetails());
        detailsLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
        detailsLabel.setForeground(Color.DARK_GRAY);
        
        infoPanel.add(seminarIdLabel);
        infoPanel.add(Box.createVerticalStrut(5));
        infoPanel.add(detailsLabel);
        
        // Right side: Register button
        JButton registerButton = StudentPanelButtons.createSeminarRegisterButton();
        registerButton.addActionListener(e -> {
            logger.info("Register button clicked for seminar: " + seminar.getSessionID());
            handleRegistration(seminar);
        });
        
        card.add(infoPanel, BorderLayout.CENTER);
        card.add(registerButton, BorderLayout.EAST);
        
        // Ensure the card is properly sized and clickable
        card.setOpaque(true);
        card.revalidate();
        card.repaint();
        
        return card;
    }
    
    private void handleRegistration(Session seminar) {
        // Store the selected seminar and navigate directly to registration form
        if (seminar == null) {
            System.err.println("Error: Seminar is null");
            JOptionPane.showMessageDialog(studentPanel, "Error: Seminar not found", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (cardLayout == null || cardContainer == null) {
            System.err.println("Error: CardLayout or cardContainer is null");
            JOptionPane.showMessageDialog(studentPanel, "Error: Panel not initialized", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        System.out.println("Navigating to registration form for seminar: " + seminar.getSessionID());
        selectedSeminarTemp = seminar;
        
        try {
            // Refresh the form panel first to ensure it's up to date
            refreshRegistrationFormPanel();
            // Then show it
            cardLayout.show(cardContainer, "RegistrationForm");
            cardContainer.revalidate();
            cardContainer.repaint();
            System.out.println("Successfully navigated to registration form");
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(studentPanel, 
                "Error navigating to registration form: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private JPanel createStatusPanel() {
        JPanel statusPanel = new JPanel(new BorderLayout());
        statusPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Check if student has submitted
        if (registeredSeminar == null || submissionStatus == null) {
            JLabel noSubmissionLabel = new JLabel("No submission", SwingConstants.CENTER);
            noSubmissionLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
            statusPanel.add(noSubmissionLabel, BorderLayout.CENTER);
            return statusPanel;
        }
        
        // Create status card
        JPanel statusCard = new JPanel();
        statusCard.setLayout(new BoxLayout(statusCard, BoxLayout.Y_AXIS));
        statusCard.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.GRAY, 1),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        statusCard.setBackground(Color.WHITE);
        
        // Seminar ID
        JLabel seminarIdLabel = new JLabel("Seminar ID: " + registeredSeminar.getSessionID());
        seminarIdLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        
        // Seminar details
        JLabel detailsLabel = new JLabel(registeredSeminar.getDetails());
        detailsLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
        detailsLabel.setForeground(Color.DARK_GRAY);
        
        // Status
        JLabel statusLabel = new JLabel("Status: " + submissionStatus);
        statusLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
        
        statusCard.add(seminarIdLabel);
        statusCard.add(Box.createVerticalStrut(10));
        statusCard.add(detailsLabel);
        statusCard.add(Box.createVerticalStrut(15));
        statusCard.add(statusLabel);
        statusCard.add(Box.createVerticalStrut(10));
        
        // Show marks, comments, award only after coordinator has finalised (submission.status = Completed)
        boolean isFinalised = currentUser != null && "Completed".equals(DBHelper.getStudentSubmissionStatus(currentUser.getId()));
        if (isFinalised) {
            DBHelper.StudentEvaluationResults results = DBHelper.getStudentEvaluationResults(currentUser.getId());
            if (results != null) {
                // Evaluation Results Section
                JLabel resultsTitleLabel = new JLabel("Evaluation Results:");
                resultsTitleLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
                statusCard.add(resultsTitleLabel);
                statusCard.add(Box.createVerticalStrut(10));
                
                // Scores
                JPanel scoresPanel = new JPanel(new GridLayout(4, 2, 5, 5));
                scoresPanel.setBackground(Color.WHITE);
                scoresPanel.add(new JLabel("Problem Clarity:"));
                scoresPanel.add(new JLabel(String.valueOf(results.avgClarityScore) + "/5"));
                scoresPanel.add(new JLabel("Methodology:"));
                scoresPanel.add(new JLabel(String.valueOf(results.avgMethodologyScore) + "/5"));
                scoresPanel.add(new JLabel("Results:"));
                scoresPanel.add(new JLabel(String.valueOf(results.avgResultsScore) + "/5"));
                scoresPanel.add(new JLabel("Presentation:"));
                scoresPanel.add(new JLabel(String.valueOf(results.avgPresentationScore) + "/5"));
                
                statusCard.add(scoresPanel);
                statusCard.add(Box.createVerticalStrut(10));
                
                // Comments
                if (results.comments != null && !results.comments.trim().isEmpty()) {
                    JLabel commentsTitleLabel = new JLabel("Comments:");
                    commentsTitleLabel.setFont(new Font("SansSerif", Font.BOLD, 12));
                    statusCard.add(commentsTitleLabel);
                    
                    JTextArea commentsArea = new JTextArea(results.comments);
                    commentsArea.setEditable(false);
                    commentsArea.setLineWrap(true);
                    commentsArea.setWrapStyleWord(true);
                    commentsArea.setBackground(Color.WHITE);
                    commentsArea.setBorder(BorderFactory.createLineBorder(Color.GRAY));
                    JScrollPane commentsScroll = new JScrollPane(commentsArea);
                    commentsScroll.setPreferredSize(new Dimension(0, 80));
                    statusCard.add(commentsScroll);
                    statusCard.add(Box.createVerticalStrut(10));
                }
                
                // Award Result
                JLabel awardLabel = new JLabel("Award: " + results.award);
                awardLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
                if (!"No award".equals(results.award)) {
                    awardLabel.setForeground(new Color(0, 150, 0));
                }
                statusCard.add(awardLabel);
            } else {
                // Award Result (fallback if no results yet)
                JLabel awardLabel = new JLabel("Award Result: " + awardResult);
                awardLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
                statusCard.add(awardLabel);
            }
        } else {
            // Award Result (pending)
            JLabel awardLabel = new JLabel("Award Result: " + awardResult);
            awardLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
            statusCard.add(awardLabel);
        }
        
        statusPanel.add(statusCard, BorderLayout.CENTER);
        
        return statusPanel;
    }
    
    private void refreshRegisterPanel() {
        // Find and remove existing Register panel by name
        java.awt.Component existingComponent = null;
        for (java.awt.Component comp : cardContainer.getComponents()) {
            if ("Register".equals(comp.getName())) {
                existingComponent = comp;
                break;
            }
        }
        if (existingComponent != null) {
            cardContainer.remove(existingComponent);
        }
        
        // Create and add new Register panel
        JPanel newRegisterPanel = createRegisterPanel();
        cardContainer.add(newRegisterPanel, "Register", 0);
        cardLayout.show(cardContainer, "Register");
        studentPanel.revalidate();
        studentPanel.repaint();
    }
    
    private void refreshStatusPanel() {
        // Remove the Status panel (it's always at index 1)
        if (cardContainer.getComponentCount() > 1) {
            cardContainer.remove(1);
        }
        // Add the new Status panel
        JPanel newStatusPanel = createStatusPanel();
        cardContainer.add(newStatusPanel, "Status");
    }
    
    private List<Session> getAvailableSeminars() {
        // Get available seminars from database (excluding ones student already registered for)
        String studentId = currentUser != null ? currentUser.getId() : null;
        return DBHelper.getAvailableSessions(studentId);
    }
    
    private JPanel createRegistrationFormPanel() {
        JPanel formPanel = new JPanel(new BorderLayout());
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Top section with title and Go back button
        JPanel topSection = new JPanel(new BorderLayout());
        topSection.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        
        String seminarId = selectedSeminarTemp != null ? selectedSeminarTemp.getSessionID() : "SEMINAR";
        JLabel titleLabel = new JLabel("REGISTRATION (" + seminarId + ")");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        topSection.add(titleLabel, BorderLayout.WEST);
        
        JButton goBackButton = StudentPanelButtons.createGoBackButton();
        goBackButton.addActionListener(e -> {
            selectedSeminarTemp = null; // Clear temporary selection on cancel/go back
            cardLayout.show(cardContainer, "Register");
        });
        topSection.add(goBackButton, BorderLayout.EAST);
        formPanel.add(topSection, BorderLayout.NORTH);
        
        // Seminar details section
        JPanel detailsPanel = new JPanel();
        detailsPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder("Seminar details"),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        detailsPanel.setBackground(new Color(240, 240, 240));
        detailsPanel.setPreferredSize(new Dimension(Integer.MAX_VALUE, 80));
        
        if (selectedSeminarTemp != null) {
            JLabel detailsLabel = new JLabel(selectedSeminarTemp.getDetails());
            detailsLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
            detailsPanel.add(detailsLabel);
        } else {
            JLabel noDetailsLabel = new JLabel("No seminar selected");
            detailsPanel.add(noDetailsLabel);
        }
        formPanel.add(detailsPanel, BorderLayout.CENTER);
        
        // Form fields panel
        JPanel formFieldsPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;
        
        // Error message label (initially hidden)
        errorLabel = new JLabel("");
        errorLabel.setForeground(Color.RED);
        errorLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        formFieldsPanel.add(errorLabel, gbc);
        gbc.gridwidth = 1;
        
        // Research Title
        gbc.gridx = 0;
        gbc.gridy = 1;
        formFieldsPanel.add(new JLabel("Research Title:"), gbc);
        researchTitleField = new JTextField(30);
        researchTitleField.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        gbc.gridx = 1;
        formFieldsPanel.add(researchTitleField, gbc);
        
        // Abstract
        gbc.gridx = 0;
        gbc.gridy = 2;
        formFieldsPanel.add(new JLabel("Abstract:"), gbc);
        abstractField = new JTextArea(5, 30);
        abstractField.setLineWrap(true);
        abstractField.setWrapStyleWord(true);
        abstractField.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        JScrollPane abstractScroll = new JScrollPane(abstractField);
        gbc.gridx = 1;
        formFieldsPanel.add(abstractScroll, gbc);
        
        // Supervisor Name
        gbc.gridx = 0;
        gbc.gridy = 3;
        formFieldsPanel.add(new JLabel("Supervisor Name:"), gbc);
        supervisorNameField = new JTextField(30);
        supervisorNameField.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        gbc.gridx = 1;
        formFieldsPanel.add(supervisorNameField, gbc);
        
        // Presentation Type
        gbc.gridx = 0;
        gbc.gridy = 4;
        JLabel presentationLabel = new JLabel("Presentation Type:");
        formFieldsPanel.add(presentationLabel, gbc);
        String[] presentationTypes = {"", "Oral", "Poster"};
        presentationTypeField = new JComboBox<>(presentationTypes);
        presentationTypeField.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        gbc.gridx = 1;
        formFieldsPanel.add(presentationTypeField, gbc);
        
        // Attach Material
        gbc.gridx = 0;
        gbc.gridy = 5;
        formFieldsPanel.add(new JLabel("Attach Material:"), gbc);
        JButton attachButton = StudentPanelButtons.createAttachMaterialButton();
        materialPathField = new JTextField(25);
        materialPathField.setEditable(false);
        materialPathField.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        JPanel materialPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        materialPanel.add(attachButton);
        materialPanel.add(materialPathField);
        attachButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Select Material File");
            FileNameExtensionFilter filter = new FileNameExtensionFilter(
                "Documents (PDF, DOC, DOCX)", "pdf", "doc", "docx");
            fileChooser.setFileFilter(filter);
            int result = fileChooser.showOpenDialog(formPanel);
            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                materialPathField.setText(selectedFile.getAbsolutePath());
                materialPathField.setBorder(BorderFactory.createLineBorder(Color.GRAY));
            }
        });
        gbc.gridx = 1;
        formFieldsPanel.add(materialPanel, gbc);
        
        // Submit button
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        JButton submitButton = StudentPanelButtons.createSubmitButton();
        submitButton.addActionListener(e -> handleSubmitRegistration());
        formFieldsPanel.add(submitButton, gbc);
        
        JScrollPane formScroll = new JScrollPane(formFieldsPanel);
        formScroll.setBorder(BorderFactory.createEmptyBorder());
        
        // Create a container panel
        JPanel containerPanel = new JPanel(new BorderLayout());
        containerPanel.add(detailsPanel, BorderLayout.NORTH);
        containerPanel.add(formScroll, BorderLayout.CENTER);
        
        formPanel.add(containerPanel, BorderLayout.CENTER);
        
        return formPanel;
    }
    
    private void refreshRegistrationFormPanel() {
        // Remove and recreate registration form panel
        if (cardContainer.getComponentCount() > 2) {
            cardContainer.remove(2);
        }
        JPanel newFormPanel = createRegistrationFormPanel();
        cardContainer.add(newFormPanel, "RegistrationForm");
        cardContainer.revalidate();
        cardContainer.repaint();
    }   
    
    
    private void handleSubmitRegistration() {
        // Reset error label and borders
        errorLabel.setText("");
        resetFieldBorders();
        
        // Validate all fields
        boolean isValid = true;
        String errorMessage = "Please fill in all of the information";
        
        if (researchTitleField.getText().trim().isEmpty()) {
            setFieldError(researchTitleField);
            isValid = false;
        }
        
        if (abstractField.getText().trim().isEmpty()) {
            setFieldError(abstractField);
            isValid = false;
        }
        
        if (supervisorNameField.getText().trim().isEmpty()) {
            setFieldError(supervisorNameField);
            isValid = false;
        }
        
        String presentationType = (String) presentationTypeField.getSelectedItem();
        if (presentationType == null || presentationType.isEmpty()) {
            setFieldError(presentationTypeField);
            isValid = false;
        }
        
        if (materialPathField.getText().trim().isEmpty()) {
            setFieldError(materialPathField);
            isValid = false;
        }
        
        if (!isValid) {
            errorLabel.setText(errorMessage);
            return;
        }
        
        // All fields are valid, save to database
        try {
            String studentId = currentUser != null ? currentUser.getId() : null;
            if (studentId == null || selectedSeminarTemp == null) {
                JOptionPane.showMessageDialog(studentPanel, "Error: User or seminar not found", 
                    "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Extract session ID from seminar (e.g., "SEM001" -> 1)
            String sessionIdStr = null;
            int sessionId = -1;
            try {
                String sessionIdFull = selectedSeminarTemp.getSessionID();
                if (sessionIdFull == null || sessionIdFull.trim().isEmpty()) {
                    throw new IllegalArgumentException("Session ID is null or empty");
                }
                // Safely strip "SEM" prefix (case-insensitive)
                sessionIdStr = sessionIdFull.toUpperCase().replace("SEM", "").trim();
                if (sessionIdStr.isEmpty()) {
                    throw new IllegalArgumentException("Session ID has no numeric part after 'SEM' prefix");
                }
                // Validate that remaining string is numeric
                if (!sessionIdStr.matches("\\d+")) {
                    throw new NumberFormatException("Session ID contains non-numeric characters: " + sessionIdStr);
                }
                sessionId = Integer.parseInt(sessionIdStr);
                if (sessionId < 0) {
                    throw new IllegalArgumentException("Session ID must be non-negative");
                }
            } catch (NumberFormatException e) {
                System.err.println("Error parsing session ID: " + e.getMessage());
                e.printStackTrace();
                JOptionPane.showMessageDialog(studentPanel, 
                    "Error: Invalid session ID format. Cannot proceed with registration.",
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
                return;
            } catch (IllegalArgumentException e) {
                System.err.println("Error parsing session ID: " + e.getMessage());
                e.printStackTrace();
                JOptionPane.showMessageDialog(studentPanel, 
                    "Error: Invalid session ID format. Cannot proceed with registration.",
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Save student profile and registration
            DBHelper.saveStudentRegistration(
                studentId,
                researchTitleField.getText().trim(),
                abstractField.getText().trim(),
                supervisorNameField.getText().trim(),
                presentationType,
                materialPathField.getText().trim(),
                sessionId
            );

            // create appointment so coordinator can assign evaluator; status "Submitted" shows in Appnt mangemntent
            DBHelper.insertAppointment(sessionId, studentId);

            // confirm the application submit for coordinator on terminal output
            System.out.println("Successfully submitted application for " + studentId + " Status = Submitted!");
            
            // Update local state - only set registeredSeminar on successful submission
            registeredSeminar = selectedSeminarTemp;
            selectedSeminarTemp = null; // Clear temporary selection after successful submission
            submissionStatus = "Submitted";
            
            // Show success message
            JOptionPane.showMessageDialog(
                studentPanel,
                "Registration submitted successfully!",
                "Success",
                JOptionPane.PLAIN_MESSAGE
            );
            
            // Return to Register panel
            cardLayout.show(cardContainer, "Register");
            refreshRegisterPanel();
            
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(studentPanel, 
                "Error saving registration: " + e.getMessage(),
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void setFieldError(javax.swing.JComponent field) {
        field.setBorder(BorderFactory.createLineBorder(Color.RED, 2));
    }
    
    private void resetFieldBorders() {
        if (researchTitleField != null) researchTitleField.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        if (abstractField != null) abstractField.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        if (supervisorNameField != null) supervisorNameField.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        if (presentationTypeField != null) presentationTypeField.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        if (materialPathField != null) materialPathField.setBorder(BorderFactory.createLineBorder(Color.GRAY));
    }

    // ---- End of Registration Form ----
    
    public void setUser(User user) {
        currentUser = user;
        
        // Reset transient state only
        selectedSeminarTemp = null;
        
        // Load student's registration and submission status from database before building UI
        if (user != null) {
            registeredSeminar = null;
            submissionStatus = null;
            awardResult = "Pending";
            loadStudentData(user.getId());
        } else {
            registeredSeminar = null;
            submissionStatus = null;
            awardResult = "Pending";
        }
        
        // Clear all form fields
        clearRegistrationForm();
        
        if (studentPanel != null) {
            // Refresh top panel with new user info
            studentPanel.remove(0);
            JPanel newTopPanel = createTopPanel();
            studentPanel.add(newTopPanel, BorderLayout.NORTH, 0);
            
            // Refresh all panels to show user-specific data (now using loaded state)
            refreshRegisterPanel();
            refreshStatusPanel();
            refreshRegistrationFormPanel();
            
            // Return to Register panel
            if (cardLayout != null && cardContainer != null) {
                cardLayout.show(cardContainer, "Register");
            }
            
            studentPanel.revalidate();
            studentPanel.repaint();
        }
    }
    
    private void clearRegistrationForm() {
        if (researchTitleField != null) researchTitleField.setText("");
        if (abstractField != null) abstractField.setText("");
        if (supervisorNameField != null) supervisorNameField.setText("");
        if (presentationTypeField != null) presentationTypeField.setSelectedIndex(0);
        if (materialPathField != null) materialPathField.setText("");
        if (errorLabel != null) errorLabel.setText("");
        resetFieldBorders();
    }
    
    private void loadStudentData(String studentId) {
        try {
            // Load registered seminar
            Session seminar = DBHelper.getStudentRegisteredSeminar(studentId);
            if (seminar != null) {
                registeredSeminar = seminar;
            }
            
            // Load display status for label: Submitted | Under evaluation | Completed
            String displayStatus = DBHelper.getStudentDisplayStatus(studentId);
            if (displayStatus != null) {
                submissionStatus = displayStatus;
            }
            
            // Load award result
            String award = DBHelper.getStudentAwardResult(studentId);
            if (award != null) {
                awardResult = award;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    // DB integration
    public void setRegisteredSeminar(Session seminar) {
        registeredSeminar = seminar;
    }
    
    public void setSubmissionStatus(String status) {
        submissionStatus = status;
    }
    
    public void setAwardResult(String award) {
        awardResult = award;
    }
    
    public Session getRegisteredSeminar() {
        return registeredSeminar;
    }
}
