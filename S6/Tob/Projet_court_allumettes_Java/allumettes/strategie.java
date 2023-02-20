package allumettes;

public interface strategie {
	int getPrise(Jeu jeu) throws CoupInvalideException;

	String toString();
}
