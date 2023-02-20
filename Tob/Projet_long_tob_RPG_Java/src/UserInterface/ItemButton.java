package UserInterface;

import java.awt.Insets;

import javax.swing.Icon;
import javax.swing.JButton;

public class ItemButton extends JButton {
    
    public ItemButton(Icon icon, int iconSize) {
        super(icon);
        this.setMargin(new Insets(0,0,0,0));
        this.setContentAreaFilled(false);
        this.setSize(iconSize, iconSize);
        this.setFocusable(false);
    }
    
}