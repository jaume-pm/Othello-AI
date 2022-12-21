/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.upc.epsevg.prop.othello;
import java.util.BitSet;
import java.util.Random;

/**
 *
 * @author roberto
 */
public class Zobrist {

    // Constants for the Othello board
    final int BOARD_SIZE = 8;
    private BitSet ocupations;
    private BitSet colors;


    // Choose a set of random hash keys for each position and each piece
    long[][][] hashKeys = new long[2][BOARD_SIZE][BOARD_SIZE];

    public Zobrist() {
        ocupations = new BitSet(64);
        colors = new BitSet(64);
        ocupations.clear();
        colors.clear();
        Random random = new Random();
        for (int p = 0; p < 2; p++) {
          for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
              hashKeys[p][i][j] = random.nextLong();
            }
          }
        }
    }




    public long computeZobristHash(GameStatus s) {
      // Initialize the hash code to 0

      long hashCode = 0;

      // Iterate over each position on the board
      for (int i = 0; i < BOARD_SIZE; i++) {
        for (int j = 0; j < BOARD_SIZE; j++) {
          // Determine the piece at this position (if any)
          CellType cellPiece = s.getPos(i,j);
          if ("EMPTY".equals(cellPiece.name())){
              continue;
          }
          int piece = 0;
          if ("PLAYER2".equals(cellPiece.name()))
              ++piece;

          // Add the corresponding hash key to the hash code
          hashCode ^= hashKeys[piece][i][j];
        }
      }

      return hashCode;
    }
    
    public long computeZobristHashBits(GameStatus s) {
      // Initialize the hash code to 0

      long hashCode = 0;

      // Iterate over each position on the board
      for (int i = 0; i < BOARD_SIZE; i++) {
        for (int j = 0; j < BOARD_SIZE; j++) {
          // Determine the piece at this position (if any)
          CellType cellPiece = s.getPos(i,j);
          if ("EMPTY".equals(cellPiece.name())){
              ocupations.set(i*BOARD_SIZE+j, false);
              colors.set(i*BOARD_SIZE+j, false);
              continue;
          }
          ocupations.set(i*BOARD_SIZE+j,true);
          int piece = 0;
          if ("PLAYER2".equals(cellPiece.name())){
              ++piece;
              colors.set(i*BOARD_SIZE+j,true);
          } else {
              colors.set(i*BOARD_SIZE+j, false);
          }
          // Add the corresponding hash key to the hash code
          hashCode ^= hashKeys[piece][i][j];
        }
      }

      return hashCode;
    }
    
    public long ocupationLong(){
        return ocupations.toLongArray()[0];
    }
    
    public long colorLong(){
        return colors.toLongArray()[0];
    }

}
