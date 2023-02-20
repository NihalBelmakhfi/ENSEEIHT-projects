package UserInterface;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import Core.App;
import Core.GameSettings;
import Main.GameState;
import Main.Main;

public class MainMenuWindow extends JPanel {

    public int command;
    private BufferedImage img;

    public MainMenuWindow() {
        this.setFocusable(true);
        
        try {
			img = ImageIO.read(getClass().getResourceAsStream("/Assets/mainmenu.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
    }

    public void executeCommand() {
    	App.audioPlayer.playSound("cursor.wav");
        switch(this.command) {
        case 0:
            Main.getMainWindow().setGamePanel();
            Main.getMainWindow().setGameState(GameState.WORLD);
            break;
        case 1:
            //add later
            break;
        case 2:
            System.exit(0);
            break;
        }
    }
    
    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        
        g2.drawImage(img, 0, 0, GameSettings.SCREEN_WIDTH, GameSettings.SCREEN_HEIGHT, null);

        //Nom du jeu
        g2.setFont(new Font("Serif", Font.BOLD, 96)); 
        String gameName = "Nihal'dventure";
        int x = getCenterdTextX(gameName, g2);
        int y = 150;
        g2.setColor(Color.GRAY);
        g2.drawString(gameName, x+4, y+4);
        g2.setColor(Color.WHITE);
        g2.drawString(gameName, x, y);

        //Menu
        g2.setFont(new Font("Serif", Font.BOLD, 48));
        String newGame = "Nouvelle Partie";
        x = getCenterdTextX(newGame, g2);
        y = 300;
        g2.drawString(newGame, x, y);
        if (this.command == 0) g2.drawString(">", x - 50, y);

        String loadGame = "Charger Partie";
        x = getCenterdTextX(loadGame, g2);
        y = 400;
        g2.drawString(loadGame, x, y);
        if (this.command == 1) g2.drawString(">", x - 50, y);

        String quitGame = "Quitter";
        x = getCenterdTextX(quitGame, g2);
        y = 500;
        g2.drawString(quitGame, x, y);
        if (this.command == 2) g2.drawString(">", x - 50, y);
        
    }

    private int getCenterdTextX(String text, Graphics2D g) {
        int length = (int) g.getFontMetrics().getStringBounds(text, g).getWidth();
        return GameSettings.SCREEN_WIDTH/2 - length/2;
    }
}
