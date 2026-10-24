package es.cristichi.obj.game;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Semaphore;

import es.cristichi.obj.Player;
import es.cristichi.obj.game.Board.Movement;
import es.cristichi.ui.BoardComponent;
import es.cristichi.util.Position;

public class Game {
    private Semaphore inputSem;
    private Object input;

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

        inputSem = new Semaphore(0);
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

    public void sendInput(Object input){
        if (inputSem.availablePermits()>0){
            throw new IllegalStateException("Can't accept input while not waiting for input.");
        }
        this.input = input;
        inputSem.release();
    }
    
    /**
     * 
     * @return <code>true</code> if another game loop is expected. <code>false</code> to finish the loops with this one.
     */
    private int round = 0;
    private boolean gameLoop(BoardComponent watcher) throws InterruptedException{
        round++;
        System.out.printf("%nRound %d.%n", round);
        watcher.getWindow().updateTopText("Round "+round+".");
        watcher.getWindow().showAcceptControls();
        inputSem.acquire();
        input = null;
        
        for (String playerName : playerOrder) {
            Player player = players.get(playerName);
            Position pos = board.getPosition(player);
            Square square = board.getSquare(pos);

            System.out.printf("%n[%s] %s's turn!%n", player.isCpu()?"CPU":"HUMAN", player.getNickname());
            watcher.getWindow().updateTopText("Round "+round+". Turn for ["+(player.isCpu()?"CPU] ":"HUMAN] ")+player.getNickname());

            if (player.isCpu()){
                // CPU movement
                Movement previousMove = null;
                for (int i = 0; i < player.getStatBlock().getSpeed(); i++) {
                    LinkedList<Movement> moves = new LinkedList<>();
                    for (Movement movement : Movement.values()) {
                        if (!movement.equals(previousMove)){
                            try {
                                Square moveSquare = board.getSquare(pos.afterMove(movement));
                                if (moveSquare.getObject() == null || moveSquare.getObject().canBeReplacedBy(player)){
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
                            case UP -> previousMove = Movement.DOWN;
                            case RIGHT -> previousMove = Movement.LEFT;
                            case DOWN -> previousMove = Movement.UP;
                            case LEFT -> previousMove = Movement.RIGHT;
                        }
                        System.out.printf("[CPU] %s moved %s.%n", player.getNickname(), movement.name());
                        watcher.getWindow().updateTopText("Round "+round+". ["+(player.isCpu()?"CPU] ":"HUMAN] ")+player.getNickname()+" moved "+movement.name());
                        watcher.getWindow().showAcceptControls();
                        inputSem.acquire();
                        input = null;
                    }
                }
                // CPU attack TBI

                System.out.println(board.toString());
                watcher.updateBoard();
            } else {
                // Player moves
                for (int i = 0; i < player.getStatBlock().getSpeed(); i++) {
                    System.out.println(board.toString());
                    watcher.updateBoard();
                    System.out.printf("[HUMAN] Waiting on move #%d input.%n", i);
                    watcher.getWindow().updateTopText("Round "+round+". [HUMAN] "+player.getNickname()+" choose your move #"+(i+1)+".");
                    watcher.getWindow().showMoveControls();
                    boolean needInput = true;
                    while(needInput) {
                        inputSem.acquire();
                        watcher.getWindow().removeControls();
                        if (input instanceof Movement inMove){
                            try {
                                Position nextPos = pos.afterMove(inMove);
                                Square moveSquare = board.getSquare(nextPos);
                                if (moveSquare.getObject() == null || moveSquare.getObject().canBeReplacedBy(player)){
                                    square.setObject(null);
                                    moveSquare.setObject(player);
                                    pos = nextPos;
                                    square = moveSquare;
                                    needInput = false;
                                } else {
                                    watcher.getWindow().updateTopText("Round "+round+". [HUMAN] "+player.getNickname()+" can't move to that position.");
                                    watcher.getWindow().showMoveControls();
                                }
                            } catch (IndexOutOfBoundsException e){
                                watcher.getWindow().updateTopText("Round "+round+". [HUMAN] "+player.getNickname()+" can't move to out of bounds. Speedrunners must work harder.");
                                watcher.getWindow().showMoveControls();
                            }
                        } else if (input == null){
                            needInput = false;
                        } else {
                            watcher.getWindow().updateTopText("Round "+round+". [HUMAN] "+player.getNickname()+" sent an unknown command.");
                            watcher.getWindow().showMoveControls();
                        }
                        input = null;
                    };
                }
                // Player attacks
                LinkedList<AttackInfo> possibleAttacks = new LinkedList<>();
                for (Movement movement : Movement.values()) {
                    try {
                        Square moveSquare = board.getSquare(pos.afterMove(movement));
                        if (moveSquare.getObject() != null && moveSquare.getObject().canBeAttackedBy(AttackType.MELEE, player)){
                            possibleAttacks.add(new AttackInfo(AttackType.MELEE, moveSquare.getObject()));
                        }
                    }catch (IndexOutOfBoundsException _){
                    }
                }
                System.out.println(board.toString());
                watcher.updateBoard();
                System.out.printf("[HUMAN] Waiting on attack input.%n");
                watcher.getWindow().updateTopText("Round "+round+". [HUMAN] "+player.getNickname()+" choose your attack.");
                watcher.getWindow().showAttackControls(player, possibleAttacks);
                boolean needInput = true;
                while(needInput) {
                    inputSem.acquire();
                    watcher.getWindow().removeControls();
                    if (input instanceof AttackInfo attackCommand){
                        needInput = false;
                        switch (attackCommand.type()) {
                            case NO_ATTACK:
                                break;
                            case RANGED:
                                if (!player.getStatBlock().useMana(1)){
                                    watcher.getWindow().updateTopText("Round "+round+". [HUMAN] "+player.getNickname()+" doesn't have mana to make ranged attacks.");
                                    watcher.getWindow().showMoveControls();
                                    needInput = true;
                                }
                            case MELEE:
                                AttackResult result = player.attack(attackCommand.type(), attackCommand.objective());
                                watcher.getWindow().updateTopText("Round "+round+". [HUMAN] "+player.getNickname()+" dealt "
                                    +result.damageToDefender()+" damage to "+attackCommand.objective().getTextDisplay()+".");
                                watcher.getWindow().showAcceptControls();
                                inputSem.acquire();
                                break;
                        }
                    }
                    input = null;
                };
            }
        }

        System.out.printf("%nRound %d finished! Waiting for next round.%nCurrent map:%n", round);
        System.out.println(board.toString());
        watcher.updateBoard();
        
        watcher.getWindow().updateTopText("Round "+round+". Ended. Waiting to continue.");
        watcher.getWindow().showAcceptControls();
        inputSem.acquire();
        input = null;

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
                try {
                    do {} while(gameLoop(watcher));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
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
