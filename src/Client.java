
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    // Prg soll Verbindung zur Datenbank herstellen und beim Login unterscheiden
    // ob user oder admin, damit werden verschiedene Rechte bei erstellen oder Zugriff vergeben

    public static void main(String[] args) {
        boolean IsAdmin = false;
        Scanner scanner = new Scanner(System.in);

        try {
            Socket socket = new Socket("192.168.1.15", 5000);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

            String eingabe;
            String tabelle;
            String gradeName;
            do {
                System.out.println("\nHallo, bitte loggen Sie sich ein: ");
                System.out.println("\nIhr Benutzername: ");
                eingabe = scanner.nextLine();
                System.out.println("\nIhr Passwort: ");
                tabelle = scanner.nextLine();
                out.println(eingabe);
                out.println(tabelle);
                gradeName = in.readLine();
                System.out.println(gradeName);
            } while(gradeName.equalsIgnoreCase("Fehler"));

            System.out.println("\nEingeloggt als: '" + gradeName + "'");
            if (gradeName.equalsIgnoreCase("admin")) {
                IsAdmin = true;
            }

            do {
                System.out.println("\nWas möchten Sie tun? Bitte auswählen: ");
                System.out.println("(1) - gesamte Liste zeigen");
                if (IsAdmin) {
                    System.out.println("(2) - Student zur Liste hinzufügen");
                }

                System.out.println("(3) - für ENDE");
                eingabe = scanner.nextLine();
                if (eingabe.equals("1")) {
                    out.println("Get");

                    for(tabelle = in.readLine(); !tabelle.equalsIgnoreCase("Fertig"); tabelle = in.readLine()) {
                        System.out.println(tabelle);
                    }
                }

                if (eingabe.equals("2")) {
                    out.println("Add");
                    System.out.println("Wie heisst der Studierende?");
                    tabelle = scanner.nextLine();
                    System.out.println("Wie heisst der Kurs?");
                    gradeName = scanner.nextLine();
                    out.println(tabelle);
                    out.println(gradeName);
                }
            } while(!eingabe.equalsIgnoreCase("3"));

            out.println("Exit");
            in.close();
            out.close();
            socket.close();
        } catch (IOException var9) {
            IOException e = var9;
            throw new RuntimeException(e);
        }
    }
}
