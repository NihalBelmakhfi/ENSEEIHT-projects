package Main;
public class Statistics {

	private int health;
	private int maxHealth;
	private int strength;
	private int maxStrength;
	private int armor; 
	private int maxArmor; 
	
	
	public Statistics(int health, int maxHealth, int strength, int maxStrength, int armor, int maxArmor) {
		this.maxHealth = maxHealth;
		this.maxStrength = maxStrength;
		this.maxArmor = maxArmor;
		this.reset();
	}
	
	public void multiplier(int multiplicateur) {
		this.maxHealth = this.maxHealth*multiplicateur;
		this.maxStrength = this.maxStrength*multiplicateur;
		this.maxArmor = this.maxArmor*multiplicateur;
	}
	
	// reset réinitialise les statistiques effectives
	public void reset() {
		this.health = this.maxHealth;
		this.strength = this.maxStrength; 
		this.armor = this.maxArmor; 	
	}
	
//----------------------------------------------------------------	
	
	public void setHealth(int health) {
		this.health = health;
	}
	
	public int getHealth() {
		return this.health;
	}
	
	public void modifyHealth(int valeur) {
		if (this.health + valeur < 0) {
			this.health = 0;
		}
		else if (this.health + valeur > this.maxHealth) {
			this.health = this.maxHealth; 
		}
		else {
			this.health += valeur; 
		}
	}
	
//----------------------------------------------------------------	

	public void setMaxHealth(int maxHealth) {
		this.maxHealth = maxHealth;
	}

	public int getMaxHealth() {
		return this.maxHealth;
	}
	
	public void modifyMaxHealth(int valeur) {
		if (this.maxHealth + valeur < 1) {
			this.maxHealth = 1;
		}
		else {
			this.maxHealth += valeur;
		}
	}
	
//----------------------------------------------------------------	

	public void setStrength(int strength) {
		this.strength = strength;
	}
	
	public int getStrength() {
		return this.strength;
	}
	
	public void modifyStrength(int valeur) {
		if (this.strength + valeur < 1) {
			this.strength = 1;
		}
		else if (this.strength + valeur > this.maxStrength) {
			this.strength = this.maxStrength;
		}
		else {
			this.strength += valeur; 
		}
	}
	
//----------------------------------------------------------------	
	
	public void setMaxStrength(int maxStrength) {
		this.maxStrength = maxStrength;
	}
	
	public int getMaxStrength() {
		return this.maxStrength;
	}
	
	public void modifyMaxStrength(int valeur) {
		if (this.maxStrength + valeur < 1) {
			this.maxStrength = 1;
		}
		else {
			this.maxStrength += valeur; 
		}
	}
	
//----------------------------------------------------------------	

	public void setArmor(int armor) {
		this.armor = armor;
	}
	
	public int getArmor() {
		return this.armor;
	}	
	public void modifyArmor(int valeur) {
		if (this.armor + valeur < 1) {
			this.armor = 1;
		}
		else if (this.armor + valeur > this.maxArmor) {
			this.armor = this.maxArmor;
		}
		else {
			this.armor += valeur; 
		}
	}

//-------------------------------------------------------
	
	public void setMaxArmor(int maxArmor) {
		this.maxArmor = maxArmor;
	}
	
	public int getMaxArmor() {
		return this.maxStrength;
	}
	public void modifyMaxArmor(int valeur) {
		if (this.maxArmor + valeur < 1) {
			this.maxArmor = 1;
		}
		else {
			this.maxArmor += valeur; 
		}
	}
}
