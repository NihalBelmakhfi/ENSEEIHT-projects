package Main;

public abstract class Competences {
	
	protected Fighter casteur;
	protected int cd;
	protected int on_cd = 0;

	
	public Competences(Fighter casteur) {
		this.casteur = casteur;
	}
	
	public Competences() {
	}
	
	// cast() va contenir l'effet de la compétence
	abstract void cast(Fighter target) throws CdException;
	
	public float reductionDmg(Fighter fighter) {
		int armor = fighter.getStats().getArmor();
		return 100/(100+(float)armor);
	}
	
	public String getName() {
		return this.getClass().getSimpleName();
	}
	
	public int getCd() {
		return this.on_cd;
	}
	
	public void reduce_cd() {
		if (this.on_cd > 0) {
			this.on_cd -= 1;
		}
	}
}
