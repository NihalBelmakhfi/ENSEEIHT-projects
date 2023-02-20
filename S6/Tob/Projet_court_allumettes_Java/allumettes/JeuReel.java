package allumettes;

public class JeuReel implements Jeu {

	// Attribut
	public static final int PRISE_MAX = 3;
	private int nbAllumettes;

	// Constructeur
	public JeuReel(int nombreAllum) {
		this.nbAllumettes = nombreAllum;
	}

	@Override
	public int getNombreAllumettes() {
		return this.nbAllumettes;
	}

	@Override
	public void retirer(int nbPrises) throws CoupInvalideException {
		if (nbPrises > 0 && nbPrises <= JeuReel.PRISE_MAX
				&& nbPrises <= this.nbAllumettes) {
			this.nbAllumettes -= nbPrises;
		} else {
			throw new CoupInvalideException(nbPrises, "nombre pris est invalide");
		}
	}

}
