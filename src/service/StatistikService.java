package service;

import domain.Artikel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.TextField;
import org.apache.log4j.Logger;
import persistance.RechnungsDAO;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;

public class StatistikService {

    private static Logger logger = Logger.getLogger(DataManagementService.class);


    /**
     * Gibt eine Liste der letzten n Tage zurück, wobei n erst als String bergeben wird
     * @param sTage     Zahl der Tage
     * @return          ArrayList mit den geordneten, gesuchten Tagen
     */
    public static ArrayList<Timestamp> createZeitraum(String sTage){
        int tage;
        try {
            tage = Integer.parseInt(sTage);
        } catch (IllegalArgumentException i){
            logger.info("Keine Zahl im Zeitraum angegeben.");
            throw new IllegalArgumentException("Zeitraum muss eine ganze, positive Zahl sein");
        }

        ArrayList<Timestamp> result = new ArrayList<>();

        for(int i = tage-1; i >= 0; i--){

            result.add(new Timestamp(todayMinus(i)));

        }

        return result;
    }

    /**
     * Berechnet die Millisekunden des heutigen Tages minus days Tage
     * @param days  Anzahl der Tage, die abgezogen werden sollen
     * @return      gesuchte Millisekunden
     */
    private static long todayMinus(int days) {
        long now = System.currentTimeMillis();
        long today = now - (now % 86400000);
        return today - days*86400000;
    }

    public static void createStatistik(LineChart<String, Integer> artikelChart, CategoryAxis tageAxis, NumberAxis verkaufsAxis, Artikel a, String zeitraum) {

        try {
            ArrayList<Timestamp> xAchse = createZeitraum(zeitraum);
            System.out.println(xAchse.toString());
            ObservableList<XYChart.Data<String,Integer>> data = FXCollections.observableArrayList(new ArrayList<>());

            XYChart.Series<String,Integer> series = new XYChart.Series<>();
            series.setName("Verkaufte Artikel");

            ArrayList<String> tage = new ArrayList<>();
            for(Timestamp t : xAchse){
                tage.add(t.toString().substring(0,10));
            }
            Collections.reverse(tage);
            tageAxis.setCategories((FXCollections.observableArrayList(tage));

            for(Timestamp t : xAchse){
                series.getData().add(new XYChart.Data<>(t.toString().substring(0,10),countArtikelSince(t,a)));
            }

            artikelChart.getData().add(series);
            artikelChart.setVisible(false);
            artikelChart.setVisible(true);
        } catch (Exception e){
            logger.error("Konnte Statistik nicht befüllen");
        }

    }

    private static int countArtikelSince(Timestamp t, Artikel a) {
        RechnungsDAO rao = new RechnungsDAO();
        if(a == null){
            return rao.getVerkaufteArtikelSince(t);
        }
        return 0;
    }
}
