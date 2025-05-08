import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    public static final String DIR_ARRIBADA = "C:\\tmp";
    private static final int PORT = 9999;
    private static final String HOST = "localhost";
    private static final String MSG_SORTIR = "sortir";
    private Socket clientSocket;
    private ObjectInputStream entrada;
    private ObjectOutputStream sortida;

    public void connectar() {
        try {
            clientSocket = new Socket(HOST, PORT);
            System.out.println("Connectant a -> " + HOST + ":" + PORT);
            System.out.println("Connexió acceptada: " + clientSocket.getInetAddress());
            entrada = new ObjectInputStream(clientSocket.getInputStream());
            sortida = new ObjectOutputStream(clientSocket.getOutputStream());
        } catch (IOException e) {
            System.out.println("Error en connectar al servidor");
            e.printStackTrace();
        }
    }

    public void rebreFitxers() throws IOException, ClassNotFoundException {
        try (Scanner scanner = new Scanner(System.in)) {
            while (true) {
                System.out.println("Nom del fitxer a rebre('sortir' per sortir): ");
                String nomFitxer = scanner.nextLine();
                if (nomFitxer.equalsIgnoreCase(MSG_SORTIR)) {
                    System.out.println("Sortint...");
                    break;
                }
                sortida.writeObject(nomFitxer);
                byte[] contingut = (byte[]) entrada.readObject();
                if (contingut == null) {
                    System.out.println("Fitxer rebut buit o no trobat al servidor.");
                } else {
                    System.out.println("Nom del fitxer a guardar: ");
                    String nomFitxerGuardat = scanner.nextLine();
                    try (FileOutputStream fos = new FileOutputStream(DIR_ARRIBADA + File.separator + nomFitxerGuardat)) {
                        fos.write(contingut);
                    }
                    System.out.println("Fitxer rebut i desat correctament.");
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error en rebre fitxers");
            e.printStackTrace();
        }
    }    

    public void tancarConexio() {
        try {
            if (entrada != null) entrada.close();
            if (sortida != null) sortida.close();
            if (clientSocket != null) clientSocket.close();
            System.out.println("Connexió tancada.");
        } catch (IOException e) {
            System.out.println("Error en tancar connexions");
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Client client = new Client();
        client.connectar();
        try {
            client.rebreFitxers();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error durant la recepció del fitxer.");
            e.printStackTrace();
        }
        client.tancarConexio();
    }    
}
