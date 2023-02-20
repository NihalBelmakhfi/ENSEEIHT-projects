package Main;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JPanel;

import Core.App;

public class GamePanel extends JPanel {

    public GamePanel() {
        this.setBackground(Color.BLACK);
        this.setFocusable(true);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;

        if (App.getCurrentMap() != null) {
            App.getCurrentMap().draw(g2);
        }

    }
}