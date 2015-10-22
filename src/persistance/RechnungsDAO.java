package persistance;

import domain.Artikel;
import domain.Domain;
import domain.Rechnung;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

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
     * Prüft, ob eine Rechnung Einträge enthält
     * @param r     Rechnung, die geprüft werden soll
     * @return      False, falls leer, sonst True
     */
    private boolean isCorrect(Rechnung r) {
        return r.getArtikelListe().size()>0;
    }

}
