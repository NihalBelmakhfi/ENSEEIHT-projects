package Test;

import java.awt.event.MouseListener;

import javax.swing.JFrame;

import Main.BattleMode;
import Main.Competences;
import Main.Ennemy;
import Main.FireBall;
import Main.GamePanel;
import Main.Player;
import Main.Statistics;

public class TestBattle {
	public static GamePanel gamePanel = new GamePanel();
    public static void main(String[] args) throws Exception {
        
    	Competences[] abilities = {new FireBall(), new FireBall(), new FireBall(), new FireBall()};
    	Statistics testStats = new Statistics(100,100,100,100,100,100);
    	
    	Ennemy thomas = new Ennemy("Thomas l'obéidient créateur de ...", 10, testStats, abilities);
    	
    	Player théo = new Player("Théo, le mage noir corrompu", 10, testStats, abilities);
    	
        JFrame window = new JFrame();
       
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);
        window.setTitle("2D RPG");
        
        BattleMode battle = new BattleMode(théo, thomas);
        
        battle.getBattlePanel().setBounds(0, 0, 1280, 720);
        
        window.add(battle.getBattlePanel());
        
        window.add(gamePanel);

        window.pack();

        window.setLocationRelativeTo(null);

        window.setVisible(true);

        battle.Battle();
    }

    public static void addMouseListener(MouseListener Object) {
        gamePanel.addMouseListener(Object);
    }
}
