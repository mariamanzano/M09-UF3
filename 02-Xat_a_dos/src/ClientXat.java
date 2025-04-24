import java.net.ServerSocket;

public class ClientXat {
    private ServerSocket clientSocket;
    private ObjectInputStream sortida;
    private ObjectOutputStream entrada;

    public void connecta() {
        try {
            clientSocket = new Socket(HOST, PORT);
            System.out.println("Client connectat a localhost: " + clientSocket.getInetAddress());
            entrada = new ObjectOutputStream(clientSocket.getOutputStream());
            sortida = new ObjectInputStream(clientSocket.getInputStream());
        } catch (IOException e) {
            System.out.println("Error al connectar al servidor");
            e.printStackTrace();
        }
    }

    public void enviarMissatges(String missatge) {
        if (entrada != null) {
            out.println(missatge);
            System.out.println("Enviat al servidor: " + missatge);
        }
    }
}
