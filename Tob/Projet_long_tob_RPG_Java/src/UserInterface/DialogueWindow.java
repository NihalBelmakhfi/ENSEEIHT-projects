package UserInterface;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JLabel;

public class DialogueWindow extends Window {

    private JLabel textLabel = new JLabel();

    public DialogueWindow(int x, int y, int width, int height, Color backgroundColor) {
        super(x, y, width, height, 5, backgroundColor);
        this.add(textLabel, BorderLayout.CENTER);
    }

    public void changeText(String text) {
        this.textLabel.setText(text);
    }
    
}
