import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class ClientXat {
    private Socket socket;
    private ObjectInputStream entrada;
    private ObjectOutputStream sortida;
    private boolean sortir = false;

    public void connecta() {
        try {
            socket = new Socket("localhost", 9999);
            System.out.println("Client connectat a localhost:9999");

            sortida = new ObjectOutputStream(socket.getOutputStream());
            System.out.println("Flux d'entrada i sortida creat.");

            Thread escoltador = new Thread(() -> {
                try {
                    entrada = new ObjectInputStream(socket.getInputStream());
                    System.out.println("DEBUG: Iniciant rebuda de missatges...");
                    while (!sortir) {
                        String missatgeCru = (String) entrada.readObject();

                        if (missatgeCru == null || missatgeCru.trim().isEmpty()) {
                            continue;
                        }

                        if (!missatgeCru.contains("#")) {
                            System.out.println(missatgeCru);
                            continue;
                        }

                        String codi = Missatge.getCodiMissatge(missatgeCru);
                        String[] parts = Missatge.getPartsMissatge(missatgeCru);

                        if (codi == null || parts == null || parts.length < 2) {
                            System.out.println("WARN: missatge buit o incorrecte [" + missatgeCru + "]");
                            continue;
                        }

                        switch (codi) {
                            case Missatge.CODI_SORTIR_TOTS:
                                sortir = true;
                                break;
                            case Missatge.CODI_MSG_PERSONAL:
                            case Missatge.CODI_MSG_GRUP:
                                System.out.println(parts[1]);
                                break;
                            default:
                                System.out.println("Error rebent missatge amb codi desconegut: " + codi);
                        }
                    }

                } catch (Exception e) {
                    System.out.println("Error rebent missatge. Sortint...");
                } finally {
                    tancarClient();
                }
            });

            escoltador.start();

        } catch (IOException e) {
            System.out.println("Error connectant: " + e.getMessage());
        }
    }

    public void enviarMissatge(String missatge) {
        try {
            System.out.println("Enviant missatge: " + missatge);
            sortida.writeObject(missatge);
        } catch (IOException e) {
            System.out.println("Error enviant missatge.");
        }
    }

    public void tancarClient() {
        try {
            if (entrada != null) entrada.close();
            if (sortida != null) sortida.close();
            if (socket != null) socket.close();
            System.out.println("Tancant client...");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void ajuda() {
        System.out.println("---------------------");
        System.out.println("Comandes disponibles:");
        System.out.println("1.- Conectar al servidor (primer pass obligatori)");
        System.out.println("2.- Enviar missatge personal");
        System.out.println("3.- Enviar missatge al grup");
        System.out.println("4.- (o línia en blanc)-> Sortir del client");
        System.out.println("5.- Finalitzar tothom");
        System.out.println("---------------------");
    }

    public static String getLinea(Scanner sc, String missatge, boolean obligatori) {
        String input;
        do {
            System.out.print(missatge);
            input = sc.nextLine().trim();
        } while (obligatori && input.isEmpty());
        return input;
    }

    public static void main(String[] args) {
        ClientXat client = new ClientXat();
        client.connecta();
        Scanner sc = new Scanner(System.in);
        String nom = null;

        while (!client.sortir) {
            client.ajuda();
            String opcio = sc.nextLine().trim();

            if (opcio.isEmpty()) {
                client.sortir = true;
                client.enviarMissatge(Missatge.getMissatgeSortirClient("Adéu"));
                break;
            }

            switch (opcio) {
                case "1":
                    nom = getLinea(sc, "Introdueix el nom: ", true);
                    client.enviarMissatge(Missatge.getMissatgeConectar(nom));
                    break;
                case "2":
                    String desti = getLinea(sc, "Destinatari:: ", true);
                    String miss = getLinea(sc, "Missatge a enviar: ", true);
                    client.enviarMissatge(Missatge.getMissatgePersonal(desti, miss));
                    break;
                case "3":
                    String grupMsg = getLinea(sc, "Missatge per a tothom: ", true);
                    client.enviarMissatge(Missatge.getMissatgeGrup(grupMsg));
                    break;
                case "5":
                    client.sortir = true;
                    client.enviarMissatge(Missatge.getMissatgeSortirTots("Adéu"));
                    break;
                default:
                    System.out.println("Opció no vàlida.");
            }
        }
        sc.close();
        client.tancarClient();
    }
}