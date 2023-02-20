package Main;

import java.util.Random;

public class EnnemyAI_dumb extends EnnemyAI {

	public EnnemyAI_dumb(Ennemy ennemy) {
		super(ennemy);
	}

	@Override
	public int getAction() {
		boolean tab[] = this.availableCompetences();
		if (tab[0] || tab[1] || tab[2] || tab[3]) {
			Random random = new Random();
			int k = random.nextInt(4);
			while (!tab[k]) {
				k = random.nextInt(4);
			}
			return k + 1;
		}
		else {
			// Si toutes les compétences sont en CD, on passe le tour 
			return 7;
		}
	}
}
