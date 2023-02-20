package Core;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * Interface entre un humain et le pion qu'il contrôle.
 */
public abstract class PlayerController extends Controller implements KeyListener {

    @Override
    public void keyPressed(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public String toString() {
        return "PlayerController";
    }
}
