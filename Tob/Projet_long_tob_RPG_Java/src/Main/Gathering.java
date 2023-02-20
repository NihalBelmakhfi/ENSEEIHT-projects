package Main;
public class Gathering extends Quest{
	

	private int nombre_recolte;
	private String nom_recolte;
	
	public Gathering( int nb_recolte, String nom_recolte, int id, String description, Item loot , String quest_type) {
		this.identifiant=id;
		this.nombre_recolte=nb_recolte;
		this.nom_recolte=nom_recolte;
		this.description=description;
		this.loot=loot;
		this.quest_type=quest_type;
		
	}
	
	private int getNbRecolte(){
		return this.nombre_recolte;
	}
	
	private String getNomRecolte(){
		return this.nom_recolte;
	}
}
