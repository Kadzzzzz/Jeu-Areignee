public class JeuAraignee {
    
    // ========== ATTRIBUTS ==========
    
    private final Plateau plateau;
    private final Joueur joueur1;
    private final Joueur joueur2;
    private final VictoireChecker victoireChecker;
    
    private Joueur joueurActuel;
    private Phase phaseActuelle;
    private boolean partieTerminee;
    
    // ========== CONSTRUCTEUR ==========
    
    /**
     * Crée une nouvelle partie avec deux joueurs.
     * 
     * @param nomJoueur1 nom du premier joueur (Rouge)
     * @param nomJoueur2 nom du deuxième joueur (Bleu)
     */
    public JeuAraignee(String nomJoueur1, String nomJoueur2) {
        // Créer les composants
        this.plateau = new Plateau();
        this.joueur1 = new Joueur(nomJoueur1, "Rouge");
        this.joueur2 = new Joueur(nomJoueur2, "Bleu");
        this.victoireChecker = new VictoireChecker();
        
        // Créer les 3 pions pour chaque joueur
        creerPionsPourJoueur(joueur1);
        creerPionsPourJoueur(joueur2);
        
        // État initial
        this.joueurActuel = joueur1;  // Joueur 1 (Rouge) commence
        this.phaseActuelle = Phase.PLACEMENT;
        this.partieTerminee = false;
    }
    
    /**
     * Crée 3 pions pour un joueur.
     */
    private void creerPionsPourJoueur(Joueur joueur) {
        for (int i = 0; i < 3; i++) {
            Pion pion = new Pion(joueur);
            joueur.ajouterPion(pion);
        }
    }
    
    // ========== MÉTHODES PRINCIPALES ==========
    
    /**
     * Place un pion sur le plateau (phase de placement).
     * 
     * @param position la position où placer le pion
     * @return true si le placement a provoqué une victoire, false sinon
     * @throws IllegalArgumentException si le coup n'est pas valide
     */
    public boolean placerPion(Position position) {
        // Validation
        validerCoupPlacement(position);
        
        // Récupérer le prochain pion du joueur à placer
        Pion pionAplacer = joueurActuel.getPions().get(joueurActuel.getNbPionsPlaces());
        
        // Placer le pion sur le plateau
        plateau.placerPion(pionAplacer, position);
        
        // Incrémenter le compteur du joueur
        joueurActuel.incrementerPionsPlaces();
        
        // Vérifier la victoire
        if (victoireChecker.aGagne(plateau, joueurActuel)) {
            partieTerminee = true;
            return true;
        }
        
        // Vérifier si on doit changer de phase
        verifierChangementPhase();
        
        // Changer de joueur
        changerJoueur();
        
        return false;
    }
    
    /**
     * Déplace un pion sur le plateau (phase de déplacement).
     * 
     * @param origine position actuelle du pion
     * @param destination nouvelle position du pion
     * @return true si le déplacement a provoqué une victoire, false sinon
     * @throws IllegalArgumentException si le coup n'est pas valide
     */
    public boolean deplacerPion(Position origine, Position destination) {
        // Validation
        validerCoupDeplacement(origine, destination);
        
        // Effectuer le déplacement
        Pion pion = plateau.retirerPion(origine);
        plateau.placerPion(pion, destination);
        
        // Vérifier la victoire
        if (victoireChecker.aGagne(plateau, joueurActuel)) {
            partieTerminee = true;
            return true;
        }
        
        // Changer de joueur
        changerJoueur();
        
        return false;
    }
    
    // ========== GESTION DES TOURS ET PHASES ==========
    
    /**
     * Change le joueur actuel (alterne entre joueur1 et joueur2).
     */
    private void changerJoueur() {
        joueurActuel = (joueurActuel == joueur1) ? joueur2 : joueur1;
    }
    
    /**
     * Vérifie si on doit passer de la phase PLACEMENT à DEPLACEMENT.
     */
    private void verifierChangementPhase() {
        if (phaseActuelle == Phase.PLACEMENT && 
            joueur1.estTousPlaces() && 
            joueur2.estTousPlaces()) {
            
            phaseActuelle = Phase.DEPLACEMENT;
        }
    }
    
    // ========== VALIDATION DES COUPS ==========
    
    /**
     * Valide un coup de placement.
     * 
     * @throws IllegalArgumentException si le coup n'est pas valide
     */
    private void validerCoupPlacement(Position position) {
        if (position == null) {
            throw new IllegalArgumentException("La position ne peut pas être null");
        }
        
        if (partieTerminee) {
            throw new IllegalArgumentException("La partie est terminée");
        }
        
        if (phaseActuelle != Phase.PLACEMENT) {
            throw new IllegalArgumentException("Nous sommes en phase de déplacement, vous ne pouvez plus placer de pions");
        }
        
        if (joueurActuel.estTousPlaces()) {
            throw new IllegalArgumentException("Vous avez déjà placé tous vos pions");
        }
        
        if (!plateau.estLibre(position)) {
            throw new IllegalArgumentException("La case " + position + " est déjà occupée");
        }
    }
    
    /**
     * Valide un coup de déplacement.
     * 
     * @throws IllegalArgumentException si le coup n'est pas valide
     */
    private void validerCoupDeplacement(Position origine, Position destination) {
        if (origine == null || destination == null) {
            throw new IllegalArgumentException("Les positions ne peuvent pas être null");
        }
        
        if (partieTerminee) {
            throw new IllegalArgumentException("La partie est terminée");
        }
        
        if (phaseActuelle != Phase.DEPLACEMENT) {
            throw new IllegalArgumentException("Nous sommes en phase de placement, vous devez d'abord placer vos pions");
        }
        
        // Vérifier qu'il y a un pion à l'origine
        Pion pion = plateau.obtenirPion(origine);
        if (pion == null) {
            throw new IllegalArgumentException("Aucun pion à la position " + origine);
        }
        
        // Vérifier que le pion appartient au joueur actuel
        if (!pion.appartientA(joueurActuel)) {
            throw new IllegalArgumentException("Ce pion ne vous appartient pas");
        }
        
        // Vérifier que la destination est libre
        if (!plateau.estLibre(destination)) {
            throw new IllegalArgumentException("La destination " + destination + " est déjà occupée");
        }
        
        // Vérifier que le déplacement est adjacent (pas de diagonales)
        if (!plateau.estAdjacent(origine, destination)) {
            throw new IllegalArgumentException("Le déplacement doit se faire vers une case adjacente (haut, bas, gauche, droite)");
        }
        
        // Vérifier qu'on ne reste pas sur place
        if (origine.equals(destination)) {
            throw new IllegalArgumentException("Vous devez déplacer le pion vers une autre case");
        }
    }
    
    // ========== GETTERS ==========
    
    /**
     * Retourne le plateau de jeu.
     * 
     * @return le plateau
     */
    public Plateau getPlateau() {
        return plateau;
    }
    
    /**
     * Retourne le premier joueur (Rouge).
     * 
     * @return joueur 1
     */
    public Joueur getJoueur1() {
        return joueur1;
    }
    
    /**
     * Retourne le deuxième joueur (Bleu).
     * 
     * @return joueur 2
     */
    public Joueur getJoueur2() {
        return joueur2;
    }
    
    /**
     * Retourne le joueur dont c'est le tour.
     * 
     * @return le joueur actuel
     */
    public Joueur getJoueurActuel() {
        return joueurActuel;
    }
    
    /**
     * Retourne la phase actuelle du jeu.
     * 
     * @return la phase (PLACEMENT ou DEPLACEMENT)
     */
    public Phase getPhaseActuelle() {
        return phaseActuelle;
    }
    
    /**
     * Vérifie si la partie est terminée.
     * 
     * @return true si la partie est terminée
     */
    public boolean estPartieTerminee() {
        return partieTerminee;
    }
}