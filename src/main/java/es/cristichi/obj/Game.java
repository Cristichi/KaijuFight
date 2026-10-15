package es.cristichi.obj;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import es.cristichi.util.Position;

public class Game {
    protected boolean started;

    protected Random rng;

    protected Board board;
    protected HashMap<String, Kaiju> players;

    public Game(Random rng, Board board, HashMap<String, Kaiju> players) {
        this.rng = rng;

        this.board = board;
        this.players = players;

        started = false;

    }

    public Game(long seed, Board board, HashMap<String, Kaiju> players) {
        this(new Random(seed), board, players);
    }

    public Game(Board board, HashMap<String, Kaiju> players) {
        this(new Random(), board, players);
    }

    public void start(){
        if (started){
            throw new IllegalStateException("Game is already started.");
        }

        List<Position> spawns = board.getSpawns();
        ArrayList<String> names = new ArrayList<>(players.keySet());
        
        if (names.size() > spawns.size()){
            throw new IllegalArgumentException("Too many players (%d) for the amount of spawns in the Board (%d). Point more spawns or make less friends."
                .formatted(names.size(), spawns.size()));
        }

        Collections.shuffle(spawns, rng);
        Collections.shuffle(names, rng);
        Iterator<String> rngNames = names.iterator();

        for (Position spawn : spawns) {
            if (rngNames.hasNext()){
                board.getSquare(spawn).setObject(players.get(rngNames.next()));
            }
        }

        started = true;
    }

    @Override
    public String toString() {
        StringBuilder ret = new StringBuilder();
        ret.append(started?"Game Started\n":"Waiting to Start\n");
        ret.append(board.toString());
        return ret.toString();
    }
}
