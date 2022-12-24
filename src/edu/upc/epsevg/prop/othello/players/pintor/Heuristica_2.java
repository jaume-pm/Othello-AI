/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.upc.epsevg.prop.othello.players.pintor;

import edu.upc.epsevg.prop.othello.CellType;
import edu.upc.epsevg.prop.othello.GameStatus;

/**
 * Heurística millorada respecte a la 1. Ara ofereix valors dinàmics. Assigna
 * els valors tenint en compte la mobilitat dels jugadors i els extrems del
 * tauler (cantonades i vores) per determinar quin és el millor moviment a fer.
 *
 * @author Jaume i Roberto
 */
public class Heuristica_2 {

    // Constants per als pesos dels diferents factors en la funció d'avaluació
    private static final int PES_MOBILITAT = 10;
    private static final int PES_CANTONADA = 50;
    private static final int PES_VORA = 10;

    /**
     * Funció auxiliar que calcula la mobilitat d'un jugador. La mobilitat és la
     * quantitat de moviments legals que té el jugador en el tauler actual.
     *
     * @param s Estat del joc.
     * @return Mobilitat del jugador.
     */
    public static int calcularMobilitat(GameStatus s) {
        return s.getMoves().size() * PES_MOBILITAT;
    }

    /**
     * Funció auxiliar que calcula la mobilitat de l'enemic del jugador. La
     * mobilitat és la quantitat de moviments legals que té l'enemic en el
     * tauler actual.
     *
     * @param s Estat del joc.
     * @return Mobilitat de l'enemic.
     */
    public static int calcularMobilitatEnemic(GameStatus s) {
        GameStatus p = new GameStatus(s);
        p.skipTurn();
        return p.getMoves().size() * PES_MOBILITAT;
    }

    /**
     * Funció auxiliar que calcula els extrems del tauler ocupats per un
     * jugador. Els extrems són les cantonades i les vores del tauler.
     *
     * @param s Estat del joc.
     * @param jugador Jugador del qual es vol calcular els extrems ocupats.
     * @return Extrems ocupats pel jugador.
     */
    public static int calcularExtrems(GameStatus s, CellType jugador) {
        int extrems = 0;
        for (int i = 0; i < s.getSize(); i++) {
            for (int j = 0; j < s.getSize(); j++) {
                if (s.getPos(i, j) == jugador) {
                    // Comprovem si la peça és a una cantonada
                    if ((i == 0 || i == s.getSize() - 1) && (j == 0 || j == s.getSize() - 1)) {
                        extrems += PES_CANTONADA;
                    } // Comprovem si la peça és a un vora
                    else if (i == 0 || i == s.getSize() - 1 || j == 0 || j == s.getSize() - 1) {
                        extrems += PES_VORA;
                    }
                }
            }
        }
        return extrems;
    }

    /**
     * Funció principal de l'heurística que retorna una puntuació de l'estat del
     * joc actual. Aquesta puntuació es basa en la mobilitat dels jugadors i els
     * extrems del tauler ocupats per cada jugador.
     *
     * @param s Estat del joc.
     * @param aliat Aliat del qual es vol calcular la puntuació.
     * @return Valor heurístic de l'estat del joc.
     */
    public static int heuristica(GameStatus s, CellType aliat) {
        int mobilitat = calcularMobilitat(s) - calcularMobilitatEnemic(s);
        int extrems = calcularExtrems(s, aliat) - calcularExtrems(s, CellType.opposite(aliat));
        return mobilitat + extrems;
    }
}
