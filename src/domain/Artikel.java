package domain;

public class Artikel implements Domain {

    /**
     * Eindeutige Identität eines Artikels
     */
    private long id;

    /**
     * Eindeutige Identität des Artikels, falls dies nur eine Kopie für eine Rechnung ist
     * -1, falls Original
     */
    private long originalId;

    /**
     * Gibt an, ob es sich bei diesem Artikel um einen neuen Eintrag noch ohne eindeutige ID handelt
     */
    private boolean neu;

    /**
     * Name des Artikels
     */
    private String name;

    /**
     * Preis des Artikels in Euro
     */
    private float preis;

    /**
     * Stückzahl des Artikels auf Lager
     * Wird der Artikel für einen Rechnungseintrag erwendet, entspricht dies der gekauften Menge
     */
    private int stueckzahl;

    /**
     * Adresse, unter der ein Bild des Artikels gespeichert ist
     */
    private String bildadresse;

    /**
     * Gibt an, ob es sich bei diesem Artikel um eine Kopie für eine Rechnung handelt
     */
    private boolean istKlon;

    /**
     * Allgemeiner Konstruktor des Artikels
     * @param id            Eindeutige ID des Artikels
     * @param originalId    Verweis auf den Originalartikel
     * @param name          Name des Artikels
     * @param preis         Preis des Artikels
     * @param stueckzahl    Anzahl der verfügbaren oder gekauften Artikel
     * @param bildadresse   Adresse zum Bild des Artikels
     * @param istKlon       Gibt an, ob dies eine Kopie für eine Rechnung ist
     */
    public Artikel(long id, long originalId, String name, float preis, int stueckzahl,String bildadresse, boolean istKlon){
        this.id = id;
        this.originalId = originalId;
        neu = false;
        this.name = name;
        this.preis = preis;
        this.stueckzahl = stueckzahl;
        this.bildadresse = bildadresse;
        this.istKlon = istKlon;
    }

    /**
     * Konstruktor für neue Artikel
     * @param name          Name des Artikels
     * @param preis         Preis des Artikels
     * @param stueckzahl    Anzahl der verfügbaren oder gekauften Artikel
     * @param bildadresse   Adresse zum Bild des Artikels
     * @param istKlon          Gibt an, ob dies eine Kopie für eine Rechnung ist
     */
    public Artikel(String name, float preis, int stueckzahl, String bildadresse, boolean istKlon){
        this.id = -1;   //Symbolischer Wert
        this.originalId = -1; //Symbolischer Wert
        neu = true;
        this.name = name;
        this.preis = preis;
        this.stueckzahl = stueckzahl;
        this.bildadresse = bildadresse;
        this.istKlon = istKlon;
    }

    /**
     * Konstruktor für Kopien für Rechnungen
     * @param originalId    Eindeutige ID des Originalartikels
     * @param name          Name des Artikels
     * @param preis         Preis des Artikels
     * @param stueckzahl    Anzahl der verfügbaren oder gekauften Artikel
     * @param bildadresse   Adresse zum Bild des Artikels
     * @param istKlon       Gibt an, ob dies eine Kopie für eine Rechnung ist
     */
    public Artikel(long originalId, String name, float preis, int stueckzahl, String bildadresse, boolean istKlon){
        this.id = -1;   //Symbolischer Wert
        this.originalId = originalId;
        neu = false;
        this.name = name;
        this.preis = preis;
        this.stueckzahl = stueckzahl;
        this.bildadresse = bildadresse;
        this.istKlon = istKlon;
    }

    /**
     * Gibt zurück, ob es sich hierbei um eine Kopie um eine Rechnung handelt
     * @return  True, falls Kopie für eine Rechnung, sonst False
     */
    public boolean isKlon(){
        return istKlon;
    }

    /**
     * Gibt zurück, ob dieser Artikel korrekt erstellt wurde
     * @return  True, falls dieser Artikel korrekt ist, sonst False
     */
    public boolean isCorrect(){
        return stueckzahl >= 0;
    }

    public long getOriginalId() {
        return originalId;
    }

    public String getName() {
        return name;
    }

    public float getPreis() {
        return preis;
    }

    public int getStueckzahl() {
        return stueckzahl;
    }

    public String getBildadresse() {
        return bildadresse;
    }

    public long getId() {
        return id;
    }
}
