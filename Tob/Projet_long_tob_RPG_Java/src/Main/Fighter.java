
package Main;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import Core.Orientation;
import Core.Pawn;
import Core.RectangleCollisionComponent;
import Core.SpriteAnimationComponent;
import GeometricObjects.Vector2D;

public abstract class Fighter extends Pawn {
	
	protected Statistics stats; 
	protected Competences[] abilities = new Competences[4];
	protected Buff[] buffList = new Buff[20];

	protected SpriteAnimationComponent spriteAnim;
	protected int animLength;
	protected BufferedImage[] upAnim;
	protected BufferedImage[] leftAnim;
	protected BufferedImage[] downAnim;
	protected BufferedImage[] rightAnim;
	protected BufferedImage[] upIdle;
	protected BufferedImage[] leftIdle;
	protected BufferedImage[] downIdle;
	protected BufferedImage[] rightIdle;

	protected RectangleCollisionComponent hitbox; 
	protected String name;
	
	public Fighter(String animSet, int animLength, float animScale, int animDelay, Vector2D hitboxPosition, Vector2D hitboxSize, float hitboxScale) {
		super();
		this.animLength = animLength;
		this.upAnim = new BufferedImage[animLength];
		this.leftAnim = new BufferedImage[animLength];
		this.downAnim = new BufferedImage[animLength];
		this.rightAnim = new BufferedImage[animLength];
		this.upIdle = new BufferedImage[1];
		this.leftIdle = new BufferedImage[1];
		this.downIdle = new BufferedImage[1];
		this.rightIdle = new BufferedImage[1];

		setAnimation(animSet);
		this.spriteAnim = new SpriteAnimationComponent(this);
		this.spriteAnim.setScale(animScale);
		this.spriteAnim.setAnimationRate(animDelay);
		this.spriteAnim.setAnimation(downIdle);
		this.components.add(spriteAnim);

		this.hitbox = new RectangleCollisionComponent(this, true);
		this.hitbox.setPosition((int) hitboxPosition.x, (int) hitboxPosition.y);
		this.hitbox.setSize((int) hitboxSize.x, (int) hitboxSize.y);
		this.hitbox.setScale(hitboxScale);
		this.components.add(hitbox);
	}

	/**
	 * Charge les bonnes animations.
	 * @param set le set d'animation à charger
	 */
	private void setAnimation(String set) {
		try {
			for(int i = 1; i <= this.animLength; i++) {
				upAnim[i - 1] = ImageIO.read(getClass().getResourceAsStream("/Assets/Sprites/" + set + "/up" + i + ".png"));
			}
			
			for(int i = 1; i <= 9; i++) {
				leftAnim[i - 1] = ImageIO.read(getClass().getResourceAsStream("/Assets/Sprites/" + set + "/left" + i + ".png"));
			}

			for(int i = 1; i <= 9; i++) {
				downAnim[i - 1] = ImageIO.read(getClass().getResourceAsStream("/Assets/Sprites/" + set + "/down" + i + ".png"));
			}

			for(int i = 1; i <= 9; i++) {
				rightAnim[i - 1] = ImageIO.read(getClass().getResourceAsStream("/Assets/Sprites/" + set + "/right" + i + ".png"));
			}

			upIdle[0] = ImageIO.read(getClass().getResourceAsStream("/Assets/Sprites/" + set + "/idle_up.png"));
			leftIdle[0] = ImageIO.read(getClass().getResourceAsStream("/Assets/Sprites/" + set + "/idle_left.png"));
			downIdle[0] = ImageIO.read(getClass().getResourceAsStream("/Assets/Sprites/" + set + "/idle_down.png"));
			rightIdle[0] = ImageIO.read(getClass().getResourceAsStream("/Assets/Sprites/" + set + "/idle_right.png"));

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public BufferedImage[] getFightAnim() {
		return this.leftIdle;
	}

	public Statistics getStats() {
		return this.stats;
	};
	
	public Competences[] getCompetences() {
		return this.abilities;
	}; 
	
	public String getName() {
		return this.name;
	};
	
	public void setCompetences(Competences[] abilities) {
		this.abilities = abilities;
	}
	
	abstract int getAction(BattlePanel battlePanel);
	
	abstract Fighter getTarget(BattlePanel battlePanel);

	public abstract void onDeath();
}
