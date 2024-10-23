import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.*;

// "daten" ist Platzhalter
public class Server {
    static final String SQL_URL = "jdbc:mysql://127.0.0.1:3306/studentdb";
    static final String USER = "daten";
    static final String PASS = "daten";

    public static void main(String[] args) {
        //Class.forName("com.mysql.cj.jdbc.Driver");

        int maxClients = 3;
        int currentClient = 1;

        Connection connection = null;
        Statement statement = null;

        try {
            ServerSocket serverSocket = new ServerSocket(5000);
            System.out.println("\nServer erstellt, warte auf Verbindung...");

            while (true) {
                Socket socket = serverSocket.accept();
                if (currentClient <= maxClients) {
                    Clients client = new Clients(socket, currentClient);
                    currentClient = currentClient + 1;
                    client.start();
                } else {
                    BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                    out.println("Maximum Number of Clients reached!");
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
class Clients extends Thread{

    private Socket socket;
    int clientNumber;
    boolean IsAdmin = false;

    public Clients(Socket socket, int clientnumber){
        this.socket=socket;
        this.clientNumber = clientnumber;
    }
    public void run(){
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            Connection connection = null;
            Statement statement = null;
            //out.println("You are Client Number: " + clientNumber);
            while (true) {//einlogfunktion
                String message1 = in.readLine();
                String message2 = in.readLine();

                if (message1.contains(" ") || message2.contains(" ")) {
                    out.println("Fehler");
                } else {
                    System.out.println("Verbindung zur MySQL-Datenbank wird hergestellt...");
                    try {
                        Class.forName("com.mysql.cj.jdbc.Driver");
                        System.out.println("Verbindung zur MySQL-Datenbank wird hergestellt...");
                        connection = DriverManager.getConnection(Server.SQL_URL, Server.USER, Server.PASS);
                        System.out.println("Verbindung erfolgreich...");
                        statement = connection.createStatement();
                        String sql = "SELECT username, role, passwd FROM users WHERE username = '" + message1 + "'AND passwd = '" + message2 + "'";
                        ResultSet resultset = statement.executeQuery(sql);
                        System.out.println("\nDatenbankeinträge: ");
                        if (resultset.next()) {
                            String username = resultset.getString("username");
                            String password = resultset.getString("passwd");
                            String role = resultset.getString("role");
                            System.out.println("role: " + role);
                            out.println(role);
                            if (role.equalsIgnoreCase("admin")){
                                IsAdmin = true;
                            }
                            break;
                        } else {
                            System.out.println("Falsches Passwort");
                            out.println("Fehler");
                        } // Login ende

                    } catch (SQLException e) {
                        e.printStackTrace();
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }
            while (true) {
                String request = in.readLine();
                if (request.equalsIgnoreCase("Get")) {
                    String sql = "SELECT * from students ";
                    ResultSet resultset = statement.executeQuery(sql);
                    while (resultset.next()) {
                        String id = resultset.getString("idstudents");
                        String name = resultset.getString("name");
                        String grade = resultset.getString("grade");
                        System.out.println("id" + id + "name" + name + "grade" + grade);
                        out.println("id" + id + "name" + name + "grade" + grade);
                        System.out.println("\nDatenbankeinträge: ");
                    }
                    out.println("Fertig");
                }
                if (request.equalsIgnoreCase("Add")) {
                    String stutendName = in.readLine();
                    String stutendGrade = in.readLine();
                    String sql = "insert into students(name, grade) values ('" + stutendName + "', '" + stutendGrade + "')";
                    int rowsAffected = statement.executeUpdate(sql);
                }
                if (request.equalsIgnoreCase("Exit")) {
                    break;
                }
            }
            connection.close();
            socket.close();
            in.close();
            out.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}