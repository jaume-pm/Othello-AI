package edu.upc.epsevg.prop.othello.players;


import edu.upc.epsevg.prop.othello.CellType;
import edu.upc.epsevg.prop.othello.GameStatus;
import edu.upc.epsevg.prop.othello.IAuto;
import edu.upc.epsevg.prop.othello.IPlayer;
import edu.upc.epsevg.prop.othello.Move;
import edu.upc.epsevg.prop.othello.SearchType;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Random;

/**
 * Jugador aleatori
 * @author bernat
 */
public class RandomPlayer implements IPlayer, IAuto {

    private String name;
    private GameStatus s;

    public RandomPlayer(String name) {
        this.name = name;
    }

    @Override
    public void timeout() {
        // Nothing to do! I'm so fast, I never timeout 8-)
    }

    /**
     * Decideix el moviment del jugador donat un tauler i un color de peça que
     * ha de posar.
     *
     * @param s Tauler i estat actual de joc.
     * @return el moviment que fa el jugador.
     */
    @Override
    public Move move(GameStatus s) {

        ArrayList<Point> moves =  s.getMoves();
        if(moves.isEmpty())
        {
            // no podem moure, el moviment (de tipus Point) es passa null.
            return new Move(null, 0L,0,  SearchType.RANDOM); 
        } else {
            Random rand = new Random();
            int q = rand.nextInt(moves.size());
            return new Move( moves.get(q), 0L, 0, SearchType.RANDOM);         
        }
    }

    /**
     * Ens avisa que hem de parar la cerca en curs perquè s'ha exhaurit el temps
     * de joc.
     */
    @Override
    public String getName() {
        return "Random(" + name + ")";
    }

}
