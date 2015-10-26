package service;

import domain.Artikel;
import domain.Domain;
import domain.Rechnung;
import org.apache.log4j.Logger;
import persistance.ArtikelDAO;
import persistance.RechnungsDAO;

import java.sql.SQLException;
import java.util.ArrayList;

public class DataManagementService {

    private static Logger logger = Logger.getLogger(DataManagementService.class);
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

    public static void updateArtikel(long id, String name, String bildadresse, String sPreis, String sStueckzahl) throws SQLException {
        float preis;
        int stueckzahl;
        try {
            preis = Float.parseFloat(sPreis);
            if(preis < 0){
                throw new IllegalArgumentException();
            }
        } catch (Exception e){
            throw new IllegalArgumentException("Der Preis muss als positive Zahl mit '.' als Dezimalzeich angegeben sein.");
        }
        preis *= 100;
        Math.round(preis);//TODO richten!
        preis *= 0.01;

        try {
            stueckzahl = Integer.parseInt(sStueckzahl);
            if(stueckzahl<0){
                throw new IllegalAccessError();
            }
        } catch (Exception e){
            throw new IllegalArgumentException("Die Stückzahl darf nur als ganze, positve Zahl angegeben werden.");
        }
        boolean success = new ArtikelDAO().update(new Artikel(id,-1,name,preis,stueckzahl,bildadresse,false));
        System.out.println(success);
        if(!success){
            logger.error("Update eines Artikels fehlgeschlagen!");
            throw new SQLException("Update fehlgeschlagen.");
        }
    }


    public static void createNewArtikel(String name, String bildadresse, String sPreis, String sStueckzahl) throws SQLException {
        float preis;
        int stueckzahl;
        if(name.length() == 0){
            throw new IllegalArgumentException("Ein Artikel muss einen Namen enthalten.");
        }

        try {
            preis = Float.parseFloat(sPreis);
            if(preis < 0) {
                throw new IllegalArgumentException();
            }
        } catch (Exception e){
            throw new IllegalArgumentException("Der Preis muss als positive Zahl mit '.' als Dezimalzeich angegeben sein.");
        }
        preis *= 100;
        Math.round(preis);
        preis *= 0.01;

        try {
            stueckzahl = Integer.parseInt(sStueckzahl);
            if(stueckzahl<0){
                throw new IllegalAccessError();
            }
        } catch (Exception e){
            throw new IllegalArgumentException("Die Stückzahl darf nur als ganze, positive Zahl angegeben werden.");
        }
        boolean success = new ArtikelDAO().create(new Artikel(name,preis,stueckzahl,bildadresse,false));
        System.out.println(success);
        if(!success){
            logger.error("Erstellen eines Artikels fehlgeschlagen!");
            throw new SQLException("Erstellen fehlgeschlagen.");
        }
    }
}
