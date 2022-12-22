package edu.upc.epsevg.prop.othello.players;


import edu.upc.epsevg.prop.othello.CellType;
import edu.upc.epsevg.prop.othello.GameStatus;
import edu.upc.epsevg.prop.othello.Heuristica_0;
import edu.upc.epsevg.prop.othello.Heuristica_1;
import edu.upc.epsevg.prop.othello.Heuristica_2;
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
public class PlayerMinimax implements IPlayer, IAuto {

  private String name;
  final private  int MAX = 10000000;  // Maxim d'heuristica (10M)
  private CellType color;
  private int profunditat;
  private int nodes;
  private int maxDepth;
  private int heuristic = 2;
  private Heuristica_1 heur1;
  private Heuristica_0 heur0;
  private Heuristica_2 heur2;
  
    public PlayerMinimax(String name, int profunditat) {
        this.name = name+" ("+profunditat+")";
        this.profunditat = profunditat;
        heur1 = new Heuristica_1();
        heur0 = new Heuristica_0();
        heur2 = new Heuristica_2();
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
        color = s.getCurrentPlayer();
        nodes = 1;
        maxDepth = 0;
        return new Move(minMax(s, profunditat),nodes,maxDepth,SearchType.MINIMAX);
    }

    /**
     * Ens avisa que hem de parar la cerca en curs perquè s'ha exhaurit el temps
     * de joc.
     */
    @Override
    public String getName() {
        return name;
    }

     public Point minMax(GameStatus s, int profunditat){
        ArrayList<Point> moves =  s.getMoves();
        Point res = moves.get(0);
        if(moves.isEmpty()){
            // no podem moure, el moviment (de tipus Point) es passa null.
            return res; 
        }
        int valor = -MAX-1;
        int alfa = -MAX;
        int beta = MAX;
        for (Point move : moves){
                GameStatus aux = new GameStatus(s);
                aux.movePiece(move); //Cal fer un tauler auxiliar cada cop
                ++nodes;
                int min = minValor(aux, alfa, beta, profunditat-1);
                if (valor < min){
                    res = move;
                    valor = min;
                }
                if (beta <= valor){
                    return res;
                }
                alfa = Math.max(valor,alfa);
        }
        return res;
    }
    
    /**
    * Funcio de suport per l'algoritme minmax creat.
    *
    * @param t tauler sobre el qual fer el moviment
    * @param col columna sobre la qual s'ha fet l'ultima jugada.
    * @param alfa valor de alfa per a la poda
    * @param beta valor de beta per a la poda.
    * @param profunditat profunditat del arbre de jugades.
    */
    public int maxValor(GameStatus s, int alfa, int beta, int profunditat){
        maxDepth = Math.max(maxDepth, this.profunditat-profunditat);
        if(s.isGameOver()){
            if(color == s.GetWinner())
                return MAX;
            else
                return -MAX;
        }
        if(s.getCurrentPlayer() != color)
            return minValor(s, alfa, beta, profunditat);
        if(profunditat > 0){
            Integer valor = -MAX-1;
            ArrayList<Point> moves =  s.getMoves();
            for (Point move : moves){
                    GameStatus aux = new GameStatus(s);
                    aux.movePiece(move);
                    ++nodes;
                    valor = Math.max(valor, minValor(aux, alfa, beta, profunditat-1));
                    if (beta <= valor){
                        return valor;
                    }
                    alfa = Math.max(valor,alfa);
            }
            return valor;
        }else{
            return getHeuristica(s);
        }
        
    }
    /**
    * Funcio de suport per l'algoritme minmax creat.
    *
    * @param t tauler sobre el qual fer el moviment
    * @param col columna sobre la qual s'ha fet l'ultima jugada.
    * @param alfa valor de alfa per a la poda
    * @param beta valor de beta per a la poda.
    * @param profunditat profunditat del arbre de jugades.
    */
    public int minValor(GameStatus s, int alfa, int beta, int profunditat){
        maxDepth = Math.max(maxDepth, this.profunditat-profunditat);
        if(s.isGameOver()){
            if(color == s.GetWinner())
                return MAX;
            else
                return -MAX;
        }
        if(s.getCurrentPlayer() == color)
            return maxValor(s, alfa, beta, profunditat);
        if(profunditat > 0){
            Integer valor = MAX-1;
            ArrayList<Point> moves =  s.getMoves();
            for (Point move : moves){
                    GameStatus aux = new GameStatus(s);
                    aux.movePiece(move);
                    ++nodes;
                    valor = Math.min(valor, maxValor(aux, alfa, beta, profunditat-1));
                    if (valor <= alfa){
                        return valor; 
                    }
                    beta = Math.min(valor,beta);
            }
            return valor;
        }
        else{
            return getHeuristica(s);
        }
    }
    
    public int getHeuristica(GameStatus s) {
        switch (heuristic) {
            case 1:
                //Segona heurística
                return heur1.heuristica(s, color);
            case 2:
                //Segona heurística
                return heur2.heuristica(s, color);
            default:
                //Primera heuristica, la més senzilla 
                return heur0.heuristica(s, color);
        }
    }
    
}
