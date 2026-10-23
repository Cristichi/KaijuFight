package es.cristichi.obj;

import java.util.LinkedList;
import java.util.function.Consumer;

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

    public Square getSquare(Position position){
        return squares[position.row()][position.column()];
    }

    public Position getPosition(Player p) {
        for (int i = 0; i < squares.length; i++) {
            Square[] row = squares[i];
            for (int j = 0; j < row.length; j++) {
                if (squares[i][j].getObject().equals(p)){
                    return new Position(i, j);
                }
            }
        }
        throw new NullPointerException("Player is not on the board!");
    }

    public void forEachRow(Consumer<? super Square[]> action){
        for (Square[] row : squares) {
            action.accept(row);
        }
    }

    public void forEachSquare(Consumer<? super Square> action){
        for (Square[] row : squares) {
            for (Square square : row) {
                action.accept(square);
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
        return ret.toString();
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
        U_UP(0,1),
        U_RIGHT(1,0),
        U_DOWN(0,-1),
        U_LEFT(-1,0);
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
}
