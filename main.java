import java.awt.*;
import javax.swing.*;

public class main extends JFrame{
    private JPanel mainPanel;
    private CardLayout cardLayout;

    //constructor
    public main(){
        //defaults
        super("Postgraduate Academic Research Seminar");
        setSize(600, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        
        //Coordinator 
        JPanel coordinatorLayout = new JPanel(new BorderLayout());
        JPanel seminarManagement = new JPanel();
        JPanel scheduleManagement = new JPanel();
        JPanel assignManagement = new JPanel();

        JTabbedPane coordinatorTabs = new JTabbedPane();
        coordinatorTabs.addTab("Seminar",seminarManagement);
        coordinatorTabs.addTab("Schedule",scheduleManagement);
        coordinatorTabs.addTab("Assign",assignManagement);
        coordinatorLayout.add(coordinatorTabs,BorderLayout.CENTER);

        //final setup
        add(coordinatorLayout);
        setVisible(true);
    }

    public static void main(String[] args) {
        new main();
    }

}
