import java.awt.*;
import javax.swing.*;

public class MainFrame extends JFrame {

    CardLayout cardLayout;
    JPanel container;

    public MainFrame() {
        setTitle("Seminar Management System");
        setSize(600, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        cardLayout = new CardLayout();
        container = new JPanel(cardLayout);

        // Create screens
        EditSchedule editScheduleFrame = new EditSchedule(this);
        ScheduleFrame viewScheduleFrame = new ScheduleFrame(this);
        CoordinatorFrame coordinatorFrame = new CoordinatorFrame(this);
        StudMngmentFrame studMngmentFrame = new StudMngmentFrame(this);
        ReportFrame reportFrame = new ReportFrame(this);

        // store screen into a single JPanel holding all the CardLayout (container)
        container.add(editScheduleFrame, "EDIT-SCHEDULE");
        container.add(viewScheduleFrame, "VIEW-SCHEDULE");
        container.add(coordinatorFrame,"coordinatorFrame");
        container.add(studMngmentFrame,"studMngmentFrame");
        container.add(reportFrame,"reportFrame");

        // add the container which contains all the CardLayout
        add(container);
        //show which screen u want to
        cardLayout.show(container, "coordinatorFrame"); //show the first screen to be displayed

        setVisible(true);
    }

    // Navigation method 
    public void showScreen(String name) {
        cardLayout.show(container, name);
        System.out.println("Switching to screen " + name);
    }

    public static void main(String[] args) {
        new MainFrame();
    }
}

