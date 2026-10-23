package es.cristichi.obj;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import es.cristichi.obj.Board.Movement;
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
    
    /**
     * 
     * @return <code>true</code> if another game loop is expected. <code>false</code> to finish the loops with this one.
     */
    private int round = 0;
    private boolean gameLoop(){
        round++;
        System.out.printf("Round %d.%n", round);
        for (String playerName : playerOrder) {
            Player player = players.get(playerName);
            Position pos = board.getPosition(player);
            Square square = board.getSquare(pos);
            if (player.isCpu()){
                LinkedList<Movement> moves = new LinkedList<>();
                for (Movement movement : Movement.values()) {
                    try {
                        Square moveSquare = board.getSquare(pos.afterMove(movement));
                        if (moveSquare.getObject().canBeReplacedBy(player)){
                            moves.add(movement);
                        }
                    }catch (IndexOutOfBoundsException _){
                    }   
                }

                if (moves.isEmpty()){
                    System.out.printf("[CPU] %s is skipping move (no available moves).%n", player.getNickname());
                } else {
                    Movement movement = moves.get(rng.nextInt(moves.size()));
                    Square moveSquare = board.getSquare(pos.afterMove(movement));
                    square.setObject(null);
                    moveSquare.setObject(player);
                }
            } else {

            }
        }
        return false;
    }

    public Thread readyGame(){
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
                do {} while(gameLoop());
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
