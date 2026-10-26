package es.cristichi.kaiju.game;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Semaphore;

import es.cristichi.kaiju.game.obj.AttackInfo;
import es.cristichi.kaiju.game.obj.AttackResult;
import es.cristichi.kaiju.game.obj.AttackType;
import es.cristichi.kaiju.game.obj.Board;
import es.cristichi.kaiju.game.obj.Kaiju;
import es.cristichi.kaiju.game.obj.Position;
import es.cristichi.kaiju.game.obj.Board.Movement;
import es.cristichi.kaiju.ui.BoardComponent;

public class Game {
    private Semaphore inputSem;
    private Object input;

    protected boolean ready;

    protected Random rng;

    protected Board board;
    protected HashMap<String, Kaiju> kaijus;
    protected LinkedList<String> kaijuOrder;

    public Game(Random rng, Board board, Kaiju[] kaijus) {
        this.rng = rng;

        this.board = board;
        this.kaijus = new HashMap<>(kaijus.length);
        for (Kaiju kaiju : kaijus) {
            this.kaijus.put(kaiju.getName(), kaiju);
        }

        ready = false;

        inputSem = new Semaphore(0);
    }

    public Game(long seed, Board board, Kaiju[] kaijus) {
        this(new Random(seed), board, kaijus);
    }

