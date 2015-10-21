package persistance;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class RechnungsDAO extends DAO{

    /**
     * String zur Erstellung der Tabelle der Rechnungen
     **/
    private static final String initRechnungsTabelle = "CREATE TABLE Rechnungen( " +
            "id identity primary key," +
            "datum timestamp);";

    /**
     * String zur Erstellung der Tabelle der RechnungsEinträge
     **/
    private static final String initEintragsTabelle = "CREATE TABLE Rechnungseintraege( " +//TODO!!!
            "id identity primary key," +
            "datum timestamp);";

    @Override
    public boolean createTable(){//TODO!!!

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
}
