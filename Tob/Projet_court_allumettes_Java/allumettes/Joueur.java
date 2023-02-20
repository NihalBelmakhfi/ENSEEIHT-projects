package allumettes;

import java.util.Scanner;

public class Joueur {
	// les attributs
	private String nom;
	private strategie strategie;

	// les constructeurs
	public Joueur(String name, strategie stratg) {
		this.nom = name;
		this.strategie = stratg;
	}

	// les méthodes
	/**
	 * Obtenir le nom.
	 * @return nom du joueur
	 */
	public String getNom() {
		return this.nom;
	}

	/**
	 * Obtenir la prise.
	 * @return prise
	 * @throws CoupInvalideException
	 */
	public int getPrise(Jeu jeu) throws CoupInvalideException {
		return this.strategie.getPrise(jeu);

	}

	/**
	 * Obtenir la strategie. 
	 * @return strategie
	 */
	public strategie getStrategie() {
		return this.strategie;
	}

	/**
	 * modifier la strategie. 
	 * @param newstrategie La Nouvelle strategie
	 */
	public void setStrategie(strategie newstrategie) {
		assert (newstrategie != null);
		this.strategie = newstrategie;
	}

	public String getStrategieToString() {
		return this.strategie.toString();
	}

}
