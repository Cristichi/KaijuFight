package es.cristichi.obj.game;

import java.awt.Dimension;
import java.util.LinkedList;
import java.util.function.Consumer;

import es.cristichi.obj.GameObject;
import es.cristichi.obj.Player;
import es.cristichi.util.Position;

public class Board {
    protected Square[][] squares;
    protected LinkedList<Position> spawns;

    protected Board(Square[][] squares, LinkedList<Position> spawns){
        this.squares = squares;
        this.spawns = spawns;

        if (spawns.size() < 2){
            throw new IllegalArgumentException("There must be at least 2 spawns for players.");
        }
    }

    public int getRowsLength(){
        return squares.length;
    }

    public int getColumnsLength(){
        return squares.length>0?squares[0].length:0;
    }

    public Dimension getSize(){
        return new Dimension(getColumnsLength(), getRowsLength());
    }

    public Square getSquare(Position position){
        return squares[position.row()][position.column()];
    }

    public Position getPosition(Player p) {
        for (int i = 0; i < squares.length; i++) {
            Square[] row = squares[i];
            for (int j = 0; j < row.length; j++) {
                if (squares[i][j].getObject() != null &&
                    squares[i][j].getObject().equals(p)){
                    return new Position(i, j);
                }
            }
        }
        throw new NullPointerException("Player is not on the board!");
    }

    public void forEachRow(RowConsumer action){
        for (int i = 0; i < squares.length; i++) {
            action.action(i, squares[i]);
        }
    }

    public void forEachSquare(SquareConsumer action){
        for (int i = 0; i < squares.length; i++) {
            for (int j = 0; j < squares[i].length; j++) {
                action.action(new Position(i, j), squares[i][j]);
            }
        }
    }

    public LinkedList<Position> getSpawns() {
        return spawns;
    }

    public void forEachSpawn(Consumer<? super Square> action){
        for (Position spawn : spawns){
            action.accept(squares[spawn.row()][spawn.column()]);
        }
    }

    @Override
    public String toString() {
        StringBuilder ret = new StringBuilder();
        for (Square[] row : squares) {
            for (Square square : row) {
                ret.append("|"+square.getTextDisplay());
            }
            ret.append("|\n");
        }
        return ret.toString().trim();
    }

    public static class Builder {
        protected Square[][] squares;
        protected LinkedList<Position> spawns;

        public Builder(int rows, int columns){
            squares = new Square[rows][columns];
            spawns = new LinkedList<>();
        }

        public Builder addObject(Position position, GameObject gameObject){
            squares[position.row()][position.column()] = new Square(gameObject);
            return this;
        }

        public Builder addSpawnPoint(Position position){
            spawns.add(position);
            return this;
        }

        public Board build(){
            if (spawns.size() < 2){
                throw new IllegalArgumentException("There must be at least 2 spawns for players.");
            }
            
            for (int i = 0; i < squares.length; i++) {
                Square[] row = squares[i];
                for (int j = 0; j < row.length; j++) {
                    if (squares[i][j] == null){
                        squares[i][j] = new Square(null);
                    }
                }
            }

            return new Board(squares, spawns);
        }
    }

    public static enum Movement {
        UP(0,-1),
        RIGHT(1,0),
        DOWN(0,1),
        LEFT(-1,0);

        private int x,y;

        private Movement(int x, int y){
            this.x = x;
            this.y = y;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }
    }
    
    public static interface RowConsumer{
        public void action(int index, Square[] row);
    }
    
    public static interface SquareConsumer{
        public void action(Position position, Square square);
    }
}
