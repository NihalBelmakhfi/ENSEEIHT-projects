package Interface1.src.application;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;

public class SecondaryController implements Initializable{

    @FXML
    private ChoiceBox<String> joueur;

    @FXML
    private Button menu;
    
    @FXML
    private void Demarrer() {
		
    }

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		joueur.getItems().addAll("Women1", "Women2", "Man1", "Man2");
		joueur.setValue("Woman1");
	}

}
