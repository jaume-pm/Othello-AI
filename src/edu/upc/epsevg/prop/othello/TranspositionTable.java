/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.upc.epsevg.prop.othello;

/**
 *
 * @author roberto
 */
public class TranspositionTable {
    private TranspositionNode[] table;
    private int size;
    final private  int CONST = -100000000;  // Maxim d'heuristica (10M)

    public TranspositionTable(int size) {
        this.size = size*1073741824/33;
        table = new TranspositionNode[this.size];
    }
    
    public int getValue(long hash, long num1, long num2){
        int index = (int)(hash%size);
        if(index<0)
            index+=size;
        TranspositionNode aux = table[index];
        if(aux == null || aux.getOcupations() != num1 || aux.getColors() != num2)
            return CONST;
        else
            return aux.getValue();
    }
    
    public void addNode(GameStatus s, Zobrist z, byte type, int depth, int value){
        TranspositionNode nou = new TranspositionNode(s,z,type,depth,value);
        int index = (int)(nou.getHash()%size);
        if(index<0)
            index+=size;
        table[index] = nou;
    }
    
}
