package chess.domain.game;

import chess.domain.piece.Color;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class ResultTest {

	@Test
	void resultWhite() {
		Result result = new Result(10, 15);
		Map<Color, String> winOrLose = result.getWinOrLose();

		assertThat(winOrLose.get(Color.BLACK)).isEqualTo(Result.LOSE);
		assertThat(winOrLose.get(Color.WHITE)).isEqualTo(Result.WIN);
	}

	@Test
	void resultDraw() {
		Result result = new Result(15, 15);
		Map<Color, String> winOrLose = result.getWinOrLose();

		assertThat(winOrLose.get(Color.BLACK)).isEqualTo(Result.DRAW);
		assertThat(winOrLose.get(Color.WHITE)).isEqualTo(Result.DRAW);
	}

	@Test
	void resultBlack() {
		Result result = new Result(15, 10);
		Map<Color, String> winOrLose = result.getWinOrLose();

		assertThat(winOrLose.get(Color.BLACK)).isEqualTo(Result.WIN);
		assertThat(winOrLose.get(Color.WHITE)).isEqualTo(Result.LOSE);
	}
}
