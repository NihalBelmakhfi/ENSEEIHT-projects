package Main;
public class Zap extends Competences  {

	public Zap(Fighter casteur) {
		super(casteur);
		this.cd = 1;
	}


	public void cast(Fighter target) throws CdException{
		if (this.on_cd > 0) {
			throw new CdException();
		}
		else {
			double multiplicator = 0.1;
			double dmg = -this.casteur.getStats().getStrength()*multiplicator*reductionDmg(target);
			target.getStats().modifyHealth((int)dmg);
			this.on_cd = this.cd;
		}
	}
}