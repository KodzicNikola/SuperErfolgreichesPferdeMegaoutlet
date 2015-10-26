package persistance;

import domain.Artikel;
import domain.Domain;
import domain.Rechnung;
import org.apache.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;

public class RechnungsDAO extends DAO{

    /**
     * String zur Erstellung der Tabelle der Rechnungen
     **/
    private static final String initTabelle = "CREATE TABLE Rechnungen( " +
            "id INT," +
            "datum timestamp,"+
            "artikelid bigint REFERENCES Artikel(id) on delete CASCADE ," +
            "originalartikel long,"+
            "primary key(id, artikelid));";

    private static Logger logger = Logger.getLogger(RechnungsDAO.class);

    /**
     * Erstellt die Tabellen für Rechnungen und deren Einträge
     * @return  True, falls beide Tabellen erstellt wurden, sonst False
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
            logger.debug("createTable fehlgeschlagen, Tabelle vorhanden?");
            return false;
        }
    }

    /**
     * Erstellt eine Rechnung in der Datenbank
     * @param d     Rechnung, die in der Datenbank eingefügt werden soll
     * @return      True, falls erfolgreich, sonst False
     */
    @Override
    public boolean create(Domain d){

        Rechnung r;
        if (d instanceof Rechnung){
            r = (Rechnung) d;
        } else {
            return false;
        }

        //Überprüft die Korrektheit der Daten
        if (!isCorrect(r)){
            return false;
        }

        try{
            //Fügt ein, mithilfe eines PreparedStatements
            int id = getNextFreeId();

            ArtikelDAO aao = new ArtikelDAO();
            Artikel neu;
            for(Artikel a:r.getArtikelListe()){

                long newid = aao.getLastId();
                neu = new Artikel(a.getOriginalId(),a.getName(),a.getPreis(),a.getStueckzahl(),a.getBildadresse(),true);
                aao.create(neu);

                PreparedStatement prep = getConnection().prepareStatement("INSERT INTO Rechnungen(id, datum,"+
                        "artikelid, originalartikel)"+
                        " VALUES(?, ?, ?, ?);");
                prep.setLong(1, id);
                prep.setTimestamp(2, r.getDate());

                prep.setLong(3,newid);
                prep.setLong(4,a.getOriginalId());
                prep.execute();
                prep.close();
            }

            getConnection().close();
            return true;
        } catch (SQLException s){
            logger.debug("create fehlgeschlagen", s);
        }

        return false;
    }

    /**
     * Löscht eine Rechnung aus der Tabelle
     * @param d     Die zu löschende Rechnung
     * @return      True, falls sich nach dem Löschvorgang kein Eintrag mit der ID der Rechnung mehr in der Tabelle befindet
     */
    public boolean delete(Domain d){
        Rechnung r;

        if(d instanceof Rechnung){
            r = (Rechnung) d;
        } else {
            return false;
        }

        try {
            Connection c = getConnection();
            PreparedStatement prep = c.prepareStatement("DELETE FROM Rechnungen WHERE id = ?;");
            prep.setInt(1,r.getId());
            prep.execute();


            prep = c.prepareStatement("SELECT * FROM Rechnungen WHERE id = ?;");
            prep.setLong(1,r.getId());
            ResultSet result = prep.executeQuery();

            boolean succsess = !result.next();

            result.close();
            prep.close();
            c.close();
            return succsess;

        } catch (SQLException s){
            logger.debug("delete fehlgeschlagen", s);
        }

        return false;
    }

