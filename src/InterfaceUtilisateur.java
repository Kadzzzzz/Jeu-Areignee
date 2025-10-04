import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Interface graphique du jeu de l'Araignée.
 * 
 * Responsabilités :
 * - Afficher la grille de jeu avec les pions
 * - Écouter les clics sur les cases
 * - Afficher les messages (tour actuel, victoire, erreurs)
 * - Permettre de recommencer une partie
 * 
 * Cette classe NE gère PAS :
 * - La logique du jeu (c'est JeuAraignee)
 * - Les règles (c'est JeuAraignee)
 * - La validation des coups (c'est JeuAraignee)
 */
public class InterfaceUtilisateur extends JFrame {
    
    // ========== COMPOSANTS GRAPHIQUES ==========
    
    private JButton[][] boutonsCases;  // Grille 3x3 de boutons
    private JTextField champNomRouge;
    private JTextField champNomBleu;
    private JLabel labelTour;
    
    // ========== JEU ==========
    
    private JeuAraignee jeu;
    
    // ========== ÉTAT POUR DÉPLACEMENT ==========
    
    private Position positionSelectionnee;  // Pour la phase de déplacement
    
    // ========== CONSTRUCTEUR ==========
    
    /**
     * Crée l'interface graphique.
     */
    public InterfaceUtilisateur() {
        creerInterface();
        demarrerNouvellePartie();
    }
    
    // ========== CRÉATION DE L'INTERFACE ==========
    
    /**
     * Crée tous les composants graphiques.
     */
    private void creerInterface() {
        setTitle("Jeu de l'Araignée");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        
        // Panneau du haut : noms des joueurs
        add(creerPanneauNoms(), BorderLayout.NORTH);
        
        // Panneau du centre : grille + label tour
        JPanel panneauCentre = new JPanel(new BorderLayout(10, 10));
        panneauCentre.add(creerLabelTour(), BorderLayout.NORTH);
        panneauCentre.add(creerGrille(), BorderLayout.CENTER);
        add(panneauCentre, BorderLayout.CENTER);
        
        // Panneau du bas : bouton recommencer
        add(creerPanneauBas(), BorderLayout.SOUTH);
        
        // Configuration finale
        pack();
        setLocationRelativeTo(null);  // Centrer la fenêtre
        setResizable(false);
    }
    
    /**
     * Crée le panneau avec les noms des joueurs.
     */
    private JPanel creerPanneauNoms() {
        JPanel panneau = new JPanel(new GridLayout(1, 2, 5, 0));
        
        // Zone rouge
        JPanel zoneRouge = new JPanel(new FlowLayout());
        zoneRouge.setBackground(new Color(255, 200, 200));  // Rose clair
        zoneRouge.add(new JLabel("Joueur Rouge:"));
        champNomRouge = new JTextField("Joueur 1", 10);
        zoneRouge.add(champNomRouge);
        
        // Zone bleue
        JPanel zoneBleu = new JPanel(new FlowLayout());
        zoneBleu.setBackground(new Color(200, 230, 255));  // Bleu clair
        zoneBleu.add(new JLabel("Joueur Bleu:"));
        champNomBleu = new JTextField("Joueur 2", 10);
        zoneBleu.add(champNomBleu);
        
        panneau.add(zoneRouge);
        panneau.add(zoneBleu);
        
        return panneau;
    }
    
    /**
     * Crée le label qui affiche le tour actuel.
     */
    private JLabel creerLabelTour() {
        labelTour = new JLabel("", SwingConstants.CENTER);
        labelTour.setFont(new Font("Arial", Font.BOLD, 16));
        labelTour.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        labelTour.setPreferredSize(new Dimension(400, 60));
        return labelTour;
    }
    
    /**
     * Crée la grille 3x3 de boutons.
     */
    private JPanel creerGrille() {
        JPanel panneauGrille = new JPanel(new GridLayout(3, 3, 5, 5));
        panneauGrille.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        boutonsCases = new JButton[3][3];
        
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                JButton bouton = new JButton();
                bouton.setPreferredSize(new Dimension(120, 120));
                bouton.setFont(new Font("Arial", Font.BOLD, 60));
                bouton.setBackground(Color.WHITE);
                bouton.setFocusPainted(false);
                
                final int x = i;
                final int y = j;
                
                // Écouteur de clic sur la case
                bouton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        gererClicCase(x, y);
                    }
                });
                
                boutonsCases[i][j] = bouton;
                panneauGrille.add(bouton);
            }
        }
        
        return panneauGrille;
    }
    
    /**
     * Crée le panneau du bas avec le bouton recommencer.
     */
    private JPanel creerPanneauBas() {
        JPanel panneau = new JPanel(new FlowLayout());
        
        JButton boutonRecommencer = new JButton("Recommencer");
        boutonRecommencer.setFont(new Font("Arial", Font.BOLD, 16));
        boutonRecommencer.addActionListener(e -> recommencer());
        
        panneau.add(boutonRecommencer);
        return panneau;
    }
    
    // ========== GESTION DES ÉVÉNEMENTS ==========
    
    /**
     * Gère le clic sur une case de la grille.
     * Délègue la logique à JeuAraignee.
     */
    private void gererClicCase(int x, int y) {
        Position position = new Position(x, y);
        
        try {
            if (jeu.getPhaseActuelle() == Phase.PLACEMENT) {
                // Phase de placement : placer un pion
                gererClicPlacement(position);
                
            } else {
                // Phase de déplacement : sélectionner puis déplacer
                gererClicDeplacement(position);
            }
            
        } catch (IllegalArgumentException e) {
            // Afficher l'erreur à l'utilisateur
            afficherErreur(e.getMessage());
        }
    }
    
    /**
     * Gère un clic en phase de placement.
     */
    private void gererClicPlacement(Position position) {
        // Demander à JeuAraignee de placer le pion
        boolean victoire = jeu.placerPion(position);
        
        // Rafraîchir l'affichage
        rafraichirAffichage();
        
        if (victoire) {
            afficherVictoire();
        }
    }
    
    /**
     * Gère un clic en phase de déplacement.
     */
    /**
 * Gère un clic en phase de déplacement.
 */
