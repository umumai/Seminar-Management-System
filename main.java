import javax.swing.SwingUtilities;

import Database.DBHelper;
import ui.CLNavigation;
import ui.LoginFrame;

public class main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> { // ensure GUI is created on the EDT 
            DBHelper.initDatabase(); // initialize database (create tables and seed initial data) for testing purposes
           // DBHelper.authenticate(null, null);
            LoginFrame lf = new LoginFrame(); // open login frame
            lf.setVisible(true);
            CLNavigation cl = new CLNavigation();
        });
    }
}

