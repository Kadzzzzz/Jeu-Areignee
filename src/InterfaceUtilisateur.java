import javax.swing.*;
import java.awt.*;

/**
 * Interface graphique du jeu de l'Araignée.
 * Gère l'affichage et les interactions utilisateur (clics sur les cases).
 * Délègue toute la logique métier à JeuAraignee.
 */
public class InterfaceUtilisateur extends JFrame {
    
    private JButton[][] boutonsCases;
    private JTextField champNomRouge;
    private JTextField champNomBleu;
    private JLabel labelTour;
    private JeuAraignee jeu;
    private Position positionSelectionnee;
    
    public InterfaceUtilisateur() {
        creerInterface();
        demarrerNouvellePartie();
    }
    
    private void creerInterface() {
        setTitle("Jeu de l'Araignée");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        
        add(creerPanneauNoms(), BorderLayout.NORTH);
        
        JPanel panneauCentre = new JPanel(new BorderLayout(10, 10));
        panneauCentre.add(creerLabelTour(), BorderLayout.NORTH);
        panneauCentre.add(creerGrille(), BorderLayout.CENTER);
        add(panneauCentre, BorderLayout.CENTER);
        
        add(creerPanneauBas(), BorderLayout.SOUTH);
        
        pack();
        setLocationRelativeTo(null);
        setResizable(false);
    }
    
    private JPanel creerPanneauNoms() {
        JPanel panneau = new JPanel(new GridLayout(1, 2, 5, 0));
        
        JPanel zoneRouge = new JPanel(new FlowLayout());
        zoneRouge.setBackground(new Color(255, 200, 200));
        zoneRouge.add(new JLabel("Joueur Rouge:"));
        champNomRouge = new JTextField("Joueur 1", 10);
        zoneRouge.add(champNomRouge);
        
        JPanel zoneBleu = new JPanel(new FlowLayout());
        zoneBleu.setBackground(new Color(200, 230, 255));
        zoneBleu.add(new JLabel("Joueur Bleu:"));
        champNomBleu = new JTextField("Joueur 2", 10);
        zoneBleu.add(champNomBleu);
        
        panneau.add(zoneRouge);
        panneau.add(zoneBleu);
        
        return panneau;
    }
    
    private JLabel creerLabelTour() {
        labelTour = new JLabel("", SwingConstants.CENTER);
        labelTour.setFont(new Font("Arial", Font.BOLD, 16));
        labelTour.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        labelTour.setPreferredSize(new Dimension(400, 60));
        return labelTour;
    }
    
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
                
                bouton.addActionListener(e -> gererClicCase(x, y));
                
