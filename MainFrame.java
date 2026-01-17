

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
        EditSchedule editSchedule = new EditSchedule(this);
        ScheduleFrame viewSchedule = new ScheduleFrame(this);
        CoordinatorFrame coordinatorFrame = new CoordinatorFrame(this);
        CreateSeminar createSeminar = new CreateSeminar(this);
        StudentReport viewReport = new StudentReport(this);

        // store screen into a single JPanel holding all the CardLayout (container)
        container.add(editSchedule, "EDIT-SCHEDULE");
        container.add(viewSchedule, "VIEW-SCHEDULE");
        container.add(coordinatorFrame,"COORDINATOR");
        container.add(viewReport,"VIEW-REPORT");

        // add the container which contains all the CardLayout
        add(container);
        //show which screen u want to
        cardLayout.show(container, "COORDINATOR"); //show the first screen to be displayed

        setVisible(true);
    }

    // Navigation method (IMPORTANT)
    public void showScreen(String name) {
        cardLayout.show(container, name);
        System.out.println("Switching to screen " + name);
    }

    public static void main(String[] args) {
        new MainFrame();
    }
}

