public class VictoireChecker {

    public boolean aGagne(Plateau plateau, Joueur joueur) {
        if (plateau == null || joueur == null) {
            return false;
        }
        
        if (verifierLignes(plateau, joueur)) {
            return true;
        }
        
        if (verifierColonnes(plateau, joueur)) {
            return true;
        }
        
        return false;
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
