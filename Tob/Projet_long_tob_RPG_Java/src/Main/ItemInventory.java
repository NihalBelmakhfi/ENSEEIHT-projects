package Main;

public class ItemInventory extends Item {
	
	private int quantite;
	
	public ItemInventory(String name, String description, int id, Statistics stat,
			boolean consommable, int quantite) {
		super(name, description, id, stat, consommable);
		this.quantite = quantite;
	}
	
	public ItemInventory(Item item, int quantite) {
		super(item.getName(), item.getDescription(), item.getId(), item.getStat(), item.isConsommable());
		this.quantite = quantite;
	}
	
    public int getQuantite(){
        return this.quantite;
    }

    public void setQuantite(int x){
        this.quantite = x;
    }

    public void ajouterQuantite (int x){
        this.quantite += x;
    } 
}
