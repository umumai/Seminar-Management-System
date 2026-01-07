import java.awt.BorderLayout;
import java.awt.Font;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class DashboardPanel {
    private static User currentUser;
    private static int currentCode;
    private static JPanel dashboardPanel;

    public static JPanel createPanel(JFrame parent) {
        dashboardPanel = new JPanel(new BorderLayout());
        
        // Return button
        JPanel topPanel = new JPanel(new BorderLayout());
        JButton returnButton = new JButton("Logout");
        returnButton.addActionListener(e -> {
            if (parent instanceof LoginFrame) {
                ((LoginFrame) parent).showLoginPanel();
            }
        });
        topPanel.add(returnButton, BorderLayout.EAST);
        dashboardPanel.add(topPanel, BorderLayout.NORTH);

        // Will be updated when user logs in
        updateContent();
        
        return dashboardPanel;
    }

    public static void setUser(User user, int code) {
        currentUser = user;
        currentCode = code;
        updateContent();
    }

    private static void updateContent() {
        if (dashboardPanel == null) return;
        
        // Remove existing content (except top panel)
        if (dashboardPanel.getComponentCount() > 1) {
            dashboardPanel.remove(1);
        }
        if (dashboardPanel.getComponentCount() > 1) {
            dashboardPanel.remove(1);
        }

        if (currentUser != null) {
            JLabel welcome = new JLabel("Welcome, " + currentUser.getName() + " (" + currentUser.getId() + ")", SwingConstants.CENTER);
            welcome.setFont(new Font("SansSerif", Font.PLAIN, 16));
            dashboardPanel.add(welcome, BorderLayout.CENTER);

            JLabel codeLabel = new JLabel(String.valueOf(currentCode), SwingConstants.CENTER);
            codeLabel.setFont(new Font("SansSerif", Font.BOLD, 48));
            dashboardPanel.add(codeLabel, BorderLayout.SOUTH);
        }

        dashboardPanel.revalidate();
        dashboardPanel.repaint();
    }
}