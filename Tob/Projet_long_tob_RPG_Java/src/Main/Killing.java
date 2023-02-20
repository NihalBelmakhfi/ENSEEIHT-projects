package Main;
public class Killing extends Quest{

	
	
	private int nombre_monstres;
	private String nom_monstres;
	
	public Killing( int nb_monstres, String nom_monstres, int id, String description, Item loot , String quest_type) {
		this.identifiant=id;
		this.nombre_monstres=nb_monstres;
		this.description=description;
		this.loot=loot;
		this.quest_type=quest_type;
		this.nom_monstres=nom_monstres;
		
	}
	
	private int getNbMonstres(){
		return this.nombre_monstres;
	}
	
	private String getNomMonstres(){
		return this.nom_monstres;
	}
}
