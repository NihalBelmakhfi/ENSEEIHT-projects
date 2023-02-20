package GeometricObjects;

// Devrait implémentait une interface copy
public class Segment {
    private Point e1;
    private Point e2;

    public Segment(Point e1, Point e2) {
        this.e1 = e1.copy();
        this.e2 = e2.copy();
    }

    public Segment(Segment s) {
        this.e1 = s.e1.copy();
        this.e2 = s.e2.copy();
    }

    public Point getExtremite1() {
        return this.e1.copy();
    }

    public void setExtremite1(Point pt) {
        this.e1 = pt.copy();
    }

    public Point getExtremite2() {
        return this.e2.copy();
    }

    public void setExtremite2(Point pt) {
        this.e2 = pt.copy();
    }

    public Vector2D getNormal() {
        float dx = this.e1.getX() - this.e2.getX();
        float dy = this.e1.getY() - this.e2.getY();

        return new Vector2D(-dy, dx);
    }

    public Vector2D getTangent() {
        return new Vector2D(this.e1.getX()-this.e2.getX(), this.e1.getY()-this.e2.getY());
    }

    public Segment copy() {
        return new Segment(this);
    }

    @Override
    public String toString() {
        return "[" + this.e1 + "-" + this.e2 + "]";
    }
}
