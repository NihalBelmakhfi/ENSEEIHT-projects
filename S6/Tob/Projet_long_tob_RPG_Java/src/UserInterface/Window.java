package UserInterface;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

public class Window extends JPanel{
    
    public Window(int x, int y, int width, int height, int padding, Color backgroundColor) {
        this.setBounds(x, y, width, height);
        this.setBackground(backgroundColor);
        this.setLayout(new BorderLayout());
        this.setBorder(BorderFactory.createCompoundBorder(new EmptyBorder(padding, padding, padding, padding),  new LineBorder(Color.BLACK, 0)));
    }

}
