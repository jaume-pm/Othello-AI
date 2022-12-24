/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.upc.epsevg.prop.othello.players.pintor;

import edu.upc.epsevg.prop.othello.CellType;
import edu.upc.epsevg.prop.othello.GameStatus;

/**
 * Heurística millorada respecte a la 0. Assigna pesos a cada casella. Tot i
 * així aquests pesos són estàtics, és a dir, o varien segons l'estat dFel joc.
 *
 * @author Jaume i Roberto
 */
//Problemes: Assigna valors estàtics a cada casella. No varien segons l'estat 
//           del joc.
public class Heuristica_1 {

    /**
     * Matriu que conté els valors assignats a cada casella del tauler.
     */
    static int[][] matrix = {{300, -50, 30, 30, 30, 30, -50, 300},
    {-50, -100, -20, -20, -20, -20, -100, -50},
    {30, -20, 1, 1, 1, 1, -20, 30},
    {30, -20, 1, 1, 1, 1, -20, 30},
    {30, -20, 1, 1, 1, 1, -20, 30},
    {30, -20, 1, 1, 1, 1, -20, 30},
    {-50, -100, -20, -20, -20, -20, -100, -50},
    {300, -50, 30, 30, 30, 30, -50, 300}};

    /**
     * Mida del tauler.
     */
    static int size = 0;

    /**
     * Estat del joc.
     */
    static GameStatus gs = new GameStatus();

    /**
     * String que indica quin és el color de l'aliat.
     */
    static String aliat;

    /**
     * Comptador de caselles ocupades per l'aliat.
     */
    static int contadorAliat;

    /**
     * Comptador de caselles ocupades per l'enemic.
     */
    static int contadorEnemic;

    /**
     * Inicialitza les variables globals de la classe amb les dades del joc i
     * del jugador.
     *
     * @param s Estat del joc.
     * @param aliatp Color del jugador aliat.
     */
    static void init(GameStatus s, CellType aliatp) {
        contadorAliat = 0;
        contadorEnemic = 0;
        gs = s;
        size = gs.getSize();
        aliat = aliatp.toString();
    }

    /**
     * Retorna el valor d'una casella específica de la matriu "matrix".
     *
     * @param c Columna de la casella.
     * @param f Fila de la casella.
     * @return Valor de la casella de la matriu "matrix".
     */
    static int getPes(int c, int f) {
        return matrix[f][c];
    }

    /**
     * Recorre totes les caselles del tauler i, suma el valor del pes de la
     * casella al comptador del jugador corresponent (contadorAliat o
     * contadorEnemic).
     */
    static void recorregut() {
        for (int f = 0; f < size; ++f) {
            for (int c = 0; c < size; ++c) {
                CellType ct = gs.getPos(c, f);
                if (ct.name() == aliat) {
                    contadorAliat = contadorAliat + getPes(c, f);
                } else if (ct.name() != "EMPTY") {
                    contadorEnemic = contadorEnemic + getPes(c, f);
                }
            }
        }
    }

    /**
     * Retornael valor heurístic, que és la diferència entre els contadors dels
     * dos jugadors. Si l'enemic no té cap casella retorna 100000 i si és
     * l'aliat qui no en té cap retorna -100000.
     *
     * @param s Estat del joc.
     * @param aliatp Color del jugador aliat.
     * @return Valor heurístic del tauler.
     */
    public static int heuristica(GameStatus s, CellType aliatp) {
        init(s, aliatp);
        recorregut();
        if (contadorAliat == 0) {
            return -100000;
        }

        if (contadorEnemic == 0) {
            return 100000;
        }
        return contadorAliat - contadorEnemic;
    }
}
