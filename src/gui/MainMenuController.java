package gui;

import domain.Artikel;

import domain.Rechnung;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ValueAxis;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import service.DataManagementService;

import java.util.Optional;


public class MainMenuController {

    private static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(MainMenuController.class);

    /**
     *  TAB: Artikel
     */
    @FXML
    private TableView<Artikel> artikelTable;

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
    private TextField artikelNameField;
    @FXML
    private TextField artikelPreisField;
    @FXML
    private TextField artikelVerfuegbarField;

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
    private TableView<Rechnung> rechnungenTable;
    @FXML
    private TableView<Artikel> rechnungsEintragsTable;

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

    /**
     * TAB: Statistik
     */
    @FXML
    private TableView<Artikel> artikelStatistikTable;
    @FXML
    private TableColumn<Artikel,String> artikelStatistikColumn;
    @FXML
    private CheckBox allegewaehltBox;
    @FXML
    private LineChart<Integer, Integer> artikelChart;
    @FXML
    private NumberAxis verkaufsAxis;
    @FXML
    private CategoryAxis tageAxis;


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

        showArtikelDetails(null);

        artikelTable.setItems(FXCollections.observableArrayList(DataManagementService.readAllArtikel()));
        artikelTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> showArtikelDetails(newValue));

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

        showRechnungsDetails(null);

        rechnungenTable.setItems(FXCollections.observableArrayList(DataManagementService.readAllRechungen()));
        rechnungenTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> showRechnungsDetails(newValue));

    }


    /**
     * Zeigt die Details eines ausgwewählten Artikels
     * @param a     Artikel, der angezeigt werden soll, null leert die Anzeige
     */
    private void showArtikelDetails(Artikel a) {
        if (a==null){
            artikelImage.setVisible(false);
            artikelImageLabel.setText("Kein Artikel ausgewählt");
            artikelIdLabel.setText("");
            artikelNameField.setText("");
            artikelPreisField.setText("");
            artikelVerfuegbarField.setText("");
            artikelSpeichernButton.setDisable(true);
            artikelLoeschenButton.setDisable(true);
        } else {
            try{
                Image image = new Image(a.getBildadresse());
                artikelImage.setImage(image);
            } catch (Exception e) {
                if(!a.getBildadresse().equals("")){
                    logger.info("Konnte Artikelbild nicht finden");
                }
            }
            artikelImage.setVisible(true);
            artikelImageLabel.setText(a.getBildadresse());
            artikelIdLabel.setText(a.getId()+"");
            artikelNameField.setText(a.getName());
            artikelPreisField.setText(a.getPreis()+"");
            artikelVerfuegbarField.setText(a.getStueckzahl()+"");
            artikelSpeichernButton.setDisable(false);
            artikelLoeschenButton.setDisable(false);
        }

    }

    /**
     * Zeigt die Einträge einer ausgewählten Rechnung
     * @param r  Die zu zeigende Rechnung
     */
    private void showRechnungsDetails(Rechnung r) {
        if (r==null){
            rechnungsEintragsTable.setItems(FXCollections.observableArrayList());
            rechnungsEintragsTable.setVisible(false);
            rechnungsEintragsTable.setVisible(true);
        } else {
            rechnungsEintragsTable.setItems(FXCollections.observableArrayList(r.getArtikelListe()));
            rechnungsEintragsTable.setVisible(false);
            rechnungsEintragsTable.setVisible(true);
        }
    }

    /**
     * Öffnet ein Fenster um einen neuen Artikel zu erstellen
     */
    public void handleNewArtikel() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("newArtikel.fxml"));

            Stage secondaryStage = new Stage();


            secondaryStage.initModality(Modality.WINDOW_MODAL);
            secondaryStage.initOwner(artikelTable.getScene().getWindow());

            secondaryStage.setTitle("Neuer Artikel");
            secondaryStage.setScene(new Scene(root, 327, 391));
            secondaryStage.show();

            artikelTable.setVisible(false);
            artikelTable.setItems(FXCollections.observableArrayList(DataManagementService.readAllArtikel()));
            artikelTable.setVisible(true);
        } catch (Exception e){
            logger.error("Menü für neue Artikel konnte nicht geöffnet werden.");
        }
    }

    /**
     * Löscht einen Artikel aus der Tabelle
     */
    public void handleDeleteArtikel(){
        Artikel a = artikelTable.getSelectionModel().getSelectedItem();

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Artikel löschen");
        alert.setHeaderText(null);
        alert.setContentText("Sind Sie sicher, dass Sie den Artikel " + a.getName() + " unwiderruflich löschen wollen?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
            int selectedIndex = artikelTable.getSelectionModel().getSelectedIndex();

            if(DataManagementService.delete(a)){
                artikelTable.getItems().remove(selectedIndex);
                artikelTable.setVisible(false);
                artikelTable.setVisible(true);

            }
        }

    }

    /**
     * Speichert die im Menü gemachten änderungen
     */
    public void handleArtikelSpeichern(){
        String name;
        String bildadresse = artikelTable.getSelectionModel().getSelectedItem().getBildadresse();
        String preis;
        String stueckzahl;
        long id = artikelTable.getSelectionModel().getSelectedItem().getId();

        if(artikelNameField.getText().equals("")){
            name = artikelTable.getSelectionModel().getSelectedItem().getName();
        } else {
            name = artikelNameField.getText();
        }
        if(artikelPreisField.getText().equals("")){
            preis = artikelTable.getSelectionModel().getSelectedItem().getPreis() + "";
        } else {
            preis = artikelPreisField.getText();
        }
        if(artikelVerfuegbarField.getText().equals("")){
            stueckzahl = artikelTable.getSelectionModel().getSelectedItem().getStueckzahl() + "";
        } else {
            stueckzahl = artikelVerfuegbarField.getText();
        }

        try{
            DataManagementService.updateArtikel(id, name, bildadresse, preis, stueckzahl);

        } catch(Exception e){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Speichern fehlgeschlagen");
            alert.setHeaderText(null);
            alert.setContentText(e.getMessage());
        }

        artikelTable.setItems(FXCollections.observableArrayList(DataManagementService.readAllArtikel()));
        artikelTable.setVisible(false);
        artikelTable.setVisible(true);
    }

    /**
     * Öffnet ein Fenster um neue Rechnungen zu erstellen
     */
    public void handleNewRechnung(){
        try {
            Parent root = FXMLLoader.load(getClass().getResource("newRechnung.fxml"));

            Stage secondaryStage = new Stage();


            secondaryStage.initModality(Modality.WINDOW_MODAL);
            secondaryStage.initOwner(artikelTable.getScene().getWindow());

            secondaryStage.setTitle("Neue Rechnung");
            secondaryStage.setScene(new Scene(root, 713, 422f));
            secondaryStage.showAndWait();

            rechnungenTable.setVisible(false);
            rechnungenTable.setItems(FXCollections.observableArrayList(DataManagementService.readAllRechungen()));
            rechnungenTable.setVisible(true);
        } catch (Exception e){
            logger.error("Konnte Menü für neue Rechnung nicht öffnen.");
        }
    }

    public void handleDeleteRechnungen(){
        Rechnung r = rechnungenTable.getSelectionModel().getSelectedItem();

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Rechnung löschen");
        alert.setHeaderText(null);
        alert.setContentText("Sind Sie sicher, dass Sie diese Rechnung unwiderruflich löschen wollen?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
            int selectedIndex = rechnungenTable.getSelectionModel().getSelectedIndex();

            if(DataManagementService.delete(r)){
                rechnungenTable.getItems().remove(selectedIndex);
                rechnungenTable.setVisible(false);
                rechnungenTable.setVisible(true);

            }
        }
    }

}
