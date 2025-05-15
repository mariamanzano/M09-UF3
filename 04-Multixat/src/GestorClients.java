public class GestorClients {
    private Socket clientSocket;
    private ObjectInputStream entrada;
    private ObjectOutputStream sortida;   
    private ServidorXat xat;
    private String nom;
    private boolean sortir;

    public GestorClients(Socket clientSocket, ObjectInputStream entrada, ObjectOutputStream sortida, ServidorXat xat, String nom) {
        this.clientSocket = clientSocket;
        this.entrada = entrada;
        this.sortida = sortida;
        this.xat = xat;
        this.nom = nom;
    }

    public String getNom() {
        return nom;
    }

    public void executar() {
        try {
            while(!sortir) {
                String missatge = (String) entrada.readObject();
                processaMissatge(missatge);
            }
        } catch (Exception e) {
            sortir = true;
        } finally {
            try {
                entrada.close();
                sortida.close();
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void enviarMissatge(String remitent, String missatge) {
        try {
            sortida.writeObject("Missatge de (" + remitent + "): " + missatge);
        } catch (IOException e) {
            System.out.println("Error enviant missatge a " + nom);
        }
    }

    public void processaMissatge(String missatge) {
        String codi = Missatge.getCodiMissatge(missatge);
        String[] parts = Missatge.getPartsMissatge(missatge);
        switch (codi) {
            case Missatge.CODI_CONECTAR:
                this.nom = parts[1];
                xat.afegirClient(nom, this);
                break;

            case Missatge.CODI_SORTIR_CLIENT:
                sortir = true;
                xat.eliminarClient(nom);
                break;

            case Missatge.CODI_SORTIR_TOTS:
                sortir = true;
                xat.finalitzarXat();
                break;

            case Missatge.CODI_MSG_PERSONAL:
                xat.enviarMissatgePersonal(parts[1], nom, parts[2]);
                break;

            case Missatge.CODI_MSG_GRUP:
                xat.enviarMissatgeGrup(parts[1]);
                break;

            default:
                System.out.println("Error: codi de missatge desconegut");
        }
    }
}