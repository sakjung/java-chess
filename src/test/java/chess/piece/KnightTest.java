package chess.piece;

import chess.domain.board.ChessBoard;
import chess.domain.board.Position;
import chess.domain.piece.Bishop;
import chess.domain.piece.Color;
import chess.domain.piece.Knight;
import chess.domain.piece.Pawn;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static chess.domain.piece.Piece.NOT_MOVABLE_POSITION_ERROR;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

public class KnightTest {
	private ChessBoard chessBoard;

	@BeforeEach
	void setUp() {
		chessBoard = new ChessBoard();
		chessBoard.initBoard();
	}

	@DisplayName("나이트가 빈 공간으로 제대로 이동하는지")
	@Test
	void moveKnight_blank_movePosition() {
		chessBoard.move("b1", "a3");
		assertThat(chessBoard.getPiece(Position.of("a3")).getName()).isEqualTo("n");
	}

	@DisplayName("나이트가 이동하는 자리에 적군이 존재하면 적군을 제대로 죽이는지")
	@Test
	void moveKnight_enemyAtDestination_movePosition() {
		chessBoard.replace(Position.of("b5"), new Knight(Color.WHITE, Position.of("b5")));
		chessBoard.move("b5", "c7");
		assertThat(chessBoard.getPiece(Position.of("c7")).getName()).isEqualTo("n");
	}

	@DisplayName("나이트가 이동하는 자리에 아군이 존재하면 에러를 반환 하는지")
	@Test
	void moveKnight_allyAtDestination_throwError() {
		assertThatThrownBy(() -> chessBoard.move("b1", "d2"))
				.isInstanceOf(IllegalArgumentException.class)
				.hasMessage(NOT_MOVABLE_POSITION_ERROR);
	}
}
