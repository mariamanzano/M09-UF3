import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class Fitxer {
    private String nom;
    private byte[] contingut;
    
    public Fitxer(String nom) {
        this.nom = nom;
    }

    public byte[] getContingut() throws IOException {
        if (contingut == null) {
            File fitxer = new File(nom);
            if (!fitxer.exists() || !fitxer.isFile()) {
                throw new IOException("Fitxer no trobat: " + nom);
            }
            contingut = Files.readAllBytes(fitxer.toPath());
        }
        return contingut; 
    }
}
