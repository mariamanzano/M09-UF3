import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {
    private static final int PORT = 7777;
    private static final String HOST = "localhost";
    private Socket socket;
    private PrintWriter out;
    
    public void connecta() {
        try {
            socket = new Socket(HOST, PORT);
            out = new PrintWriter(socket.getOutputStream(), true);
            System.out.println("Client connectat a servidor en loclahost:" + PORT);
        } catch (Exception e) {
            System.out.println("Error en connectar al servidor");
            e.printStackTrace();
        }
    }

    public void tanca() {
        try {
            out.close();
            socket.close();
        } catch (Exception e) {
            System.out.println("Error en tancar socket");
            e.printStackTrace();
        }
    }

    public void envia(String missatge) {
        if (out != null) {
            out.println(missatge);
            System.out.println("Enviat al servidor: " + missatge);
        }
    }

    public static void main(String[] args) {
        Client client = new Client();
        client.connecta();
        try {
            client.envia("Prova d'enviament 1");
            client.envia("Prova d'enviament 2");
            client.envia("Adéu!");
            System.out.println("Prem Enter per enviar els missatges i tancar el client...");
            BufferedReader entrada = new BufferedReader(new InputStreamReader(System.in));
            entrada.readLine();
        } catch (Exception e) {
            System.out.println("Error en llegir dades");
            e.printStackTrace();
        } finally {
            client.tanca();
            System.out.println("Client tancat");
        }
    }
}
