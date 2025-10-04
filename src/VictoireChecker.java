/**
 * Vérifie les conditions de victoire du jeu de l'Araignée.
 * Un joueur gagne en alignant 3 pions sur une ligne ou une colonne.
 * Note : Les diagonales ne comptent PAS comme victoire.
 */
public class VictoireChecker {

    /**
     * Vérifie si un joueur a gagné en alignant 3 pions sur une ligne ou une colonne.
     * 
     * @param plateau le plateau de jeu à vérifier
     * @param joueur le joueur dont on vérifie la victoire
     * @return true si le joueur a 3 pions alignés (ligne ou colonne), false sinon
     */
    public boolean aGagne(Plateau plateau, Joueur joueur) {
        if (plateau == null || joueur == null) {
            return false;
        }
        return verifierLignes(plateau, joueur) || verifierColonnes(plateau, joueur);
    }

    private boolean verifierLignes(Plateau plateau, Joueur joueur) {
        for (int ligne = 0; ligne < 3; ligne++) {
            if (verifierLigne(plateau, joueur, ligne)) {
                return true;
            }
        }
        return false;
    }
    
    private boolean verifierLigne(Plateau plateau, Joueur joueur, int ligne) {
        for (int colonne = 0; colonne < 3; colonne++) {
            Position pos = new Position(ligne, colonne);
            Pion pion = plateau.obtenirPion(pos);
            
            if (pion == null || !pion.appartientA(joueur)) {
                return false;
            }
        }
        return true;
    }
    
    private boolean verifierColonnes(Plateau plateau, Joueur joueur) {
        for (int colonne = 0; colonne < 3; colonne++) {
            if (verifierColonne(plateau, joueur, colonne)) {
                return true;
            }
        }
        return false;
    }

    private boolean verifierColonne(Plateau plateau, Joueur joueur, int colonne) {
        for (int ligne = 0; ligne < 3; ligne++) {
            Position pos = new Position(ligne, colonne);
            Pion pion = plateau.obtenirPion(pos);
            
            if (pion == null || !pion.appartientA(joueur)) {
                return false;
            }
        }
        return true;
    }
}