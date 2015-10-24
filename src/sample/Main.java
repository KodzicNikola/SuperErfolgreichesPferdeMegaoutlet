package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("MainMenu.fxml"));
        primaryStage.setTitle("Super Erfolgreiches Pferde Megaoutlet");
        primaryStage.setScene(new Scene(root, 700, 460));
        primaryStage.show();
    }


    private static Logger log = Logger.getLogger(Main.class);
    public static void main(String[] args) {
        BasicConfigurator.configure();
        log.debug("test");
        launch(args);
    }
}
