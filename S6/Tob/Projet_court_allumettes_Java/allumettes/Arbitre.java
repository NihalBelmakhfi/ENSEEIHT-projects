package allumettes;

import java.util.InputMismatchException;

public class Arbitre {

	// les attributs
	private Joueur joueur1;
	private Joueur joueur2;
	private boolean confiant;

	// les constructeurs
	public Arbitre(Joueur j1, Joueur j2, boolean confiant) {
		this.joueur1 = j1;
		this.joueur2 = j2;
		this.confiant = confiant;
	}

	public Arbitre(Joueur j1, Joueur j2) {
		this.joueur1 = j1;
		this.joueur2 = j2;
	}
	// les méthodes

	public boolean estConfiant() {
		return this.confiant;
	}

	public void setConfiant(boolean confiant) {
		this.confiant = confiant;
	}

	public Joueur getJoueur1() {
		return this.joueur1;
	}

	public Joueur getJoueur2() {
		return this.joueur2;
	}

	public void nbrInvalide(int nbChoisi, Jeu jeu) {
		System.out.print("Impossible ! Nombre invalide : ");
		System.out.print(nbChoisi);
		if (nbChoisi > jeu.getNombreAllumettes()) {
			System.out.println(" (> " + jeu.getNombreAllumettes() + ")");
		} else if (nbChoisi > Jeu.PRISE_MAX) {
			System.out.print(" (> " + Jeu.PRISE_MAX + ")\n");
		} else if (nbChoisi < 1) {
			System.out.print(" (< 1)\n");
		}
		System.out.println();
	}

	public void arbitrer(Jeu jeu) {
		assert (jeu != null && jeu.getNombreAllumettes() > 0);
		Joueur joueur = this.joueur1;
		boolean roleJoueur1 = true;
		boolean fin = false;
		boolean triche = false;
		int nbTire = 0;
		while (jeu.getNombreAllumettes() > 0 && !triche) {
			if (!roleJoueur1) {
				joueur = this.joueur2;
			} else {
				joueur = this.joueur1;
			}
			try {
				boolean tourSuivant = false;
				while (!tourSuivant) {
					Jeu jeuDuJoueur = jeu;
					if (!this.confiant) {
						jeuDuJoueur = new JeuMandataire(jeu);
					}
					System.out.println("Allumettes restantes : " + jeu.getNombreAllumettes());
					nbTire = joueur.getPrise(jeuDuJoueur);
					String nom = joueur.getNom();
					if (nbTire == 1 || nbTire == 0 || nbTire == -1) {
						System.out.println(nom + " prend " + nbTire + " allumette.");
					} else {
						System.out.println(nom + " prend " + nbTire + " allumettes.");
					}
					jeu.retirer(nbTire);
					tourSuivant = true;
				}

				System.out.println();
				roleJoueur1 = !roleJoueur1;
				fin = (jeu.getNombreAllumettes() == 0);

			} catch (OperationInterditeException e) {
				System.out.println("Abandon de la partie car " + joueur.getNom() + " triche !");
				triche = true;
				fin = true;
			} catch (CoupInvalideException e) {
				nbrInvalide(nbTire, jeu);
			}
		}

		// Retourner le gagnant et le perdant
		if (!triche) {
			Joueur vainqueur = this.joueur1;
			Joueur perdant = this.joueur2;
			if (!roleJoueur1) {
				perdant = this.joueur1;
				vainqueur = this.joueur2;
			}
			System.out.println(perdant.getNom() + " perd !");
			System.out.println(vainqueur.getNom() + " gagne !");
		}
	}
}
