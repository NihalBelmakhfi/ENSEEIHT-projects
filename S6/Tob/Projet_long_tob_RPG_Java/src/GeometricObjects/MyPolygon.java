package GeometricObjects;

import java.util.ArrayList;
import java.util.List;

public class MyPolygon {
    private List<Point> vertices = new ArrayList<Point>();
    private List<Segment> edges = new ArrayList<Segment>();

    public MyPolygon(Point[] vertices) {
        if (vertices.length < 3 ) throw new RuntimeException();

        for (int i = 0; i < vertices.length; i++) {
            this.vertices.add(vertices[i]);

            if (i < vertices.length - 1) {
                this.edges.add(new Segment(vertices[i], vertices[i + 1]));
            }
            else {
                this.edges.add(new Segment(vertices[i], vertices[0]));
            }
        }
    }

    public MyPolygon(Segment[] edges) {
        if (edges.length < 3 ) throw new RuntimeException();

        for (int i = 0; i < edges.length; i++) {
            this.edges.add(edges[i]);
            this.vertices.add(edges[i].getExtremite1());
            this.vertices.add(edges[i].getExtremite2());
        }
    }

    public List<Segment> getEdges() {
        return this.edges;
    }

    public List<Point> getVertices() {
        return this.vertices;
    }

    @Override
    public String toString() {
        String ret = "[";
        for (Point p: this.vertices) {
            ret += p + "-";
        }
        ret = ret.substring(0, ret.length()-1) + "]";

        return ret;
    }
}
