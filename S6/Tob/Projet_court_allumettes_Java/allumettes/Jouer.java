package allumettes;

/**
 * Lance une partie des 13 allumettes en fonction des arguments fournis sur la
 * ligne de commande.
 * 
 * @author Xavier Crégut
 * @version $Revision: 1.5 $
 */
public class Jouer {
	public static strategie getstrategie(String nom, String name) {
		strategie strategie = null;
		switch (nom) {
		case "humain":
			strategie = new Human(name);
			break;
		case "expert":
			strategie = new Expert();
			break;
		case "rapide":
			strategie = new Rapide();
			break;
		case "naif":
			strategie = new Naif();
			break;
		case "tricheur":
			strategie = new Tricheur();
			break;
		}
		return strategie;
	}

	private static boolean isCorrect(String saisie) {
		boolean oui = (saisie.equals("naif") || saisie.equals("expert") || saisie.equals("rapide")
				|| saisie.equals("humain") || saisie.equals("tricheur"));
		return oui;
	}

	/**
	 * Lancer une partie. En argument sont donnés les deux joueurs sous la forme
	 * nom@stratégie.
	 * 
	 * @param args la description des deux joueurs
	 */

	public static void main(String[] args) {
		try {
			final int nbJoueurs = 2;
			Joueur joueur1 = null;
			Joueur joueur2 = null;
			verifierNombreArguments(args);
			boolean confiant = (args.length == nbJoueurs + 1) && (args[0].equals("-confiant"));
			// récupération des joueurs
			joueur1 = creatJoueur(args[args.length - 2]);
			joueur2 = creatJoueur(args[args.length - 1]);
			// nombre d'allumettes au début
			JeuReel jeu = new JeuReel(13);
			// créer l'arbitre
			Arbitre arbitre = new Arbitre(joueur1, joueur2, confiant);
			arbitre.setConfiant(confiant);
			arbitre.arbitrer(jeu);

		} catch (ConfigurationException e) {
			System.out.println();
			System.out.println("Erreur : " + e.getMessage());
			afficherUsage();
			System.exit(1);
		} catch (ArrayIndexOutOfBoundsException e2) {
			System.out.println("Erreur : strategie manquante");
		}

	}

	public static Joueur creatJoueur(String arg) {
		String[] j1 = arg.split("@");
		if (j1.length > 2) {
			throw new ConfigurationException("strategie de " + j1[0] + " est invalide");
		}
		if (!isCorrect(j1[1])) {
			throw new ConfigurationException("strategie de " + j1[0] + " est invalide");
		}
		return new Joueur(j1[0], getstrategie(j1[1], j1[0]));
	}

	private static void verifierNombreArguments(String[] args) {
		final int nbJoueurs = 2;
		if (args.length < nbJoueurs) {
			throw new ConfigurationException("Trop peu d'arguments : " + args.length);
		}
		if (args.length > nbJoueurs + 1) {
			throw new ConfigurationException("Trop d'arguments : " + args.length);
		}
	}

	/** Afficher des indications sur la manière d'exécuter cette classe. */
	public static void afficherUsage() {
		System.out.println("\n" + "Usage :" + "\n\t" + "java allumettes.Jouer joueur1 joueur2" + "\n\t\t"
				+ "joueur est de la forme nom@stratégie" + "\n\t\t"
				+ "strategie = naif | rapide | expert | humain | tricheur" + "\n" + "\n\t" + "Exemple :" + "\n\t"
				+ "	java allumettes.Jouer Xavier@humain " + "Ordinateur@naif" + "\n");
	}
}
