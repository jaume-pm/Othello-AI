package edu.upc.epsevg.prop.othello.players.pintor;

import edu.upc.epsevg.prop.othello.CellType;
import edu.upc.epsevg.prop.othello.GameStatus;
import edu.upc.epsevg.prop.othello.players.pintor.Heuristica_0;
import edu.upc.epsevg.prop.othello.players.pintor.Heuristica_1;
import edu.upc.epsevg.prop.othello.players.pintor.Heuristica_2;
import edu.upc.epsevg.prop.othello.players.pintor.Heuristica_3;
import edu.upc.epsevg.prop.othello.IAuto;
import edu.upc.epsevg.prop.othello.IPlayer;
import edu.upc.epsevg.prop.othello.Move;
import edu.upc.epsevg.prop.othello.SearchType;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Random;

/**
 * Jugador IDS
 *
 * @author Jaume i Roberto
 */
public class PlayerIDS implements IPlayer, IAuto {
  private TranspositionTable taula;
  private Zobrist z;
  private String name;
  final private  int MAX = 10000000;  // Maxim d'heuristica (10M)
  private CellType color;
  private boolean timeout;
  private int heuristic = 3;
  private int profunditat=2;
  private int nodes;
  private int maxDepth;
  private long lastHash;
  private Boolean lastHashAvailible;
  private Boolean agressive = true;
  private Heuristica_1 heur1;
  private Heuristica_0 heur0;
  private Heuristica_2 heur2;
  private Heuristica_3 heur3;
  private double sumTime = 0;
  private double nJugades = 0;
  private int sumProf = 0;

  
    /**
     * Constructora de la classe PlayerIDS
     * @param name nom del jugador
     */
    public PlayerIDS(String name) {
        this.name = name+"-h"+heuristic;
    }

    /**
     * Activa el flag de timeout per tal de parar la cerca que s'està realitzant, en cas de que s'estés realitzant
     */
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
        double startTime = System.currentTimeMillis();
        
        color = s.getCurrentPlayer();
        profunditat = 1;
        Point res = IterativeMinMax(s);
        
        double endTime = System.currentTimeMillis();
        //Calculem el temps que ha tardat i fem mitja
        double time = (endTime - startTime)/1000.0;
        sumTime+=time;
        ++nJugades;
        //System.out.printf("Mitja de temps de jugades de "+name+": %.4f%n", sumTime/(double)nJugades);
        //System.out.printf("Mitja profunditat assolida de "+name+": %.4f%n", sumProf/(double)nJugades);
        return new Move(res, nodes, maxDepth, SearchType.MINIMAX_IDS);
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
     * Realitza un IDS a partir del tauler proporcionat i no dona resposta fins que o bé acaba la cerca (l'arbre es tan curt que s'ha estudiat per complet) o bé s'activa el tiemout
     * @param s tauler arrel dels arbres de cerca
     * @return el moviment que més convé al jugador segons l'heurística seleccionada en el temps donat
     */
    public Point IterativeMinMax(GameStatus s) {
        timeout = true;
        nodes = 1;
        maxDepth = 0;
        Point preres = minMax(s,1);
        int preNodes = nodes, preMaxDepth = maxDepth;
        Point res = preres;
        while(timeout){
            nodes = 1;
            maxDepth = 0;
            ++profunditat;
            preres = res;
            res = minMax(s,profunditat);
            if(!timeout){
                nodes = preNodes;
                maxDepth = preMaxDepth;
                sumProf+=maxDepth;
                return preres;
            }else{
                preNodes = nodes;
                preMaxDepth = maxDepth;
            }
        }
        sumProf+=maxDepth;
        return res;
    }


    /**
     * Realitza una cerca minimax amb poda alfa beta utilitzant taules de taules de transposicio
     * @param s tauler arrel de l'arbre de cerca
     * @param profunditat profunditat màxima a la que arribarà l'arbre de creca
     * @return el moviment que més convé al jugador segons l'heurística i profunditat seleccionades amb el temps donat (timeout s'ho peta tot)
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
                if(!timeout)return res;
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
        if(!timeout)return 0;
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
        if(!timeout)return 0;
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
