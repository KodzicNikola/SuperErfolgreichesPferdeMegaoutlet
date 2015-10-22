package domain;

import java.nio.file.attribute.UserDefinedFileAttributeView;
import java.sql.Timestamp;
import java.util.ArrayList;

public class Rechnung implements Domain{

    /**
     * Eindeutige ID einer Rechnung
     */
    int id;

    /**
     * Datum der Rechnung, fungiert als lesbare Bezeichnung
     */
    Timestamp date;

    /**
     * Liste der Artikel auf der Rechnung
     */
    ArrayList<Artikel> artikelListe;

    /**
     * Konstruktor für die Rechnung
     * @param id            Eindeutige ID der Rechnung
     * @param date          Zeitpunkt, an dem die Rechnung erstellt wurde
     * @param artikelListe  Liste der Artikel auf der Rechnung
     */
    public Rechnung(int id, Timestamp date, ArrayList<Artikel> artikelListe){
        this.id = id;
        this.date = date;
        this.artikelListe = artikelListe;
    }

    /**
     * Konstruktor für die Rechnung mit lehrer Artikelliste
     * @param id    Eindeutige ID der Rechnung
     * @param date  Zetpunkt, an dem die Rechnung erstellt wurde
     */
    public Rechnung(int id, Timestamp date){
        this.id = id;
        this.date = date;
        artikelListe = new ArrayList<Artikel>();
    }

    //TODO Berechnen der ID

    /**
     * Fügt einen Artikel in die Rechnung ein
     * @param a     Der einzufügende Artikel
     */
    public void addArtikel(Artikel a){
        artikelListe.add(a);
    }

    public int getId() {
        return id;
    }

    public Timestamp getDate() {
        return date;
    }

    public ArrayList<Artikel> getArtikelListe() {
        return artikelListe;
    }
}
