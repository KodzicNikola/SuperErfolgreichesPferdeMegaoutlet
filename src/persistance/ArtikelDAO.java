package persistance;


import domain.Artikel;
import domain.Domain;
import org.apache.log4j.Logger;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ArtikelDAO extends DAO{

    /**
    * String zur Erstellung der Tabelle der Artikel
    **/
    private static final String initTabelle = "CREATE TABLE ARTIKEL( " +
            "id identity primary key," +
            "originalid long"+
            "name varchar(50)," +
            "bildadresse varchar(255)," +
            "preis decimal(20,2)"+
            "stueckzahl int,"+
            "klon bool);";

    private static Logger logger = Logger.getLogger(ArtikelDAO.class);

    /**
     * Erstellt eine Tabelle für Artikel, falls noch keine existiert
     * @return  True, falls eine neue Tabelle erstellt wurde, sonst False
     */
    @Override
    public boolean createTable(){

        try {
            Connection connection = getConnection();
            PreparedStatement prep = connection.prepareStatement(initTabelle);
            prep.execute();
            prep.close();
            connection.close();
            return true;
        } catch (SQLException s) {
            logger.debug("createTable fehlgeschlagen, Tabelle vorhanden?", s);
            return false;
        }
    }

    /**
     * Prüft Korrektheit eines Artikels
     * @param a     Der zu prüfende Artikel
     * @return      True, falls Artikel korrekt erstellt wurde, sonst falls
     */
    private boolean isCorrect(Artikel a){
        return a.isCorrect();
    }

    /**
     * Erstellt eine Liste von Artikeln aus einem ResultSet
     * @param r     ResultSet, aus dem die Artikel gewesen sollen
     * @return      Liste der Artikel aus dem Set, null falls fehlgeschlagen
     */
    private ArrayList<Artikel> toArtikel(ResultSet r){
        ArrayList<Artikel> result;

        try{
            result = new ArrayList<>();
            while(r.next()){
                Artikel a = new Artikel(r.getLong("id"),
                        r.getLong("originalid"),
                        r.getString("name"),
                        r.getInt("preis"),
                        r.getInt("stueckzahl"),
                        r.getString("bildadresse"),
                        r.getBoolean("klon"));
                result.add(a);
            }
        } catch (SQLException s){
            logger.debug("toArtikel fehlgeschlagen, falsch aus Resultset gelesen", s);
            result = null;
        }
        return result;
    }

    /**
     * Erstellt einen Artikel in der Datenbank
     * @param d     Artikel, der in der Datenbank eingefügt werden soll
     * @return      True, falls erfolgreich, sonst False
     */
    @Override
    public boolean create(Domain d){

        Artikel a;
        if (d instanceof Artikel){
            a = (Artikel) d;
        } else {
            return false;
        }

        //Überprüft die Korrektheit der Daten
        if (!isCorrect(a)){
            return false;
        }

        try{
            //Fügt ein, mithilfe eines PreparedStatements
            PreparedStatement prep = getConnection().prepareStatement("INSERT INTO Artikel(originalId, name,"+
                    "preis, stueckzahl, bildadresse, klon)"+
                    " VALUES(?, ?, ?, ?, ?, ?)");
            prep.setLong(1, a.getOriginalId());
            prep.setString(2, a.getName());
            prep.setBigDecimal(3, new BigDecimal(Float.toString(a.getPreis())));
            prep.setInt(4, a.getStueckzahl());
            prep.setString(5, a.getBildadresse());
            prep.setBoolean(6, a.isKlon());
            prep.execute();
            prep.close();
            getConnection().close();
            return true;
        } catch (SQLException s){
            logger.debug("create fehlgeschlagen", s);
        }

        return false;
    }

    /**
     * Liest alle originalen Artikel der Datenbank
     * @return  Liste aller Artikel, die keine Klone für eine Rechnung sind
     */
    @Override
    public ArrayList<Artikel> read(){

        ArrayList<Artikel> result;
        Connection c;
        PreparedStatement p;
        ResultSet r;


        try{
            c = getConnection();
            p = c.prepareStatement("SELECT * FROM Artikel WHERE Klon = FALSE;");
            r = p.executeQuery();
            result = toArtikel(r);
            r.close();
            p.close();
            c.close();

        } catch (SQLException s) {
            logger.debug("read fehlgeschlagen", s);
            result = new ArrayList<>();
        }

        return result;
    }


    /**
     * Aktualisiert den Eintrag einer Domain, falls es sich dabei um einen Artikel handelt
     * @param d     Die zu aktualisierende Domain
     * @return      True, falls das update erfolgreich war, sonst False
     */
    @Override
    public boolean update(Domain d) {

        Artikel a;

        if(d instanceof Artikel){
            a = (Artikel) d;
        } else {
            return false;
        }

        if(!isCorrect(a)){
            return false;
        }

        try {
            Connection c = getConnection();
            PreparedStatement prep = c.prepareStatement("UPDATE Artikel SET " +
                    "originalid = ?, name = ?, preis = ?, " +
                    "stueckzahl = ?, bildadresse = ?, klon = ? "+
                    "WHERE id = ?;");
            prep.setLong(1, a.getOriginalId());
            prep.setString(2, a.getName());
            prep.setBigDecimal(3, new BigDecimal(Float.toString(a.getPreis())));
            prep.setInt(4, a.getStueckzahl());
            prep.setString(5, a.getBildadresse());
            prep.setBoolean(6, a.isKlon());
            prep.execute();
            prep = c.prepareStatement("SELECT * FROM Artikel WHERE id = ?;"); //Erstellt eine Abfrage um zu sehen, welche Artikel betroffen waren
            prep.setLong(1, a.getOriginalId());

            ResultSet r = prep.executeQuery();

            if(!r.next()){
                return false;
            }

            r.close();
            prep.close();
            c.close();
            return true;

        } catch (SQLException s){
            logger.debug("update fehlgeschlagen", s);
        }

        return false;
    }

    /**
     * Löscht einen Artikel aus dem Inventar, falls vorhanden und unabhängig von den Rechnungseinträgen
     * @param d     Der zu löschende Artikel
     * @return      True, falls sich nach dem Vorgang kein Artikel mit der ID von d in der Datenbank befindet, sonst false
     */
    @Override
    public boolean delete(Domain d) {

        Artikel a;

        if(d instanceof Artikel){
            a = (Artikel) d;
        } else {
            return false;
        }

        try {
            Connection c = getConnection();
            PreparedStatement prep = c.prepareStatement("DELETE FROM Artikel WHERE id = ?;");
            prep.setLong(1,a.getId());
            prep.execute();
            prep.close();

            prep = c.prepareStatement("SELECT * FROM Artikel WHERE id = ?;");
            prep.setLong(1,a.getId());
            ResultSet r = prep.executeQuery();

            boolean succsess = !r.next();

            r.close();
            prep.close();
            c.close();
            return succsess;

        } catch (SQLException s){
            logger.debug("delete fehlgeschlagen", s);
        }

        return false;
    }

    /**
     * Holt einen Artikel aus der Datenbank anhand der ID
     * @param id    Id des gesuchten Artikels
     * @return      Gesuchter Artikel oder null, falls nicht gefunden
     */
    public Artikel getArtikelById(long id) {
        Artikel result;
        try {
            getConnection();
            PreparedStatement p = getConnection().prepareStatement("SELECT * FROM Artikel WHERE id = ?");
            p.setLong(1,id);
            ResultSet r = p.executeQuery();

            if (!r.next()){
                r.close();
                p.close();
                getConnection().close();
                return null;
            }

            result = new Artikel(id,r.getLong("originalid"), r.getString("name"), r.getBigDecimal("preis").floatValue(),
                    r.getInt("stueckzahl"), r.getString("bildadresse"), r.getBoolean("klon"));

            r.close();
            p.close();
            getConnection().close();
        } catch (SQLException s){
            logger.debug("getArtikelById fehlgeschlagen", s);
            result = null;
        }

        return result;
    }

    /**
     * Gibt die Liste aller Kopien für einen Artikel, die für Rechnungen erstellt wurden
     * @param id    ID des gesuchten Originalartikels
     * @return      Liste der gefundenen Ergebnisse
     */
    public ArrayList<Artikel> getKloneOf(long id){

        ArrayList<Artikel> results;
        try {

            PreparedStatement prep = getConnection().prepareStatement("SELECT * FROM Artikel WHERE originalid = ?;");
            prep.setLong(1,id);
            ResultSet r = prep.executeQuery();

            results = toArtikel(r);

        } catch (SQLException s){
            logger.debug("getKloneOf fehlgeschlagen", s);
            results = new ArrayList<>();
        }

        return results;
    }

}
