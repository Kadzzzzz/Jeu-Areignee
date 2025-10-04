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
    
    private void validerPosition(Position position) {
        if (position == null) {
            throw new IllegalArgumentException("La position ne peut pas être null");
        }
    }
}