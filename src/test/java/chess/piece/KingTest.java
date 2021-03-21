package chess.piece;

import chess.domain.board.ChessBoard;
import chess.domain.board.Position;
import chess.domain.piece.Color;
import chess.domain.piece.King;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static chess.domain.piece.Piece.NOT_MOVABLE_POSITION_ERROR;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

public class KingTest {
	private ChessBoard chessBoard;

	@BeforeEach
	void setUp() {
		chessBoard = new ChessBoard();
		chessBoard.initBoard();
	}

	@DisplayName("킹이 빈 공간으로 제대로 이동하는지")
	@Test
	void moveKing_Blank_movePosition() {
		chessBoard.move("e2", "e4");
		chessBoard.move("e1", "e2");
		assertThat(chessBoard.getPiece(Position.of("e2")).getName()).isEqualTo("k");
	}

	@DisplayName("킹이 이동하는 자리에 아군이 존재하면 에러를 반환 하는지")
	@Test
	void moveKing_allyAtDestination_throwError() {
		assertThatThrownBy(() -> chessBoard.move("e1", "e2"))
				.isInstanceOf(IllegalArgumentException.class)
				.hasMessage(NOT_MOVABLE_POSITION_ERROR);
	}

	@DisplayName("킹이 이동하는 자리에 적군이 존재하면 적군을 제대로 죽이는지")
	@Test
	void moveKing_enemyAtDestination_movePosition() {
		chessBoard.replace(Position.of("b6"), new King(Color.WHITE, Position.of("b6")));
		chessBoard.move("b6", "b7");
		assertThat(chessBoard.getPiece(Position.of("b7")).getName()).isEqualTo("k");

		chessBoard.move("b7", "a7");
		assertThat(chessBoard.getPiece(Position.of("a7")).getName()).isEqualTo("k");

		chessBoard.move("a7", "b8");
		assertThat(chessBoard.getPiece(Position.of("b8")).getName()).isEqualTo("k");
	}
}
