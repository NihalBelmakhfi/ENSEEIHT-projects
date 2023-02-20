package Main;
public abstract class Quest {
	
	protected int identifiant;
	protected String description;
	protected Item loot;
	protected String quest_type;
	protected boolean follow;
	
	private void launch() {
		this.follow=true;
	}
	
	public int getId() {
		return this.identifiant;
	}
	
	public String getDesc() {
		return this.description;
	}
	
	
	public String getType() {
		return this.quest_type;
	}
}
