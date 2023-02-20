package Test;

import java.awt.event.MouseListener;

import javax.swing.JFrame;

import Main.GamePanel;

public class TestMoving {
	public static GamePanel gamePanel = new GamePanel();
    public static void main(String[] args) throws Exception {
    	
        JFrame window = new JFrame();
        
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);
        window.setTitle("2D RPG");

        window.add(gamePanel);

        window.pack();

        window.setLocationRelativeTo(null);

        window.setVisible(true);

        gamePanel.startGameThread();
    }

    public static void addMouseListener(MouseListener Object) {
        gamePanel.addMouseListener(Object);
    }
}