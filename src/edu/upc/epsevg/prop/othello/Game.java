package edu.upc.epsevg.prop.othello;

import edu.upc.epsevg.prop.othello.players.HumanPlayer;
import edu.upc.epsevg.prop.othello.players.RandomPlayer;
import edu.upc.epsevg.prop.othello.Level;
import edu.upc.epsevg.prop.othello.IPlayer;
import edu.upc.epsevg.prop.othello.players.DesdemonaPlayer;
import edu.upc.epsevg.prop.othello.players.PlayerID;
import edu.upc.epsevg.prop.othello.players.PlayerMinimax;


import javax.swing.SwingUtilities;

/**
 * Lines Of Action: el joc de taula.
 * @author bernat
 */
public class Game {
        /**
     * @param args
     */
    public static void main(String[] args) {
        
        
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                
                //IPlayer player1 = new RandomPlayer("Crazy Ivan");
                //IPlayer player1 = new HumanPlayer("Human1");
                IPlayer player2 = new DesdemonaPlayer(1);//GB
                IPlayer player1 = new PlayerID("Minimax Iteratiu");
                                
                new Board(player1 , player2, 1, false);
             }
        });
    }
}
