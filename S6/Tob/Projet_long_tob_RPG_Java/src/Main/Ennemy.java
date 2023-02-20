package Main;

import Core.App;
import Core.RectangleCollisionComponent;
import GeometricObjects.Vector2D;

public class Ennemy extends Fighter{
	
	private int level;
	private Statistics statsBase; 
	private int xp;
	private int gold; 
	private int ID_ennemy;
	private EnnemyAI AI;
	protected RectangleCollisionComponent aggroBox; 
	
	public Ennemy(String name, int level, Statistics statsBase) {
		super("werewolf", 9, 0.5f, 5, new Vector2D(12f, 32f), new Vector2D(40f, 32f), 0.5f);
		this.name = name;
		this.level = level;
		this.stats = statsBase;
		this.statsBase = statsBase;
		
		this.aggroBox = new RectangleCollisionComponent(this, true);
		this.aggroBox.setPosition(-150, -150);
		this.aggroBox.setSize(300, 300);
		this.aggroBox.setScale(0.5f);
		this.aggroBox.canCollide = false;
		this.components.add(aggroBox);
	}

	@Override
	public void onDeath() {
		App.getCurrentMap().removeActor(this);
	}

	@Override
	int getAction(BattlePanel battlePanel) {
		return AI.getAction();
	}
	
	public void setAI(EnnemyAI AI) {
		this.AI = AI;
	}

	@Override
	Fighter getTarget(BattlePanel battlePanel) {
		return battlePanel.getPlayer();
	}
	
	@Override
	public void tick(float deltaTime) {
		super.tick(deltaTime);
		
		// Problème car le composant de collision s'arrète au premier acteur croisé qui n'est pas forcément le joueur
		/*if (this.aggroBox.isColliding() && this.aggroBox.getCollidingActor() instanceof Player) {
			Player player = this.aggroBox.getCollidingActor();
		}*/
		
		float distx = this.position.x - App.getPlayerController(0).getActorLocation().x;
		float disty = this.position.y - App.getPlayerController(0).getActorLocation().y;
		double dist = Math.sqrt(Math.pow(disty, 2) + Math.pow(distx, 2));
		
		if (dist < 150) {
			this.position.sub(new Vector2D(distx*0.07f, disty*0.07f));
		}
	}
}
