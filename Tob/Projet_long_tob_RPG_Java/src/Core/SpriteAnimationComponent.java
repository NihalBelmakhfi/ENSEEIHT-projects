package Core;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import GeometricObjects.Vector2D;

/**
 * Composant permettant d'afficher un sprite à l'écran et de l'animer.
 * La position de l'image dépend de la position du joueur sur la carte sachant qu'il est toujours au centre de l'écran
 */
public class SpriteAnimationComponent extends ActorComponent{
    
    /** Suite de sprite définissant l'animation à effectuer. */
    private BufferedImage[] animation;
    /** Indice du sprite à afficher.*/
    private int spriteIndex;
    /** Nombres de sprites pour l'animation. */
    private int spriteNum;
    /** Nombres d'image effectuée. */
    private int spriteCounter;
    /** Nombres d'image à attendre avant de passer au sprite suivant. */
    private int spriteDelay = GameSettings.TARGET_FPS / 4;
    /** La taille des sprites. */
    private Vector2D spriteSize;

    /**
     * Initialise une animation de sprite en l'associant à un parent.
     * @param parent le parent auquel le composant est associé.
     */
    public SpriteAnimationComponent(Actor parent) {
        super(parent);
        this.canDraw = true;
        this.canTick = true;
    }

    /**
     * Change l'animation du composant.
     * @param animation la nouvelle animation.
     */
    public void setAnimation(BufferedImage[] animation) {
        if (this.animation == animation) return;
        this.animation = animation;
        this.spriteIndex = 0;
        this.spriteNum = animation.length;
        this.spriteSize = new Vector2D(animation[0].getWidth(), animation[0].getHeight());
    }

    /**
     * Permet de changer la durée de chaque entre chaque sprites.
     * @param delay le nombre d'image avant de passer au sprite suivant.
     */
    public void setAnimationRate(int delay) {
        this.spriteDelay = delay;
    }
    
    @Override
    public void tick(float deltaTime) {
        this.spriteCounter ++;

        if(this.spriteCounter >= this.spriteDelay) {
            this.spriteCounter = 0;
            this.spriteIndex ++;
            if(this.spriteIndex >= this.spriteNum) this.spriteIndex = 0;
        }
    }

    @Override
    public void draw(Graphics2D g) {
        Vector2D screenPosition = getScreenPosition();

        Vector2D screenSize = this.spriteSize.copy();
        screenSize.times(this.scale*this.parent.scale*GameSettings.SCALE);

        g.drawImage(this.animation[this.spriteIndex], (int) screenPosition.x, (int) screenPosition.y, (int) screenSize.x, (int) screenSize.y, null);
    }

    @Override
    public String toString() {
        return "SpriteAnimationComponent";
    }
}
