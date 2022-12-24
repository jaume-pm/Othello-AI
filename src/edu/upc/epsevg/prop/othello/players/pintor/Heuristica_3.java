/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.upc.epsevg.prop.othello.players.pintor;

import edu.upc.epsevg.prop.othello.CellType;
import static edu.upc.epsevg.prop.othello.CellType.opposite;
import edu.upc.epsevg.prop.othello.GameStatus;

/**
 *
 * @author Pollito
 *
 */
//Problemes: Valors massa propers, posar més diferencia entre ells.
public class Heuristica_3 {

    // Constants for the weights of different factors in the evaluation function
    private static final int MOBILITY_WEIGHT = 20;
    private static final int STABILITY_WEIGHT = 40;
    private static final int CORNER_WEIGHT = 1000;
    private static final int EDGE_WEIGHT = 40;
    private static final int PIECE_VALUE = 1;

    private static int corners = 0;
    static CellType aliat;
    static int size = 0;
    static GameStatus gs = new GameStatus();

    // Helper function to compute the mobility of a player
    private static int computeMobility(GameStatus s) {
        int legalMoves = s.getMoves().size();
        return legalMoves;
    }

    private static int computeEnemyMobility(GameStatus s) {
        GameStatus p = new GameStatus(s);
        p.skipTurn();
        int legalMoves = p.getMoves().size();
        return legalMoves;
    }

    private static void init(GameStatus s, CellType paliat) {
        corners = 0;
        gs = s;
        size = gs.getSize();
        aliat = paliat;
    }

    private static CellType getPosFiltre(int col, int fil) {
        CellType res;
        if (0 <= col && col < size && 0 <= fil && fil < size) {
            res = gs.getPos(col, fil);
        } else {
            res = CellType.EMPTY;
        }
        return res;
    }

    static boolean isStable(int c, int f) {
        boolean stable = true;
        stable = isStableVertical(c, f);
        if (stable) {
            stable = isStableHorizontal(c, f);
        }
        if (stable) {
            stable = isStableDiagonal1(c, f);
        }
        if (stable) {
            stable = isStableDiagonal2(c, f);
        }
        if (stable) {
            //System.out.print("La posició: " + "fila: " + f + " columna: " + c + " es estable :D" + "\n");
        }
        return stable;
    }

    static boolean isCorner(int f, int c) {
        // Check if the position is a top left corner
        if (f == 0 && c == 0) {
            return true;
        }

        // Check if the position is a top right corner
        if (f == 0 && c == size - 1) {
            return true;
        }

        // Check if the position is a bottom left corner
        if (f == size - 1 && c == 0) {
            return true;
        }

        // Check if the position is a bottom right corner
        if (f == size - 1 && c == size - 1) {
            return true;
        }

        // If the position is none of the above, it is not a corner
        return false;
    }

    // Helper function to compute the stability of a player's pieces
    private static int computeStability(GameStatus s, CellType player) {
        int stability = 0;
        for (int i = 0; i < s.getSize(); i++) {
            for (int j = 0; j < s.getSize(); j++) {
                if (s.getPos(i, j) == player) {
                    stability += PIECE_VALUE;
                    if (isStable(i, j)) {
                        stability += STABILITY_WEIGHT;
                    }
                    // Check if the piece is on a corner
                    if ((i == 0 || i == s.getSize() - 1) && (j == 0 || j == s.getSize() - 1)) {
                        stability += CORNER_WEIGHT;
                    } // Check if the piece is on an edge
                    else if (i == 0 || i == s.getSize() - 1 || j == 0 || j == s.getSize() - 1) {
                        stability += EDGE_WEIGHT;
                    }
                }
            }
        }
        return stability;
    }

    public static int heuristica(GameStatus s, CellType aliat) {
        init(s, aliat);
        int stability = computeStability(s, aliat) - computeStability(s, aliat.opposite(aliat));
        int mobility = computeMobility(s) - computeEnemyMobility(s);
        if (gs.getScore(aliat) + gs.getScore(opposite(aliat)) == 64) {
            if (gs.getScore(aliat) <= gs.getScore(opposite(aliat))) {
                stability -= 100000;
            } else {
                stability += 100000;
            }
        }
        return stability + mobility * MOBILITY_WEIGHT;
    }

