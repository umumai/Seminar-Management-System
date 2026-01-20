import javax.swing.SwingUtilities;

import Database.DBHelper;
import ui.CLNavigation;
import ui.LoginFrame;

public class main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> { // ensure GUI is created on the EDT 
            DBHelper.authenticate(null, null);
            LoginFrame lf = new LoginFrame(); // open login frame
            lf.setVisible(true);
            CLNavigation cl = new CLNavigation();
                        
            // DEV MODE (Switch to any panel name to open it)
            // lf.showPanel("CoordinatorPanel");  // open coordinator panel

        });
    }
}

// import javax.swing.SwingUtilities;

// public class main {
//     public static void main(String[] args) {

//         SwingUtilities.invokeLater(() -> {

//             // Auto-create default users if database is empty
//             DBHelper.seedUsersIfEmpty();

//             // Open login frame
//             LoginFrame lf = new LoginFrame();
//             lf.setVisible(true);
//         });
//     }
// }
