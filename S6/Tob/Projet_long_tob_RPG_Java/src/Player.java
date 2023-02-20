

public class Player extends Pawn{
	// instance variables
	private final String name;
	private int level;
	private int score;
	private Statistics stat;
	private boolean magic;	//magic mode pour la princesse
	private Competences[] abilities = new Competences[4];
	private String EndGame;
	

	// contructeur
	public Player(String name, int level, int score, Statistics stat, boolean magic, Competences[] abilities, String EndGame) {
		this.name = name;
		this.level = level;
		this.score = score;
		this.stat = stat;
		this.magic = magic;
		this.abilities = abilities;
		this.EndGame = EndGame;
	}
	
	// méthodes
	protected void IncreaseHealth(int healthAdded) {
		if (this.stat.getHealth() + healthAdded > this.stat.getMaxHealth()) {
			this.stat.setHealth(this.stat.getMaxHealth());
		}
		else {
			this.stat.setHealth(this.stat.getHealth() + healthAdded);
		}
	}
	
	
	protected void DecreaseHealth(int healthLost) {
		if (this.stat.getHealth() - healthLost < 0) {
			this.stat.setHealth(0);
		}
		else {
			this.stat.setHealth(this.stat.getHealth() - healthLost);
		}
	}
	
	
	public void die() {
		EndGame = (this.name + "Vous êtes mort...gnegnegnegne");
	}
	
	public Competences getCompetence() {
		return this.abilities[0];
	}
	
	public void openInventory() {
		
	}
	
	//getters, setters
	public String getName() {
		return name;
	}
	
	public int getLevel() {
		return level;
	}
	
	public int getScore() {
		return score;
	}

}

