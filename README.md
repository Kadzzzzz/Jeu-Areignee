# Jeu de l'Araignée

Projet Java développé dans le cadre du BE du cours d'approfondissement "Applications concurrentes, mobiles et réparties en Java" à Centrale Lyon.

## Description

Jeu de stratégie à deux joueurs sur grille 3×3. Chaque joueur dispose de 3 pions et tente d'aligner ses pions avant son adversaire.

**Deux phases de jeu :**
- **Placement** : Les joueurs posent alternativement leurs 3 pions
- **Déplacement** : Les joueurs déplacent leurs pions vers des cases adjacentes

Victoire par alignement de 3 pions sur une ligne ou colonne (diagonales exclues).

## Lancement

```bash
javac *.java
java Main
```

## Architecture

- **Modèle** : `Plateau`, `Joueur`, `Pion`, `Position`, `VictoireChecker`
- **Contrôleur** : `JeuAraignee`
- **Vue** : `InterfaceUtilisateur` (Java Swing)

## Auteur

Jeremy Luccioni - Centrale Lyon (2024)
