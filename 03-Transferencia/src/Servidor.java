import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Servidor {
    private static final int PORT = 9999;
    private static final String HOST = "localhost";
    private ServerSocket serverSocket;
    private Socket clientSocket;

    public void connectar() {
        try {
            serverSocket = new ServerSocket(PORT);
            System.out.println("Acceptant connexions en -> " + HOST + ":" + PORT);
            System.out.println("Esperant connexio...");
            clientSocket = serverSocket.accept();
            System.out.println("Connexio acceptada: " + clientSocket.getInetAddress());
        } catch (IOException e) {
            System.out.println("Error en iniciar el servidor");
            e.printStackTrace();
        }
    }

    public void tancarConexio() {
        try {
            if (clientSocket != null) clientSocket.close();
            if (serverSocket != null) serverSocket.close();
            System.out.println("Connexió tancada.");
        } catch (IOException e) {
            System.out.println("Error en tancar connexions");
            e.printStackTrace();
        }
    }

    public void enviarFitxers() {
        try (
            ObjectOutputStream sortida = new ObjectOutputStream(clientSocket.getOutputStream());
            ObjectInputStream entrada = new ObjectInputStream(clientSocket.getInputStream())
        ) {
            while (true) {
                System.out.println("Esperant el nom del fitxer del client...");
                String nomFitxer = (String) entrada.readObject();
                if (nomFitxer == null || nomFitxer.isEmpty()) {
                    System.out.println("Nom del fitxer buit o nul. Sortint...");
                    break;
                }
                System.out.println("Nom fitxer rebut: " + nomFitxer);
                try {
                    Fitxer fitxer = new Fitxer(nomFitxer);
                    byte[] contingut = fitxer.getContingut();
                    System.out.println("Contingut del fitxer a enviar: " + contingut.length + " bytes");
                    sortida.writeObject(contingut);
                    System.out.println("Fitxer enviat al client: " + nomFitxer);
                } catch (IOException e){
                    System.out.println("Error llegint el fitxer: " + nomFitxer);
                    sortida.writeObject(null);
                }   
            }
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error durant la comunicació amb el client.");
            e.printStackTrace();
        }
    }    

    public static void main(String[] args) {
        Servidor servidor = new Servidor();
        servidor.connectar();
        servidor.enviarFitxers();
        servidor.tancarConexio();
    }
}