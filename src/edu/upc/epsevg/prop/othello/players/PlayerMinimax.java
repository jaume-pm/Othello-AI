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
public class PlayerMinimax implements IPlayer, IAuto {

  private String name;
  private GameStatus s;
  final private  int MAX = 10000000;  // Maxim d'heuristica (10M)
  private CellType color;
  private String nom;
  private int profunditat;
  private int jugades;  //Jugades explorades
  private int nJugades; //# jugades reals
  private double sumTime = 0; //Suma del temps que tarda cada jugada
  private int heuristic = 1;
  static int[][] matrix = {{6, 2, 4, 4, 4, 4, 2, 6},
                             {2, -4, -2, -2, -2, -2, -4, 2},
                             {4, -2, 1, 1, 1, 1, -2, 4},
                             {4, -2, 1, 1, 1, 1, -2, 4},
                             {4, -2, 1, 1, 1, 1, -2, 4},
                             {4, -2, 1, 1, 1, 1, -2, 4},
                             {2, -4, -2, -2, -2, -2, -2, 2},
                             {6, 2, 4, 4, 4, 4, 2, 6}};
  
    public PlayerMinimax(String name, int profunditat) {
        this.name = name+" ("+profunditat+")";
        this.profunditat = profunditat;
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
        return new Move(minMax(s, profunditat),0,0,SearchType.MINIMAX);
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
                int min = minValor(aux, alfa, beta, profunditat-1);
                if (valor < min){
                    res = move;
                    valor = min;
                }
                if (beta < valor){
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
                    valor = Math.max(valor, minValor(aux, alfa, beta, profunditat-1));
                    if (beta < valor){
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
                    valor = Math.min(valor, maxValor(aux, alfa, beta, profunditat-1));
                    if (valor < alfa){
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
    
    public int getHeuristica(GameStatus s){
        switch(heuristic){
            case 1:
                //Segona heurística
                return heuristica_1(s);
            default:
                //Primera heuristica, la més senzilla 
                return heuristica_0(s);
        }
    }
    
    public int heuristica_0(GameStatus s) {
        int res = 0;
        for (int f = 0; f < s.getSize(); ++f) {
            for (int c = 0; c < s.getSize(); ++c) {
                CellType ct = s.getPos(c, f);
                if (ct == color) {
                    ++res;
                } else if (ct.name() != "EMPTY") {
                    --res;
                }
            }
        }
        return res;
    }
    
    public int heuristica_1(GameStatus s) {
            //Aquesta heuristica només és vàlida per a taulells de mida 8
        if(s.getSize()!=8) return 0;
        int res = 0;
        for (int f = 0; f < 8; ++f) {
            for (int c = 0; c < 8; ++c) {
                CellType ct = s.getPos(c, f);
                if (ct == color) {
                    res+=matrix[f][c];
                } else if (ct.name() != "EMPTY") {
                    res-=matrix[f][c];
                }
            }
        }
        return res;
    }
    
}
