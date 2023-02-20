package Core;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.imageio.ImageIO;

// Import the gson-2.9.0.jar from ExternalLibrary
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import GeometricObjects.*;
import Main.*;


/**
 * Cette classe s'occupe d'afficher la carte en fonction de la position du joueur.
 * Il existe un instance de cette classe par carte.
 */
public class Map {
	
	private List<TileSet> tileSets = new ArrayList<TileSet>();
	private List<int[]> layers = new ArrayList<int[]>();
	private int actorsLayer;
	private int columns;
	private int rows;
	private int tileSize;
	private final int OFF_SCEEN_TILES = 2;
	private List<Actor> actors = new ArrayList<Actor>();
	private List<MyPolygon> polygons = new ArrayList<MyPolygon>();

	private File file;

	/**
	 * 
	 * @param filePath the path from src without '/' at first.
	 * @param player le joueur.
	 */
	public Map(String filePath, Player player) {
		this.file = new File(filePath);
		
		try {
			Gson gson = new Gson();
			Reader jsonFile = new FileReader(this.file.getPath());
			JsonElement element = gson.fromJson(jsonFile, JsonElement.class);
			
			JsonObject jsonObj = element.getAsJsonObject();
			JsonArray objects = null;
			
			this.rows = jsonObj.get("height").getAsInt();
			this.columns = jsonObj.get("width").getAsInt();
			this.tileSize = jsonObj.get("tileheight").getAsInt();
			
			JsonArray jsonArray = jsonObj.get("layers").getAsJsonArray(); 
	        if (jsonArray != null) {
	            for (int i = 0; i < jsonArray.size(); i++){
	            	JsonObject obj = jsonArray.get(i).getAsJsonObject();
	            	if (!(obj.get("type").getAsString().equals("tilelayer"))) {
						if (!(obj.get("type").getAsString().equals("objectgroup"))) continue;
						if (!(obj.get("name").getAsString().equals("Collision"))) continue;
						objects = obj.get("objects").getAsJsonArray();
					}
					else {
						int[] layer = gson.fromJson(obj.get("data"), int[].class);
						if (obj.get("name").getAsString().equals("Actor")) this.actorsLayer = i;
	            		layers.add(layer);
					}
	            }
	        }
	        
	        jsonArray = jsonObj.get("tilesets").getAsJsonArray(); 
	        if (jsonArray != null) {
	            for (int i = 0; i < jsonArray.size(); i++){
	            	JsonObject obj = jsonArray.get(i).getAsJsonObject();

					String imgPath = "/" + this.file.getParent() + File.separator + new File(obj.get("image").getAsString()).getName();
					int firstGID = obj.get("firstgid").getAsInt();
					int tileCount = obj.get("tilecount").getAsInt();
					int tileSize = obj.get("tileheight").getAsInt();
					int columns = obj.get("columns").getAsInt();

	            	BufferedImage tileSet = ImageIO.read(getClass().getResourceAsStream(imgPath));
					tileSets.add(new TileSet(tileSet, firstGID,  tileCount,  tileSize, columns));
	            }
	        }

			if (objects != null) {
				for(int i = 0; i < objects.size(); i++) {
					int startX = objects.get(i).getAsJsonObject().get("x").getAsInt();
					int startY = objects.get(i).getAsJsonObject().get("y").getAsInt();
					JsonArray polygon = objects.get(i).getAsJsonObject().get("polygon").getAsJsonArray();
					List<Point> vertices = new ArrayList<Point>();
					if (polygon == null) continue;
					for (int j = 0; j < polygon.size(); j++) {
						int x = polygon.get(j).getAsJsonObject().get("x").getAsInt();
						int y = polygon.get(j).getAsJsonObject().get("y").getAsInt();
						vertices.add(new Point((startX + x)*GameSettings.SCALE, (startY + y)*GameSettings.SCALE));
					}
					this.polygons.add(new MyPolygon(vertices.toArray(new Point[0])));
				}
			}
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
			
		player.setActorLocation(new Vector2D(1200, 1000));
		actors.add(player);

		Ennemy thomas = new Ennemy("Thomas l'obéidient créateur de ...", 10, new Statistics(100,100,100,100,100,100));
		Competences[] abilities_ennemy = {new FireBall(thomas), new FireBall(thomas), new FireBall(thomas), new FireBall(thomas)};
		thomas.setCompetences(abilities_ennemy);
		thomas.setAI(new EnnemyAI_dumb(thomas));
		thomas.setActorLocation(new Vector2D(1000, 1000));
		actors.add(thomas);

		String text = "Il fait beau aujourd'hui...";
		Npc marchand = new Speaker("marchand", 0.5f, new Vector2D(12f, 32f), new Vector2D(40f, 32f), 0.5f, text);
		marchand.setActorLocation(new Vector2D(1200, 1200));
		actors.add(marchand);
	}

	/**
     * Cette classe définit un jeu de tuiles et des méthodes permettant de facilement récupérer la bonne tuile.
     */
    class TileSet {
        private BufferedImage tileSet;
        private int firstIndex;
        private int lastIndex;
        private int tileSize;
        private int columns;

        /**
         * Initialise le jeu de tuiles (test uniquement).
         * @param tileSet
         * @param firstIndex
         * @param lastIndex
         * @param tileSize
         * @param columns
         */
        public TileSet(BufferedImage tileSet, int firstIndex, int tileCount, int tileSize, int columns) {
            this.tileSet = tileSet;
            this.firstIndex = firstIndex;
            this.lastIndex = firstIndex + tileCount - 1;
            this.tileSize = tileSize;
            this.columns = columns;
        }

		/**
		 * Vérifie que l'indice en paramètre appartient bien à ce jeu de tuiles.
		 * @param index indice à vérifier.
		 * @return vrai si l'indice appartient à ce jeu de tuiles set sinon faux.
		 */
        public boolean isValidIndex(int index) {
            return index >= this.firstIndex && index <= this.lastIndex;
        }

		/**
		 * Dessine la tuile.
		 * @param index indice de la tuile à dessiner.
		 * @param x position en x de la tuile à l'écran.
		 * @param y position en y de la tuile à l'écran.
		 * @param g "outil de dessin".
		 */
        public void drawTile(int index, int x, int y, Graphics2D g) {
			int i = index - this.firstIndex;
			int tileX = (i % this.columns) * this.tileSize;
			int tileY = (i / this.columns) * this.tileSize;

            g.drawImage(this.tileSet,
						x, y, x + this.tileSize*GameSettings.SCALE, y + this.tileSize*GameSettings.SCALE,
						tileX, tileY, tileX + this.tileSize, tileY + this.tileSize,
						null);
        }
    }

	/**
	 * Dessine la carte autour de la position (x, y) de la carte.
	 * L'origine de la carte est le coin supérieur gauche.
	 * @param g "outile de dessin".
	 * @param layer la couche à dessiner.
	 * @param x la coordonnée x
	 * @param y la coordonnée y
	 */
	public void drawLayer(Graphics2D g, int layer, int x, int y) {
		for (int i = 0; i < this.rows; i++) {
			for (int j = 0; j < this.columns; j++) {
				int index = layers.get(layer)[i*this.columns + j];
				for (TileSet t : this.tileSets) {
					if (t.isValidIndex(index)) {
						Vector2D position = GameSettings.SCREEN_CENTER.copy();
       		 			position.sub(x, y);
						position.add(j*this.tileSize*GameSettings.SCALE, i*this.tileSize*GameSettings.SCALE);

						if (position.x >= (0 - (OFF_SCEEN_TILES*this.tileSize*GameSettings.SCALE)) &&
								position.x <= (GameSettings.SCREEN_WIDTH + (OFF_SCEEN_TILES*this.tileSize*GameSettings.SCALE)) &&
								position.y >= (0 - (OFF_SCEEN_TILES*this.tileSize*GameSettings.SCALE)) &&
								position.y <= (GameSettings.SCREEN_HEIGHT + (OFF_SCEEN_TILES*this.tileSize*GameSettings.SCALE))) {
							t.drawTile(index, (int) position.x, (int) position.y, g);
						}
						
						break;
					}
				}
			}
		}
	}

	public List<Actor> getActors() {
		return this.actors;
	}

	/**
     * Permet de dessiner à l'écran et appel la méthode draw des acteurs afin de les dessiner si nécessaire.
     * Appeler après un tick.
     * @param g "outil de dessin"
     */
    public void draw(Graphics2D g) {
		for (int i = 0; i < this.layers.size(); i++) {
			this.drawLayer(g, i, (int) App.getPlayerController(0).getActorLocation().x, (int) App.getPlayerController(0).getActorLocation().y);

			if (i == this.actorsLayer) {
				Collections.sort(actors, (a1, a2) -> {
					return (int) (a1.getActorLocation().y - a2.getActorLocation().y);
				});

				List<Actor> actorsCopy = new ArrayList<>(actors);
		
				for(Actor a: actorsCopy) {
					if (a.canDraw) a.draw(g);
				}
			}
		}
    }

    /**
     * Permet de mettre à jour les acteurs et si nécessaire.
     * @param deltaTime le temps entre 2 images successives.
     */
    public void tick(float deltaTime) {
    	List<Actor> actorsCopy = new ArrayList<>(actors);
    	
        for(Actor a: actors) {
            if (a.canTick) a.tick(deltaTime);
        }
    }

	/**
	 * Récupère les polygons de la carte afin de tester des collisions.
	 * @return	les polygons de la carte.
	 */
	public List<MyPolygon> getCollisions() {
		return this.polygons;
	}

	/**
	 * Supprime l'acteur a de la liste des acteurs afin de ne plus l'afficher.
	 * @param a acteur à supprimer.
	 */
	public void removeActor(Actor a) {
		if (!this.actors.contains(a)) return;

		this.actors.remove(a);
	}
}