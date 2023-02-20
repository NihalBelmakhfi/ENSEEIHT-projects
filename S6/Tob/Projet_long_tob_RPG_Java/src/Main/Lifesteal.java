package Main;
public class Lifesteal extends Competences  {

	public Lifesteal(Fighter casteur) {
		super(casteur);
		this.cd = 6;
	}


	public void cast(Fighter target) throws CdException{
		if (this.on_cd > 0) {
			throw new CdException();
		}
		else {
			double multiplicator = 0.2;
			double dmg = -this.casteur.getStats().getStrength()*multiplicator*reductionDmg(target);
			target.getStats().modifyHealth((int)dmg);
			casteur.getStats().modifyHealth((int)-dmg);
			this.on_cd = this.cd;
		}
	}
}
