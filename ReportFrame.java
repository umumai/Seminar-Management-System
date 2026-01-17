

import java.awt.*;
import javax.swing.*;

public class ReportFrame extends JPanel {
    private MainFrame frame;
    public ReportFrame(MainFrame frame) {
        this.frame = frame;
        setLayout(new BorderLayout());
                //panels are stored inside tabbedPane
        JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.LEFT);
        //configure how many session exist
        int sessionList = 5;
        for (int i = 1; i <= sessionList; i++) {
            JPanel mainPanel = new JPanel(new BorderLayout());
            //adjust the tab name here
            String tabsName = "Session " + i;
            //create stuffs here==========
            //JLabel label = new JLabel(tabsName);

        
        //table panel
        JPanel tablePanel = new JPanel(new GridLayout(9, 3, 1, 1));
        tablePanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        //header row
        JButton R1C1 = headerBox("Student ID", Color.BLACK, Color.white);
        JButton R1C2 = headerBox("Student Name", Color.BLACK, Color.white);
        JButton R1C3 = headerBox("Evaluator Name", Color.BLACK, Color.white);
        JButton R1C4 = headerBox("",Color.white,Color.black);
        //9:00AM row
        JButton R2C1 = headerBox("", Color.gray, Color.white);
        JButton R2C2 = headerBox("", Color.white, Color.black);
        JButton R2C3 = headerBox("", Color.white, Color.black);
        JButton R2C4 = headerBox("",Color.white,Color.black);
        //10:00AM row
        JButton R3C1 = headerBox("", Color.gray, Color.white);
        JButton R3C2 = headerBox("", Color.white, Color.black);
        JButton R3C3 = headerBox("", Color.white, Color.black);
        JButton R3C4 = headerBox("",Color.white,Color.black);
        //11:00AM row
        JButton R4C1 = headerBox("", Color.gray, Color.white);
        JButton R4C2 = headerBox("", Color.white, Color.black);
        JButton R4C3 = headerBox("", Color.white, Color.black);
        JButton R4C4 = headerBox("",Color.white,Color.black);
        //12:00PM row
        JButton R5C1 = headerBox("", Color.gray, Color.white);
        JButton R5C2 = headerBox("", Color.white, Color.black);
        JButton R5C3 = headerBox("", Color.white, Color.black);
        JButton R5C4 = headerBox("",Color.white,Color.black);
        //1:00PM row
        JButton R6C1 = headerBox("", Color.gray, Color.white);
        JButton R6C2 = headerBox("", Color.white, Color.black);
        JButton R6C3 = headerBox("", Color.white, Color.black);
        JButton R6C4 = headerBox("",Color.white,Color.black);
        //2:00PM row
        JButton R7C1 = headerBox("", Color.gray, Color.white);
        JButton R7C2 = headerBox("", Color.white, Color.black);
        JButton R7C3 = headerBox("", Color.white, Color.black);
        JButton R7C4 = headerBox("",Color.white,Color.black);
        //3:00PM row
        JButton R8C1 = headerBox("", Color.gray, Color.white);
        JButton R8C2 = headerBox("", Color.white, Color.black);
        JButton R8C3 = headerBox("", Color.white, Color.black);
        JButton R8C4 = headerBox("",Color.white,Color.black);
        //4:00PM row
        JButton R9C1 = headerBox("", Color.gray, Color.white);
        JButton R9C2 = headerBox("", Color.white, Color.black);
        JButton R9C3 = headerBox("", Color.white, Color.black);
        JButton R9C4 = headerBox("",Color.white,Color.black);

        //add all the JButton into the table panel
        tablePanel.add(R1C1);
        tablePanel.add(R1C2);
        tablePanel.add(R1C3);

        tablePanel.add(R2C1);
        tablePanel.add(R2C2);
        tablePanel.add(R2C3);

        tablePanel.add(R3C1);
        tablePanel.add(R3C2);
        tablePanel.add(R3C3);

        tablePanel.add(R4C1);
        tablePanel.add(R4C2);
        tablePanel.add(R4C3);

        tablePanel.add(R5C1);
        tablePanel.add(R5C2);
        tablePanel.add(R5C3);

        tablePanel.add(R6C1);
        tablePanel.add(R6C2);
        tablePanel.add(R6C3);

        tablePanel.add(R7C1);
        tablePanel.add(R7C2);
        tablePanel.add(R7C3);

        tablePanel.add(R8C1);
        tablePanel.add(R8C2);
        tablePanel.add(R8C3);

        tablePanel.add(R9C1);
        tablePanel.add(R9C2);
        tablePanel.add(R9C3);


            //add other panels to main Panel
            mainPanel.add(tablePanel, BorderLayout.CENTER);
            //mainPanel.add(label);
            //===========
            //add each tabs
            tabbedPane.addTab(tabsName, mainPanel);
        }

        //final setup
        add(tabbedPane, BorderLayout.CENTER);
    
    }

    public JButton headerBox(String text, Color bgColor, Color textColor){
        JButton btn = new JButton(text);
        btn.setBackground(bgColor);
        btn.setForeground(textColor);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setEnabled(false);
        return btn;
    }
    
}
