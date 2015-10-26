package gui;

import domain.Artikel;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import service.DataManagementService;

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
    private Button addButton;
    @FXML
    private Button removeButton;
    @FXML
    private Button datumButton;
    @FXML
    private Button speichernButton;
    @FXML
    private Button abbrechenButton;

    public newRechnungController(){
        eintraege = new ArrayList<>();
    }

    public void handleAddButton(){
        //TODO
    }

    public void handleRemoveButton(){
        //TODO
    }

    public void handleDatumButton(){
        //TODO
    }

    public void handleSpeichernButton(){
        //TODO

    }


    public void handleAbbrechenButton(){
        //TODO
        return;
    }
}