                boutonsCases[i][j] = bouton;
                panneauGrille.add(bouton);
            }
        }
        
        return panneauGrille;
    }
    
    private JPanel creerPanneauBas() {
        JPanel panneau = new JPanel(new FlowLayout());
        
        JButton boutonRecommencer = new JButton("Recommencer");
        boutonRecommencer.setFont(new Font("Arial", Font.BOLD, 16));
        boutonRecommencer.addActionListener(e -> recommencer());
        
        panneau.add(boutonRecommencer);
        return panneau;
    }
    
    private void gererClicCase(int x, int y) {
        Position position = new Position(x, y);
        
        try {
            if (jeu.getPhaseActuelle() == Phase.PLACEMENT) {
                gererClicPlacement(position);
            } else {
                gererClicDeplacement(position);
            }
        } catch (IllegalArgumentException e) {
            afficherErreur(e.getMessage());
        }
    }
    
    private void gererClicPlacement(Position position) {
        boolean victoire = jeu.placerPion(position);
        rafraichirAffichage();
        
        if (victoire) {
            afficherVictoire();
        }
    }
    
    /**
     * Gère les clics en phase de déplacement.
     * - Clic sur un pion du joueur actuel : sélection/désélection ou changement de sélection
     * - Clic sur une case vide : tentative de déplacement du pion sélectionné
     */
    private void gererClicDeplacement(Position position) {
        Pion pionClique = jeu.getPlateau().obtenirPion(position);
        
        if (pionClique != null && pionClique.appartientA(jeu.getJoueurActuel())) {
            if (position.equals(positionSelectionnee)) {
                // Désélection : clic sur le pion déjà sélectionné
                surlignerCase(position.getX(), position.getY(), Color.WHITE);
                positionSelectionnee = null;
            } else if (positionSelectionnee != null) {
                // Changement de sélection : passer d'un pion à un autre
                surlignerCase(positionSelectionnee.getX(), positionSelectionnee.getY(), Color.WHITE);
                positionSelectionnee = position;
                surlignerCase(position.getX(), position.getY(), Color.YELLOW);
            } else {
                // Première sélection
                positionSelectionnee = position;
                surlignerCase(position.getX(), position.getY(), Color.YELLOW);
            }
        } else {
            // Tentative de déplacement vers case vide ou pion adverse
            if (positionSelectionnee == null) {
                afficherErreur("Sélectionnez d'abord un de vos pions.");
            } else {
                try {
                    boolean victoire = jeu.deplacerPion(positionSelectionnee, position);
                    
                    surlignerCase(positionSelectionnee.getX(), positionSelectionnee.getY(), Color.WHITE);
                    positionSelectionnee = null;
                    rafraichirAffichage();
                    
                    if (victoire) {
                        afficherVictoire();
                    }
                } catch (IllegalArgumentException e) {
                    afficherErreur(e.getMessage());
                }
            }
        }
    }
    
    private void recommencer() {
        positionSelectionnee = null;
        
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                boutonsCases[i][j].setBackground(Color.WHITE);
            }
        }
        
        demarrerNouvellePartie();
        rafraichirAffichage();
    }
    
    private void demarrerNouvellePartie() {
        String nomRouge = champNomRouge.getText().trim();
        String nomBleu = champNomBleu.getText().trim();
        
        if (nomRouge.isEmpty()) nomRouge = "Joueur 1";
        if (nomBleu.isEmpty()) nomBleu = "Joueur 2";
        
        jeu = new JeuAraignee(nomRouge, nomBleu);
    }
    
    private void rafraichirAffichage() {
        rafraichirGrille();
        rafraichirLabelTour();
    }
    
    private void rafraichirGrille() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                Position pos = new Position(i, j);
                Pion pion = jeu.getPlateau().obtenirPion(pos);
                
                if (pion == null) {
                    boutonsCases[i][j].setText("");
                    boutonsCases[i][j].setBackground(Color.WHITE);
                    boutonsCases[i][j].setForeground(Color.BLACK);
                } else {
                    boutonsCases[i][j].setText("●");
                    boutonsCases[i][j].setForeground(
                        pion.getProprietaire().getCouleur().equalsIgnoreCase("Rouge") 
                            ? Color.RED 
                            : Color.BLUE
                    );
                }
            }
        }
    }
    
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
        labelTour.setForeground(
            joueurActuel.getCouleur().equalsIgnoreCase("Rouge") 
                ? Color.RED 
                : Color.BLUE
        );
    }
    
    private void surlignerCase(int x, int y, Color couleur) {
        boutonsCases[x][y].setBackground(couleur);
    }
    
    private void afficherErreur(String message) {
        JOptionPane.showMessageDialog(this, message, "Erreur", JOptionPane.ERROR_MESSAGE);
    }
    
    private void afficherVictoire() {
        Joueur gagnant = jeu.getJoueurActuel();
        String message = String.format("Victoire de %s !", gagnant.getNom());
        
        JOptionPane.showMessageDialog(this, message, "Partie terminée", JOptionPane.INFORMATION_MESSAGE);
        
        int choix = JOptionPane.showConfirmDialog(this, 
            "Voulez-vous rejouer ?", 
            "Nouvelle partie", 
            JOptionPane.YES_NO_OPTION);
        
        if (choix == JOptionPane.YES_OPTION) {
            recommencer();
        }
    }
}