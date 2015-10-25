package service;

import domain.Artikel;
import domain.Domain;
import domain.Rechnung;
import persistance.ArtikelDAO;
import persistance.RechnungsDAO;

import java.util.ArrayList;

public class DataManagementService {

    /**
     * Lädt die Liste aller verfügbaren Artikel aus der Datenbank
     * @return  Liste aller Artikel aus der Datenbank
     */
    public static ArrayList<Artikel> readAllArtikel(){
        return new ArtikelDAO().read();
    }

    /**
     * Lädt die Liste aller verfügbaren Rechnungen aus der Datenbank
     * @return  Liste aller Rechnungen aus der Datenbank
     */
    public static ArrayList<Rechnung> readAllRechungen(){
        return new RechnungsDAO().read();
    }

    /**
     * Löscht einen Domain aus der Datenbank
     * @param d     Zu löschende Domain
     * @return      True, falls sich nach dem Löschvorgang d nicht mehr in der Datenbank befindet
     */
    public static boolean delete(Domain d){
        if(d instanceof Artikel){
            return new ArtikelDAO().delete(d);
        } else {
            return new RechnungsDAO().delete(d);
        }
    }

}
