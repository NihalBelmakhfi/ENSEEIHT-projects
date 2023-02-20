package Main;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import Core.*;
import GeometricObjects.*;


public abstract class Npc extends Actor{
	private BufferedImage[] sprite; //L'image du NPC (Non-Player-Character)
	protected SpriteAnimationComponent spriteAnim;
	protected RectangleCollisionComponent hitbox;
	
	public Npc(String animSet, float animScale, Vector2D hitboxPosition, Vector2D hitboxSize, float hitboxScale) {
		super();
		this.sprite = new BufferedImage[1];
		try {
			sprite[0] = ImageIO.read(getClass().getResourceAsStream("/Assets/Sprites/" + animSet + "/idle_down.png"));
			this.spriteAnim = new SpriteAnimationComponent(this);
			this.spriteAnim.setScale(animScale);
			this.spriteAnim.setAnimation(sprite);
			this.components.add(spriteAnim);
		} catch (IOException e) {
			e.printStackTrace();
		}

		this.hitbox = new RectangleCollisionComponent(this, true);
		this.hitbox.setPosition((int) hitboxPosition.x, (int) hitboxPosition.y);
		this.hitbox.setSize((int) hitboxSize.x, (int) hitboxSize.y);
		this.hitbox.setScale(hitboxScale);
		this.components.add(hitbox);
	}
}
