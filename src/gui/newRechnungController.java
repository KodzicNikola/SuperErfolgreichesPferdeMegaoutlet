package gui;

import domain.Artikel;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import service.DataManagementService;

import java.sql.Timestamp;
import java.util.ArrayList;

public class newRechnungController {

    private ArrayList<Artikel> eintraege;

    @FXML
    private TableView<Artikel> artikelTable;
    @FXML
    private TableView<Artikel> eintragsTable;

    @FXML
    private TableColumn<Artikel, String> artikelNameColumn;
    @FXML
    private TableColumn<Artikel, Float> artikelPreisColumn;
    @FXML
    private TableColumn<Artikel, Integer> artikelStueckzahlColumn;

    @FXML
    private TableColumn<Artikel, String> eintragsNamenColumn;
    @FXML
    private TableColumn<Artikel, Float> eintragsPreisColumn;
    @FXML
    private TableColumn<Artikel, Integer> eintragsStueckzahlColumn;

    @FXML
    private TextField stueckzahlField;
    @FXML
    private TextField datumField;
    @FXML
    private Label summeLabel;

    @FXML
    private Button addButton;
    @FXML
    private Button removeButton;
    @FXML
    private Button datumButton;
    @FXML
    private Button speichernButton;
    @FXML
    private Button abbrechenButton;


    public void initialize(){
        eintraege = new ArrayList<>();
        stueckzahlField.setText("0");

        artikelTable.setItems(FXCollections.observableArrayList(DataManagementService.readAllArtikel()));
        artikelTable.setVisible(false);
        artikelTable.setVisible(true);

        artikelNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
        artikelPreisColumn.setCellValueFactory(cellData -> new SimpleFloatProperty(cellData.getValue().getPreis()).asObject());
        artikelStueckzahlColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getStueckzahl()).asObject());

        eintragsTable.setItems(FXCollections.observableArrayList(eintraege));
        eintragsNamenColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
        eintragsPreisColumn.setCellValueFactory(cellData -> new SimpleFloatProperty(cellData.getValue().getPreis()).asObject());
        eintragsStueckzahlColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getStueckzahl()).asObject());

        speichernButton.setDisable(true);
    }

    /**
     * Entfernt oder fügt Einträge einer neuen Rechnung hinzu
     * @param add   Falls true, wird die Anzahl an Artikeln hinzugefügt, sonst entfernt
     */
    public void handleAddRemove(boolean add){
        int stueck = 0;

        try{

            try {
                stueck = Integer.parseInt(stueckzahlField.getText());
            } catch (Exception e){
                throw new IllegalArgumentException("Es kann nur eine ganze, positive Zahl übertragen werden.");
            }
            if(stueck<=0){
                throw new IllegalArgumentException("Es kann nur eine positive Zahl übertragen werden.");
            }



            Artikel a = artikelTable.getSelectionModel().getSelectedItem();
            if(a == null && add){
                throw new IllegalArgumentException("Ein Artikel muss ausgewählt sein");
            }

            Artikel b= eintragsTable.getSelectionModel().getSelectedItem();
            if(b == null && !add){
                throw new IllegalArgumentException("Ein Artikel muss ausgewählt sein");
            }

            if(add && a.getStueckzahl() < stueck){
                throw new IllegalArgumentException("Es sind nicht genug "+a.getName()+" vorrätig.");
            }

            Artikel eintrag = null;
            Artikel neuerArtikel;

            if(add){


                for(Artikel e: eintraege){
                    if(e.getOriginalId() == a.getId()){
                        eintrag = e;
                    }
                }


                if(eintrag != null){
                    if(eintrag.getStueckzahl()+stueck > a.getStueckzahl()){
                        throw new IllegalArgumentException("Es sind nicht genug "+a.getName()+" vorrätig.");
                    }
                    neuerArtikel = new Artikel(eintrag.getId(),eintrag.getName(), eintrag.getPreis(),eintrag.getStueckzahl()+stueck,eintrag.getBildadresse(), true);
                    eintraege.remove(eintrag);
                } else {
                    neuerArtikel = new Artikel(a.getId(),a.getName(),a.getPreis(),stueck,a.getBildadresse(),true);
                }
                eintraege.add(neuerArtikel);

            } else {
                for (Artikel e : eintraege){
                    if(b.getOriginalId() == e.getOriginalId()){
                        eintrag = e;
                    }
                }
                if(eintrag != null){
                    if(eintrag.getStueckzahl() - stueck > 0){
                        neuerArtikel = new Artikel(eintrag.getOriginalId(),eintrag.getName(),eintrag.getPreis(),
                                eintrag.getStueckzahl()-stueck,eintrag.getBildadresse(),true);
                        eintraege.add(neuerArtikel);
                    }
                    eintraege.remove(eintrag);
                }
            }

            eintragsTable.setItems(FXCollections.observableArrayList(eintraege));
            eintragsTable.setVisible(false);
            eintragsTable.setVisible(true);

            float summe = 0;
            for(Artikel e : eintraege){
                summe += e.getPreis()*e.getStueckzahl();
            }
            summeLabel.setText(summe+"");
            speichernButton.setDisable(eintraege.size()==0);

        } catch (IllegalArgumentException e){
            stueckzahlField.setText("0");
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle(e.getMessage());
            alert.setHeaderText(null);
            alert.setContentText(e.getMessage());
            alert.show();
        }
    }

    /**
     * Triggert das Hinzufügen von Artikeln zur Rechnung
     */
    public void handleAddButton(){
        handleAddRemove(true);
    }

    /**
     * Triggert das Entfernen von Artikeln von der Rechnung
     */
    public void handleRemoveButton(){
        handleAddRemove(false);
    }

    /**
     * Fügt den aktuellen Zeitpunkt für die Rechnung hinzu.
     */
    public void handleDatumButton(){
        datumField.setText((new Timestamp(System.currentTimeMillis())).toString());
    }

    public void handleSpeichernButton(){
        try {
            DataManagementService.createNewRechnung(eintraege,datumField.getText());

            handleAbbrechenButton();
        } catch (Exception e){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle(e.getMessage());
            alert.setHeaderText(null);
            alert.setContentText(e.getMessage());
            alert.show();
        }
    }

    /**
     * Schließt das Fenster
     */
    public void handleAbbrechenButton(){
        ((Stage) (abbrechenButton.getScene().getWindow())).close();
    }
}
