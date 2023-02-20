package Main;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.swing.JPanel;
import javax.swing.border.Border;

import Core.GameSettings;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;

public class BattlePanel extends JPanel implements ActionListener{
	
	private int action = 0; 
	private Fighter player;
	private Fighter ennemy;
	private BufferedImage img;
	private BufferedImage[] playerAnim;
	private BufferedImage[] ennemyAnim;

	JButton inventaire = new JButton("Inventaire");
	JButton fuite = new JButton("Fuite");
	JButton endTurn = new JButton("Passer son tour");
	JButton competence_1 = new JButton();
	JButton competence_2 = new JButton();
	JButton competence_3 = new JButton();
	JButton competence_4 = new JButton();


	public BattlePanel(Fighter player, Fighter ennemy) {
		
		try {
			img = ImageIO.read(getClass().getResourceAsStream("/Assets/forestbattle.jpg"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		this.player = player;
		this.ennemy = ennemy;
		
		this.playerAnim = player.getFightAnim();
		this.ennemyAnim = ennemy.getFightAnim();

		this.setDoubleBuffered(true);
		this.setFocusable(true);
		this.setLayout(null);

		fuite.setFont(new Font("sérif", Font.BOLD, 30));
		fuite.setBounds(10, 580, 280, 130);
		fuite.addActionListener(this);
		fuite.setFocusPainted(false);
		this.add(fuite);
		
		endTurn.setFont(new Font("sérif", Font.BOLD, 25));
		endTurn.setBounds(10, 440, 280, 130);
		endTurn.addActionListener(this);
		endTurn.setFocusPainted(false);
		this.add(endTurn);

		inventaire.setFont(new Font("sérif", Font.BOLD, 30));
		inventaire.setBounds(1020, 580, 250, 130);
		inventaire.addActionListener(this);
		inventaire.setFocusPainted(false);
		this.add(inventaire);

		competence_1.setText(player.getCompetences()[0].getName());
		competence_1.setFont(new Font("sérif", Font.BOLD, 20));
		competence_1.setBounds(340, 580, 150, 130);
		competence_1.addActionListener(this);
		competence_1.setFocusPainted(false);
		this.add(competence_1);	

		competence_2.setText(player.getCompetences()[1].getName());
		competence_2.setFont(new Font("sérif", Font.BOLD, 20));
		competence_2.setBounds(500, 580, 150, 130);
		competence_2.addActionListener(this);
		competence_2.setFocusPainted(false);
		this.add(competence_2);

		competence_3.setText(player.getCompetences()[2].getName());
		competence_3.setFont(new Font("sérif", Font.BOLD, 20));
		competence_3.setBounds(660, 580, 150, 130);
		competence_3.addActionListener(this);
		competence_3.setFocusPainted(false);
		this.add(competence_3);

		competence_4.setText(player.getCompetences()[3].getName());
		competence_4.setFont(new Font("sérif", Font.BOLD, 20));
		competence_4.setBounds(820, 580, 150, 130);
		competence_4.addActionListener(this);
		competence_4.setFocusPainted(false);
		this.add(competence_4);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == competence_1) {
			action = 1;
		}
		else if (e.getSource() == competence_2) {
			action = 2;
		}
		else if (e.getSource() == competence_3) {
			action = 3;
		}
		else if (e.getSource() == competence_4) {
			action = 4;
		}
		else if (e.getSource() == fuite) {
			action = 6;
		}
		else if (e.getSource() == endTurn) {
			action = 7;
		}
	}

	public void resetAction() {
		this.action = 0; 
	}

	public int getAction() {
		return action; 
	}

	public Fighter getTarget() {
		return ennemy;
	}

	public float healthRatio(Fighter fighter) {
		Statistics statsFighter = fighter.getStats();
		return (float)((float)statsFighter.getHealth()/(float)statsFighter.getMaxHealth());
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		Graphics2D g2 = (Graphics2D) g;
		g2.drawImage(img, 0, 0, GameSettings.SCREEN_WIDTH, GameSettings.SCREEN_HEIGHT, null);
		g2.drawImage(this.playerAnim[0], 150, 100, 484, 484, null);
		g2.drawImage(this.ennemyAnim[0], 750, -100, 484, 484, null);

		Font font = new Font("sérif", Font.BOLD, 18);
		g2.setFont(font);

		g2.setColor(new Color(154, 154, 154));
		g2.fillRoundRect(255, 20, 400, 85, 20, 20);		
		g2.fillRoundRect(625, 435, 400, 85, 20, 20);
		
		g2.setColor(new Color(255, 255, 255, 200));
		
		int[] x = {5, 5, 1275, 1275, 295, 295, 5};
		int[] y = {440, 715, 715, 575, 575, 435, 435};
		
		g2.drawPolygon(x, y, x.length);
		g2.fillPolygon(x, y, x.length);

		g2.setColor(Color.black);

		// Nom du joueur
		g2.drawString(ennemy.getName(), 280, 50);

		// Nom de l'ennemi 
		g2.drawString(player.getName(), 650 , 465);

		// Partie rouge des barres de vie des combattants indiquant leur vie restante
		g2.setColor(Color.red);
		g2.fillRect(280, 65, 350, 20);
		g2.fillRect(650, 480, 350, 20);

		// Barres de vie des combattants 
		g2.setColor(Color.green);
		g2.fillRect(280, 65, (int)(healthRatio(ennemy)*350), 20);
		g2.fillRect(650, 480, (int)(healthRatio(player)*350), 20);

		// Point de vie des combattants 
		g2.setColor(Color.black);
		g2.drawString(ennemy.getStats().getHealth() + "/" + ennemy.getStats().getMaxHealth(), 425, 82);
		g2.drawString(player.getStats().getHealth() + "/" + player.getStats().getMaxHealth(), 785, 496);

		
		if (player.getCompetences()[0].getCd() > 0) {
			competence_1.setContentAreaFilled(false);
			g2.setFont(new Font("sérif", Font.BOLD, 120));
			g2.setColor(new Color(0, 0, 0, 100));
			g2.drawString("" + player.getCompetences()[0].getCd(), 375, 690);
		}
		else {
			competence_1.setBackground(null);
			competence_1.setContentAreaFilled(true);
			competence_1.setText(player.getCompetences()[0].getName());
		}

		if (player.getCompetences()[1].getCd() > 0) {
			competence_2.setContentAreaFilled(false);
			g2.setFont(new Font("sérif", Font.BOLD, 120));
			g2.setColor(new Color(0, 0, 0, 100));
			g2.drawString("" + player.getCompetences()[1].getCd(), 530, 690);
		}
		else {
			competence_2.setBackground(null);
			competence_2.setContentAreaFilled(true);
			competence_2.setText(player.getCompetences()[1].getName());
		}

		if (player.getCompetences()[2].getCd() > 0) {
			competence_3.setContentAreaFilled(false);
			g2.setFont(new Font("sérif", Font.BOLD, 120));
			g2.setColor(new Color(0, 0, 0, 100));
			g2.drawString("" + player.getCompetences()[2].getCd(), 695, 690);
		}
		else {
			competence_3.setBackground(null);
			competence_3.setContentAreaFilled(true);
			competence_3.setText(player.getCompetences()[2].getName());
		}

		if (player.getCompetences()[3].getCd() > 0) {
			competence_4.setContentAreaFilled(false);
			g2.setFont(new Font("sérif", Font.BOLD, 120));
			g2.setColor(new Color(0, 0, 0, 100));
			g2.drawString("" + player.getCompetences()[3].getCd(), 850, 690);
		}
		else {
			competence_4.setBackground(null);
			competence_4.setContentAreaFilled(true);
			competence_4.setText(player.getCompetences()[3].getName());
		}
	}
	
	public Fighter getPlayer() {
		return this.player;
	}
	
	public Fighter getEnnemy() {
		return this.ennemy;
	}
}
