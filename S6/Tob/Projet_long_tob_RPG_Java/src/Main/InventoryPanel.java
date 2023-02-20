package Main;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JPanel;

public class InventoryPanel extends JPanel{

	private Inventory inventory;

	public InventoryPanel(Inventory inventaire) {
		this.inventory = inventaire;
		this.setDoubleBuffered(true);
		this.setFocusable(true);
		this.setLayout(null);
		this.setBackground(new Color(0, 0, 0, 0));
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		Graphics2D g2 = (Graphics2D) g;
		
		g2.setColor(new Color(110, 44, 0));
		g2.fillRect(0, 0, 700, 500);
		
		g2.setColor(Color.black);
		g2.drawString(inventory.getInventaire()[0].getName(), 100, 50);
	}
	
}
