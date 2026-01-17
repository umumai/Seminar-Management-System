

import java.awt.*;
import javax.swing.*;

//dalam coordinator frame ni kita just place the JTabbedPane. 
// and then we place the other frame (from other file) into each tabs
public class StudentReport extends JPanel {
    private MainFrame frame;
    public StudentReport(MainFrame frame) {
        this.frame = frame;
        setLayout(new BorderLayout());
    }
    
}