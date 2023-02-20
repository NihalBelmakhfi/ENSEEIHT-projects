package Main;
public class Inventory {
    
    private ItemInventory[] inventaire;
    private int nbItem;

    public Inventory(ItemInventory[] inventaire) {
        this.inventaire = inventaire;
        this.nbItem = inventaire.length;
    }
    public Inventory() {
    	this.inventaire = new ItemInventory[100];
        this.nbItem = 0;
    }
    
    //fonction qui lance l'inventaire
                       
    public int getNbItem() {
        return this.nbItem;
    }
    
    public ItemInventory[] getInventaire() {
    	return this.inventaire;
    }
    public boolean estPresent(ItemInventory objet) {
        boolean resultat = false;
        int i = 0;
        while (i < this.nbItem && !resultat) {
            if (this.inventaire[i].getId() == objet.getId()) {
                resultat = true;
            }
            i++;
        }
        return resultat;
    }

    public int indiceItem(ItemInventory objet) {
        int i = 0;
        while (i < this.nbItem) {
            if (this.inventaire[i].getId() == objet.getId()) {
                return i;
            } else {
                i++;
            }
        }
        return i;
    }

    public void ajouterItem(ItemInventory objet) {
        if (this.estPresent(objet)) {
            inventaire[indiceItem(objet)].ajouterQuantite(objet.getQuantite());
        } else {
            inventaire[this.nbItem] = objet;
            nbItem ++; 
        }
    }

    public void jeterItem(ItemInventory objet, int x) {
    	inventaire[indiceItem(objet)].ajouterQuantite(-x);
    	this.verifierInventaire();
    }

    public void supprimerItem(ItemInventory objet) {
    	int indice = indiceItem(objet);
    	for (int i = indice; i < this.nbItem; i++) {
    		inventaire[indice] = inventaire[indice + 1];
    	}
    	nbItem--;
    }

    public void verifierInventaire() {
    	for (int i = 0; i < this.nbItem; i++) {
    		if (inventaire[i].getQuantite() <= 0 ) {
    			this.supprimerItem(inventaire[i]);
    		}
    	}
    }

}