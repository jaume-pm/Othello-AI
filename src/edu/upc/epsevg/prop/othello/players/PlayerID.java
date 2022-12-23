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
import edu.upc.epsevg.prop.othello.TranspositionNode;
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
  private TranspositionTable taula;
  private Zobrist z;
  private Boolean heuristicDepth;
  private String name;
  final private  int MAX = 10000000;  // Maxim d'heuristica (10M)
  private CellType color;
  private boolean timeout;
  private int heuristic = 2;
  private int profunditat=2;
  private int nodes;
  private int maxDepth;
  private long lastHash;
  private Boolean lastHashAvailible;
  private Heuristica_1 heur1;
  private Heuristica_0 heur0;
  private Heuristica_2 heur2;
  private byte tipus = 0;//0 = Exacte, 1 = Alfa, 2 = Beta
  
    public PlayerID(String name, int size) {
        this.name = name+"-h"+heuristic;
        taula = new TranspositionTable(size);
        z = new Zobrist();
        heur1 = new Heuristica_1();
        heur0 = new Heuristica_0();
        heur2 = new Heuristica_2();
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
        profunditat = 1;
        Point res = IterativeMinMax(s);
        return new Move(res, nodes, maxDepth, SearchType.MINIMAX_IDS);
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
        heuristicDepth = false;
        nodes = 1;
        maxDepth = 0;
        Point preres = minMax(s,1);
        int preNodes = nodes, preMaxDepth = maxDepth;
        Point res = preres;
        while(timeout && heuristicDepth){
            heuristicDepth = false;
            nodes = 1;
            maxDepth = 0;
            ++profunditat;
            preres = res;
            res = minMax(s,profunditat);
            if(!timeout){
                nodes = preNodes;
                maxDepth = preMaxDepth;
                return preres;
            }else{
                preNodes = nodes;
                preMaxDepth = maxDepth;
            }
        }
        return res;
    }
    
    public Point bestMove(GameStatus s, ArrayList<Point> moves){
        lastHashAvailible = false;
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
                lastHash = hash;
                lastHashAvailible = true;
            }
        }
        moves.remove(res);
        return res;
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
        int min  = 0;
        while(!moves.isEmpty()){
                if(!timeout)return res;
                GameStatus aux = new GameStatus(s);
                Point move = bestMove(s, moves);
                aux.movePiece(move);
                ++nodes;
                Boolean obtenirMin = true;
                if(lastHashAvailible){
                    TranspositionNode info = taula.getInfo(lastHash);
                    if(info.getDepth() >= 60-s.getEmptyCellsCount()+profunditat){
                        if(info.getType() == 0){
                            min = info.getValue();
                            obtenirMin = false;
                        }else{
                            if(info.getType() == 1){
                                alfa = Math.max(alfa, info.getValue());
                            }
                        }
                    }
                }
                if(obtenirMin)min = minValor(aux, alfa, beta, profunditat-1);
                tipus = 0;
                if (valor < min){
                    res = move;
                    valor = min;
                }
                if (beta <= valor){
                    tipus = 1;
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
        if(!timeout)return 0;
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
            int min = 0;
            while(!moves.isEmpty()){
                GameStatus aux = new GameStatus(s);
                Point move = bestMove(s, moves);
                aux.movePiece(move);
                ++nodes;
                if(lastHashAvailible){
                    TranspositionNode info = taula.getInfo(lastHash);
                    if(info.getDepth() >= 60-s.getEmptyCellsCount()+profunditat){
                        if(info.getType() == 0){
                            return info.getValue();
                        }else{
                            if(info.getType() == 1){
                                alfa = Math.max(alfa, info.getValue());
                            }
                        }
                    }
                }
                min = minValor(aux, alfa, beta, profunditat-1);
                taula.addNode(s, z, tipus, 60-s.getEmptyCellsCount()+profunditat, min);
                tipus = 0;
                valor = Math.max(valor, min);
                if (beta <= valor){
                    tipus = 1;
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
        if(!timeout)return 0;
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
            while(!moves.isEmpty()){
                GameStatus aux = new GameStatus(s);
                Point move = bestMove(s, moves);
                aux.movePiece(move);
                ++nodes;
                if(lastHashAvailible){
                    TranspositionNode info = taula.getInfo(lastHash);
                    if(info.getDepth() >= 60-s.getEmptyCellsCount()+profunditat){
                        if(info.getType() == 0){
                            return info.getValue();
                        }else{
                            if(info.getType() == 2){
                                alfa = Math.min(beta, info.getValue());
                            }
                        }
                    }
                }
                int max = maxValor(aux, alfa, beta, profunditat-1);
                taula.addNode(s, z, tipus, 60-s.getEmptyCellsCount()+profunditat, max);
                tipus = 0;
                valor = Math.min(valor, max);
                if (valor <= alfa){
                    tipus = 2;
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
        heuristicDepth = true;
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
