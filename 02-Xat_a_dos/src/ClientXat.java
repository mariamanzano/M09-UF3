import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class ClientXat {
    private static final int PORT = 9999;
    private static final String HOST = "localhost";
    private static final String MSG_SORTIR = "sortir";

    private Socket clientSocket;
    private ObjectInputStream entrada;
    private ObjectOutputStream sortida;

    public void connecta() {
        try {
            clientSocket = new Socket(HOST, PORT);
            System.out.println("Client connectat a " + HOST + ":" + PORT);
            sortida = new ObjectOutputStream(clientSocket.getOutputStream());
            entrada = new ObjectInputStream(clientSocket.getInputStream());
            System.out.println("Flux d'entrada i sortida creat.");
        } catch (IOException e) {
            System.out.println("Error al connectar al servidor");
            e.printStackTrace();
        }
    }

    public void enviarMissatge(String missatge) {
        try {
            sortida.writeObject(missatge);
            System.out.println("Enviant missatge: " + missatge);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void tancarClient() {
        try {
            System.out.println("Tancant client...");
            if (entrada != null) entrada.close();
            if (sortida != null) sortida.close();
            if (clientSocket != null) clientSocket.close();
            System.out.println("Client tancat.");
        } catch (IOException e) {
            System.out.println("Error al tancar el client");
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        ClientXat client = new ClientXat();
        client.connecta();
        FilLectorCX lector = new FilLectorCX(client.entrada);
        lector.start();

        try (Scanner scanner = new Scanner(System.in)) {
            String missatge = "";
            while (!missatge.equalsIgnoreCase(MSG_SORTIR)) {
                System.out.print("Missatge ('sortir' per tancar): ");
                missatge = scanner.nextLine();
                client.enviarMissatge(missatge);
            }
        }
        client.tancarClient();
    }
}
