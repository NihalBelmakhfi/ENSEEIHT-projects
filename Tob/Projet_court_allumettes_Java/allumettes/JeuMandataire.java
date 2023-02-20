package allumettes;

public class JeuMandataire implements Jeu {

	// Attribut
	private Jeu jeu;

	// Constructeur
	public JeuMandataire(Jeu jeu) {
		this.jeu = jeu;
	}

	@Override
	public int getNombreAllumettes() {
		return this.jeu.getNombreAllumettes();
	}

	@Override
	public void retirer(int nbPrises) throws CoupInvalideException {
		throw new OperationInterditeException("nombre invalide");
	}

}
