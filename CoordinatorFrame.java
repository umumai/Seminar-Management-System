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

        //==============
        //CREATE SEMINAR
        //==============
        JPanel createSeminar = new JPanel();
        createSeminar.setLayout(null);

        //[TEXT] date :
        JLabel dateLabel = new JLabel("Date :");
        dateLabel.setBounds(50, 30, 80, 25);
        JTextField dateInput = new JTextField();
        dateInput.setBounds(140, 30, 150, 25);
        JLabel dateText = new JLabel("10 Jan 2026");
        dateText.setBounds(90, 30, 150, 25);

        //venue details
        JLabel venueLabel = new JLabel("Venue :");
        venueLabel.setBounds(50, 70, 80, 25);
        JTextField venueInput = new JTextField();
        venueInput.setBounds(140, 70, 150, 25);
        JLabel venueText = new JLabel("MMU Cyberjaya");
        venueText.setBounds(100, 70, 150, 25);

        
        //session type input
        JLabel sTypeLabel = new JLabel("Session Type :");
        sTypeLabel.setBounds(50, 120, 120, 25);
        JTextField sTypeInput = new JTextField();
        sTypeInput.setBounds(140, 120, 150, 25);
        JLabel sTypeText = new JLabel("Oral");
        sTypeText.setBounds(140, 120, 150, 25);

        
        //create button
        JButton createSeminarButton = new JButton("CREATE");
        createSeminarButton.setBounds(90,170,90,25);
        
        //add into the createSeminar panel
        createSeminar.add(dateLabel);
        createSeminar.add(dateInput);
        createSeminar.add(venueLabel);
        createSeminar.add(venueInput);
        createSeminar.add(sTypeLabel);
        createSeminar.add(sTypeInput);
        createSeminar.add(createSeminarButton);
        
        //========
        //SCHEDULE
        //========
        JPanel scheduleManagement = new JPanel();
        scheduleManagement.setLayout(null);
        //add into scheduleManagement panel
        scheduleManagement.add(dateLabel);
        scheduleManagement.add(dateText);
        scheduleManagement.add(venueLabel);
        scheduleManagement.add(venueText);
        scheduleManagement.add(sTypeLabel);
        scheduleManagement.add(sTypeText);

        //create button
        JButton scheduleManagementButton = new JButton("CREATE");
        scheduleManagementButton.setBounds(90,170,90,25);


        JPanel assignManagement = new JPanel();

        // JTabbedPane coordinatorTabs = new JTabbedPane();
        // coordinatorTabs.addTab("Seminar",createSeminar);
        // coordinatorTabs.addTab("Schedule",scheduleManagement);
        // coordinatorTabs.addTab("Assign",assignManagement);
        // coordinatorLayout.add(coordinatorTabs,BorderLayout.CENTER);

        mainPanel.add(createSeminar,"CREATE SEMINAR");
        mainPanel.add(scheduleManagement,"SCHEDULER");
        mainPanel.add(assignManagement,"ASSIGN");

        CardLayout cl = (CardLayout) mainPanel.getLayout();
        cl.show(mainPanel,"SCHEDULER");

        //final setup
        add(mainPanel);
        setVisible(true);
    }

}
//     public static void main(String[] args) {
//         new CoordinatorFrame();
//     }

// }
