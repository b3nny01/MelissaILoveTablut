package melissaILoveTablut.heuristics;

import melissaILoveTablut.MILTState;

public class MILTEvaluator {
	public static final int WHITE_WIN_VAL = 1000;
	public static final int BLACK_WIN_VAL = 1000;

	private int evaluateBlack(MILTState state) {
		int val = 0;
		if (state.isTerminal()) {
			val = (state.isWhiteWin()) ? -WHITE_WIN_VAL : +BLACK_WIN_VAL;
		} else {
			val += 2*(state.getBlacksWhitesDiff() - 8);
			val += state.getWhitePawnsThreatened();
			val += this.evaluateBlackKingPos(state);
			val -= state.getBlackPawnsThreatened();

			val = Math.min(val, BLACK_WIN_VAL);
			val = Math.max(val, -WHITE_WIN_VAL);
		}
		return val;
	}

	private int evaluateBlackKingPos(MILTState state) {
		int val = 0;
		if (state.isKingThreatened()) {
			val = BLACK_WIN_VAL;
		}
		if (state.getKing().intersects(MILTState.throne)) {
			val = 0;
		} else if (state.getKing().intersects(MILTState.aroundThrone)) {
			val = 0;
		}
		return val;

	}

	private int evaluateWhiteKingPos(MILTState state) {
		int val = 0;
		if (state.isKingThreatened()) {
			val = -20;
		}
		if (state.getKing().intersects(MILTState.throne)) {
			val = 4;
		} else if (state.getKing().intersects(MILTState.aroundThrone)) {
			val = 2;
		}
		return val;

	}

	private int evaluateWhite(MILTState state) {
		int val = 0;
		if (state.isTerminal()) {
			val = (state.isWhiteWin()) ? +WHITE_WIN_VAL : -BLACK_WIN_VAL;
			System.out.println(val);
		} else {
			val += 3*(8 - state.getBlacksWhitesDiff());
			val += state.getBlackPawnsThreatened();
			//val += this.evaluateWhiteKingPos(state);
			val -= state.getWhitePawnsThreatened();
			
			val = Math.min(val, WHITE_WIN_VAL);
			val = Math.max(val, -BLACK_WIN_VAL);
		}
		return val;
	}

	public int evaluate(MILTState state) {
		return -evaluateWhite(state);
//				switch (state.getTurn()) {
//		case WHITE -> evaluateWhite(state);
//		case BLACK -> evaluateBlack(state);
//		};
	}

}
