import java.awt.*;
import javax.swing.*;

public class CreateSeminar extends JPanel{
    private MainFrame frame;
    public CreateSeminar(MainFrame frame) {
        this.frame = frame;
        setLayout(new BorderLayout());
        int sessionList = 5;

        JPanel mainPanel = new JPanel(new GridLayout(3,2,1,1));
        JPanel bottomPanel = new JPanel();
        //nnti tambah data dekat tepi date punya text tu, ejas situ je
        JLabel sessionLabel = new JLabel("Session : ");
        JLabel sessionNumLabel = new JLabel(String.valueOf(sessionList));

        JLabel dateLabel = new JLabel("Date : ");
        JTextField dateInput = new JTextField();

        JLabel venueLabel = new JLabel("Venue : ");
        JTextField venueInput = new JTextField();


        JButton createButton = new JButton("Create");



        mainPanel.add(sessionLabel);
        mainPanel.add(sessionNumLabel);
        mainPanel.add(dateLabel);
        mainPanel.add(dateInput);
        mainPanel.add(venueLabel);
        mainPanel.add(venueInput);
        bottomPanel.add(createButton);

        mainPanel.setBorder(BorderFactory.createEmptyBorder(80, 150, 5, 150));
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(5,150,90,150));

        add(mainPanel,BorderLayout.CENTER);
        add(bottomPanel,BorderLayout.SOUTH);
    }
}
