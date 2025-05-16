import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Hashtable;

public class ServidorXat {
    private static final int PORT = 9999;
    private static final String HOST = "localhost";
    private ServerSocket serverSocket;
    private Socket clientSocket;
    private static final String MSG_SORTIR = "sortir";
    private Hashtable<String, GestorClients> nom = new Hashtable<>();
    private boolean sortir = false;

    public void servidorAEscoltar() {
        try {
            serverSocket = new ServerSocket(PORT);
            System.out.println("Servidor iniciat a " + HOST + ":" + PORT);
            while(!sortir) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connectat: " + clientSocket.getInetAddress());
                ObjectOutputStream sortida = new ObjectOutputStream(clientSocket.getOutputStream());
                ObjectInputStream entrada = new ObjectInputStream(clientSocket.getInputStream());
                GestorClients gestor = new GestorClients(clientSocket, entrada, sortida, this, null);
                new Thread(() -> gestor.executar()).start();

            }
        } catch (IOException e) {
            System.out.println("Error iniciant servidor: " + e.getMessage());
        }
    }

    public void pararServidor() {
        try {
            if (serverSocket != null) serverSocket.close();
        } catch (IOException e) {
            System.out.println("Error tancant servidor: " + e.getMessage());
        }
    }

    public void finalitzarXat() {
        enviarMissatgeGrup(Missatge.getMissatgeSortirTots("sortir"));
        nom.clear();
        sortir = true;
        pararServidor();
    }

    public void afegirClient(String nomClient, GestorClients gestor) {
        nom.put(nomClient, gestor);
        String entradaMsg = "DEBUG: multicast Entra: " + nomClient;
        System.out.println(entradaMsg);
        enviarMissatgeGrup(Missatge.getMissatgeGrup(entradaMsg));
    }


    public void eliminarClient(String nomClient) {
        if (nom.containsKey(nomClient)) {
            nom.remove(nomClient);
        }
    }

    public void enviarMissatgeGrup(String missatge) {
        for (GestorClients gestor : nom.values()) {
            gestor.enviarMissatge(null, missatge);
        }
    }

    public void enviarMissatgePersonal(String destinatari, String remitent, String missatge) {
        GestorClients gestor = nom.get(destinatari);
        if (gestor != null) {
            gestor.enviarMissatge(remitent, missatge);
        }
    }


    public static void main(String[] args) {
        ServidorXat servidor = new ServidorXat();
        servidor.servidorAEscoltar();
    }
}