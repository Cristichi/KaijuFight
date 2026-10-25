package es.cristichi.game;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Semaphore;

import es.cristichi.game.obj.AttackInfo;
import es.cristichi.game.obj.AttackResult;
import es.cristichi.game.obj.AttackType;
import es.cristichi.game.obj.Board;
import es.cristichi.game.obj.Player;
import es.cristichi.game.obj.Position;
import es.cristichi.game.obj.Square;
import es.cristichi.game.obj.Board.Movement;
import es.cristichi.ui.BoardComponent;

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
            this.players.put(player.getName(), player);
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
        watcher.getWindow().updateTopText("Round "+round+".");
        watcher.getWindow().showAcceptControls();
        inputSem.acquire();
        input = null;
        
        for (String playerName : playerOrder) {
            Player player = players.get(playerName);
            Position pos = board.getPosition(player);
            Square square = board.getSquare(pos);

            watcher.getWindow().updateTopText("Round "+round+". Turn for ["+(player.isCpu()?"CPU] ":"HUMAN] ")+player.getName());

            if (player.isCpu()){
                // CPU movement
                Movement previousMove = null;
                for (int i = 0; i < player.getSpeed(); i++) {
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

                    if (!moves.isEmpty()){
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
                        watcher.getWindow().updateTopText("Round "+round+". ["+(player.isCpu()?"CPU] ":"HUMAN] ")+player.getName()+" moved "+movement.name());
                        watcher.getWindow().showAcceptControls();
                        inputSem.acquire();
                        input = null;
                    }
                }
                // CPU attack TBI

                watcher.updateBoard();
            } else {
                // Player moves
                for (int i = 0; i < player.getSpeed(); i++) {
                    watcher.updateBoard();
                    watcher.getWindow().updateTopText("Round "+round+". [HUMAN] "+player.getName()+" choose your move #"+(i+1)+".");
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
                                    watcher.getWindow().updateTopText("Round "+round+". [HUMAN] "+player.getName()+" can't move to that position.");
                                    watcher.getWindow().showMoveControls();
                                }
                            } catch (IndexOutOfBoundsException e){
                                watcher.getWindow().updateTopText("Round "+round+". [HUMAN] "+player.getName()+" can't move to out of bounds. Speedrunners must work harder.");
                                watcher.getWindow().showMoveControls();
                            }
                        } else if (input == null){
                            needInput = false;
                            i = player.getSpeed();
                        } else {
                            watcher.getWindow().updateTopText("Round "+round+". [HUMAN] "+player.getName()+" sent an unknown command.");
                            watcher.getWindow().showMoveControls();
                        }
                        input = null;
                    };
                }
                // Player attacks
                LinkedList<AttackInfo> possibleAttacks = new LinkedList<>();
                final Position playerPos = new Position(pos.row(), pos.col());
                board.forEachSquare((sPos, sSquare) -> {
                    if ((sPos.col() == playerPos.col() || sPos.row() == playerPos.row())
                        &&(sPos.col() != playerPos.col() || sPos.row() != playerPos.row()) && sSquare.getObject() != null){
                        AttackType type = playerPos.isOrtogonalMelee(sPos)?AttackType.MELEE:AttackType.RANGED;
                        if (sSquare.getObject().canBeAttackedBy(type, player)){
                            if (type.equals(AttackType.MELEE)){
                                possibleAttacks.add(new AttackInfo(AttackType.MELEE, sSquare.getObject()));
                            } else {
                                boolean obstacled = false;
                                if (sPos.col() == playerPos.col()){
                                    boolean sLower = sPos.row() < playerPos.row();
                                    for (int start = (sLower?sPos.row():playerPos.row()); !obstacled && start < (sLower?playerPos.row():sPos.row());start++){
                                        Square checkObs = board.getSquare(new Position(sPos.col(), start));
                                        if (checkObs.getObject() != null && checkObs.getObject().stopsRangedAttacks(player)){
                                            obstacled = true;
                                        }
                                    }
                                } else if (sPos.row() == playerPos.row()){
                                    boolean sLower = sPos.col() < playerPos.col();
                                    for (int start = (sLower?sPos.col():playerPos.col()); !obstacled && start < (sLower?playerPos.col():sPos.col());start++){
                                        Square checkObs = board.getSquare(new Position(start, sPos.row()));
                                        if (checkObs.getObject() != null && checkObs.getObject().stopsRangedAttacks(player)){
                                            obstacled = true;
                                        }
                                    }
                                }
                                if (!obstacled){
                                    possibleAttacks.add(new AttackInfo(AttackType.RANGED, sSquare.getObject()));
                                }
                            }
                        }
                    }
                });

                watcher.updateBoard();
                watcher.getWindow().updateTopText("Round "+round+". [HUMAN] "+player.getName()+" choose your attack.");
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
                                if (!player.modifyMana(-1)){
                                    watcher.getWindow().updateTopText("Round "+round+". [HUMAN] "+player.getName()+" doesn't have mana to make ranged attacks.");
                                    watcher.getWindow().showMoveControls();
                                    needInput = true;
                                }
                            case MELEE:
                                AttackResult result = attackCommand.objective().receiveAttack(attackCommand.type(), player, player.getAttackDamage(attackCommand.type()));
                                watcher.getWindow().updateTopText("Round "+round+". [HUMAN] "+player.getName()+" dealt "
                                    +result.damageToDefender()+" damage to "+attackCommand.objective().getName()+".");
                                watcher.getWindow().showAcceptControls();
                                inputSem.acquire();
                                break;
                        }
                    }
                    input = null;
                };
            }
        }

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
