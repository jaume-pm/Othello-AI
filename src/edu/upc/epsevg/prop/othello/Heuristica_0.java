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
//Problemes: No té en compte la posició de les fiches
//Parámetros: GameStatus y color jugador
public class Heuristica_0 {

    static int size = 0;
    static GameStatus gs = new GameStatus();
    static String aliat;
    static int contadorAliat;
    static int contadorEnemic;

    static void init() {
        // GameStatus ha d'ENTRAR COM A PARÀMETRE!
        size = gs.getSize();
        aliat = "AQUÍ ENTRA PER PARÀMETRE"; // Afegir un paràmetre per posar aquí
        contadorAliat = 0;
        contadorEnemic = 0;
    }

    static void finit() {
        contadorAliat = 0;
        contadorEnemic = 0;
    }

    static void recorregut() {
        for (int f = 0; f <= size; ++f) {
            for (int c = 0; c <= size; ++c) {
                CellType ct = gs.getPos(c, f);
                if (ct.name() == aliat) {
                    ++contadorAliat;
                } else if (ct.name() != "EMPTY") {
                    ++contadorEnemic;
                }
            }
        }
    }
    
    static void heuristica() {
        init();
        recorregut();
        finit();
    }
}
