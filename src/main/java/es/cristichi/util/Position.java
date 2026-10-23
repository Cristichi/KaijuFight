package es.cristichi.util;

import es.cristichi.obj.game.Board.Movement;

public record Position(int row, int column) {

    public Position afterMove(Movement movement) {
        return new Position(row+movement.getY(), column+movement.getX());
    }
}
