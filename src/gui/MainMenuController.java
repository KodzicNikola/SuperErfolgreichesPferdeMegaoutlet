package gui;

import domain.Artikel;

import domain.Rechnung;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import service.DataManagementService;


public class MainMenuController {

    /**
     *  TAB: Artikel
     */
    @FXML
    private TableView artikelTable;

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
    private Button artikelLoeschenButton;
    @FXML
    private Button artikelNeuButton;

    /**
     * TAB: Rechnungen
     */
    @FXML
    private TableView rechnungenTable;

    @FXML
    private TableColumn<Rechnung, Integer> rechnungsIdColumn;
    @FXML
    private TableColumn<Rechnung, String> rechnungsDatumColumn;
    @FXML
    private TableColumn<Rechnung, Float> rechnungsSummeColumn;

    @FXML
    private TableColumn<Artikel, String> artikelNameEintragColumn;
    @FXML
    private TableColumn<Artikel, Float> artikelPreisEintragColumn;
    @FXML
    private TableColumn<Artikel, Integer> artikelStueckEintragColumn;
    @FXML
    private TableColumn<Artikel, Float> artikelSummeEintragColumn;

    @FXML
    private Button rechnungNeuButton;
    @FXML
    private Button rechnungDeleteButton;


    public MainMenuController(){

    }

    /**
     * Initialisiert die Klasse
     */
    @FXML
    private void initialize() {

        //Vorbereitung der Artikeltabelle
        artikelNameColumn.setCellValueFactory(cellData
                -> new SimpleStringProperty(cellData.getValue().getName()));
        artikelStueckzahlColumn.setCellValueFactory(cellData
                -> new SimpleIntegerProperty(cellData.getValue().getStueckzahl()).asObject());
        artikelPreisColumn.setCellValueFactory(cellData
                -> new SimpleFloatProperty((cellData.getValue().getPreis())).asObject());

        artikelTable.setItems(FXCollections.observableArrayList(DataManagementService.readAllArtikel()));

        //Vorbereitung der Rechnungstabellen
        rechnungsIdColumn.setCellValueFactory(cellData
                -> new SimpleIntegerProperty(cellData.getValue().getId()).asObject());
        rechnungsDatumColumn.setCellValueFactory(cellData
                -> new SimpleStringProperty(cellData.getValue().getDate().toString()));
        rechnungsSummeColumn.setCellValueFactory(cellData
                -> new SimpleFloatProperty(cellData.getValue().getSumme()).asObject());
        artikelNameEintragColumn.setCellValueFactory(cellData
                -> new SimpleStringProperty(cellData.getValue().getName()));
        artikelPreisEintragColumn.setCellValueFactory(cellData
                -> new SimpleFloatProperty((cellData.getValue().getPreis())).asObject());
        artikelStueckEintragColumn.setCellValueFactory(cellData
                -> new SimpleIntegerProperty(cellData.getValue().getStueckzahl()).asObject());
        artikelSummeEintragColumn.setCellValueFactory(cellData
                -> new SimpleFloatProperty(cellData.getValue().getPreis()*cellData.getValue().getStueckzahl()).asObject());

        rechnungenTable.setItems(FXCollections.observableArrayList(DataManagementService.readAllRechungen()));

        System.out.println(((Artikel) artikelTable.getItems().get(0)).getOriginalId());
    }

}
