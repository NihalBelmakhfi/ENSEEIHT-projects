package Interface1.src.application;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;

import java.awt.event.MouseEvent;
import java.io.IOException;

public class PrimaryController implements Initializable {

    @FXML
    private ChoiceBox<String> avancement;

	@FXML
    private void switchToSecondary() throws IOException {
		String avan = avancement.getSelectionModel().getSelectedItem();
		switch(avan) {
		case "continuer":
			System.out.println("Il n'y a pas de sauvegarde");
			break;
		case "nouvelle partie":
			Main.setRoot("secondary");
			break;
		}   
    }

	@FXML
    private void quit() throws IOException {
        System.exit(0);
    }

	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {
		avancement.getItems().addAll("continuer", "nouvelle partie");
		avancement.setValue("nouvelle partie");
	}

}