    private static boolean isStableVertical(int c, int f) {
        CellType origen = gs.getPos(c, f);
        int forigen = f;
        boolean enemic = false, casellaBlanca = false;
        while (f >= 0 && !casellaBlanca) {
            CellType actual = gs.getPos(c, f);
            if (actual == CellType.EMPTY) {
                casellaBlanca = true;
            }
            if (actual == opposite(origen)) {
                enemic = true;
            }
            --f;
        }
        boolean enemic2 = false, casellaBlanca2 = false;
        f = forigen;
        while (f < size && !casellaBlanca2) {
            CellType actual = gs.getPos(c, f);
            if (actual == CellType.EMPTY) {
                casellaBlanca2 = true;
            }
            if (actual == opposite(origen)) {
                enemic2 = true;
            }
            ++f;
        }

        if (!casellaBlanca && !enemic || !casellaBlanca2 && !enemic2) {
            return true;
        }

        if (!casellaBlanca && !casellaBlanca2) {
            return true;
        }

        return false;
    }

    private static boolean isStableHorizontal(int c, int f) {
        CellType origen = gs.getPos(c, f);
        int corigen = c;
        boolean enemic = false, casellaBlanca = false;
        while (c >= 0 && !casellaBlanca) {
            CellType actual = gs.getPos(c, f);
            if (actual == CellType.EMPTY) {
                casellaBlanca = true;
            }
            if (actual == opposite(origen)) {
                enemic = true;
            }
            --c;
        }
        boolean enemic2 = false, casellaBlanca2 = false;
        c = corigen;
        while (c < size && !casellaBlanca2) {
            CellType actual = gs.getPos(c, f);
            if (actual == CellType.EMPTY) {
                casellaBlanca2 = true;
            }
            if (actual == opposite(origen)) {
                enemic2 = true;
            }
            ++c;
        }

        if (!casellaBlanca && !enemic || !casellaBlanca2 && !enemic2) {
            return true;
        }

        if (!casellaBlanca && !casellaBlanca2) {
            return true;
        }

        return false;
    }

    private static boolean isStableDiagonal1(int c, int f) {
        CellType origen = gs.getPos(c, f);
        int corigen = c, forigen = f;
        boolean enemic = false, casellaBlanca = false;
        while (c >= 0 && f >= 0 && !casellaBlanca) {
            CellType actual = gs.getPos(c, f);
            if (actual == CellType.EMPTY) {
                casellaBlanca = true;
            }
            if (actual == opposite(origen)) {
                enemic = true;
            }
            --c;
            --f;
        }
        boolean enemic2 = false, casellaBlanca2 = false;
        c = corigen;
        f = forigen;
        while (c < size && f < size && !casellaBlanca2) {
            CellType actual = gs.getPos(c, f);
            if (actual == CellType.EMPTY) {
                casellaBlanca2 = true;
            }
            if (actual == opposite(origen)) {
                enemic2 = true;
            }
            ++c;
            ++f;
        }

        if (!casellaBlanca && !enemic || !casellaBlanca2 && !enemic2) {
            return true;
        }

        if (!casellaBlanca && !casellaBlanca2) {
            return true;
        }

        return false;
    }

    private static boolean isStableDiagonal2(int c, int f) {
        CellType origen = gs.getPos(c, f);
        int corigen = c, forigen = f;
        boolean enemic = false, casellaBlanca = false;
        while (c >= 0 && f < size && !casellaBlanca) {
            CellType actual = gs.getPos(c, f);
            if (actual == CellType.EMPTY) {
                casellaBlanca = true;
            }
            if (actual == opposite(origen)) {
                enemic = true;
            }
            --c;
            ++f;
        }
        boolean enemic2 = false, casellaBlanca2 = false;
        c = corigen;
        f = forigen;
        while (c < size && f >= 0 && !casellaBlanca2) {
            CellType actual = gs.getPos(c, f);
            if (actual == CellType.EMPTY) {
                casellaBlanca2 = true;
            }
            if (actual == opposite(origen)) {
                enemic2 = true;
            }
            ++c;
            --f;
        }

        if (!casellaBlanca && !enemic || !casellaBlanca2 && !enemic2) {
            return true;
        }

        if (!casellaBlanca && !casellaBlanca2) {
            return true;
        }

        return false;
    }

}
