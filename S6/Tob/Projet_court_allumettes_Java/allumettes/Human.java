package allumettes;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Human implements strategie {

	// attribut
	private static Scanner scanner;
	private String name;

	// constructeur
	public Human(String name) {
		this.scanner = new Scanner(System.in);
		this.name = name;
	}

	@Override
	public int getPrise(Jeu jeu) throws CoupInvalideException {
		assert (jeu != null);
		assert (jeu.getNombreAllumettes() > 0);
		int nbAllumettes = 0;
		String nbAllumettesStr = "0";

		boolean varBoucle = false;
		while (!varBoucle) {
			try {
				System.out.print(this.name + ", combien d'allumettes ? ");
				nbAllumettesStr = this.scanner.next();
				nbAllumettes = Integer.parseInt(nbAllumettesStr);
				varBoucle = true;
			} catch (NumberFormatException e) {
				if (nbAllumettesStr.equals("triche")) {
					jeu.retirer(1);
					System.out.println("[Une allumette en moins, plus que " + jeu.getNombreAllumettes() + ". Chut !]");
				} else {
					System.out.println("Vous devez donner un entier.");
				}
			}
		}
		return nbAllumettes;
	}

	@Override
	public String toString() {
		return "humain";
	}

}
