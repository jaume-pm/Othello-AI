/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.upc.epsevg.prop.othello.players.pintor;

import edu.upc.epsevg.prop.othello.CellType;
import edu.upc.epsevg.prop.othello.GameStatus;

/**
 * Heurística bàsica. Només mira el nombre de fitxes de cada jugador.
 *
 * @author Pollito
 */
//Problemes: No té en compte la posició de les fiches
public class Heuristica_0 {

    static int size = 0;
    static GameStatus gs;
    static CellType aliat;
    static int contadorAliat;
    static int contadorEnemic;

    /**
     * Inicialitza l'heurística amb l'estat actual del joc i el color del
     * jugador.
     *
     * @param s l'estat actual del joc
     * @param color el color del jugador
     */
    static void init(GameStatus s, CellType color) {
        gs = s;
        size = gs.getSize();
        aliat = color;
        contadorAliat = 0;
        contadorEnemic = 0;
    }

    /**
     * Compta el nombre de fitxes aliades i enemigues del tauler.
     */
    static void recorregut() {
        for (int f = 0; f < size; ++f) {
            for (int c = 0; c < size; ++c) {
                CellType ct = gs.getPos(c, f);
                if (ct == aliat) {
                    ++contadorAliat;
                } else if (ct.name() != "EMPTY") {
                    ++contadorEnemic;
                }
            }
        }
    }

    /**
     * Calcula el valor heurístic de l'estat actual del joc.
     *
     * @param s l'estat actual del joc
     * @param color el color del jugador
     * @return el valor heurístic
     */
    public static int heuristica(GameStatus s, CellType color) {
        init(s, color);
        recorregut();
        if (contadorAliat == 0) {
            return -10000;
        } else if (contadorEnemic == 0) {
            return 10000;
        } else {
            return contadorAliat - contadorEnemic;
        }
    }

    /**
     * Calcula el valor heurístic de l'estat actual del joc després de llegir la
     * documentació. Fa el mateix que la funció heuristica() però en 3 línies.
     *
     * @param s l'estat actual del joc
     * @param aliat el color del jugador
     * @return el valor heurístic
     */
    public static int heuristicaDespresDhaverLlegitLaDocumentacio(GameStatus s, CellType aliat) {
        if (s.getScore(aliat) == 0) {
            return -10000;
        } else if (s.getScore(CellType.opposite(aliat)) == 0) {
            return 10000;
        } else {
            return s.getScore(aliat) - s.getScore(CellType.opposite(aliat));
        }

    }
}
