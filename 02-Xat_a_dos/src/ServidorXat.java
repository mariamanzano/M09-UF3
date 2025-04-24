import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class ServidorXat {
    private static final int PORT = 9999;
    private static final String HOST = "localhost";
    private static final String MSG_SORTIR = "sortit";

    private ServerSocket serverSocket;
    private Socket clientSocket;

    public void iniciarServidor () {
        try {
            serverSocket = new ServerSocket(PORT);
            System.out.println("Servidor iniciat a " + HOST + ":" + PORT);
            clientSocket = serverSocket.accept();
            System.out.println("Client connectat: " + clientSocket.getInetAddress());
        } catch (IOException e) {
            System.out.println("Error al iniciar el servidor");
            e.printStackTrace();
        }
    }

    public void pararServidor () {
        try {
            if (clientSocket != null) clientSocket.close();
            if (serverSocket != null) serverSocket.close();
        } catch (IOException e) {
            System.out.println("Error en tancar sockets");
            e.printStackTrace();
        }
    }

    public void getNom() {
        try (ObjectInputStream entrada = new ObjectInputStream(clientSocket.getInputStream())) {
            Object obj;
            while ((obj = entrada.readObject()) != null) {
                if (obj instanceof String) {
                    String fila = (String) obj;
                    System.out.println("Nom: " + fila);
                    if (fila.equalsIgnoreCase(MSG_SORTIR)) break;
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error en llegir dades");
            e.printStackTrace();
        }
    }    

    public static void main(String[] args) {
        ServidorXat servidor = new ServidorXat();
        servidor.iniciarServidor();
        servidor.getNom();
        servidor.pararServidor();
    }
}
