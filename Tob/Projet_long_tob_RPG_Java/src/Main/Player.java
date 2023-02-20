package Main;

import java.awt.image.BufferedImage;

import Core.Orientation;
import GeometricObjects.Vector2D;

public class Player extends Fighter{
	
	// Niveau du joueur
	private int level;
	
	// Score du joueur (je voudrais le supprimer)
	private int score;
	Inventory inventory = new Inventory();
    //ItemInventory potionInventory = new ItemInventory(new PotionDeSoin(), 1);
	private Statistics statsBase;
	
	//(je voudrais le supprimer, pour utiliser de la magie il suffit de faire un 
	// player avec des compétences "magiques")
	private boolean magic;	//magic mode pour la princesse
	
	//(je supprimerais - à quoi sert cette variable ?)
	private String EndGame;
	// contructeur
	public Player(String name, int level, Statistics statsBase, String animSet, int animLength) {
		super(animSet, animLength, 0.5f, 5, new Vector2D(12f, 32f), new Vector2D(40f, 32f), 0.5f);
		this.name = name;
		this.level = level;
		this.statsBase = statsBase;
		this.stats = statsBase;
	}

	public Player() {
		super("woman1", 9, 0.5f, 5, new Vector2D(12f, 32f), new Vector2D(40f, 32f), 0.5f);
		this.name = "Théo le zozo - destruceur du néant";
		this.level = 1;
		this.score = 0;
		this.stats = new Statistics(100, 100, 100, 100, 100, 100);
		this.magic = false;
		this.EndGame = "Fin";
	}
	
	// méthodes
	@Override
	public void setActorOrientation(Orientation direction) {
		super.setActorOrientation(direction);

		switch(direction) {
            case NORTH:
                spriteAnim.setAnimation(this.isMoving ? upAnim : upIdle);
				break;
            case WEST:
				spriteAnim.setAnimation(this.isMoving ? leftAnim : leftIdle);
				break;
            case SOUTH:
				spriteAnim.setAnimation(this.isMoving ? downAnim : downIdle);
				break;
            case EAST:
				spriteAnim.setAnimation(this.isMoving ? rightAnim : rightIdle);
				break;
        }
	}
	
	@Override
	public void onDeath() {
		EndGame = (this.name + "Vous êtes mort...gnegnegnegne");
	}
	
	@Override
	public BufferedImage[] getFightAnim() {
		return this.rightIdle;
	}
	
	public Competences[] getCompetences() {
		return this.abilities;
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
	
	public Statistics getStats() {
		return this.stats;
	}

	@Override
    public String toString() {
        return "Player";
    }

	@Override
	int getAction(BattlePanel battlePanel) {
		return battlePanel.getAction();
	}

	@Override
	Fighter getTarget(BattlePanel battlePanel) {
		return battlePanel.getTarget();
	}
	
	@Override
	public void receiveEvent(Object o) {
		super.receiveEvent(o);
		if (o == this.hitbox) {
			if (this.hitbox.getCollidingActor() != null) {
				if (this.hitbox.getCollidingActor() instanceof Ennemy) {
					Main.getMainWindow().startBattle((Ennemy) this.hitbox.getCollidingActor());
					Main.getMainWindow().setGameState(GameState.BATTLE);
				}
			}
		}
	}
}

