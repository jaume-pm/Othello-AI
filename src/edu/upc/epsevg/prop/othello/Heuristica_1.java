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
public class Heuristica_1 {
    
    static int[][] matrix = {{6, 2, 4, 4, 4, 4, 2, 6},
                             {2, -4, -2, -2, -2, -2, -4, 2},
                             {4, -2, 1, 1, 1, 1, -2, 4},
                             {4, -2, 1, 1, 1, 1, -2, 4},
                             {4, -2, 1, 1, 1, 1, -2, 4},
                             {4, -2, 1, 1, 1, 1, -2, 4},
                             {2, -4, -2, -2, -2, -2, -2, 2},
                             {6, 2, 4, 4, 4, 4, 2, 6}};
    static int size = 0;
    static GameStatus gs = new GameStatus();
    static String aliat;
    static int contadorAliat;
    static int contadorEnemic;

    static void init() {
        // GameStatus ha d'ENTRAR COM A PARÀMETRE!
        contadorAliat = 0;
        contadorEnemic = 0;
        size = gs.getSize();
        aliat = "AQUÍ ENTRA PER PARÀMETRE"; // Afegir un paràmetre per posar aquí
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
        for (int f = 0; f <= size; ++f) {
            for (int c = 0; c <= size; ++c) {
                CellType ct = gs.getPos(c, f);
                if (ct.name() == aliat) {
                    contadorAliat = contadorAliat + getPes(c,f);
                } else if (ct.name() != "EMPTY") {
                    contadorEnemic = contadorEnemic + getPes(c,f);
                }
            }
        }
    }
    
    static int heuristica() {
        init();
        recorregut();
        if(contadorEnemic == 0) return 10000;
        else return contadorAliat - contadorEnemic;
    }
}
