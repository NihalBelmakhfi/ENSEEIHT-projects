package Core;

/**
 * Tout acteur qui a besoin d'être controllé (par un joueur ou une IA).
 */
public abstract class Pawn extends Actor{

    /** le contrôleur associé à ce pawn */
    protected Controller controller;
    /**
     * Permet de déplacer un acteur.
     * @param direction la direction du déplacement.
     * @param scale la vitesse de déplacement.
     */
    public void addMovementInput(Orientation direction, float scale) {
        switch(direction) {
            case NORTH:
                this.position.y -= 1 * scale;
                break;
            case WEST:
                this.position.x -= 1 * scale;
                break;
            case SOUTH:
                this.position.y += 1 * scale;
                break;
            case EAST:
                this.position.x += 1 * scale;
                break;
        }
    }

    /**
     * Change le controleur associé à ce pion.
     * @param controller le nouveau controleur.
     */
    public void setController(Controller controller) {
        this.controller = controller;
    }

    /**
     * Renvoie le controleur associé à ce pion.
     * @return le controleur associé à ce pion.
     */
    public Controller getController() {
        return this.controller;
    }

    @Override
    public void tick(float deltaTime) {
        this.lastPosition = this.position.copy();
        if(this.controller != null) this.controller.tick(deltaTime);
        super.tick(deltaTime);
        this.isMoving = !lastPosition.equals(this.position);
    }

    @Override
    public String toString() {
        return "Pawn";
    }
}
