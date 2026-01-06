import javax.swing.SwingUtilities;

public class main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> { // ensure GUI is created on the EDT 
            DBHelper.initDatabase(); 
            LoginFrame lf = new LoginFrame(); // open login frame
            lf.setVisible(true);
        });
    }
}

