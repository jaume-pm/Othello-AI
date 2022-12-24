package edu.upc.epsevg.prop.othello.players.pintor;


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
 * Jugador Minimax
 *
 * @author Jaume i Roberto
 */
public class PlayerMinimax implements IPlayer, IAuto {

  private String name;
  final private  int MAX = 10000000;  // Maxim d'heuristica (10M)
  private CellType color;
  private int profunditat;
  private int nodes;
  private int maxDepth;
  private int heuristic = 3;
  private Heuristica_1 heur1;
  private Heuristica_0 heur0;
  private Heuristica_2 heur2;  
  private Heuristica_3 heur3;
  private double sumTime = 0;
  private double nJugades = 0;

  
    /**
     * Constructora de la classe PlayerMinimax
     * @param name nom del jugador
     * @param profunditat profunditat máxima de l'arbre de cerca per cada moviment
     */
    public PlayerMinimax(String name, int profunditat) {
        this.name = name+" ("+profunditat+")";
        this.profunditat = profunditat;
    }

    /**
     *
     */
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
        double startTime = System.currentTimeMillis();
        
        color = s.getCurrentPlayer();
        nodes = 1;
        maxDepth = 0;
        Point res = minMax(s, profunditat);
        
        double endTime = System.currentTimeMillis();
        //Calculem el temps que ha tardat i fem mitja
        double time = (endTime - startTime)/1000.0;
        sumTime+=time;
        ++nJugades;
        //System.out.printf("Mitja de temps de jugades de "+name+": %.4f%n", sumTime/(double)nJugades);
        return new Move(res, nodes, maxDepth, SearchType.MINIMAX);
    }

    /**
     * Retorna el nom del jugador
     *
     * @return el nom del jugador
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * Realitza una cerca minimax amb poda alfa beta
     * @param s tauler arrel de l'arbre de cerca
     * @param profunditat profunditat màxima a la que arribarà l'arbre de creca
     * @return el moviment que més convé al jugador segons l'heurística i profunditat seleccionades
     */
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
    * Funcio de suport per l'algoritme minmax creat. S'encarrega de retornar el valor de la heuristica d'un tauler en els nivell MAX de l'algorisme per a una profunditat donada
    *
     * @param s tauler node del nivell MAX a retornar el valor
    * @param alfa valor de alfa per a la poda
    * @param beta valor de beta per a la poda.
    * @param profunditat profunditat del arbre de jugades.
     * @return el valor de la heuristica d'un tauler en els nivell MAX de l'algorisme per a una profunditat donada
    */
    public int maxValor(GameStatus s, int alfa, int beta, int profunditat){
        maxDepth = Math.max(maxDepth, this.profunditat-profunditat);
        if(s.isGameOver()){
            int punts = s.getScore(color)-s.getScore(color.opposite(color));
            if(punts > 0)
                return MAX;
            else
                if(punts == 0)return 0;
                else return -MAX;
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
    * Funcio de suport per l'algoritme minmax creat. S'encarrega de retornar el valor de la heuristica d'un tauler en els nivell MIN de l'algorisme per a una profunditat donada
    *
     * @param s tauler node del nivell MAX a retornar el valor
    * @param alfa valor de alfa per a la poda
    * @param beta valor de beta per a la poda.
    * @param profunditat profunditat del arbre de jugades.
     * @return el valor de la heuristica d'un tauler en els nivell Min de l'algorisme per a una profunditat donada
    */
    public int minValor(GameStatus s, int alfa, int beta, int profunditat){
        maxDepth = Math.max(maxDepth, this.profunditat-profunditat);
        if(s.isGameOver()){
            int punts = s.getScore(color)-s.getScore(color.opposite(color));
            if(punts > 0)
                return MAX;
            else
                if(punts == 0)return 0;
                else return -MAX;
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
    
    /**
     * Calcula l'heurística del tauler rebut per paràmetre segons l'heuristica que tingui configurada a l'atribut privat heuristic
     * @param s tauler del qual se'n calcularà l'heurística
     * @return l'heurística del tauler rebut per paràmetre
     */
    public int getHeuristica(GameStatus s) {
        switch (heuristic) {
            case 1:
                //Segona heurística
                return heur1.heuristica(s, color);
            case 2:
                //Segona heurística
                return heur2.heuristica(s, color);
            case 3:
                //Segona heurística
                return heur3.heuristica(s, color);
            default:
                //Primera heuristica, la més senzilla 
                return heur0.heuristica(s, color);
        }
    }
    
}
