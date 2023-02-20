package Main;

public abstract class EnnemyAI{
	
	protected Ennemy ennemy;
	
	public EnnemyAI(Ennemy ennemy) {
		this.ennemy = ennemy;
	}
	
	abstract int getAction();
	
	public boolean[] availableCompetences() {
		boolean tab[] = new boolean[4];
		for (int k = 0; k<4; k++) {
			if (ennemy.getCompetences()[k].getCd() == 0) {
				tab[k] = true;
			}
			else {
				tab[k] = false;
			}
		}
		return tab;
	}
	
	public void setEnnemy(Ennemy ennemy) {
		this.ennemy = ennemy;
	}

}
