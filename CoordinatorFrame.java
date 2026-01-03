import java.awt.*;
import javax.swing.*;

public class CoordinatorFrame extends JFrame{
    private JPanel mainPanel;
    private CardLayout cardLayout;

    //constructor
    public CoordinatorFrame(){
        //defaults
        super("Postgraduate Academic Research Seminar");
        setSize(600, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        
        //Coordinator 
        JPanel coordinatorLayout = new JPanel(new BorderLayout());

        //BOTTOM PANEL
        JPanel bottomPanel = new JPanel(new BorderLayout());
        JButton returnButton = new JButton("return");
        bottomPanel.add(returnButton,BorderLayout.EAST);
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        coordinatorLayout.add(bottomPanel,BorderLayout.SOUTH);

        //CREATE SEMINAR
        JPanel seminarManagement = new JPanel();
        seminarManagement.setLayout(null);

        //date input
        JLabel dateLabel = new JLabel("Date :");
        dateLabel.setBounds(50, 30, 80, 25);
        seminarManagement.add(dateLabel);
        JTextField dateInput = new JTextField();
        dateInput.setBounds(140, 30, 150, 25);
        seminarManagement.add(dateInput);

        //venue input
        JLabel venueLabel = new JLabel("Venue :");
        venueLabel.setBounds(50, 70, 80, 25);
        seminarManagement.add(venueLabel);
        JTextField venueInput = new JTextField();
        venueInput.setBounds(140, 70, 150, 25);
        seminarManagement.add(venueInput);

        //session type input
        JLabel sTypeLabel = new JLabel("Session Type :");
        sTypeLabel.setBounds(50, 120, 120, 25);
        seminarManagement.add(sTypeLabel);
        JTextField sTypeInput = new JTextField();
        sTypeInput.setBounds(140, 120, 150, 25);
        seminarManagement.add(sTypeInput);

        //create button
        JButton createSeminarButton = new JButton("CREATE");
        createSeminarButton.setBounds(90,170,90,25);
        seminarManagement.add(createSeminarButton);


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

}
//     public static void main(String[] args) {
//         new CoordinatorFrame();
//     }

// }
