package Core;
import GeometricObjects.Vector2D;

/**
 * Classe permettant de posséder un pion afin de contrôler ses actions.
 * Un controller est fortement lié au pion qu'il possède.
 */
public abstract class Controller extends Actor {
    protected Pawn pawn;

    public Controller() {
        this.canDraw = false;
    }
    /**
     * Possède le pion.
     * @param pawn le pion à posséder.
     */
    public void possess(Pawn pawn) {
        this.pawn = pawn;
        this.pawn.setController(this);
    }
    
    /**
     * Renvoie le pion controllé.
     * @return le pion controllé.
     */
    public Pawn getPossessedPawn() {
        return this.pawn;
    }

    @Override
    public Vector2D getActorLocation() {
        return this.pawn != null ? this.pawn.getActorLocation() : new Vector2D();
    }

    @Override
    public String toString() {
        return "Controller";
    }
}
