package ui;

import java.awt.*;
import java.util.ArrayList;
import javax.swing.*;

import util.GUI;

public class ScheduleFrame extends JFrame{

    //constructor
    public ScheduleFrame(){
        //defaults
        super("Postgraduate Academic Research Seminar");
        setSize(600, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        
        //schedulePanel is the main panel
        JPanel schedulePanel = new JPanel(new BorderLayout());
        
        //DATA FOR SCHEDULE (all schedule use this same data)
        String[] columns = {"Time", "Event", "Person"};
        Object[][] datas = {
            {"09:00 AM", "Math Class", "Alice"},
            {"10:00 AM", "Science Lab", "Bob"},
            {"11:00 AM", "Meeting", "Admin"}
        };

        //Store for date info
        ArrayList<String> dateList = new ArrayList<>();
        dateList.add("12 Dec 2025");
        dateList.add("1 Jan 2026");
        dateList.add("15 Jan 2026");

        //Store for venue info
        ArrayList<String> venueList = new ArrayList<>();
        venueList.add("CQCR1004");
        venueList.add("CNMX0001");
        venueList.add("CNMX0003");

        int sessionList = 3;
        int tabsList = sessionList;

        JTabbedPane scheduleTabs = new JTabbedPane();

        for (int i = 1; i <= tabsList; i++) {
            JPanel panel = new JPanel();
            panel.setLayout(null);
            String tabsName = "Session " + i;

            //==========
            // add stuffs into the each panel
            GUI.createText("Date :", 40, 10, panel);
            GUI.createText(dateList.get(i-1), 90, 10, panel);
            GUI.createText("Venue :", 40, 30, panel);
            GUI.createText(venueList.get(i-1), 100, 30, panel);
            GUI.createText("Session Type :", 40, 50, panel);
            GUI.createText("Oral", 130, 50, panel);
            //uncomment for example of createClickableText function
            //GUI.createClikableText("Oral", 130, 50, Color.BLACK, Color.BLUE, panel);
            
            GUI.createTable(datas, columns, panel);

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
