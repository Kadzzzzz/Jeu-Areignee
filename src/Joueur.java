import java.util.ArrayList;
import java.util.List;

public class Joueur {

    private static final int MAX_PIONS=3;
    private final String nom;
    private final String couleur;
    private final List<Pion> pions;
    private int nbPionsPlaces;

    public Joueur(String nom, String couleur){
        if (nom==null){
            throw new IllegalArgumentException("Le nom du joueur ne peut pas être null");
        }
        if (couleur==null){
            throw new IllegalArgumentException("La couleur du joueur ne peut pas être null");
        }
        this.nom=nom;
        this.couleur=couleur;
        this.pions= new ArrayList<>();
        this.nbPionsPlaces=0;

        
    }

    public String getNom() {
        return nom;
    }

    public String getCouleur() {
        return couleur;
    }

    public List<Pion> getPions() {
        return pions;
    }

    public int getNbPionsPlaces() {
        return nbPionsPlaces;
    }

    public int getNbPionsRestants(){
        return MAX_PIONS-this.nbPionsPlaces;
    }

    public void ajouterPion(Pion pion){
        if (pion==null){
            throw new IllegalArgumentException("Le pion ne peut pas être null");
        }
        if (pions.size()>=MAX_PIONS){
            throw new IllegalStateException("Le joueur à déjà "+ MAX_PIONS +" pions");
        }

        pions.add(pion);
    }

    public void incrementerPionPlaces(){
        if (nbPionsPlaces<MAX_PIONS){
            nbPionsPlaces++;
        }
    }

    public boolean estTousPlaces(){
        return nbPionsPlaces==MAX_PIONS;
    }

    public void incrementerPionsPlaces() {
    if (nbPionsPlaces < 3) {
        nbPionsPlaces++;
    }
}

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((nom == null) ? 0 : nom.hashCode());
        result = prime * result + ((couleur == null) ? 0 : couleur.hashCode());
        result = prime * result + ((pions == null) ? 0 : pions.hashCode());
        result = prime * result + nbPionsPlaces;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Joueur other = (Joueur) obj;
        if (nom == null) {
            if (other.nom != null)
                return false;
        } else if (!nom.equals(other.nom))
            return false;
        if (couleur == null) {
            if (other.couleur != null)
                return false;
        } else if (!couleur.equals(other.couleur))
            return false;
        if (pions == null) {
            if (other.pions != null)
                return false;
        } else if (!pions.equals(other.pions))
            return false;
        if (nbPionsPlaces != other.nbPionsPlaces)
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "Joueur [nom=" + nom + ", couleur=" + couleur + ", pions=" + pions + ", nbPionsPlaces=" + nbPionsPlaces
                + "]";
    }    
}

