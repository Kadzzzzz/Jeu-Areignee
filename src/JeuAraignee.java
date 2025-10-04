/**
 * Gère la logique du jeu de l'Araignée.
 * Responsable des règles, de la validation des coups, et de la détection de victoire.
 * Le jeu se déroule en deux phases : PLACEMENT puis DEPLACEMENT.
 */
public class JeuAraignee {
    
    private final Plateau plateau;
    private final Joueur joueur1;
    private final Joueur joueur2;
    private final VictoireChecker victoireChecker;
    
    private Joueur joueurActuel;
    private Phase phaseActuelle;
    private boolean partieTerminee;
    
    public JeuAraignee(String nomJoueur1, String nomJoueur2) {
        this.plateau = new Plateau();
        this.joueur1 = new Joueur(nomJoueur1, "Rouge");
        this.joueur2 = new Joueur(nomJoueur2, "Bleu");
        this.victoireChecker = new VictoireChecker();
        
        creerPionsPourJoueur(joueur1);
        creerPionsPourJoueur(joueur2);
        
        this.joueurActuel = joueur1;
        this.phaseActuelle = Phase.PLACEMENT;
        this.partieTerminee = false;
    }
    
    private void creerPionsPourJoueur(Joueur joueur) {
        for (int i = 0; i < 3; i++) {
            joueur.ajouterPion(new Pion(joueur));
        }
    }
    
    /**
     * Place un pion du joueur actuel sur le plateau durant la phase de placement.
     * Change automatiquement de joueur après le placement.
     * Passe en phase DEPLACEMENT quand tous les pions sont placés.
     * 
     * @param position la position où placer le pion
     * @return true si ce placement fait gagner le joueur, false sinon
     * @throws IllegalArgumentException si la position est null, occupée, 
     *         ou si on n'est pas en phase de placement
     */
    public boolean placerPion(Position position) {
        validerCoupPlacement(position);
        
        Pion pionAplacer = joueurActuel.getPions().get(joueurActuel.getNbPionsPlaces());
        plateau.placerPion(pionAplacer, position);
        joueurActuel.incrementerPionsPlaces();
        
        if (victoireChecker.aGagne(plateau, joueurActuel)) {
            partieTerminee = true;
            return true;
        }
        
        verifierChangementPhase();
        changerJoueur();
        
        return false;
    }
    
    /**
     * Déplace un pion du joueur actuel durant la phase de déplacement.
     * Le déplacement doit être vers une case adjacente (pas de diagonales).
     * Change automatiquement de joueur après le déplacement.
     * 
     * @param origine la position actuelle du pion à déplacer
     * @param destination la position où déplacer le pion (doit être adjacente)
     * @return true si ce déplacement fait gagner le joueur, false sinon
     * @throws IllegalArgumentException si les positions sont null, si le pion
     *         n'appartient pas au joueur actuel, si la destination est occupée,
     *         ou si le déplacement n'est pas adjacent
     */
    public boolean deplacerPion(Position origine, Position destination) {
        validerCoupDeplacement(origine, destination);
        
        Pion pion = plateau.retirerPion(origine);
        plateau.placerPion(pion, destination);
        
        if (victoireChecker.aGagne(plateau, joueurActuel)) {
            partieTerminee = true;
            return true;
        }
        
        changerJoueur();
        return false;
    }
    
    private void changerJoueur() {
        joueurActuel = (joueurActuel == joueur1) ? joueur2 : joueur1;
    }
    
    private void verifierChangementPhase() {
        if (phaseActuelle == Phase.PLACEMENT && 
            joueur1.estTousPlaces() && 
            joueur2.estTousPlaces()) {
            phaseActuelle = Phase.DEPLACEMENT;
        }
    }
    
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
        
        Pion pion = plateau.obtenirPion(origine);
        if (pion == null) {
            throw new IllegalArgumentException("Aucun pion à la position " + origine);
        }
        if (!pion.appartientA(joueurActuel)) {
            throw new IllegalArgumentException("Ce pion ne vous appartient pas");
        }
        if (!plateau.estLibre(destination)) {
            throw new IllegalArgumentException("La destination " + destination + " est déjà occupée");
        }
        if (!origine.estAdjacente(destination)) {
            throw new IllegalArgumentException("Le déplacement doit se faire vers une case adjacente (haut, bas, gauche, droite)");
        }
        if (origine.equals(destination)) {
            throw new IllegalArgumentException("Vous devez déplacer le pion vers une autre case");
        }
    }
    
    public Plateau getPlateau() {
        return plateau;
    }
    
    public Joueur getJoueurActuel() {
        return joueurActuel;
    }
    
    public Phase getPhaseActuelle() {
        return phaseActuelle;
    }
    
    public boolean estPartieTerminee() {
        return partieTerminee;
    }
}