package GeometricObjects;

/**
 * Cette classe définit un point anonyme dans un plan.
 * Devrait implémentait une interface copy
 */
public class Point {
    private float x;
    private float y;

    public Point() {
        this.x = 0;
        this.y = 0;
    }

    public Point(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public Point(Point pt) {
        this.x = pt.x;
        this.y = pt.y;
    }

    public float getX() {
        return this.x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return this.y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public Vector2D toVector2D() {
        return new Vector2D(this.x, this.y);
    }

    public Point copy() {
        return new Point(this);
    }

    @Override
    public String toString() {
        return "(" + this.x + ", " + this.y + ")";
    }
}
