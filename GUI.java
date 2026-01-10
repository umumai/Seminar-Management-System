import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class GUI {
    //create a function to put text on the screen
    public static JLabel createText(String text, int x, int y, JPanel panel) {
    JLabel label = new JLabel(text);
    label.setBounds(x, y, 120, 25);
    panel.add(label);
    return label;
    };

    //a function to make the text clikable
    public static JLabel createClikableText(String text, int x, int y, Color normalColor, Color hoverColor, JPanel panel) {
        JLabel label = new JLabel(text);
        label.setBounds(x, y, 120, 25);
        label.setForeground(normalColor);
        // label.setCursor(new Cursor(Cursor.HAND_CURSOR));

        label.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                label.setForeground(hoverColor); // hover color
            }

            @Override
            public void mouseExited(MouseEvent e) {
                label.setForeground(normalColor); // normal color
            }

            // @Override
            // public void mouseClicked(MouseEvent e) {
            //      // switch screen
            // }

        });

        panel.add(label);
        return label;
    };

    public static JTable createTable(Object[][] data, String[] header, JPanel panel) {
        JTable scheduleTable = new JTable(data, header);
        scheduleTable.setEnabled(false); //table is uneditable
        JScrollPane schedulePane = new JScrollPane(scheduleTable);
        schedulePane.setBounds(30, 90, 520, 180);
        panel.add(schedulePane);
        return scheduleTable;
    };
}
