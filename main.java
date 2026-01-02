import java.awt.*;
import javax.swing.*;

public class main {
    private JFrame frame;
    private JPanel mainPanel;
    private CardLayout cardLayout;

    //constructor
    public main(){
        //defaults
        frame = new JFrame("Postgraduate Academic Research Seminar");
        frame.setSize(600, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        frame.add(mainPanel);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        new main();
    }

}
