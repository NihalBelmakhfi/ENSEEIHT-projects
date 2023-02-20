package Core;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import GeometricObjects.Point;
import GeometricObjects.MyPolygon;
import GeometricObjects.Segment;
import GeometricObjects.Vector2D;

/**
 * Cette classe permet de vérifier si il y a collision entre 2 polygons en utilisant le théorème de séparation des hyperplans.
 * Les polygons sont représentés par une liste de segments.
 */
public class SATCollisionDetection {

    public static boolean areCollinding(MyPolygon poly1, MyPolygon poly2) {
        Set<Vector2D> normals = new HashSet<Vector2D>();
        List<Segment> edges1 = poly1.getEdges();
        List<Segment> edges2 = poly2.getEdges();
        List<Point> vertices1 = poly1.getVertices();
        List<Point> vertices2 = poly2.getVertices();

        for (Segment s: edges1) {
            normals.add(s.getNormal().getNormalized());
        }
        for (Segment s: edges2) {
            normals.add(s.getNormal().getNormalized());
        }

        for (Vector2D n: normals) {
            float p1_min = Float.POSITIVE_INFINITY;
            float p1_max = Float.NEGATIVE_INFINITY;
            float p2_min = Float.POSITIVE_INFINITY;
            float p2_max = Float.NEGATIVE_INFINITY;

            for (Point p: vertices1) {
                float projection = Vector2D.dot(n, p.toVector2D());
                if(projection < p1_min) p1_min = projection;
                if(projection > p1_max) p1_max = projection;
            }

            for (Point p: vertices2) {
                float projection = Vector2D.dot(n, p.toVector2D());
                if(projection < p2_min) p2_min = projection;
                if(projection > p2_max) p2_max = projection;
            }

            if (p1_max < p2_min || p2_max < p1_min) return false;
        }

        return true;
    }
}
