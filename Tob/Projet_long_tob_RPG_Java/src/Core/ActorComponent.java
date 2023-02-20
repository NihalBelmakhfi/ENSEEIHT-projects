package Core;

import java.awt.Graphics2D;

import GeometricObjects.Vector2D;

/**
 * Classe de base définissant un composant qui est attaché à un parent.
 * @see Changer le constructeur afin de n'avoir que le parent en paramètre et rajouter les setters et getters nécessaire.
 * faire cela aussi pour les sous-classe.
 */
public abstract class ActorComponent {

    /** Si vrai la fonction tick du composant est appelé par le parent le possédant. */
    public boolean canTick = false;
    /** Si vrai la fonction draw du composant est appelé par le parent le possédant. */
    public boolean canDraw = false;
    /** Le parent possédant le composant. */
    protected Actor parent;
    /** Décalage de la position du composant par rapport à son parent. */
    protected Vector2D position = new Vector2D();
    /** Le facteur de mise à l'échelle global. */
    protected float scale = 1;

    /**
     * Initialise un composant en l'associant à un parent.
     * @param parent le parent auquel le composant est associé.
     */
    public ActorComponent(Actor parent) {
        this.parent = parent;
    }

    /**
     * Change la position relative du composant par rapport au parent.
     * @param width la position x en pixel.
     * @param heigth la position y en pixel.
     */
    public void setPosition(int x, int y) {
        this.position = new Vector2D(x, y);
    }

    /**
     * Change le facteur de mise à l'échelle local du composant.
     * @param scale le facteur de mise à l'échelle.
     */
    public void setScale(float scale) {
        this.scale = scale;
    }

    /**
     * Récupère la position à l'écran du composant.
     * @return la position à l'écran du composant.
     */
    protected Vector2D getScreenPosition() {
        Vector2D screenPosition = GameSettings.SCREEN_CENTER.copy();
        screenPosition.add(getWorldPosition());
        screenPosition.sub(App.getPlayerController(0).getActorLocation());
        return screenPosition;
    }

    /**
     * Récupère la position dans le monde du composant.
     * @return la position dans le monde du composant.
     */
    protected Vector2D getWorldPosition() {
        Vector2D scaledPosition = this.position.copy();
        scaledPosition.times(this.scale*this.parent.getScale()*GameSettings.SCALE);

        Vector2D worldPosition = this.parent.getActorLocation();
        worldPosition.add(scaledPosition);

        return worldPosition;
    }

    /**
     * Dessine le composant si nécessaire.
     * @param g "outil de dessin".
     */
    public void draw(Graphics2D g) {

    }

    /**
     * Met à jour le composant si nécessaire.
     * @param deltaTime le temps entre 2 images consécutives.
     */
    public void tick(float deltaTime) {

    }

    @Override
    public String toString() {
        return "ActorComponent";
    }
}
