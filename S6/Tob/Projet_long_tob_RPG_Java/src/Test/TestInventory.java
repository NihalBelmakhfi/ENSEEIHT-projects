package Test;

import java.awt.event.MouseListener;

import javax.swing.JFrame;

import Main.GamePanel;
import Main.Inventory;
import Main.InventoryPanel;
import Main.ItemInventory;
import Main.PotionDeSoin;

public class TestInventory {
	public static GamePanel gamePanel = new GamePanel();
    public static void main(String[] args) throws Exception {
    	
        JFrame window = new JFrame();
        
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);
        window.setTitle("2D RPG");
        Inventory inventory = new Inventory();
        ItemInventory potionInventory = new ItemInventory(new PotionDeSoin(), 1);
        inventory.ajouterItem(potionInventory);
        InventoryPanel inventoryPanel = new InventoryPanel(inventory);
        
        inventoryPanel.setBounds(920, 150, 350, 500);
        window.add(inventoryPanel);
        
        window.add(gamePanel);

        window.pack();

        window.setLocationRelativeTo(null);

        window.setVisible(true);
    }

    public static void addMouseListener(MouseListener Object) {
        gamePanel.addMouseListener(Object);
    }
}
