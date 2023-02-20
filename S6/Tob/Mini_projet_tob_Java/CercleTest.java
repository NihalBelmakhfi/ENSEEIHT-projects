import java.awt.Color;
import org.junit.*;
import static org.junit.Assert.*;

/**
  * Classe de test des exigences E12, E13 et E14.
  * @author	Nihal BELMAKHFI.
  * @version	$Revision$
  */
public class CercleTest {

	// précision pour les comparaisons réelle
	public final static double EPSILON = 0.001;

	// Les points du sujet
	private static Point C, D, E;

	// Les cercles du sujet
	private static Cercle C2, C3, C4;

	@Before public void setUp() {
		// Construire les points
		C = new Point(1, 2);
		D = new Point(3, 4);
		E = new Point(5, 6);

		// Construire les cercles
		C2 = new Cercle(C, D);
		C3 = new Cercle(C, D, Color.yellow);
	}
	

	/** Vérifier si deux points ont mêmes coordonnées.
	  * @param p1 le premier point
	  * @param p2 le deuxième point
	  */
	static void memesCoordonnees(String message, Point p1, Point p2) {
		assertEquals(message + " (x)", p1.getX(), p2.getX(), EPSILON);
		assertEquals(message + " (y)", p1.getY(), p2.getY(), EPSILON);
	}

	
	@Test public void testerE12C2() {
		memesCoordonnees("E12 : Centre de C2 incorrect", new Point(2, 3), C2.getCentre());
		assertEquals("E12 : Rayon de C2 incorrect", Math.sqrt(8)/2, C2.getRayon(), EPSILON);
		assertEquals(Color.blue, C2.getCouleur());
	}
		
	
	@Test public void testerE13C3() {
		memesCoordonnees("E13 : Centre de C3 incorrect", new Point(2, 3), C2.getCentre());
		assertEquals("E13 : Rayon de C3 incorrect", Math.sqrt(8)/2, C3.getRayon(), EPSILON);
		assertEquals(Color.yellow, C3.getCouleur());
	}
	
	
	@Test public void testerE14C4() {
		C4 = Cercle.creerCercle(D, E);
		memesCoordonnees("E14 : Centre de C4 incorrect",D, C4.getCentre());
		assertEquals("E14 : Rayon de C4 incorrect", Math.sqrt(8), C4.getRayon(), EPSILON);
		assertEquals(Color.blue, C4.getCouleur());
	}
}
	
	
	
	
	
	
	