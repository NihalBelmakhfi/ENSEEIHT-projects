package UserInterface;

import java.awt.Color;
import java.awt.event.*;
import java.awt.GridLayout;
import java.awt.Image;

import javax.swing.ImageIcon;

import Main.Inventory;

public class InventoryWindow extends Window implements ActionListener {
    private Inventory inventory;
    private int rows;
    private int cols;

    /** Returns an ImageIcon, or null if the path was invalid. */
    protected ImageIcon createImageIcon(String path, String description) {
        java.net.URL imgURL = getClass().getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL, description);
        } else {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    }

    public InventoryWindow(int x, int y, int iconSize, int cols, int rows, int padding, Color backgroundColor, Inventory inventory) {
        super(x, y, cols*iconSize, rows*iconSize, padding, backgroundColor);
        this.setLayout(new GridLayout(rows, cols));

        for(int i = 0; i < rows; i++) {
            for(int j = 0; j < cols; j++) {
                ImageIcon icon = createImageIcon("/Assets/Items/emptySlot.png", "");
                Image img = icon.getImage();
                img = img.getScaledInstance(iconSize, iconSize, Image.SCALE_DEFAULT);
                icon = new ImageIcon(img);
                ItemButton button = new ItemButton(icon, iconSize);
                button.setActionCommand(Integer.toString(j+i*cols));
                button.addActionListener(this);
                this.add(button);
            }
        }

        this.inventory = inventory;
        this.rows = rows;
        this.cols = cols;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        System.out.println(e.getActionCommand());
    }
    
}
