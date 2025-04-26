import java.io.ObjectInputStream;

public class FilServidorXat extends Thread {
    private ObjectInputStream entrada;
    private String nom;
    private static final String MSG_SORTIR = "sortir";

    public FilServidorXat(ObjectInputStream entrada, String nom) {
        this.entrada = entrada;
        this.nom = nom;
    }

    @Override
    public void run() {
        try {
            String missatge;
            while ((missatge = (String) entrada.readObject()) != null) {
                System.out.println("Rebut: " + missatge);
                if (missatge.equalsIgnoreCase(MSG_SORTIR)) break;
            }
        } catch (Exception e) {
            System.out.println("Error en la comunicació");
        }
        System.out.println("Fil de xat finalitzat.");
    }
}
