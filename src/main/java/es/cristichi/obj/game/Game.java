package es.cristichi.obj.game;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import es.cristichi.obj.Player;
import es.cristichi.obj.game.Board.Movement;
import es.cristichi.ui.BoardComponent;
import es.cristichi.util.Position;

public class Game {
    protected boolean ready;

    protected Random rng;

    protected Board board;
    protected HashMap<String, Player> players;
    protected LinkedList<String> playerOrder;

    public Game(Random rng, Board board, Player[] players) {
        this.rng = rng;

        this.board = board;
        this.players = new HashMap<>(players.length);
        for (Player player : players) {
            this.players.put(player.getNickname(), player);
        }

        ready = false;
    }

    public Game(long seed, Board board, Player[] players) {
        this(new Random(seed), board, players);
    }

    public Game(Board board, Player[] players) {
        this(new Random(), board, players);
    }

    public Board getBoard() {
        return board;
    }
    
    /**
     * 
     * @return <code>true</code> if another game loop is expected. <code>false</code> to finish the loops with this one.
     */
    private int round = 0;
    private boolean gameLoop(BoardComponent watcher){
        round++;
        System.out.printf("%nRound %d.%n", round);
        
        for (String playerName : playerOrder) {
            Player player = players.get(playerName);
            Position pos = board.getPosition(player);
            Square square = board.getSquare(pos);

            System.out.printf("%n[%s] %s's turn!%n", player.isCpu()?"CPU":"HUMAN", player.getNickname());

            if (player.isCpu()){
                Movement ban = null;
                for (int i = 0; i < player.getStatBlock().speed(); i++) {
                    LinkedList<Movement> moves = new LinkedList<>();
                    for (Movement movement : Movement.values()) {
                        if (!movement.equals(ban)){
                            try {
                                Square moveSquare = board.getSquare(pos.afterMove(movement));
                                if (square.canBeMovedTo(moveSquare)){
                                    moves.add(movement);
                                }
                            }catch (IndexOutOfBoundsException _){
                            }   
                        }
                    }

                    if (moves.isEmpty()){
                        System.out.printf("[CPU] %s is skipping move (no available moves).%n", player.getNickname());
                    } else {
                        Movement movement = moves.get(rng.nextInt(moves.size()));
                        Position nextPos = pos.afterMove(movement);
                        Square moveSquare = board.getSquare(nextPos);
                        square.setObject(null);
                        moveSquare.setObject(player);
                        pos = nextPos;
                        square = moveSquare;
                        switch (movement) {
                            case UP -> ban = Movement.DOWN;
                            case RIGHT -> ban = Movement.LEFT;
                            case DOWN -> ban = Movement.UP;
                            case LEFT -> ban = Movement.RIGHT;
                        }
                        System.out.printf("[CPU] %s moved %s.%n", player.getNickname(), movement.name());
                    }
                }
                System.out.println(board.toString());
                watcher.updateBoard(board);
            } else {
                System.out.println(board.toString());
                watcher.updateBoard(board);
                System.out.printf("[HUMAN] No input yet.%n");
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        System.out.printf("%nRound %d finished! Waiting for next round.%nCurrent map:%n", round);
        System.out.println(board.toString());
        watcher.updateBoard(board);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return round<30;
    }

    public Thread readyGame(BoardComponent watcher){
        if (ready){
            throw new IllegalStateException("Game is already started.");
        }

        List<Position> spawns = board.getSpawns();
        playerOrder = new LinkedList<>(players.keySet());
        
        if (playerOrder.size() > spawns.size()){
            throw new IllegalArgumentException("Too many players (%d) for the amount of spawns in the Board (%d). Point more spawns or make less friends."
                .formatted(playerOrder.size(), spawns.size()));
        }

        Collections.shuffle(spawns, rng);
        Collections.shuffle(playerOrder, rng);
        Iterator<String> rngNames = playerOrder.iterator();

        for (Position spawn : spawns) {
            if (rngNames.hasNext()){
                board.getSquare(spawn).setObject(players.get(rngNames.next()));
            }
        }
        
        Collections.shuffle(playerOrder, rng);


        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                do {} while(gameLoop(watcher));
            }
        });
        ready = true;

        return thread;
    }

    @Override
    public String toString() {
        StringBuilder ret = new StringBuilder();
        ret.append(ready?"Game Started\n":"Waiting to Start\n");
        ret.append(board.toString());
        return ret.toString();
    }
}
