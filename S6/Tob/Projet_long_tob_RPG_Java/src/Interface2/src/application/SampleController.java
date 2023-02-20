
package application;

import java.awt.event.MouseEvent;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;

public class SampleController implements Initializable{

    @FXML
    private ChoiceBox<String> joueur;

    @FXML
    private Button menu;

    @FXML
    void getJoueur(MouseEvent event) {
    	String j = joueur.getSelectionModel().getSelectedItem();
    	System.out.println(j);

    }

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		joueur.getItems().addAll("Women1", "Women2", "Man1", "Man2");
		
	}

}
