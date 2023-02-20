package Main;
public class Principal extends Quest{
	
	private String nom_pnj;
	
	public Principal(String nom_pnj, int id, String description, Item loot , String quest_type) {
		this.identifiant=id;
		this.nom_pnj=nom_pnj;
		this.description=description;
		this.loot=loot;
		this.quest_type=quest_type;
		
	}
	
	public String getNomPnj() {
		return this.nom_pnj;
	}
}
