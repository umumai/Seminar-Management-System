package ui;
import java.awt.*;
import javax.swing.*;

public class CLNavigation extends JFrame {

    CardLayout cardLayout;
    JPanel container;
    // private List<Student> students = new ArrayList<>();

    public CLNavigation() {
        setTitle("Seminar Management System");
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        cardLayout = new CardLayout();
        container = new JPanel(cardLayout);

        System.out.println("CLNavigation initialised.");
        

        // // Create screens
        EditSchedule editScheduleFrame = new EditSchedule(this);
        ScheduleFrame viewScheduleFrame = new ScheduleFrame(this);
        CoordinatorFrame coordinatorFrame = new CoordinatorFrame(this, null);
        AppointmentFrame appointmentFrame = new AppointmentFrame(this);
        ReportFrame reportFrame = new ReportFrame(this);
        // LoginFrame loginFrame = new LoginFrame(this);
        // StudentRegisterPanel studentRegisterPanel = new StudentRegisterPanel(this);

        // // store screen into a single JPanel holding all the CardLayout (container)
        container.add(editScheduleFrame, "editScheduleFrame");
        container.add(viewScheduleFrame, "viewScheduleFrame");
        container.add(coordinatorFrame,"coordinatorFrame");
        container.add(appointmentFrame,"appointmentFrame");
        container.add(reportFrame,"reportFrame");
        // container.add(loginFrame,"loginFrame");
        // container.add(studentRegisterPanel,"studentRegisterPanel");

        // // add the container which contains all the CardLayout
        add(container);
        // //show which screen u want to
        cardLayout.show(container, "loginFrame"); //show the first screen to be displayed

        //setVisible(true);
    }

    // Navigation method 
    public void showScreen(String name) {
        cardLayout.show(container, name);
        System.out.println("Switching to screen " + name);
    }
}

