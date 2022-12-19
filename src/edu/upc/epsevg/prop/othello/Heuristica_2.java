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
    
    static int[][] matrix = {{300, -50, 30, 30, 30, 30, -50, 300},
                             {-50, -100, -20, -20, -20, -20, -100, -50},
                             {30, -20, 1, 1, 1, 1, -20, 30},
                             {30, -20, 1, 1, 1, 1, -20, 30},
                             {30, -20, 1, 1, 1, 1, -20, 30},
                             {30, -20, 1, 1, 1, 1, -20, 30},
                             {-50, -100, -20, -20, -20, -20, -100, -50},
                             {300, -50, 30, 30, 30, 30, -50, 300}};

    static int size = 0;
    static GameStatus gs = new GameStatus();
    static String aliat;
    static int contadorAliat;
    static int contadorEnemic;
    static int contadorFitxesAliat;
    static int contadorFitxesEnemic;

    static void init(GameStatus s, CellType aliatp) {
        contadorFitxesAliat = 0;
        contadorFitxesEnemic = 0;
        contadorAliat = 0;
        contadorEnemic = 0;
        gs = s;
        size = gs.getSize();
        aliat = aliatp.toString();
    }
/*
    static void finit() {
        contadorAliat = 0;
        contadorEnemic = 0;
    }
    */
    static int getPes(int c, int f) {
        return matrix[f][c];
    }

    static void recorregut() {
        for (int f = 0; f < size; ++f) {
            for (int c = 0; c < size; ++c) {
                CellType ct = gs.getPos(c, f);
                if (ct.name() == aliat) {
                    contadorAliat = contadorAliat + getPes(c,f);
                    contadorFitxesAliat++;
                } else if (ct.name() != "EMPTY") {
                    contadorEnemic = contadorEnemic + getPes(c,f);
                    contadorFitxesEnemic++;
                }
            }
        }
    }
    
    public static int heuristica(GameStatus s, CellType aliatp) {
        init(s, aliatp);
        recorregut();
        //System.out.print(contadorAliat - contadorEnemic + "\n");
        if(contadorFitxesAliat == 0) return -100000;
        else if(contadorFitxesEnemic == 0) return 100000;
        else return contadorAliat - contadorEnemic;
    }
}
