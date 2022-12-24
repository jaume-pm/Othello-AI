/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.upc.epsevg.prop.othello.players.pintor;

import edu.upc.epsevg.prop.othello.GameStatus;
import edu.upc.epsevg.prop.othello.players.pintor.Zobrist;

/**
 * Taula de transposició
 * @author roberto
 */
public class TranspositionTable {
    private TranspositionNode[] table;
    private int size;
    final private  int CONST = -100000000;  // Maxim d'heuristica (10M)

    /**
     * Constructora de la taula de transposicions
     * @param size mida de la taula de transposicions en GB
     */
    public TranspositionTable(int size) {
        this.size = size*1073741824/30;
        table = new TranspositionNode[this.size];
    }
    
    /**
     * Si troba el tauler amb els parametres donats
     * @param hash valor hash del tauler que es busca
     * @param num1 long format pels bits del vector d'ocupacions del tauler
     * @param num2  long format pels bits del vector d'ocupacions del tauler
     * @return el valor de la heuristica del tauler si el troba, o una constant si no el troba
     */
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
    
    /**
     * Afegeix un node a la taula de transposicions sobreescrivint l'anterior si hi havia
     * @param s tauler
     * @param z objecte Zobrist 
     * @param type tipus de node
     * @param depth profunditat del node
     * @param value valor de l'heuristica del node
     */
    public void addNode(GameStatus s, Zobrist z, byte type, byte depth, int value){
        TranspositionNode nou = new TranspositionNode(s,z,type,depth,value);
        int index = (int)(nou.getHash()%size);
        if(index<0)
            index+=size;
        table[index] = nou;
    }

    /**
     * Retorna el node a partir d'un hash si existeix
     * @param hash valor del hash que es busca
     * @return el node a partir d'un hash si existeix
     */
    public TranspositionNode getInfo(long hash) {
        int index = (int)(hash%size);
        if(index<0)
            index+=size;
        return table[index];
    }
    
}
