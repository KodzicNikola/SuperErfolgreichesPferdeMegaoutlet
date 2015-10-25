package gui;

import domain.Artikel;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import persistance.ArtikelDAO;
import persistance.RechnungsDAO;

public class MainMenu extends Application {

    public static void main(String[] args) {

        new ArtikelDAO().createTable();
        new RechnungsDAO().createTable();
        Artikel a = new Artikel("testArtikel", new Float(0.99), 50, "", false);
        System.out.println(a.getId());
        System.out.println(new ArtikelDAO().create(a));

        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("Menu.fxml"));
        primaryStage.setTitle("Super Erfolgreiches Pferde Megaoutlet");
        primaryStage.setScene(new Scene(root, 700, 460));
        primaryStage.show();
    }
}
