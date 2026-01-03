import java.awt.BorderLayout;
import java.awt.Font;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class DashboardFrame extends JFrame {
    public DashboardFrame(User user, int code) {
        setTitle("Dashboard - " + user.getRole());
        setSize(400,200);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel p = new JPanel(new BorderLayout());
        JLabel welcome = new JLabel("Welcome, " + user.getName() + " (" + user.getId() + ")", SwingConstants.CENTER);
        welcome.setFont(new Font("SansSerif", Font.PLAIN, 16));
        p.add(welcome, BorderLayout.NORTH);

        JLabel codeLabel = new JLabel(String.valueOf(code), SwingConstants.CENTER);
        codeLabel.setFont(new Font("SansSerif", Font.BOLD, 48));
        p.add(codeLabel, BorderLayout.CENTER);

        add(p);
    }



    
}
