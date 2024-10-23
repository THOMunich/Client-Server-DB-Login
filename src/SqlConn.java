import java.sql.*;

public class SqlConn {
    static final String SQL_URL = "jdbc:mysql://127.0.0.1:3306/studentdb";
    static final String USER = "daten";
    static final String PASS = "daten";

    // "daten" ist Platzhalter
    public static void main(String[] args) {
        Connection connection = null;
        Statement statement = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("Verbindung zur MySQL-Datenbank wird hergestellt...");
            connection = DriverManager.getConnection(SQL_URL, USER, PASS);
            System.out.println("Verbindung erfolgreich...");
            statement = connection.createStatement();
            String sql = "SELECT username, role, passwd FROM users WHERE username = 'daten' AND passwd = 'daten'";
            ResultSet resultset = statement.executeQuery(sql);
            System.out.println("\nDatenbankeinträge: ");
            if (resultset.next()) {
                String username = resultset.getString("username");
                String password = resultset.getString("passwd");
                String role = resultset.getString("role");
                System.out.println("Name: " + username + "\nRolle: " + role);
            } else {
                System.out.println("Falsches Passwort!");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                    System.out.println("\nVerbindung geschlossen");
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}


