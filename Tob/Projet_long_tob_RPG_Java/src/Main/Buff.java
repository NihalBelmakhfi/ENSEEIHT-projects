package Main;

public abstract class Buff {
	
	protected int duration_tot;
	protected int duration;
	protected Fighter casteur; 
	protected Fighter target;
	
	public Buff(Fighter casteur, Fighter target) {
		this.casteur = casteur;
		this.target = target;
		this.duration = 0;
	}
	
	abstract void effect();
	
	public void castBuff() {
		if (this.duration > 0) {
			this.effect();
			this.reduceDuration();
		}
	}
	
	public void reduceDuration() {
		if (this.duration > 0) {
			this.duration -= 1;
		}
	}
}
