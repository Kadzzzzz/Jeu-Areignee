public class Pion {

    private final Joueur proprietaire;
    private Position position;
    private boolean estSurPlateau;

    public Pion(Joueur proprietaire) {
        if (proprietaire == null) {
            throw new IllegalArgumentException("Le proprietaire ne peut pas être null");
        }
        this.proprietaire = proprietaire;
        this.position = null;
        this.estSurPlateau = false;
    }

    public Joueur getProprietaire() {
        return proprietaire;
    }

    public Position getPosition() {
        return position;
    }

    public boolean estSurPlateau() {
        return estSurPlateau;
    }

    public void setPosition(Position nouvellePosition) {
        if (nouvellePosition == null) {
            throw new IllegalArgumentException("La position ne peut pas être null lors du placement");
        }
        this.position = nouvellePosition;
        this.estSurPlateau = true;
    }

    public void retirerDuPlateau() {
        this.position = null;
        this.estSurPlateau = false;
    }

    public boolean appartientA(Joueur joueur) {
        return joueur != null && proprietaire.equals(joueur);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Pion pion = (Pion) obj;
        return proprietaire.equals(pion.proprietaire) && 
               ((position == null && pion.position == null) || 
                (position != null && position.equals(pion.position)));
    }

    @Override
    public int hashCode() {
        int result = proprietaire.hashCode();
        result = 31 * result + (position != null ? position.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Pion [proprietaire=" + proprietaire.getNom() + ", position=" + position + 
               ", estSurPlateau=" + estSurPlateau + "]";
    }
}