    public Game(Board board, Kaiju[] kaijus) {
        this(new Random(), board, kaijus);
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
        
        for (String kaijuName : kaijuOrder) {
            Kaiju kaiju = kaijus.get(kaijuName);
            Position pos = board.getPosition(kaiju);

            watcher.getWindow().updateTopText("Round "+round+". Turn for ["+(kaiju.isCpu()?"CPU] ":"HUMAN] ")+kaiju.getName());

            if (kaiju.isCpu()){
                // CPU movement
                Movement previousMove = null;
                for (int i = 0; i < kaiju.getSpeed(); i++) {
                    LinkedList<Movement> moves = new LinkedList<>();
                    for (Movement movement : Movement.values()) {
                        if (!movement.equals(previousMove)){
                            try {
                                GameObject moveGO = board.getGameObject(pos.afterMove(movement));
                                if (moveGO == null || moveGO.canBeReplacedBy(kaiju)){
                                    moves.add(movement);
                                }
                            }catch (IndexOutOfBoundsException _){
                            }
                        }
                    }

                    if (!moves.isEmpty()){
                        Movement movement = moves.get(rng.nextInt(moves.size()));
                        Position nextPos = pos.afterMove(movement);
                        board.setGameObject(pos, null);
                        board.setGameObject(nextPos, kaiju);
                        pos = nextPos;
                        switch (movement) {
                            case UP -> previousMove = Movement.DOWN;
                            case RIGHT -> previousMove = Movement.LEFT;
                            case DOWN -> previousMove = Movement.UP;
                            case LEFT -> previousMove = Movement.RIGHT;
                        }
                        watcher.getWindow().updateTopText("Round "+round+". ["+(kaiju.isCpu()?"CPU] ":"HUMAN] ")+kaiju.getName()+" moved "+movement.name());
                        watcher.getWindow().showAcceptControls();
                        inputSem.acquire();
                        input = null;
                    }
                }
                // CPU attack TBI

                watcher.updateBoard();
            } else {
                // Kaiju moves
                for (int i = 0; i < kaiju.getSpeed(); i++) {
                    watcher.updateBoard();
                    watcher.getWindow().updateTopText("Round "+round+". [HUMAN] "+kaiju.getName()+" choose your move #"+(i+1)+".");
                    watcher.getWindow().showMoveControls();
                    boolean needInput = true;
                    while(needInput) {
                        inputSem.acquire();
                        watcher.getWindow().removeControls();
                        if (input instanceof Movement inMove){
                            try {
                                Position nextPos = pos.afterMove(inMove);
                                GameObject moveGO = board.getGameObject(nextPos);
                                if (moveGO == null || moveGO.canBeReplacedBy(kaiju)){
                                    board.setGameObject(pos, null);
                                    board.setGameObject(nextPos, kaiju);
                                    pos = nextPos;
                                    needInput = false;
                                } else {
                                    watcher.getWindow().updateTopText("Round "+round+". [HUMAN] "+kaiju.getName()+" can't move to that position.");
                                    watcher.getWindow().showMoveControls();
                                }
                            } catch (IndexOutOfBoundsException e){
                                watcher.getWindow().updateTopText("Round "+round+". [HUMAN] "+kaiju.getName()+" can't move to out of bounds. Speedrunners must work harder.");
                                watcher.getWindow().showMoveControls();
                            }
                        } else if (input == null){
                            needInput = false;
                            i = kaiju.getSpeed();
                        } else {
                            watcher.getWindow().updateTopText("Round "+round+". [HUMAN] "+kaiju.getName()+" sent an unknown command.");
                            watcher.getWindow().showMoveControls();
                        }
                        input = null;
                    };
                }
                // Kaiju attacks
                LinkedList<AttackInfo> possibleAttacks = new LinkedList<>();
                final Position kaijuPos = new Position(pos.row(), pos.col());
                board.forEachSquare((sPos, sGameObj) -> {
                    if ((sPos.col() == kaijuPos.col() || sPos.row() == kaijuPos.row())
                        &&(sPos.col() != kaijuPos.col() || sPos.row() != kaijuPos.row()) && sGameObj != null){
                        AttackType type = kaijuPos.isOrtogonalMelee(sPos)?AttackType.MELEE:AttackType.RANGED;
                        if (sGameObj.canBeAttackedBy(type, kaiju)){
                            if (type.equals(AttackType.MELEE)){
                                possibleAttacks.add(new AttackInfo(AttackType.MELEE, sPos, sGameObj));
                            } else {
                                boolean obstacled = false;
                                if (sPos.col() == kaijuPos.col()){
                                    boolean sLower = sPos.row() < kaijuPos.row();
                                    for (int start = (sLower?sPos.row():kaijuPos.row()); !obstacled && start < (sLower?kaijuPos.row():sPos.row());start++){
                                        GameObject checkObs = board.getGameObject(new Position(sPos.col(), start));
                                        if (checkObs != null && checkObs.stopsRangedAttacks(kaiju)){
                                            obstacled = true;
                                        }
                                    }
                                } else if (sPos.row() == kaijuPos.row()){
                                    boolean sLower = sPos.col() < kaijuPos.col();
                                    for (int start = (sLower?sPos.col():kaijuPos.col()); !obstacled && start < (sLower?kaijuPos.col():sPos.col());start++){
                                        GameObject checkObs = board.getGameObject(new Position(start, sPos.row()));
                                        if (checkObs != null && checkObs.stopsRangedAttacks(kaiju)){
                                            obstacled = true;
                                        }
                                    }
                                }
                                if (!obstacled){
                                    possibleAttacks.add(new AttackInfo(AttackType.RANGED, sPos, sGameObj));
                                }
                            }
                        }
                    }
                });

                watcher.updateBoard();
                watcher.getWindow().updateTopText("Round "+round+". [HUMAN] "+kaiju.getName()+" choose your attack.");
                watcher.getWindow().showAttackControls(kaiju, possibleAttacks);
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
                                if (!kaiju.modifyMana(-1)){
                                    watcher.getWindow().updateTopText("Round "+round+". [HUMAN] "+kaiju.getName()+" doesn't have mana to make ranged attacks.");
                                    watcher.getWindow().showMoveControls();
                                    needInput = true;
                                }
                            case MELEE:
                                AttackResult result = attackCommand.objGameObject().receiveAttack(attackCommand.type(), kaiju, kaiju.getAttackDamage(attackCommand.type()));
                                watcher.getWindow().updateTopText("Round "+round+". [HUMAN] "+kaiju.getName()+" dealt "
                                    +result.damageToDefender()+" damage to "+attackCommand.objGameObject().getName()+".");
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
        kaijuOrder = new LinkedList<>(kaijus.keySet());
        
        if (kaijuOrder.size() > spawns.size()){
            throw new IllegalArgumentException("Too many kaijus (%d) for the amount of spawns in the Board (%d). Point more spawns or make less friends."
                .formatted(kaijuOrder.size(), spawns.size()));
        }

        Collections.shuffle(spawns, rng);
        Collections.shuffle(kaijuOrder, rng);
        Iterator<String> rngNames = kaijuOrder.iterator();

        for (Position spawn : spawns) {
            if (rngNames.hasNext()){
                board.setGameObject(spawn, kaijus.get(rngNames.next()));
            }
        }
        
        Collections.shuffle(kaijuOrder, rng);

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
