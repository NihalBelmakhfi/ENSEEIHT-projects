package Core;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

import GeometricObjects.Vector2D;

/**
 * Un acteur est tout objet qui peut être créé dans un monde.
 * Il possède des attributs sur sa position et sa rotation. ainsi que des méthodes permettant de le mettre à jour.
 */
public abstract class Actor {
    public boolean canTick = true;
    public boolean canDraw = true;

    protected Vector2D position = new Vector2D();
    protected Vector2D lastPosition = new Vector2D();
    protected Orientation direction = Orientation.SOUTH;
    protected float scale = 1;
    protected List<ActorComponent> components = new ArrayList<ActorComponent>();
    protected boolean isMoving = false;

    /**
     * Change la position de l'acteur.
     * @param position la nouvelle position de l'acteur.
     */
    public void setActorLocation(Vector2D position) {
        this.position = position.copy();
    }

    /**
     * Récupère la position de l'acteur.
     * @return la position de l'acteur
     */
    public Vector2D getActorLocation() {
        return this.position.copy();
    }

    /**
     * Change la rotation de l'acteur.
     * @param direction la nouvelle rotation de l'acteur.
     */
    public void setActorOrientation(Orientation direction) {
        this.direction = direction;
    }

    /**
     * Récupère la rotation de l'acteur.
     * @return  la rotation de l'acteur.
     */
    public Orientation getActorOrientation() {
        return this.direction;
    }

    /**
     * Récupère le facteur de mise à l'échelle de l'acteur.
     * @return le facteur de mise à l'échelle de l'acteur.
     */
    public float getScale() {
        return this.scale;
    }

    /**
     * Récupère l'ensemble des composants que l'acteur possède.
     * @return l'ensemble des composants que l'acteur possède.
     */
    public List<ActorComponent> getComponents() {
        return this.components;
    }

    /**
     * Permet de dessiner à l'écran et appel la méthode draw des composants afin de les dessiner si nécessaire.
     * Appeler après un tick.
     * @param g "outil de dessin"
     */
    public void draw(Graphics2D g) {
        for(ActorComponent c: this.components) {
            if(c.canDraw) c.draw(g);
        }
    }

    /**
     * Permet de mettre à jour l'acteur et les composants si nécessaire.
     * @param deltaTime le temps entre 2 images successives.
     */
    public void tick(float deltaTime) {
        for(ActorComponent c: this.components) {
            if(c.canTick) c.tick(deltaTime);
        }
    }

    /** Permet de recevoir un événement et de faire une action en conséquence. */
    public void receiveEvent(Object o) {
        if (o instanceof RectangleCollisionComponent) {
            RectangleCollisionComponent o2 = (RectangleCollisionComponent) o;
            if (o2.isColliding()) {
                this.position = this.lastPosition;
            }
        }
    }

    @Override
    public String toString() {
        return "Actor";
    }
}
