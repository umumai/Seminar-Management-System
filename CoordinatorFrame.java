

import java.awt.*;
import javax.swing.*;

//dalam coordinator frame ni kita just place the JTabbedPane. 
// and then we place the other frame (from other file) into each tabs
public class CoordinatorFrame extends JPanel {
    private MainFrame frame;
    public CoordinatorFrame(MainFrame frame) {
        this.frame = frame;
        setLayout(new BorderLayout());

        JTabbedPane tabPane = new JTabbedPane();

        // Create screens
        CreateSeminar createSeminar = new CreateSeminar(frame);
        EditSchedule editSchedule = new EditSchedule(frame);
        ReportFrame report = new ReportFrame(frame);
        AwardFrame award = new AwardFrame(frame);
        //add to tabbed Pane
        tabPane.add("New Seminar",createSeminar);
        tabPane.add("Schedule",editSchedule);
        tabPane.add("Report",report);
        tabPane.add("Award",award);

        add(tabPane,BorderLayout.CENTER);
    }
    
}
