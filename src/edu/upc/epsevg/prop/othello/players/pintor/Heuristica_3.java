/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.upc.epsevg.prop.othello.players.pintor;

import edu.upc.epsevg.prop.othello.CellType;
import static edu.upc.epsevg.prop.othello.CellType.opposite;
import edu.upc.epsevg.prop.othello.GameStatus;

/**
 * Heurística millorada respecte a la 2. Ara té en compte la estabilitat.
 * Assigna els valors tenint en compte la estabilitat, la mobilitat dels
 * jugadors i els extrems del tauler (cantonades i vores) per determinar quin és
 * el millor moviment a fer.
 *
 * @author Jaume i Roberto
 */

public class Heuristica_3 {

    private static final int PES_MOBILITAT = 20;

    private static final int PES_ESTABILITAT = 40;

    private static final int PES_CANTONADA = 1000;

    private static final int PES_VORA = 40;

    private static final int PES_FITXA = 1;

    static GameStatus gs = new GameStatus();

    /**
     * Calcula la mobilitat del jugador actual.
     *
     * @return El nombre de moviments possibles del jugador actual, multiplicat
     * pel pes de la mobilitat.
     */
    public static int calcularMobilitat() {
        return gs.getMoves().size() * PES_MOBILITAT;
    }

    /**
     * Calcula la mobilitat del jugador contrari.
     *
     * @return El nombre de moviments possibles del jugador contrari,
     * multiplicat pel pes de la mobilitat.
     */
    public static int calcularMobilitatEnemic() {
        GameStatus p = new GameStatus(gs);
        p.skipTurn();
        return p.getMoves().size() * PES_MOBILITAT;
    }

    static boolean esEstable(int c, int f) {
        boolean stable = esEstableVertical(c, f);
        if (stable) {
            stable = esEstableHoritzontal(c, f);
        }
        if (stable) {
            stable = esEstableDiagonal1(c, f);
        }
        if (stable) {
            stable = esEstableDiagonal2(c, f);
        }
        return stable;
    }

    // Helper function to compute the estabilitatAndExtrems of a player's pieces
    /**
     * Calcula la estabilitat i la presència de fitxes a cantonades o a vores
     * del tauler del jugador especificat.
     *
     * @param player El jugador del qual es vol calcular la estabilitat i la
     * presència de fitxes a cantonades o a vores del tauler.
     * @return La suma del pes de cada fitxa establa del jugador, més el pes de
     * les fitxes del jugador que es troben a cantonades o a vores del tauler.
     */
    public static int calculaEstabilitatAndExtrems(CellType player) {
        int estabilitatAndExtrems = 0;
        for (int i = 0; i < gs.getSize(); i++) {
            for (int j = 0; j < gs.getSize(); j++) {
                if (gs.getPos(i, j) == player) {
                    estabilitatAndExtrems += PES_FITXA;
                    if (esEstable(i, j)) {
                        estabilitatAndExtrems += PES_ESTABILITAT;
                    }
                    // Si la peça es troba a una cantonada
                    if ((i == 0 || i == gs.getSize() - 1) && (j == 0 || j == gs.getSize() - 1)) {
                        estabilitatAndExtrems += PES_CANTONADA;
                    } // Si la peça es troba a una vora
                    else if (i == 0 || i == gs.getSize() - 1 || j == 0 || j == gs.getSize() - 1) {
                        estabilitatAndExtrems += PES_VORA;
                    }
                }
            }
        }
        return estabilitatAndExtrems;
    }

    /**
     * Funció principal de l'heurística que retorna una puntuació de l'estat del
     * joc actual. Aquesta puntuació es basa en la mobilitat dels jugadors, els
     * extrems del tauler ocupats per cada jugador i la estabilitat.
     *
     * @param s Estat del joc.
     * @param aliat Aliat del qual es vol calcular la puntuació.
     * @return Valor heurístic de l'estat del joc.
     */
    public static int heuristica(GameStatus s, CellType aliat) {
        gs = s;
        int estabilitatAndExtrems = calculaEstabilitatAndExtrems(aliat) - calculaEstabilitatAndExtrems(CellType.opposite(aliat));
        int mobilitat = calcularMobilitat() - calcularMobilitatEnemic();
        if (gs.getScore(aliat) + gs.getScore(opposite(aliat)) == 64) {
            if (gs.getScore(aliat) <= gs.getScore(opposite(aliat))) {
                estabilitatAndExtrems -= 100000;
            } else {
                estabilitatAndExtrems += 100000;
            }
        }
        return estabilitatAndExtrems + mobilitat;
    }

    /**
     * Comprova si una fitxa és estable en direcció vertical.
     *
     * @param c La columna de la fitxa.
     * @param f La fila de la fitxa.
     * @return True si la fitxa és estable en direcció vertical, False en cas
     * contrari.
     */
    public static boolean esEstableVertical(int c, int f) {
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
        while (f < gs.getSize() && !casellaBlanca2) {
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

    /**
     * Comprova si una fitxa és estable en direcció horitzontal.
     *
     * @param c La columna de la fitxa.
     * @param f La fila de la fitxa.
     * @return True si la fitxa és estable en direcció horitzontal, False en cas
     * contrari.
     */
    public static boolean esEstableHoritzontal(int c, int f) {
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
        while (c < gs.getSize() && !casellaBlanca2) {
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

    /**
     * Comprova si una fitxa és estable en direcció diagonal principal.
     *
     * @param c La columna de la fitxa.
     * @param f La fila de la fitxa.
     * @return True si la fitxa és estable en direcció diagonal principal, False
     * en cas contrari.
     */
    public static boolean esEstableDiagonal1(int c, int f) {
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
        while (c < gs.getSize() && f < gs.getSize() && !casellaBlanca2) {
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

    /**
     * Comprova si una fitxa és estable en direcció diagonal secundària.
     *
     * @param c La columna de la fitxa.
     * @param f La fila de la fitxa.
     * @return True si la fitxa és estable en direcció diagonal secundària,
     * False en cas contrari.
     */
    public static boolean esEstableDiagonal2(int c, int f) {
        CellType origen = gs.getPos(c, f);
        int corigen = c, forigen = f;
        boolean enemic = false, casellaBlanca = false;
        while (c >= 0 && f < gs.getSize() && !casellaBlanca) {
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
        while (c < gs.getSize() && f >= 0 && !casellaBlanca2) {
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
