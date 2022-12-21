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
import edu.upc.epsevg.prop.othello.TranspositionTable;
import edu.upc.epsevg.prop.othello.Zobrist;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Random;

/**
 * Jugador aleatori
 *
 * @author bernat
 */
public class PlayerID implements IPlayer, IAuto {
<<<<<<< HEAD
  private TranspositionTable taula;
  private Zobrist z;
  private Boolean heuristicDepth;
  private int profunditatActual;  
  private String name;
  private GameStatus s;
  final private  int MAX = 10000000;  // Maxim d'heuristica (10M)
  private CellType color;
  private boolean timeout;
  private String nom;
  private int profunditat=2;
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
  
    public PlayerID(String name, int size) {
=======

    private String name;
    private GameStatus s;
    final private int MAX = 10000000;  // Maxim d'heuristica (10M)
    private CellType color;
    private boolean timeout;
    private String nom;
    private int profunditat = 2;
    private int jugades;  //Jugades explorades
    private int nJugades; //# jugades reals
    private double sumTime = 0; //Suma del temps que tarda cada jugada
    private int heuristic = 1;
    private Heuristica_1 heur1 = new Heuristica_1();
    private Heuristica_0 heur0 = new Heuristica_0();
    private Heuristica_2 heur2 = new Heuristica_2();

    public PlayerID(String name) {
>>>>>>> 654991e163d15b47321d0703ff5a4f6bd5dd8561
        this.name = name;
        taula = new TranspositionTable(size);
        z = new Zobrist();
    }

    @Override
    public void timeout() {
        timeout = false;
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
        profunditatActual = 60 - s.getEmptyCellsCount();
        profunditat = 1;
        Point res = IterativeMinMax(s);
        return new Move(res, 0, profunditat, SearchType.MINIMAX_IDS);
    }

    /**
     * Ens avisa que hem de parar la cerca en curs perquè s'ha exhaurit el temps
     * de joc.
     */
    @Override
    public String getName() {
        return name;
    }

    public Point IterativeMinMax(GameStatus s) {
        timeout = true;
<<<<<<< HEAD
        heuristicDepth = false;
        Point preres = minMax(s,1);
        Point res = preres;
        while(timeout && heuristicDepth){
            heuristicDepth = false;
            ++profunditat;
            preres = res;
            res = minMax(s,profunditat);
            if(!timeout)return preres;
        }
        return res;
    }
    
    public Point bestMove(GameStatus s, ArrayList<Point> moves){
        Point res = moves.get(0);
        int maxValue = -MAX-1;
        for(Point move : moves){
            GameStatus aux = new GameStatus(s);
            aux.movePiece(move);
            long hash = z.computeZobristHashBits(aux);
            int value = taula.getValue(hash, z.ocupationLong(), /*z.colorLong()*/3);
            if(maxValue < value){
                maxValue = value;
                res = move;
            }
        }
        moves.remove(res);
        return res;
    }
    
    public Point minMax(GameStatus s, int profunditat){
        ArrayList<Point> moves =  s.getMoves();
=======
        Point preres = minMax(s, 1);
        Point res = preres;
        while (timeout) {
            preres = res;
            res = minMax(s, profunditat);
            if (!timeout) {
                return preres;
            }
            ++profunditat;
        }
        return res;
    }

    public Point minMax(GameStatus s, int profunditat) {
        ArrayList<Point> moves = s.getMoves();
>>>>>>> 654991e163d15b47321d0703ff5a4f6bd5dd8561
        Point res = moves.get(0);
        if (moves.isEmpty()) {
            // no podem moure, el moviment (de tipus Point) es passa null.
            return res;
        }
        int valor = -MAX - 1;
        int alfa = -MAX;
        int beta = MAX;
<<<<<<< HEAD
        while(!moves.isEmpty()){
                if(!timeout)return res;
                GameStatus aux = new GameStatus(s);
                Point move = bestMove(s, moves);
                aux.movePiece(move);
                int min = minValor(aux, alfa, beta, profunditat-1);
                taula.addNode(s, z, (byte)0, 60-s.getEmptyCellsCount()+profunditat, min);
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
    }/*for (Point move : moves){
                if(!timeout)return res;
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
    }*/
    
=======
        for (Point move : moves) {
            if (!timeout) {
                return res;
            }
            GameStatus aux = new GameStatus(s);
            aux.movePiece(move); //Cal fer un tauler auxiliar cada cop
            int min = minValor(aux, alfa, beta, profunditat - 1);
            if (valor < min) {
                res = move;
                valor = min;
            }
            if (beta < valor) {
                return res;
            }
            alfa = Math.max(valor, alfa);
        }
        return res;
    }

>>>>>>> 654991e163d15b47321d0703ff5a4f6bd5dd8561
    /**
     * Funcio de suport per l'algoritme minmax creat.
     *
     * @param t tauler sobre el qual fer el moviment
     * @param col columna sobre la qual s'ha fet l'ultima jugada.
     * @param alfa valor de alfa per a la poda
     * @param beta valor de beta per a la poda.
     * @param profunditat profunditat del arbre de jugades.
     */
    public int maxValor(GameStatus s, int alfa, int beta, int profunditat) {
        if (!timeout) {
            return 0;
        }
        if (s.isGameOver()) {
            if (color == s.GetWinner()) {
                return MAX;
            } else {
                return -MAX;
            }
        }
        if (s.getCurrentPlayer() != color) {
            return minValor(s, alfa, beta, profunditat);
<<<<<<< HEAD
        if(profunditat > 0){
            Integer valor = -MAX-1;
            ArrayList<Point> moves =  s.getMoves();
            while(!moves.isEmpty()){
                GameStatus aux = new GameStatus(s);
                Point move = bestMove(s, moves);
                aux.movePiece(move);
                int min = minValor(aux, alfa, beta, profunditat-1);
                taula.addNode(s, z, (byte)0, 60-s.getEmptyCellsCount()+profunditat, min);
                valor = Math.max(valor, min);
                if (beta < valor){
                    return valor;
                }
                alfa = Math.max(valor,alfa);
=======
        }
        if (profunditat > 0) {
            Integer valor = -MAX - 1;
            ArrayList<Point> moves = s.getMoves();
            for (Point move : moves) {
                GameStatus aux = new GameStatus(s);
                aux.movePiece(move);
                valor = Math.max(valor, minValor(aux, alfa, beta, profunditat - 1));
                if (beta < valor) {
                    return valor;
                }
                alfa = Math.max(valor, alfa);
>>>>>>> 654991e163d15b47321d0703ff5a4f6bd5dd8561
            }
            return valor;
        } else {
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
    public int minValor(GameStatus s, int alfa, int beta, int profunditat) {
        if (!timeout) {
            return 0;
        }
        if (s.isGameOver()) {
            if (color == s.GetWinner()) {
                return MAX;
            } else {
                return -MAX;
            }
        }
        if (s.getCurrentPlayer() == color) {
            return maxValor(s, alfa, beta, profunditat);
<<<<<<< HEAD
        if(profunditat > 0){
            Integer valor = MAX-1;
            ArrayList<Point> moves =  s.getMoves();
            while(!moves.isEmpty()){
                GameStatus aux = new GameStatus(s);
                Point move = bestMove(s, moves);
                aux.movePiece(move);
                int max = maxValor(aux, alfa, beta, profunditat-1);
                taula.addNode(s, z, (byte)0, 60-s.getEmptyCellsCount()+profunditat, max);
                valor = Math.min(valor, max);
                if (valor < alfa){
                    return valor; 
                }
                beta = Math.min(valor,beta);
=======
        }
        if (profunditat > 0) {
            Integer valor = MAX - 1;
            ArrayList<Point> moves = s.getMoves();
            for (Point move : moves) {
                GameStatus aux = new GameStatus(s);
                aux.movePiece(move);
                valor = Math.min(valor, maxValor(aux, alfa, beta, profunditat - 1));
                if (valor < alfa) {
                    return valor;
                }
                beta = Math.min(valor, beta);
>>>>>>> 654991e163d15b47321d0703ff5a4f6bd5dd8561
            }
            return valor;
        } else {
            return getHeuristica(s);
        }
    }
<<<<<<< HEAD
    
    public int getHeuristica(GameStatus s){
        heuristicDepth = true;
        switch(heuristic){
=======

    public int getHeuristica(GameStatus s) {
        switch (heuristic) {
>>>>>>> 654991e163d15b47321d0703ff5a4f6bd5dd8561
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
