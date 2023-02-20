package Core;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;

import Audio.AudioPlayer;

public abstract class App extends JFrame implements Runnable {
	
	public static AudioPlayer audioPlayer = new AudioPlayer();
	protected static Map currentMap;
	protected static List<PlayerController> controllerList = new ArrayList<PlayerController>();
    protected static JPanel currentPanel;
	
    protected Thread gameThread;
    protected Thread loadingThread;

    public App(PlayerController newPlayerController, String title) {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setTitle(title);
        this.setBackground(Color.BLACK);
        this.setSize(GameSettings.SCREEN_WIDTH, GameSettings.SCREEN_HEIGHT + 24);
        this.setLocationRelativeTo(null);
        this.setFocusable(true);

        controllerList.add(newPlayerController);
        this.addKeyListener(newPlayerController);
    }

    public void startThread() {
        this.gameThread = new Thread(this);
        this.gameThread.start();
    }

    @Override
    public void run() {
        long lastTime = System.nanoTime();

        while (gameThread != null) {
            long currentTime = System.nanoTime();
            long updateLength = currentTime - lastTime;

            if (updateLength < GameSettings.OPTIMAL_TIME) {
                continue;
            }

            lastTime = currentTime;
            float deltaTime = (float) updateLength/GameSettings.OPTIMAL_TIME;

            update(deltaTime);
            this.getLayeredPane().paintImmediately(this.getLayeredPane().getBounds());
        }
    }

    protected void update(float deltaTime) {
    	if (currentMap != null) currentMap.tick(deltaTime);
    }
    
    public static Map getCurrentMap() {
    	return currentMap;
    }
    
    public static PlayerController getPlayerController(int id) {
    	return controllerList.get(id);
    }
}
