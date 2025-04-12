import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class Servidor {
    private static final int PORT = 7777;
    private static final String HOST = "localhost";
    private ServerSocket srvSocket;
    private Socket clientSocket;
    private boolean end = false;

    public void connecta() {
        try {
            srvSocket = new ServerSocket(PORT);
            System.out.println("Servidor en marxa a localhost:" + PORT);
            System.out.println("Esperant connexions a localhost:" + PORT + "...");
            clientSocket = srvSocket.accept();
            System.out.println("Client connectat: " + clientSocket.getInetAddress());

        } catch (IOException e) {
            System.out.println("Error al connectar el servidor");
            e.printStackTrace();
        }
    }

    public void repDades() throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        String fila;
        try {
            while ((fila = in.readLine()) != null) {
                System.out.println("Rebut: " + fila);
            }
        } catch (IOException e) {
            System.out.println("Error en llegir dades");
            e.printStackTrace();
        }
    }

    public void tanca() {
        try {
            clientSocket.close();
            srvSocket.close();
        } catch (IOException e) {
            System.out.println("Error en tancar sockets");
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Servidor servidor = new Servidor();
        servidor.connecta();
        try {
            servidor.repDades();
        } catch (IOException e) {
            System.out.println("Error en rebre dades: " + e.getMessage());
            e.printStackTrace();
        } finally {
            servidor.tanca();
            System.out.println("Servidor tancat.");
        }
    } 
}