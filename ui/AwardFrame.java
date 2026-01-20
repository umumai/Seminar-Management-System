package ui;
import java.awt.*;
import javax.swing.*;

//dalam coordinator frame ni kita just place the JTabbedPane. 
// and then we place the other frame (from other file) into each tabs
public class AwardFrame extends JPanel {
    private JFrame frame;
    public AwardFrame(JFrame frame) {
        this.frame = frame;
        setLayout(new BorderLayout());
    }
    
}
