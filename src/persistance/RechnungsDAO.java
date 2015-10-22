package persistance;

import domain.Artikel;
import domain.Domain;
import domain.Rechnung;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

public class RechnungsDAO extends DAO{

    /**
     * String zur Erstellung der Tabelle der Rechnungen
     **/
    private static final String initTabelle = "CREATE TABLE Rechnungen( " +
            "id INT," +
            "datum timestamp,"+
            "artikelid IDENTITY REFERENCES Artikel(id)," +
            "originalartikel IDENTITY REFERENCES Artikel(originalid)"+
            "primary key(id, artikelid));";


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
            PreparedStatement prep = getConnection().prepareStatement("INSERT INTO Rechnung(id, datum,"+
                    "artikelid, originalartikel)"+
                    " VALUES(?, ?, ?, ?)");
            prep.setLong(1, r.getId());
            prep.setTimestamp(2, r.getDate());

            for(Artikel a:r.getArtikelListe()){
                prep.setLong(3,a.getId());
                prep.setLong(4,a.getOriginalId());
                prep.execute();
            }

            prep.close();
            getConnection().close();
            return true;
        } catch (SQLException s){
            System.out.println(s.toString());
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
            prep.setLong(1,r.getId());
            prep.execute();
            prep.close();

            prep = c.prepareStatement("SELECT * FROM Artikel WHERE id = ?;");
            prep.setLong(1,r.getId());
            ResultSet result = prep.executeQuery();

            boolean succsess = !result.next();

            result.close();
            prep.close();
            c.close();
            return succsess;

        } catch (SQLException s){
            System.out.println(s.toString());
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
            p = c.prepareStatement("SELECT * FROM Rechnungen");
            r = p.executeQuery();
            result = toRechnungen(r);
            r.close();
            p.close();
            c.close();

        } catch (SQLException s) {
            System.out.println(s.toString());
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
        try{
            while(results.next()){  //Erstellt und befüllt Rechnungen
                Integer id = new Integer(results.getInt("id"));
                Rechnung r;
                if(!gefundeneRechnungen.containsKey(id)){
                    r = new Rechnung(id,results.getTimestamp("datum"));
                    gefundeneRechnungen.put(id,r);
                } else {
                    r = gefundeneRechnungen.get(id);
                }

                ArtikelDAO adao = new ArtikelDAO();
                Artikel a = adao.getArtikelById(id);
                r.addArtikel(a);

            }
        } catch (SQLException s){
            System.out.println(s.toString());
        }

        ArrayList<Rechnung> result = new ArrayList<>();
        for(Rechnung r : gefundeneRechnungen.values()){
            result.add(r);
        }

        return result;
    }

    @Override
    public boolean update(Domain d) {

        Rechnung r;

        if(d instanceof Rechnung){
            r = (Rechnung) d;
        } else {
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
        } catch (Exception e){
            System.out.println(e.toString());
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

}
