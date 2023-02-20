package allumettes;

public class Rapide implements strategie {
	// constructeur
	public Rapide() {
	}

	@Override
	public int getPrise(Jeu jeu) {
		assert (jeu != null);
		assert (jeu.getNombreAllumettes() > 0);
		int nbAllumettes = Math.min(Jeu.PRISE_MAX, jeu.getNombreAllumettes());
		return nbAllumettes;
	}

	@Override
	public String toString() {
		return "rapide";
	}
}
