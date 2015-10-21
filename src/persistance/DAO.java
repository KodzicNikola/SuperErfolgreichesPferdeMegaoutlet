package persistance;

import domain.Domain;
import java.sql.*;
import java.util.ArrayList;


public abstract class DAO {

    /**
     * Strings zum Verbinden mit H2
     */
    private static String username = "admin";
    private static String passwort = "";
    private static String datenbankName ="jdbc:h2:~/test";
    private static String driverName = "org.h2.Driver";

    private static Connection connection;

    /**
     * Öffnet die Verbindung zur Datenbank
     * @return  True, falls eine Verbindung geöffnet wurde, sonst False
     */
    private static boolean openConnection()  { //TODO Output entfernen
        try {
            Class.forName(driverName);
        } catch (Exception e) {
            System.out.println("Open Connection Error");
            System.out.println(e.toString());
            return false;
        }
        try {
            connection = DriverManager.getConnection(datenbankName, username, passwort);
        } catch (Exception e) {
            System.out.println("Open Connection Error 2");
            System.out.println(e.toString());
            return false;
        }
        return true;
    }

    /**
     * Öffnet eine Verbindung zu H2 und gibt diese zurück
     * @return  Verbindung zur Datenbank
     * @throws  SQLException
     */
    public static Connection getConnection() throws SQLException{
        if(connection == null || connection.isClosed()) { //TODO Besser machen...
            openConnection();
        }
        return  connection;
    }

    /**
     * Erstellt eine Tabelle für die Domain in der Datenbank, falls noch nicht existent
     * @return  True, falls eine Tabelle erstellt wurde, sonst False
     */
    public abstract boolean createTable();

    /**
     * Erstellt eine neue Instant in der passenden Tabelle für d
     * @param d     neues Objekt, das in der Datenbank eingefügt werden soll
     * @return      True, falls Einfüge erfolgreich war, sonst False
     */
    public abstract boolean create(Domain d);

    /**
     * Liest alle Objekte der Domain ein
     * @return  Liste aller Objekte in der Tabelle
     */
    public abstract ArrayList<? extends Domain> read();

    /**
     * Aktualisiert den Eintrag einer Domain
     * @param d     Die zu aktualisierende Domain
     * @return      True, falls Vorgang erfolgreich war, sonst False
     */
    public abstract boolean update(Domain d);
    public abstract boolean delete(Domain d);
}
