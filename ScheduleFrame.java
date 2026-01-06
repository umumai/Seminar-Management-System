import java.awt.*;
import java.util.ArrayList;
import javax.swing.*;

public class ScheduleFrame extends JFrame{

    //constructor
    public ScheduleFrame(){
        //defaults
        super("Postgraduate Academic Research Seminar");
        setSize(600, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        
        //mainPanel for this one
        JPanel schedulePanel = new JPanel(new BorderLayout());

        //TRY TABLE
        String[] columns = {"Time", "Event", "Person"};
        Object[][] data = {
            {"09:00 AM", "Math Class", "Alice"},
            {"10:00 AM", "Science Lab", "Bob"},
            {"11:00 AM", "Meeting", "Admin"}
        };

        

        //


        //============
        //Store for date info
        ArrayList<String> dateList = new ArrayList<>();

        dateList.add("12 Dec 2025");
        dateList.add("1 Jan 2026");
        dateList.add("15 Jan 2026");
        //=============

        //============
        //Store for date info
        ArrayList<String> venueList = new ArrayList<>();

        venueList.add("CQCR1004");
        venueList.add("CNMX0001");
        venueList.add("CNMX0003");
        //=============
        
        //session type 
        // JLabel sTypeText = new JLabel("Oral");
        // sTypeText.setBounds(140, 70, 150, 25);

        int sessionList = 3;
        int tabsList = sessionList;

        JTabbedPane scheduleTabs = new JTabbedPane();

        for (int i = 1; i <= tabsList; i++) {
            JPanel panel = new JPanel();
            panel.setLayout(null);
            String tabsName = "Session " + i;

            //==========
            // add stuffs into the each panel
            JLabel dateLabel = new JLabel("Date :");
            dateLabel.setBounds(40, 10, 80, 25);
            panel.add(dateLabel);

            JLabel dateText = new JLabel(dateList.get(i-1));
            dateText.setBounds(90, 10, 150, 25);
            panel.add(dateText);

            JLabel venueLabel = new JLabel("Venue :");
            venueLabel.setBounds(40, 30, 80, 25);
            panel.add(venueLabel);

            JLabel venueText = new JLabel(venueList.get(i-1));
            venueText.setBounds(100, 30, 150, 25);
            panel.add(venueText);

            JLabel sessionTypeLabel = new JLabel("Session Type :");
            sessionTypeLabel.setBounds(40, 50, 120, 25);
            panel.add(sessionTypeLabel);

            JTable j = new JTable(data, columns);
            j.setBounds(30, 40, 200, 300);

            JScrollPane sp = new JScrollPane(j);
            panel.add(sp);

            //==========

            scheduleTabs.addTab(tabsName, panel);
        }

        schedulePanel.add(scheduleTabs,BorderLayout.CENTER);

        //===========
        //BOTTOM PANEL
        JPanel bottomPanel = new JPanel(new BorderLayout());
        JButton returnButton = new JButton("return");
        bottomPanel.add(returnButton,BorderLayout.EAST);
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        schedulePanel.add(bottomPanel,BorderLayout.SOUTH);
        //=============
        

        //final setup
        add(schedulePanel);
        setVisible(true);
    }

}
