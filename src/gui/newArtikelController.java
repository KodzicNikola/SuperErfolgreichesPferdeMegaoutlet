package gui;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import service.DataManagementService;

public class newArtikelController {

    @FXML
    private ImageView image;

    @FXML
    private Label imageLabel;

    @FXML
    private TextField nameField;
    @FXML
    private TextField verfuegbarField;
    @FXML
    private TextField preisField;

    public void handleSpeichernButton(){
        String name = nameField.getText();
        String bildadresse = imageLabel.getText();
        String preis = preisField.getText();
        String stueckzahl = verfuegbarField.getText();

        try{
            DataManagementService.createNewArtikel(name, bildadresse, preis, stueckzahl);
            //TODO return
        } catch(Exception e){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Erstellen fehlgeschlagen");
            alert.setHeaderText(null);
            alert.setContentText(e.getMessage());
        }

    }


    public void handleAbbrechenButton(){
        //TODO
        return;
    }
}