    /**
     * Liest alle Rechnungen aus der Datenbank ein und gibt sie zurück
     * @return  Liste aller Rechnungen in der Datenbank
     */
    @Override
    public ArrayList<Rechnung> read() {
        ArrayList<Rechnung> result;

        Connection c;
        PreparedStatement p;
        ResultSet r;


        try{
            c = getConnection();
            p = c.prepareStatement("SELECT * FROM Rechnungen;");
            r = p.executeQuery();
            result = toRechnungen(r);
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
     * Wandelt ein Resultset mit Rechnungseinträgen in eine Liste von Rechnungen um
     * @param results   ResultSet mit Rechnungseinträgen
     * @return          Liste aller Rechnungen aus dem ResultSet
     */
    private ArrayList<Rechnung> toRechnungen(ResultSet results) {

        HashMap<Integer,Rechnung> gefundeneRechnungen = new HashMap<>(); //Merkt sich alle gefundenen Rechnungen
        HashMap<Long,Integer> fehlendeZuweisung = new HashMap<>(); //Merkt sich, welche Artikel noch einer Rechnung zugewisen werden müssen
        try{

            ArrayList<Float> IDs = new ArrayList<>();

            while(results.next()){  //Erstellt und befüllt Rechnungen
                Integer id = results.getInt("id");
                Rechnung r;
                if(!gefundeneRechnungen.containsKey(id)){
                    r = new Rechnung(id,results.getTimestamp("datum"));
                    gefundeneRechnungen.put(id,r);
                } else {
                    r = gefundeneRechnungen.get(id);
                }

                fehlendeZuweisung.put(results.getLong("artikelid"),id);

            }
            ArtikelDAO adao = new ArtikelDAO();

            for(Rechnung r : gefundeneRechnungen.values()){
                for(Long artikelId : fehlendeZuweisung.keySet()){
                    if(r.getId() == fehlendeZuweisung.get(artikelId)){
                        r.addArtikel(adao.getArtikelById(artikelId));
                    }
                }
            }
            results.close();

        } catch (SQLException s){
            logger.debug("toRechnung fehlgeschlagen", s);
        }

        ArrayList<Rechnung> result = new ArrayList<>();
        for(Rechnung r : gefundeneRechnungen.values()){
            result.add(r);
        }

        return result;
    }

    /**
     * Aktualisiert eine Rechnung, indem der vorige Eintrag gelöscht wird, und ein neuer eingetragen wird.
     * @param d     Die zu aktualisierende Rechnung
     * @return      True, falls der Vorgang erfolgreich war, sonst False
     */
    @Override
    public boolean update(Domain d) {

        if(!(d instanceof Rechnung)){
            return false;
        }

        try{
            if(!delete(d)){
                return false;
            }
            if(!create(d)){
                throw new Exception("Rechnung deleted but not updated!");
            }
            return true;
        } catch (SQLException s) {
            logger.debug("update fehlgeschlagen, konnte nicht löschen",s);
        } catch (Exception e){
            logger.error("update fehlgeschlagen, gelöscht aber nicht neu eingetragen!", e);
        }

        return false;
    }

    /**
     * Prüft, ob eine Rechnung Einträge enthält
     * @param r     Rechnung, die geprüft werden soll
     * @return      False, falls leer, sonst True
     */
    private boolean isCorrect(Rechnung r) {
        return r.getArtikelListe().size()>0;
    }

    /**
     * Ermittelt die nächste freie ID, die noch nicht verwendet wurde
     * @return  die nächste freie ID
     */
    public static int getNextFreeId() {
        int result = 0;

        try{

            ResultSet res = getConnection().prepareStatement("select max(id) as groesste from Rechnungen;").executeQuery();
            if(res.next()){
                result = res.getInt("groesste")+1;
            }

        } catch (SQLException s) {
            logger.debug("getNextFreeID fehlgeschlagen, IDs müssen überprüft werden", s);
        }

        return result;
    }

    public int getVerkaufteArtikelSince(Timestamp t) {
        int result = 0;

        try{
            Connection c = getConnection();
            PreparedStatement p = c.prepareStatement("SELECT * from Rechnungen where datum > ?;");
            p.setTimestamp(1,t);
            ResultSet r = p.executeQuery();

            ArrayList<Long> IDs = new ArrayList<>();
            while(r.next()){
                IDs.add(r.getLong("artikelID"));
            }

            r.close();
            p.close();
            c.close();
            ArtikelDAO aao = new ArtikelDAO();
            for(Long id: IDs){
                Artikel a = aao.getArtikelById(id);
                result += a.getStueckzahl();
            }
        } catch (SQLException s){
            logger.error("Konnte keine Statistik laden");
            throw new IllegalArgumentException("Zugriff auf Datenbank fehlgeschlagen.");
        }


        return result;
    }
}
