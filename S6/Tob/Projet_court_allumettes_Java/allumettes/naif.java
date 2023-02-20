package allumettes;

import java.util.Random;

public class naif implements strategie {
	// attribut
	private Random random;

	// constructeur
	public naif() {
		this.random = new Random();
	}

	@Override
	public int getPrise(Jeu jeu) {
		assert (jeu != null);
		assert (jeu.getNombreAllumettes() > 0);
		int nbAllumettes;
		// TODO Auto-generated method stub
		nbAllumettes = this.random.nextInt(Jeu.PRISE_MAX - 1) + 1;
		return nbAllumettes;
	}

	@Override
	public String toString() {
		return "naif";
	}
}
