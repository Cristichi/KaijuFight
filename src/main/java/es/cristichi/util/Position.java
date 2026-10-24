package es.cristichi.util;

import es.cristichi.obj.game.Board.Movement;

public record Position(int row, int col) {

    public Position afterMove(Movement movement) {
        return new Position(row+movement.getY(), col+movement.getX());
    }

    public boolean isOrtogonalMelee(Position o){
        return (row==o.row && (col+1==o.col || col-1==o.col))
                || (col==o.col && (row+1==o.row || row-1==o.row));
    }

    @Override
    public final String toString() {
        return String.format("Pos(%d,%d)", row, col);
    }
}
