
public class Plateau {

    private final Pion[][] grille;

    public Plateau() {
        this.grille = new Pion[3][3];
        
    }
    
    public boolean estLibre(Position position) {
        validerPosition(position);
        return grille[position.getX()][position.getY()] == null;
    }
    
    public void placerPion(Pion pion, Position position) {
        if (pion == null) {
            throw new IllegalArgumentException("Le pion ne peut pas être null");
        }
        validerPosition(position);
        
        if (!estLibre(position)) {
            throw new IllegalArgumentException("La case " + position + " n'est pas libre");
        }
        
        grille[position.getX()][position.getY()] = pion;
        pion.setPosition(position);
    }
    
    public Pion retirerPion(Position position) {
        validerPosition(position);
        
        Pion pion = grille[position.getX()][position.getY()];
        
        if (pion != null) {
            grille[position.getX()][position.getY()] = null;
            pion.retirerDuPlateau();
        }
        
        return pion;
    }
    
    public Pion obtenirPion(Position position) {
        validerPosition(position);
        return grille[position.getX()][position.getY()];
    }
   
    public boolean estAdjacent(Position pos1, Position pos2) {
        validerPosition(pos1);
        validerPosition(pos2);
        return pos1.estAdjacente(pos2);
    }
    
   
    private void validerPosition(Position position) {
        if (position == null) {
            throw new IllegalArgumentException("La position ne peut pas être null");
        }
        if (!position.estValide()) {
            throw new IllegalArgumentException("Position invalide : " + position);
        }
    }
    
    public void reinitialiser() {
        for (int x = 0; x < 3; x++) {
            for (int y = 0; y < 3; y++) {
                if (grille[x][y] != null) {
                    grille[x][y].retirerDuPlateau();
                    grille[x][y] = null;
                }
            }
        }
    }
}