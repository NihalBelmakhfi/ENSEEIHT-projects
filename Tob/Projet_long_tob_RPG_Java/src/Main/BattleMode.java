package Main;

public class BattleMode {

	private Fighter player; 
	private Fighter ennemy; 
	private BattlePanel battlePanel; 
	private Fighter actualFighter;

	public BattleMode(Fighter player, Fighter ennemy) {
		this.player = player;
		this.ennemy = ennemy;
		this.battlePanel = new BattlePanel(player, ennemy);
		this.actualFighter = player;
		this.player.getStats().reset();
		this.ennemy.getStats().reset();
	}

	public void tick(float deltaTime) {
		try {
			int actionBattle = actualFighter.getAction(this.battlePanel);
			this.battlePanel.resetAction();
			switch(actionBattle) {
			case 0:
				return;
			case 1:
				Competences competence_1 = actualFighter.getCompetences()[0];
				Fighter target_1 = actualFighter.getTarget(this.battlePanel);
				competence_1.cast(target_1);
				break;
			case 2:
				Competences competence_2 = actualFighter.getCompetences()[1];
				Fighter target_2 = actualFighter.getTarget(this.battlePanel);
				competence_2.cast(target_2);
				break;
			case 3:
				Competences competence_3 = actualFighter.getCompetences()[2];
				Fighter target_3 = actualFighter.getTarget(this.battlePanel);
				competence_3.cast(target_3);
				break; 
			case 4:
				Competences competence_4 = actualFighter.getCompetences()[3];
				Fighter target_4 = actualFighter.getTarget(this.battlePanel);
				competence_4.cast(target_4);
				break;
			case 6:
				break; 
			case 7:
				break;
			}		

			for (int k = 0; k<4; k++) {
				actualFighter.getCompetences()[k].reduce_cd();
			}

			if (actualFighter == player) {
				actualFighter = ennemy;
			}
			else {
				actualFighter = player;
			}

			// RuntimeException sur windows?
			/* actualFighter = (actualFighter == player) ? ennemy : player; */

		} catch (CdException e) {
			System.out.printf("La compétence est en cours de rechergement \n");
		} finally {
			if (this.player.getStats().getHealth() <= 0) {
				this.player.onDeath();
				Main.getMainWindow().setGameState(GameState.WORLD);
			} else if (this.ennemy.getStats().getHealth() <= 0) {
				this.ennemy.onDeath();
				Main.getMainWindow().setGameState(GameState.WORLD);
			}
		}
	}

	public BattlePanel getBattlePanel() {
		return this.battlePanel; 
	}
}
