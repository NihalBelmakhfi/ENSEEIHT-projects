package GeometricObjects;

// Devrait implémentait une interface copy
public class Vector2D {
    public float x;
    public float y;

    public static float dot(Vector2D a, Vector2D b) {
        return a.x*b.x + a.y*b.y;
    }

    public Vector2D() {
        this.x = 0;
        this.y = 0;
    }

    public Vector2D(float x, float y) {
        this.x = x;
        this.y = y;
    }

    private Vector2D(Vector2D v) {
        this.x = v.x;
        this.y = v.y;
    }

    public float getLength() {
        return (float) Math.sqrt(x*x + y*y); 
    }

    public Vector2D getNormalized() {
        float l = getLength();
        
        return new Vector2D(x/l, y/l);
    }

    public void add(Vector2D other) {
        this.x += other.x;
        this.y += other.y;
    }

    public void add(float x, float y) {
        this.x += x;
        this.y += y;
    }

    public static Vector2D add(Vector2D a, Vector2D b) {
        return new Vector2D(a.x+b.x, a.y+b.y);
    }

    public void sub(Vector2D other) {
        this.x -= other.x;
        this.y -= other.y;
    }

    public void sub(float x, float y) {
        this.x -= x;
        this.y -= y;
    }

    public static Vector2D sub(Vector2D a, Vector2D b) {
        return new Vector2D(a.x-b.x, a.y-b.y);
    }

    public void times(float s) {
        this.x *= s;
        this.y *= s;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Vector2D)) return false;
        Vector2D o2 = (Vector2D) o;
        if (this.x == o2.x && this.y == o2.y) return true;
        return false;
    }

    public Vector2D copy() {
        return new Vector2D(this);
    }

    @Override
    public String toString() {
        return "(" + this.x + ";" + this.y + ")";
    }
}