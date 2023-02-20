package allumettes;

import java.util.Random;

public class Expert implements strategie {

	// attribut
	private Random random;

	// constructeur
	public Expert() {
		this.random = new Random();
	}

	@Override
	public int getPrise(Jeu jeu) {
		assert (jeu != null);
		assert (jeu.getNombreAllumettes() > 0);
		int nbAllumettes = Math.floorMod(jeu.getNombreAllumettes(), Jeu.PRISE_MAX + 1) - 1;
		nbAllumettes = Math.floorMod(nbAllumettes, Jeu.PRISE_MAX + 1);
		return Math.max(nbAllumettes, 1);
	}

	@Override
	public String toString() {
		return "rapide";
	}
}
