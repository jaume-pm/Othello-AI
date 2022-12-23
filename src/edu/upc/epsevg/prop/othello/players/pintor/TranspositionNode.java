/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.upc.epsevg.prop.othello.players.pintor;

import edu.upc.epsevg.prop.othello.GameStatus;
import edu.upc.epsevg.prop.othello.players.pintor.Zobrist;

/**
 *
 * @author roberto
 */
public class TranspositionNode {
    private byte type,depth;
    //0 = Exacte, 1 = Alfa, 2 = Beta
    private int value;
    private long ocupations,colors,hash;
    //3 longs (8 bytes cadascun) + 1 ints (4 bytes) + 2 byte (1byte cadascun) = 30 bytes.
 
    /**
     * Constructora de node de la taula de transposicio
     * @param s tauler
     * @param z classe auxiliar per a calcular el hash
     * @param type tipus de node (0 = Exacte, 1 = Alfa, 2 = Beta)
     * @param depth profunditat de la cerca on s'ha trobat
     * @param value valor de la heuristica del taulell
     */
    public TranspositionNode(GameStatus s, Zobrist z, byte type, byte depth, int value) {
        this.type = type;
        this.depth = depth;
        hash = z.computeZobristHashBits(s);
        ocupations = z.ocupationLong();
        colors = z.colorLong();
    }

    /**
     * Retorna el hash del node
     * @return hash del node
     */
    public long getHash() {
        return hash;
    }

    /**
     * Retorna l'heuristica calculada per aquell node
     * @return l'heuristica calculada per aquell node
     */
    public int getValue() {
        return value;
    }

    /**
     * Retorna el long d'ocupacions del tauler emmagatzemat al node
     * @return el long d'ocupacions del tauler emmagatzemat al node
     */
    public long getOcupations() {
        return ocupations;
    }

    /**
     * Retorna el long de colors del tauler emmagatzemat al node
     * @return el long de colors del tauler emmagatzemat al node
     */
    public long getColors() {
        return colors;
    }

    /**
     * Retorna el tipus de node
     * @return el tipius de node
     */
    public byte getType() {
        return type;
    }

    /**
     * Retorna la profunditat del node
     * @return la profunditat del node
     */
    public byte getDepth() {
        return depth;
    }
    
    
}
     