package Main;

import Core.App;
import Core.Map;
import Core.PlayerController;
import UserInterface.MainMenuWindow;

public class MyApp extends App{

    private GameState gameState = GameState.MAIN_MENU;
    private MainMenuWindow mainMenu;
    private GamePanel gamePanel;
    private BattleMode battleMode;

    public MyApp(PlayerController newPlayerController, String title) {
        super(newPlayerController, title);
        this.mainMenu = new MainMenuWindow();
        this.add(this.mainMenu);
        this.setVisible(true);
    }

    public void setGameState(GameState newState) {
        this.getContentPane().removeAll();

        switch(newState) {
        case MAIN_MENU:
            break;
        case WORLD:
            this.getContentPane().add(this.gamePanel);
            break;
        case BATTLE:
            this.getContentPane().add(this.battleMode.getBattlePanel());
            break;
        }

        this.gameState = newState;
        this.setVisible(true);
    }

    public GameState getGameState() {
        return this.gameState;
    }

    public MainMenuWindow getMainMenu() {
        return this.mainMenu;
    }

    public void setGamePanel() {
        this.gamePanel = new GamePanel();
        currentPanel = this.gamePanel;
        audioPlayer.playMusic("Music.wav");

        Player newPlayer = new Player("Théo, le mage noir corrompu", 10, new Statistics(100,100,100,100,100,100), "man2", 9);
        Competences[] abilities_player = {new FireBall(newPlayer), new Zap(newPlayer), new Lifesteal(newPlayer), new ArmorShred(newPlayer)};
        newPlayer.setCompetences(abilities_player);

        Map newMap = new Map("Assets/Maps/map.tmj", newPlayer);
        currentMap = newMap;

        controllerList.get(0).possess(newPlayer);
    }

    public void startBattle(Ennemy ennemy) {
    	audioPlayer.playSound("hitmonster.wav");
        this.battleMode = new BattleMode((Fighter) controllerList.get(0).getPossessedPawn(), ennemy);
    }

    public GamePanel getGamePanel() {
        return this.gamePanel;
    }
    
    @Override
    public void update(float deltaTime) {
        switch(this.gameState) {
        case MAIN_MENU:
            break;
        case WORLD:
            if (currentMap != null) currentMap.tick(deltaTime);
            break;
        case BATTLE:
            this.battleMode.tick(deltaTime);
            break;
        }
    }
}
