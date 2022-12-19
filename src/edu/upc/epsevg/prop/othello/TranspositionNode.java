/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.upc.epsevg.prop.othello;

/**
 *
 * @author roberto
 */
public class TranspositionNode {
    long hash;
    byte type;
    //0 = Exacte, 1 = Alfa, 2 = Beta
    int depth,value;
    long ocupations,colors;
    //3 longs (8 bytes cadascun) + 2 ints (4 bytes cadascun) + 1 byte (1byte) = 33 bytes.
 
    public TranspositionNode(GameStatus s, Zobrist z, byte type, int depth) {
        this.type = type;
        this.depth = depth;
        hash = z.computeZobristHashBits(s);
        ocupations = z.ocupationLong();
        colors = z.colorLong();
    }

    public void setValue(int value) {
        this.value = value;
    }
    
    
}
     