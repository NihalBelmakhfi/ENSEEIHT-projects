package Main;
public class ArmorShred extends Competences  {

	public ArmorShred(Fighter casteur) {
		super(casteur);
		this.cd = 2;
	}


	public void cast(Fighter target) throws CdException{
		if (this.on_cd > 0) {
			throw new CdException();
		}
		else {
			double multiplicator = 0.1;
			double dmg = -this.casteur.getStats().getStrength()*multiplicator*reductionDmg(target);
			target.getStats().modifyHealth((int)dmg);
			target.getStats().modifyArmor((int)(2*dmg));
			System.out.println(target.getStats().getArmor());
			this.on_cd = this.cd;
		}
	}
}

