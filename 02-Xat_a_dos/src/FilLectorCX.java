import java.io.ObjectInputStream;

public class FilLectorCX extends Thread {
    private ObjectInputStream entrada;

    public FilLectorCX(ObjectInputStream entrada) {
        this.entrada = entrada;
    }

    @Override
    public void run() {
        System.out.println("Fil de lectura iniciat");
        try {
            String missatge;
            while ((missatge = (String) entrada.readObject()) != null) {
                System.out.println("Rebut: " + missatge);
            }
        } catch (Exception e) {
            System.out.println("El servidor ha tancat la connexió.");
        }
    }
}
