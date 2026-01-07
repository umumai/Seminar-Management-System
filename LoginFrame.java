import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import java.awt.CardLayout;

public class LoginFrame extends JFrame {
    private final JTextField idField = new JTextField(12);
    private final JPasswordField passField = new JPasswordField(12);
    private final CardLayout cardLayout;
    private final JPanel mainPanel;

    public LoginFrame() {
        setTitle("Seminar Management System");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(600, 400);
        setLocationRelativeTo(null);


        // Initialize CardLayout
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // Create login panel
        JPanel loginPanel = createLoginPanel();
        
        // Create other panels
        JPanel coordinatorPanel = CoordinatorPanel.createPanel(this); 
        JPanel dashboardPanel = DashboardPanel.createPanel(this); 
        JPanel registerPanel = StudentRegisterPanel.createPanel(this);

        // Add all panels to CardLayout
        mainPanel.add(loginPanel, "LoginPanel");
        mainPanel.add(coordinatorPanel, "CoordinatorPanel");
        mainPanel.add(dashboardPanel, "DashboardPanel");
        mainPanel.add(registerPanel, "RegisterPanel");
        // mainPanel.add(evaluatorPanel, "EvaluatorPanel");
        // mainPanel.add(studentPanel, "StudentPanel");

        add(mainPanel);

        // Show login panel initially
        cardLayout.show(mainPanel, "LoginPanel");
    }

    private JPanel createLoginPanel() {
        JPanel p = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(4,4,4,4);
        c.gridx = 0; c.gridy = 0; p.add(new JLabel("ID:"), c);
        c.gridx = 1; p.add(idField, c);
        c.gridx = 0; c.gridy = 1; p.add(new JLabel("Password:"), c);
        c.gridx = 1; p.add(passField, c);

        JButton loginBtn = new JButton("Login");
        JButton regBtn = new JButton("Register (Students)");

        c.gridx = 0; c.gridy = 2; c.gridwidth = 2; p.add(loginBtn, c);
        c.gridy = 3; p.add(regBtn, c);

        // DEV MODE: Quick access buttons (remove later)
        JPanel devPanel = TestDevMode.createDevPanel(this);
        c.gridy = 4; c.gridwidth = 2;
        p.add(devPanel, c);
        // DEV MODE: end



       // getRootPane().setDefaultButton(loginBtn); // can also press "Enter" to login

        loginBtn.addActionListener(e -> doLogin());

        regBtn.addActionListener(e -> {
            // StudentRegisterFrame rf = new StudentRegisterFrame(this);
            // rf.setVisible(true);
            cardLayout.show(mainPanel, "RegisterPanel");
        });
        return p;
    }

    private void doLogin() {
        String id = idField.getText().trim();
        String pass = new String(passField.getPassword());
        if (id.isEmpty() || pass.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Enter ID and password");
            return;
        }

        // User u = DBHelper.authenticate(id, pass);
        // Try to authenticate (will return null if database not set up yet)
        User u = null;
        try {
            u = DBHelper.authenticate(id, pass);
        } catch (Exception e) {
            // Database not ready - show message and allow test access
            JOptionPane.showMessageDialog(this, 
                "Database not configured yet.\nUse 'Test' buttons below for development access.");
            return;
        }
     

        if (u == null) {
            JOptionPane.showMessageDialog(this, "Invalid credentials");
            return;
        }

        String role = u.getRole();
        // int code = 0;
        switch (role) {
            // case "STUDENT": code = 1; break;
            // case "EVALUATOR": code = 2; break;
            case "COORDINATOR": 
                // CoordinatorFrame cf = new CoordinatorFrame(); // open coordinator frame
                // cf.setVisible(true);
                // this.dispose();
                cardLayout.show(mainPanel, "CoordinatorPanel");
                return;

            case "STUDENT":
                int code = 1;
                DashboardPanel.setUser(u, code);
                return;
            case "EVALUATOR":
                code = 2;
                DashboardPanel.setUser(u, code);
                break;
        }
    }

    public void showLoginPanel() {
        cardLayout.show(mainPanel, "LoginPanel");
    }
        //}

        // DashboardFrame df = new DashboardFrame(u, code);
        // df.setVisible(true);
        // this.dispose();
   // }
}
