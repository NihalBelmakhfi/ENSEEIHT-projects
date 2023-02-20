package Main;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Item {
    
    private String name;

    private String description;

    private int id;

    private Statistics stat;

    private BufferedImage image;

    private boolean consommable;

    public Item(String name, String description, int id, Statistics stat, boolean consommable){
        this.name = name;
        this.description = description;
        this.id = id;
        this.stat = stat;
        setImage(name);
        this.consommable = consommable;
    }
    
    public void setImage(String name) {
    	try {
    		this.image = ImageIO.read(getClass().getResourceAsStream("/Assets/Item/" + name + ".png"));
    	} catch (IOException e) {
    		e.printStackTrace();
    	}
    }
    
    public String getName(){
        return this.name;
    }

    public String getDescription(){
        return this.description;
    }

    public int getId (){
        return this.id;
    }

    public boolean isConsommable(){
        return this.consommable;
    }

    public Statistics getStat(){
        return this.stat;
    }

}