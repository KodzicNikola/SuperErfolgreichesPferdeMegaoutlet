package gui;

import domain.Artikel;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;


public class MainMenuController {

    /**
     *  TAB: Artikel
     */
    @FXML
    private TableColumn<Artikel,String> artikelNameColumn;
    @FXML
    private TableColumn<Artikel,Integer> artikelStueckzahlColumn;
    @FXML
    private TableColumn<Artikel,Float> artikelPreisColumn;

    @FXML
    private ImageView artikelImage;

    @FXML
    private Label artikelIdLabel;
    @FXML
    private Label artikelImageLabel;

    @FXML
    private TextField artikelName;
    @FXML
    private TextField artikelPreis;
    @FXML
    private TextField artikelVerfuegbar;

    @FXML
    private Button artikelImageButton;
    @FXML
    private Button artikelSpeichernButton;
    @FXML
    private Button artikelNeuButton;


    /**
     * Initialisiert die Klasse
     */
    @FXML
    private void initialize() {
    }

}
