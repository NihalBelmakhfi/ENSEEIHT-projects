package Main;

import Core.App;
import Core.Orientation;
import Core.PlayerController;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.KeyEvent;

import javax.swing.JLayeredPane;

/**
 * Implémentation de PlayerController.
 */
public class MyPlayerController extends PlayerController{
    private boolean upPressed, leftPressed, downPressed, rightPressed;
    private int playerSpeed = 6;

    private boolean hidden_inventory = true;
    private Inventory inventory = new Inventory();
    private InventoryPanel inventoryPanel = new InventoryPanel(inventory);
    private UserInterface.InventoryWindow test = new UserInterface.InventoryWindow(900, 100, 50, 4, 6, 15, Color.BLACK, inventory);

    public MyPlayerController() {
        super();  
        inventoryPanel.setBounds(920, 150, 350, 500);
    }

    @Override
    public void tick(float deltaTime) {
        super.tick(deltaTime);

        if(upPressed) direction = Orientation.NORTH;
        if(leftPressed) direction = Orientation.WEST;
        if(downPressed) direction = Orientation.SOUTH;
        if(rightPressed) direction = Orientation.EAST;
        
        boolean idle = !upPressed && !leftPressed && !downPressed && !rightPressed;

        this.pawn.addMovementInput(this.direction, idle ? 0 : this.playerSpeed*deltaTime);
        this.pawn.setActorOrientation(this.direction);
    }

	private void openInventory() {
		Main.getMainWindow().getLayeredPane().add(test, 200, 0);
	}
    
	private void closeInventory() {
		Main.getMainWindow().getLayeredPane().remove(test);
	}

    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();

        if (Main.getMainWindow().getGameState() == GameState.MAIN_MENU) {
            switch(code) {
            case KeyEvent.VK_Z:
                Main.getMainWindow().getMainMenu().command--;
                if (Main.getMainWindow().getMainMenu().command < 0) Main.getMainWindow().getMainMenu().command = 2;
                break;
            case KeyEvent.VK_S:
                Main.getMainWindow().getMainMenu().command++;
                if (Main.getMainWindow().getMainMenu().command > 2) Main.getMainWindow().getMainMenu().command = 0;
                break;
            case KeyEvent.VK_ENTER:
                Main.getMainWindow().getMainMenu().executeCommand();
                break;
            }
        }
        else if (Main.getMainWindow().getGameState() == GameState.WORLD && App.getCurrentMap() != null) {
            
            switch(code) {
                case KeyEvent.VK_Z:
                    this.upPressed = true;
                    break;
                case KeyEvent.VK_Q:
                    this.leftPressed = true;
                    break;
                case KeyEvent.VK_S:
                    this.downPressed = true;
                    break;
                case KeyEvent.VK_D:
                    this.rightPressed = true;
                    break;
            }
        }

        Component[] components = Main.getMainWindow().getLayeredPane().getComponentsInLayer(JLayeredPane.POPUP_LAYER);
        for (int i = 0; i < components.length; i++) {
            Main.getMainWindow().getLayeredPane().remove(components[i]);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int code = e.getKeyCode();

        if (Main.getMainWindow().getGameState() == GameState.WORLD) {
            switch(code) {
                case KeyEvent.VK_I:
                    hidden_inventory = !hidden_inventory;
                    if (hidden_inventory) {
                        this.closeInventory();                    
                    }
                    else {
                        this.openInventory();
                    }
                    break;
                case KeyEvent.VK_Z:
                    this.upPressed = false;
                    break;
                case KeyEvent.VK_Q:
                    this.leftPressed = false;
                    break;
                case KeyEvent.VK_S:
                    this.downPressed = false;
                    break;
                case KeyEvent.VK_D:
                    this.rightPressed = false;
                    break;
            }
        }
    }

}
