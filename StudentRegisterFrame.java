import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.sql.SQLException;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class StudentRegisterFrame extends JFrame {
    private final JTextField nameField = new JTextField(15);
    private final JPasswordField passField = new JPasswordField(15);
    private final JFrame parent;

    public StudentRegisterFrame(JFrame parent) {
        this.parent = parent;
        setTitle("Student Registration");
        setSize(350, 180);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JPanel p = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(4,4,4,4);
        c.gridx = 0; c.gridy = 0; p.add(new JLabel("Name:"), c);
        c.gridx = 1; p.add(nameField, c);
        c.gridx = 0; c.gridy = 1; p.add(new JLabel("Password:"), c);
        c.gridx = 1; p.add(passField, c);

        JButton createBtn = new JButton("Create Account");
        c.gridx = 0; c.gridy = 2; c.gridwidth = 2; p.add(createBtn, c);

        add(p);

        createBtn.addActionListener(e -> {
            String name = nameField.getText().trim();
            String pass = new String(passField.getPassword());
            if (name.isEmpty() || pass.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Enter name and password");
                return;
            }
            try {
                String newId = DBHelper.createStudent(name, pass);
                JOptionPane.showMessageDialog(this, "Account created. Your ID: " + newId);
                this.dispose();
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error creating account: " + ex.getMessage());
            }
        });
    }
}
