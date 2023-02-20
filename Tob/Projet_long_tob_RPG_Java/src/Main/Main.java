package Main;

public class Main {

    private static MyApp mainWindow;
    public static void main(String[] args) throws Exception {
        mainWindow = new MyApp(new MyPlayerController(), "2D Game");
        mainWindow.startThread();
    }

    public static MyApp getMainWindow() {
        return mainWindow;
    }
}
