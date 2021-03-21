package chess.piece;

import chess.domain.board.ChessBoard;
import chess.domain.board.Position;
import chess.domain.piece.*;
import chess.view.OutputView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static chess.domain.piece.Piece.NOT_MOVABLE_POSITION_ERROR;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

public class PawnTest {
	private ChessBoard chessBoard;

	@BeforeEach
	void setUp() {
		chessBoard = new ChessBoard();
		chessBoard.initBoard();
	}

	@DisplayName("폰을 빈 공간으로 한칸 전진시킬 때 제대로 가는지")
	@Test
	void movePawn_blank_movePosition() {
		chessBoard.move("b2", "b3");
		assertThat(chessBoard.getPiece(Position.of("b3")).getName()).isEqualTo("p");
	}

	@DisplayName("폰이 직진 이동시 적군이나 아군이 존재하면 에러를 반환하는지")
	@Test
	void movePawn_nonBlankDestination_throwError() {
		chessBoard.replace(Position.of("c3"), new Pawn(Color.BLACK, Position.of("c3")));
		assertThatThrownBy(() -> chessBoard.move("c3", "c2"))
				.isInstanceOf(IllegalArgumentException.class)
				.hasMessage(NOT_MOVABLE_POSITION_ERROR);

		chessBoard.replace(Position.of("c6"), new Pawn(Color.BLACK, Position.of("c6")));
		assertThatThrownBy(() -> chessBoard.move("c7", "c6"))
				.isInstanceOf(IllegalArgumentException.class)
				.hasMessage(NOT_MOVABLE_POSITION_ERROR);
	}

	@DisplayName("폰이 시작지점에 있고 빈 공간으로 두칸 전진시킬 때 제대로 가는지")
	@Test
	void movePawnAtStartingPosition_twoStepsToBlank_movePosition() {
		chessBoard.move("b2", "b4");
		assertThat(chessBoard.getPiece(Position.of("b4")).getName()).isEqualTo("p");
	}

	@DisplayName("폰이 시작지점에 있고 두칸 전진시킬 때 그 경로와 목적지에 적군이나 아군이 있다면 에러를 반환하는지")
	@Test
	void movePawnAtStartingPosition_twoStepsWithNonBlankOnPathAndDestination_throwError() {
		chessBoard.replace(Position.of("b3"), new Knight(Color.WHITE, Position.of("b3")));
		assertThatThrownBy(() -> chessBoard.move("b2", "b4"))
				.isInstanceOf(IllegalArgumentException.class)
				.hasMessage(NOT_MOVABLE_POSITION_ERROR);

		chessBoard.replace(Position.of("b3"), new Blank(Color.NO_COLOR, Position.of("b3")));
		chessBoard.replace(Position.of("b4"), new Knight(Color.WHITE, Position.of("b4"))); // 아군
		assertThatThrownBy(() -> chessBoard.move("b2", "b4"))
				.isInstanceOf(IllegalArgumentException.class)
				.hasMessage(NOT_MOVABLE_POSITION_ERROR);

		chessBoard.replace(Position.of("b4"), new Knight(Color.BLACK, Position.of("b4"))); // 적군
		assertThatThrownBy(() -> chessBoard.move("b2", "b4"))
				.isInstanceOf(IllegalArgumentException.class)
				.hasMessage(NOT_MOVABLE_POSITION_ERROR);
	}

	@DisplayName("폰이 대각선에 적이 있을 때 공격을 제대로 하는지")
	@Test
	void movePawn_enemyAtDiagonalDestination_movePosition() {
		chessBoard.replace(Position.of("c3"), new Pawn(Color.BLACK, Position.of("c3")));
		chessBoard.move("c3", "b2");
		assertThat(chessBoard.getPiece(Position.of("b2")).getName()).isEqualTo("P");

		chessBoard.replace(Position.of("c3"), new Pawn(Color.BLACK, Position.of("c3")));
		chessBoard.move("c3", "d2");
		assertThat(chessBoard.getPiece(Position.of("d2")).getName()).isEqualTo("P");
	}

	@DisplayName("폰이 대각선에 적이 없는데 이동하려고 할 때 에러를 반환 하는지")
	@Test
	void movePawn_noEnemyAtDiagonalDestination_throwError() {
		assertThatThrownBy(() -> chessBoard.move("b2", "c3"))
				.isInstanceOf(IllegalArgumentException.class)
				.hasMessage(NOT_MOVABLE_POSITION_ERROR);
		chessBoard.replace(Position.of("c3"), new Queen(Color.WHITE, Position.of("c3")));
		assertThatThrownBy(() -> chessBoard.move("b2", "c3"))
				.isInstanceOf(IllegalArgumentException.class)
				.hasMessage(NOT_MOVABLE_POSITION_ERROR);
	}
}
