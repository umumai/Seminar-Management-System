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

public class LoginFrame extends JFrame {
    private final JTextField idField = new JTextField(12);
    private final JPasswordField passField = new JPasswordField(12);

    public LoginFrame() {
        setTitle("Seminar Management - Login");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(350, 200);
        setLocationRelativeTo(null);

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

        add(p);


       // getRootPane().setDefaultButton(loginBtn); // can also press "Enter" to login

        loginBtn.addActionListener(e -> doLogin());

        regBtn.addActionListener(e -> {
            StudentRegisterFrame rf = new StudentRegisterFrame(this);
            rf.setVisible(true);
        });
    }

    private void doLogin() {
        String id = idField.getText().trim();
        String pass = new String(passField.getPassword());
        if (id.isEmpty() || pass.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Enter ID and password");
            return;
        }

        User u = DBHelper.authenticate(id, pass);
        if (u == null) {
            JOptionPane.showMessageDialog(this, "Invalid credentials");
            return;
        }

        String role = u.getRole();
        int code = 0;
        switch (role) {
            case "STUDENT": code = 1; break;
            case "EVALUATOR": code = 2; break;
            case "COORDINATOR": 
                CoordinatorFrame cf = new CoordinatorFrame(); // open coordinator frame
                cf.setVisible(true);
                this.dispose();
                return;

        }

        DashboardFrame df = new DashboardFrame(u, code);
        df.setVisible(true);
        this.dispose();
    }
}
