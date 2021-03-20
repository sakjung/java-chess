package chess.domain.board;

import chess.domain.piece.Blank;
import chess.domain.piece.Color;
import chess.domain.piece.Piece;
import java.util.List;
import java.util.Objects;

public class Square {

    private static final String SAME_TARGET = "현재 위치와 같은 곳으로 이동할 수 없습니다.";
    private static final String NOT_MOVABLE_POSITION = "이동할 수 없는 위치입니다.";
    private static final int BLACK_STARTING_POSITION = 1;
    private static final int WHITE_STARTING_POSITION = 6;

    private final Position position;
    private Piece piece;

    public Square(Position position, Piece piece) {
        this.position = position;
        this.piece = piece;
    }

    public void move(ChessBoard chessBoard, Square targetSquare) {
        if (this.equals(targetSquare)) {
            throw new IllegalArgumentException(SAME_TARGET);
        }
        if (Path.isMovable(chessBoard, this, targetSquare)) {
            targetSquare.replacePiece(piece);
            replacePiece(new Blank(Color.NO_COLOR));
            return;
        }
        throw new IllegalArgumentException(NOT_MOVABLE_POSITION);
    }

    public void replacePiece(Piece piece) {
        this.piece = piece;
    }

    public double score() {
        return piece.score();
    }

    public boolean hasSamePosition(Position position) {
        return this.position.equals(position);
    }

    public boolean hasKing() {
        return piece.isKing();
    }

    public boolean hasPawn() {
        return piece.isPawn();
    }

    public boolean hasSameColor(Color color) {
        return piece.isSameColor(color);
    }

    public boolean isNotSameColor(Square square) {
        return !piece.isSameColor(square.piece);
    }

    public Column getColumn() {
        return position.getColumn();
    }

    public boolean isBlank() {
        return piece.isBlank();
    }

    public Position getPosition() {
        return position;
    }

    public boolean isIterable() {
        return piece.isIterable();
    }

    public boolean isPawn() {
        return piece.isPawn();
    }

    public String getName() {
        return piece.getName();
    }

    public boolean hasNextPossibleSquare(Direction direction) {
        return Row.isInBound(position.getRowAsIndex() + direction.getYDegree())
            && Column.isInBound(position.getColumnAsIndex() + direction.getXDegree());
    }

    public boolean isStartingPosition() {
        if (piece.isBlack()) {
            return position.getRowAsIndex() == BLACK_STARTING_POSITION;
        }
        return position.getRowAsIndex() == WHITE_STARTING_POSITION;
    }

    public List<Direction> getDirections() {
        return piece.direction();
    }

    public Position nextPosition(Direction direction) {
        return position.nextPosition(direction.getXDegree(), direction.getYDegree());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Square square = (Square) o;
        return Objects.equals(position, square.position);
    }

    @Override
    public int hashCode() {
        return Objects.hash(position);
    }
}
