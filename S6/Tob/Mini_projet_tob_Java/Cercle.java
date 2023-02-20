import java.awt.Color;
/**Un cercle est une courbe plane fermée constituée des points situés
 *à égale distance d’un point nommé centre. La valeur de cette distance
 *distance entre est appelée rayon du cercle. On appelle diamètre la
 *deux points diamétralement opposés. La valeur du diamètre est donc le
 *double de la valeur du rayon (source : Wikipédia).
 * @author Nihal BELMAKHFI
 */
public class Cercle implements Mesurable2D {
	// les attributs
	/** Définir le premier attribut.
	 * centre le centre du cercle
	 */
	private Point centre;
	/** Définir le deuxième attribut.
	 * rayon le rayon du cercle
	 */
	private double rayon;
	/** Définir le troisème attribut.
	 * couleur la couleur du cercle
	 */
	private Color couleur;
	//les constantes
	/** Définir la constante PI.
	 * PI la constante mathématique pi
	 */
	public static final double PI = Math.PI;
	//les constructeurs
	/** Construire un cercle à partir de son centre, de son rayon.
	 * @param a centre.
	 * @param r rayon.
	 */
	public Cercle(Point a, double r) {
		assert (a != null);
		assert (r > 0);
		Point copy = new Point(a.getX(), a.getY());
		this.centre = copy;
		this.rayon = r;
		this.couleur = Color.blue;
	}
	/** Construire un cercle à partir de deux points diamétralement opposés.
	 * @param c le premier point.
	 * @param d le 2ème point diamétralement opposé au premier.
	 */
	public Cercle(Point c, Point d) {
		assert (c != null && d != null);
		assert (c.getX() != d.getX() || c.getY() != d.getY());
		double distance = c.distance(d);
		this.rayon = distance / 2;
		double xCentre = (c.getX() + d.getX()) / 2;
		double yCentre = (c.getY() + d.getY()) / 2;
		this.centre = new Point(xCentre, yCentre);
		this.couleur = Color.blue;
	}
	/** Construire un cercle à partir de deux points et de sa couleur.
	 * @param c le premier point.
	 * @param d le 2ème point diamétralement opposé au premier.
	 * @param couleur la couleur du cercle
	 */
	public Cercle(Point c, Point d, Color couleur) {
		assert (c != null && d != null);
		assert (couleur != null);
		assert (c.getX() != d.getX() || c.getY() != d.getY());
		double distance = c.distance(d);
		this.rayon = distance / 2;
		double xCentre = (c.getX() + d.getX()) / 2;
		double yCentre = (c.getY() + d.getY()) / 2;
		this.centre = new Point(xCentre, yCentre);
		this.couleur = couleur;
	}
	//les méthodes
	/** Translater le cercle.
	* @param dx déplacement suivant l'axe des X
	* @param dy déplacement suivant l'axe des Y
	*/
	public void translater(double dx, double dy) {
	    centre.translater(dx, dy);
	}
	/** Obtenir le centre.
	 * @return centre du cercle
	 */
	public Point getCentre() {
		Point copy = new Point(this.centre.getX(), this.centre.getY());
		return copy;
	}
	/** Obtenir le rayon.
	 * @return rayon du cercle
	 */
	public double getRayon() {
		return this.rayon;
	}
	/** Obtenir le diamètre.
	 * @return rayon du diamètre
	 */
	public double getDiametre() {
		double diametre = 2 * (this.rayon);
		return diametre;
	}
	/** Savoir si un point est à l'intérieur du cercle.
	 * @return booléen
	 * @param a point à l'intérieur du cercle
	 */
	public boolean contient(Point a) {
		assert (a != null);
		double d = a.distance(this.centre);
		return this.rayon >= d;
	}
	/** Obtenir le périmètre.
	 * @return périmètre du cercle
	 */
	public double perimetre() {
		double perimetre = 2 * (this.rayon) * PI;
		return perimetre;
	}
	/** Obtenir l'aire.
	 * @return l'aire du cercle
	 */
	public double aire() {
		double perimetre = (this.rayon) * (this.rayon) * PI;
		return perimetre;
	}
	/** Obtenir la couleur.
	 * @return couleur du cercle
	 */
	public Color getCouleur() {
		return this.couleur;
	}
	/** Changer la couleur du cercle.
	 * @param newColor nouvelle couleur
	 */
	public void setCouleur(Color newColor) {
		assert (newColor != null);
		this.couleur = newColor;
	}
	/** Créer un cercle.
	 * @return un cercle
	 * @param a le premier point du cercle
	 * @param b le deuxième point du cercle
	 */
	public static Cercle creerCercle(Point a, Point b) {
		assert (a != null && b != null);
		assert (a.getX() != b.getX() || a.getY() != b.getY());
		double d = a.distance(b);
		return new Cercle(a, d);
	}
	/** Afficher le cercle.
	 * @return affichage du cercle
	 * */
	public String toString() {
		return "C" + this.rayon + "@" + centre.toString();
        }
	/** Changer le rayon du cercle.
	 * @param newRayon nouveau rayon
	 */
	public void setRayon(double newRayon) {
		assert (newRayon > 0);
		this.rayon = newRayon;
	}
	/** Changer le diamètre du cercle.
	 * @param newDiametre nouveau diamètre
	 */
	public void setDiametre(double newDiametre) {
		assert (newDiametre > 0);
		this.rayon = newDiametre / 2;
	}
}
