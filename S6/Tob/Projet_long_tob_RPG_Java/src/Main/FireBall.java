package Main;
public class FireBall extends Competences  {

	public FireBall(Fighter casteur) {
		super(casteur);
		this.cd = 5;
	}


	public void cast(Fighter target) throws CdException{
		if (this.on_cd > 0) {
			throw new CdException();
		}
		else {
			double multiplicator = 0.2;
			double dmg = -this.casteur.getStats().getStrength()*multiplicator*reductionDmg(target);
			target.getStats().modifyHealth((int)dmg);
			this.on_cd = this.cd;
		}
	}
}
