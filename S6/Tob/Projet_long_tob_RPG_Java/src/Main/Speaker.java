package Main;

import java.awt.Color;

import javax.swing.JLayeredPane;

import GeometricObjects.*;
import UserInterface.DialogueWindow;

public class Speaker extends Npc {
	private DialogueWindow dialogueWindow = new DialogueWindow(250, 550, 780, 75, Color.WHITE);

	public Speaker(String animSet, float animScale, Vector2D hitboxPosition, Vector2D hitboxSize, float hitboxScale, String dialogue) {
		super(animSet, animScale, hitboxPosition, hitboxSize, hitboxScale);
		this.dialogueWindow.changeText(dialogue);
	}

	public void talk() {	
		Main.getMainWindow().getLayeredPane().add(dialogueWindow, JLayeredPane.POPUP_LAYER, 0);
	}

	@Override
	public void receiveEvent(Object o) {
		if (o == this.hitbox) {
			if (!this.hitbox.isSelected()) return;
			talk();
		}
	}

}
