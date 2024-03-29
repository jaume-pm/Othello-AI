package edu.upc.epsevg.prop.othello;

import edu.upc.epsevg.prop.othello.players.HumanPlayer;
import edu.upc.epsevg.prop.othello.players.RandomPlayer;
import edu.upc.epsevg.prop.othello.Level;
import edu.upc.epsevg.prop.othello.IPlayer;
import edu.upc.epsevg.prop.othello.players.DesdemonaPlayer;
import edu.upc.epsevg.prop.othello.players.pintor.PlayerID;
import edu.upc.epsevg.prop.othello.players.pintor.PlayerIDS;
import edu.upc.epsevg.prop.othello.players.pintor.PlayerMinimax;

import javax.swing.SwingUtilities;

/**
 * Lines Of Action: el joc de taula.
 *
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
                //IPlayer player2 = new HumanPlayer("Human1");
                IPlayer player1 = new DesdemonaPlayer(1);//GB
                //IPlayer player2 = new PlayerID("Pintor",1,true);
                IPlayer player2 = new PlayerIDS("Pintor");
                //IPlayer player2 = new PlayerIDS("IDS");
                //IPlayer player1 = new PlayerMinimax("minimax",9);                
                new Board(player1 , player2, 2, false);
             }
        });
    }
}
