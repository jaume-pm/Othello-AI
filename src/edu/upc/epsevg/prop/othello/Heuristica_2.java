/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.upc.epsevg.prop.othello;

/**
 *
 * @author Pollito
 *
 */
//Problemes: Valors massa propers, posar més diferencia entre ells.
public class Heuristica_2 {
    // Constants for the weights of different factors in the evaluation function
    private static final int MOBILITY_WEIGHT = 10;
    private static final int STABILITY_WEIGHT = 20;
    private static final int CORNER_WEIGHT = 50;
    private static final int EDGE_WEIGHT = 10;
    static String aliat;

    // Helper function to compute the mobility of a player
    private static int computeMobility(GameStatus s, CellType aliat1) {
        int legalMoves = s.getMoves().size();
        return legalMoves;
    }

    // Helper function to compute the stability of a player's pieces
    private static int computeStability(GameStatus s, CellType player) {
        int stability = 0;
        for (int i = 0; i < s.getSize(); i++) {
            for (int j = 0; j < s.getSize(); j++) {
                if (s.getPos(i, j) == player) {
                    // Check if the piece is on a corner
                    if ((i == 0 || i == s.getSize() - 1) && (j == 0 || j == s.getSize() - 1)) {
                        stability += CORNER_WEIGHT;
                    }
                    // Check if the piece is on an edge
                    else if (i == 0 || i == s.getSize() - 1 || j == 0 || j == s.getSize() - 1) {
                        stability += EDGE_WEIGHT;
                    }
                }
            }
        }
        return stability;
    }

    public static int heuristica(GameStatus s, CellType aliat) {
        int mobility = computeMobility(s, aliat) - computeMobility(s, aliat.opposite(aliat));
        int stability = computeStability(s, aliat) - computeStability(s, aliat.opposite(aliat));
        return MOBILITY_WEIGHT * mobility + STABILITY_WEIGHT * stability;
    }

}
