package Core;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

import GeometricObjects.Point;
import GeometricObjects.MyPolygon;
import GeometricObjects.Vector2D;

/**
 * Composant permettant de s'occuper des collisions avec la carte ou d'autres composants.
 * Réagit aussi au clique de souris.
 */
public class RectangleCollisionComponent extends ActorComponent implements MouseListener {

    /** Taille du rectangle de collision. */
    private Vector2D size;
    /** Le composant est il en collision. */
    private boolean isColliding;
    /** Le composant est il sélectionné. */
    private boolean isSelected;
    /** L'acteur qui est en collision avec le composant si valable. */
    private Actor collindingActor;
    /** Le composant peut bloquer les acteurs */
    public boolean canCollide;

    /**
     * Initialise une collision en forme de rectangle en l'associant à un parent.
     * @param parent le parent auquel le composant est associé.
     * @param generateMouseEvent si vrai la collision va s'enregister dans le mouseListener du GamePanel.
     */
    public RectangleCollisionComponent(Actor parent, boolean generateMouseEvent) {
        super(parent);
        this.canTick = true;
        this.canCollide = true;
        if (generateMouseEvent) {
            App.currentPanel.addMouseListener(this);
        }
    }

    /**
     * Change la taille du rectangle de collision.
     * @param width la largeur du rectangle.
     * @param height la longueur du rectangle.
     */
    public void setSize(int width, int height) {
        this.size = new Vector2D(width, height);
    }

    public MyPolygon getPolygon() {
        Vector2D worldPosition = getWorldPosition();

        Vector2D scaledSize = this.size.copy();
        scaledSize.times(this.scale*this.parent.scale*GameSettings.SCALE);
        
        Point pt1 = new Point((int) worldPosition.x, (int) worldPosition.y);
        Point pt2 = new Point((int) worldPosition.x + (int) scaledSize.x, (int) worldPosition.y);
        Point pt3 = new Point((int) worldPosition.x + (int) scaledSize.x, (int) worldPosition.y + (int) scaledSize.y);
        Point pt4 = new Point((int) worldPosition.x, (int) worldPosition.y + (int) scaledSize.y);
        Point[] vertices = {pt1, pt2, pt3, pt4};

        return new MyPolygon(vertices);
    }

    /**
     * Renvoie vrai si le composant est en collision.
     * @return vrai si il y a collision.
     */
    public boolean isColliding() {
        return this.isColliding;
    }

    /**
     * Renvoie vrai si le composant a été sélectionné.
     * @return vrai si le composant a été sélectionné.
     */
    public boolean isSelected() {
        return this.isSelected;
    }

    /**
     * Renvoie l'acteur avec lequel il y a collision.
     * @return l'acteur avec lequel il y a collision.
     */
    public Actor getCollidingActor() {
        return this.collindingActor;
    }

    @Override
    public void draw(Graphics2D g) {
        Vector2D screenPosition = getScreenPosition();

        Vector2D screenSize = this.size.copy();
        screenSize.times(this.scale*this.parent.scale*GameSettings.SCALE);
 
        g.drawRect((int) screenPosition.x, (int) screenPosition.y, (int) screenSize.x, (int) screenSize.y);
        g.fillRect((int) screenPosition.x, (int) screenPosition.y, (int) screenSize.x, (int) screenSize.y);
    }

    @Override
    public void tick(float deltaTime) {
        List<Actor> actors = new ArrayList<>(App.getCurrentMap().getActors());
        this.isColliding = false;
        this.collindingActor = null;

        for (Actor a: actors) {
            if (a == this.parent) continue;
            List<ActorComponent> components = a.getComponents();
            for (ActorComponent c: components) {
                if (!(c instanceof RectangleCollisionComponent)) continue;
                if (!((RectangleCollisionComponent) c).canCollide) continue;
                if (SATCollisionDetection.areCollinding(this.getPolygon(), ((RectangleCollisionComponent) c).getPolygon())) {
                    this.collindingActor = a;
                    this.isColliding = true;
                    parent.receiveEvent(this);
                    return;
                }
            }
        }

        List<MyPolygon> polygons = App.getCurrentMap().getCollisions();

        for (MyPolygon p: polygons) {
            if (this.canCollide && SATCollisionDetection.areCollinding(this.getPolygon(), p)) {
                this.isColliding = true;
                parent.receiveEvent(this);
                return;
            }
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) { 
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        Vector2D screenPosition = getScreenPosition();

        Vector2D screenSize = this.size.copy();
        screenSize.times(this.scale*this.parent.scale*GameSettings.SCALE);
        this.isSelected = false;

        if (e.getX() >= screenPosition.x && e.getX() <= screenPosition.x + screenSize.x && e.getY() >= screenPosition.y &&  e.getY() <= screenPosition.y + screenSize.y) {
            this.isSelected = true;
            this.parent.receiveEvent(this);
        }
    }

    @Override
    public String toString() {
        return "RectangleCollisionComponent";
    }
}
