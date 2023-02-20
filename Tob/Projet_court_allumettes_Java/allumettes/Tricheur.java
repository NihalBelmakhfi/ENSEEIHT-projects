package allumettes;

import java.util.Random;

public class Tricheur implements strategie {
	// attribut
	private Random random;

	// constructeur
	public Tricheur() {
		this.random = new Random();
	}

	@Override
	public int getPrise(Jeu jeu) throws CoupInvalideException {
		assert (jeu != null);
		assert (jeu.getNombreAllumettes() > 0);
		System.out.println("[Je triche...]");
		while (jeu.getNombreAllumettes() > 2) {
			jeu.retirer(1);
		}
		System.out.println("[Allumettes restantes : 2]");
		return 1;
	}

	@Override
	public String toString() {
		return "tricheur";
	}
}
