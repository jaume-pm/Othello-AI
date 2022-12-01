/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.upc.epsevg.prop.othello;

import edu.upc.epsevg.prop.othello.players.DesdemonaPlayer;
import edu.upc.epsevg.prop.othello.players.RandomPlayer;

import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author bernat
 */
public class HeadlessGame {

    private IPlayer players[];
    private GameStatus status;
    private int gameCount;
    private int timeout;

    public static void main(String[] args) {

        IPlayer player1 = new RandomPlayer("Crazy Ivan");
        //Player player2 = new RandomPlayer("Desdesmonasia");
        IPlayer player2 = new DesdemonaPlayer(1);//GB

        HeadlessGame game = new HeadlessGame(player1, player2, 2, 5);
        GameResult gr = game.start();
        System.out.println(gr);

    }

    //=====================================================================================0
    public HeadlessGame(IPlayer p1, IPlayer p2, int timeout, int gameCount) {

        this.players = new IPlayer[2];
        players[0] = p1;
        players[1] = p2;
        this.gameCount = gameCount;
        this.timeout = timeout;
    }

    public GameResult start() {
        GameResult gr = new GameResult();
        for (int i = 0; i < gameCount; i++) {
            //System.out.println(">" + i);
            gr.update(play(players[0], players[1]));
        }
        return gr;
    }

    private class Result {
        public boolean ok;
    }

    private CellType play(IPlayer player, IPlayer player0) {
        this.status = new GameStatus();

        while (!this.status.isGameOver()) {
            if (!status.currentPlayerCanMove()) {
                status.skipTurn();
            } else {
                final Semaphore semaphore = new Semaphore(1);
                semaphore.tryAcquire();
                //System.out.println("." + new Date());
                final Result r = new Result();
                Thread t1 = new Thread(() -> {
                    Move m = players[status.getCurrentPlayer() == CellType.PLAYER1 ? 0 : 1].move(new GameStatus(status));
                    if (m != null) {
                        status.movePiece(m.getTo());
                    } else {
                        status.forceLoser();
                    }
                    r.ok = true;
                    semaphore.release();
                });

                Thread t2 = new Thread(() -> {
                    try {
                        Thread.sleep(HeadlessGame.this.timeout * 1000);
                    } catch (InterruptedException ex) {
                    }
                    if (!r.ok) {
                        players[status.getCurrentPlayer() == CellType.PLAYER1 ? 0 : 1].timeout();
                    }
                });

                t1.start();
                t2.start();
                long WAIT_EXTRA_TIME = 2000;
                try {
                    if (!semaphore.tryAcquire(1, timeout * 1000 + WAIT_EXTRA_TIME, TimeUnit.MILLISECONDS)) {

                        System.out.println("Espera il·legal !");
                        throw new RuntimeException("Jugador trampós ! Espera il·legal !");
                    }
                } catch (InterruptedException ex) {
                    Logger.getLogger(HeadlessGame.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
        }
        return status.winnerPlayer;
    }

    private class GameResult {

        java.util.List<CellType> results;

        public GameResult() {
            results = new ArrayList<CellType>();

        }

        public void update(CellType res) {
            results.add(res);
        }

        @Override
        public String toString() {
            String res = "";
            int wins1 = 0, ties1 = 0, loose1 = 0;
            for (CellType c : results) {
                if (null == c) {
                    loose1++;
                } else {
                    switch (c) {
                        case EMPTY:
                            ties1++;
                            break;
                        case PLAYER1:
                            wins1++;
                            break;
                        default:
                            loose1++;
                            break;
                    }
                }
            }

            res += "PLAYER 1 (" + pad(players[0].getName(), 40) + "):\t wins " + wins1 + "\t ties:" + ties1 + "\t looses:" + loose1 + "\n";
            res += "PLAYER 2 (" + pad(players[1].getName(), 40) + "):\t wins " + loose1 + "\t ties:" + ties1 + "\t looses:" + wins1 + "\n";
            return res;
        }

        public String pad(String inputString, int length) {
            if (inputString.length() >= length) {
                return inputString;
            }
            StringBuilder sb = new StringBuilder();
            while (sb.length() < length - inputString.length()) {
                sb.append(' ');
            }
            sb.append(inputString);

            return sb.toString();
        }
    }

}