private void gererClicDeplacement(Position position) {
    Pion pionClique = jeu.getPlateau().obtenirPion(position);
    
    // Vérifier si on clique sur un de nos pions
    if (pionClique != null && pionClique.appartientA(jeu.getJoueurActuel())) {
        
        // Cas 1 : On clique sur le pion déjà sélectionné → désélectionner
        if (position.equals(positionSelectionnee)) {
            surlignerCase(position.getX(), position.getY(), Color.WHITE);
            positionSelectionnee = null;
        } 
        // Cas 2 : Un autre pion était sélectionné → échanger les sélections
        else if (positionSelectionnee != null) {
            surlignerCase(positionSelectionnee.getX(), positionSelectionnee.getY(), Color.WHITE);
            positionSelectionnee = position;
            surlignerCase(position.getX(), position.getY(), Color.YELLOW);
        }
        // Cas 3 : Aucun pion n'était sélectionné → sélectionner celui-ci
        else {
            positionSelectionnee = position;
            surlignerCase(position.getX(), position.getY(), Color.YELLOW);
        }
        
    } else {
        // On clique sur une case vide ou un pion adverse
        
        if (positionSelectionnee == null) {
            // Aucun pion sélectionné → erreur
            afficherErreur("Sélectionnez d'abord un de vos pions.");
        } else {
            // Un pion est sélectionné → tenter le déplacement
            try {
                boolean victoire = jeu.deplacerPion(positionSelectionnee, position);
                
                // Réinitialiser la sélection
                surlignerCase(positionSelectionnee.getX(), positionSelectionnee.getY(), Color.WHITE);
                positionSelectionnee = null;
                
                // Rafraîchir l'affichage
                rafraichirAffichage();
                
                if (victoire) {
                    afficherVictoire();
                }
                
            } catch (IllegalArgumentException e) {
                // En cas d'erreur, garder la sélection et afficher l'erreur
                afficherErreur(e.getMessage());
            }
        }
    }
}
    
    /**
     * Recommence une nouvelle partie.
     */
    private void recommencer() {
        // Réinitialiser la sélection
        positionSelectionnee = null;
        desurlignerToutesCases();
        
        // Démarrer une nouvelle partie
        demarrerNouvellePartie();
        
        // Rafraîchir l'affichage
        rafraichirAffichage();
    }
    
    // ========== GESTION DU JEU ==========
    
    /**
     * Démarre une nouvelle partie avec les noms des joueurs actuels.
     */
    private void demarrerNouvellePartie() {
        String nomRouge = champNomRouge.getText().trim();
        String nomBleu = champNomBleu.getText().trim();
        
        if (nomRouge.isEmpty()) nomRouge = "Joueur 1";
        if (nomBleu.isEmpty()) nomBleu = "Joueur 2";
        
        jeu = new JeuAraignee(nomRouge, nomBleu);
    }
    
    // ========== AFFICHAGE ==========
    
    /**
     * Rafraîchit tout l'affichage (grille + label tour).
     */
    private void rafraichirAffichage() {
        rafraichirGrille();
        rafraichirLabelTour();
    }
    
    /**
     * Met à jour la grille avec les pions actuels.
     */
    private void rafraichirGrille() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                Position pos = new Position(i, j);
                Pion pion = jeu.getPlateau().obtenirPion(pos);
                
                if (pion == null) {
                    // Case vide
                    boutonsCases[i][j].setText("");
                    boutonsCases[i][j].setBackground(Color.WHITE);
                    boutonsCases[i][j].setForeground(Color.BLACK);
                } else {
                    // Case avec pion
                    boutonsCases[i][j].setText("●");
                    
                    if (pion.getProprietaire().getCouleur().equalsIgnoreCase("Rouge")) {
                        boutonsCases[i][j].setForeground(Color.RED);
                    } else {
                        boutonsCases[i][j].setForeground(Color.BLUE);
                    }
                }
            }
        }
    }
    
    /**
     * Met à jour le label indiquant le tour actuel.
     */
    private void rafraichirLabelTour() {
        Joueur joueurActuel = jeu.getJoueurActuel();
        Phase phase = jeu.getPhaseActuelle();
        
        String message;
        if (phase == Phase.PLACEMENT) {
            int pionsRestants = 3 - joueurActuel.getNbPionsPlaces();
            message = String.format("PLACEMENT - Tour de %s (%d pions restants)", 
                joueurActuel.getNom(), pionsRestants);
        } else {
            message = String.format("DÉPLACEMENT - Tour de %s", joueurActuel.getNom());
        }
        
        labelTour.setText(message);
        
        // Couleur du texte selon le joueur
        if (joueurActuel.getCouleur().equalsIgnoreCase("Rouge")) {
            labelTour.setForeground(Color.RED);
        } else {
            labelTour.setForeground(Color.BLUE);
        }
    }
    
    /**
     * Surligne une case en changeant sa couleur de fond.
     */
    private void surlignerCase(int x, int y, Color couleur) {
        boutonsCases[x][y].setBackground(couleur);
    }
    
    /**
     * Enlève le surlignage de toutes les cases.
     */
    private void desurlignerToutesCases() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (jeu.getPlateau().estLibre(new Position(i, j))) {
                    boutonsCases[i][j].setBackground(Color.WHITE);
                }
            }
        }
    }
    
    // ========== MESSAGES ==========
    
    /**
     * Affiche un message d'information.
     */
    private void afficherMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "Information", JOptionPane.INFORMATION_MESSAGE);
    }
    
    /**
     * Affiche un message d'erreur.
     */
    private void afficherErreur(String message) {
        JOptionPane.showMessageDialog(this, message, "Erreur", JOptionPane.ERROR_MESSAGE);
    }
    
    /**
     * Affiche le message de victoire.
     */
    private void afficherVictoire() {
        Joueur gagnant = jeu.getJoueurActuel();
        String message = String.format("🎉 Victoire de %s ! 🎉", gagnant.getNom());
        
        JOptionPane.showMessageDialog(this, message, "Partie terminée", JOptionPane.INFORMATION_MESSAGE);
        
        // Proposer de recommencer
        int choix = JOptionPane.showConfirmDialog(this, 
            "Voulez-vous rejouer ?", 
            "Nouvelle partie", 
            JOptionPane.YES_NO_OPTION);
        
        if (choix == JOptionPane.YES_OPTION) {
            recommencer();
        }
    }
}