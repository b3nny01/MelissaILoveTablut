package melissaILoveTablut.heuristics;

import java.util.BitSet;

import melissaILoveTablut.MILTState;
import melissaILoveTablut.MILTState.Turn;

public class MILTBlackEvaluator implements MILTEvaluator {

	private final static int[] kingPosVals = { 
			0, 0, 0, 0, 0, 0, 0, 0, 0, 
			0, 2, 3, -1, 0, -1, 3, 2, 0, 
			0, 3, 4, 2, 2, 2, 4, 3, 0, 
			0, -1, 2, 0, 0, 0, 2, -1, 0, 
			0, 0, 2, 0, 0, 0, 2, 0, 0, 
			0, -1, 2, 0, 0, 0, 2, -1, 0, 
			0, 3, 4, 2, 2, 2, 4, 3, 0,
			0, 2, 3, -1, 0, -1, 3, 2, 0, 
			0, 0, 0, 0, 0, 0, 0, 0, 0 };
	
	private final static int B_W_DIFF_VAL = 20;
	private final static int B_THREATENED = 5;
	private final static int W_THREATENED = 10;
	private final static int K_THREATENED = 150;
	private final static int KING_POS_VAL = 5;
	private final static int KING_MOV_VAL = 10;
	private final static int FREE_WAYS_VAL = 20;

	private double evaluateKingPos(MILTState state) {
		int kingPos = state.getKing().nextSetBit(0);
		double val = 0;
		if (kingPos >= 0) {
			if (state.getTurn() == Turn.WHITE) {
				if (state.hasKingEscapes()) {
					val = Double.POSITIVE_INFINITY;
				} else if (state.couldKingBeThreatened()) {
					val = -K_THREATENED;
				} else {
					val = kingPosVals[kingPos] * KING_POS_VAL;
					val += state.getKingMovements() * KING_MOV_VAL;
				}

			} else {
				if (state.isKingThreatened()) {
					val = Double.NEGATIVE_INFINITY;
				} else {
					val = kingPosVals[kingPos] * KING_POS_VAL;
					val += state.getKingMovements() * KING_MOV_VAL;
				}
			}
		}
		return val;

	}
	
	private double evaluateFreeWays(MILTState state) {
		BitSet temp=new BitSet(MILTState.BOARD_SIZE*MILTState.BOARD_SIZE);
		temp.or(state.getWhites());
		temp.or(state.getBlacks());
		temp.and(MILTState.whiteStarts);
		return FREE_WAYS_VAL * (8-temp.cardinality());
	}

	public double evaluate(MILTState state) {
		double val = 0;
		if (state.isTerminal()) {
			val = (state.isWhiteWin()) ? Double.NEGATIVE_INFINITY : Double.POSITIVE_INFINITY;
		} else {
			val += B_W_DIFF_VAL * state.getBlacksWhitesDiff();
			val -= B_THREATENED * state.getBlackPawnsThreatened();
			val -= this.evaluateKingPos(state);
			val -= this.evaluateFreeWays(state);
			val += W_THREATENED * state.getWhitePawnsThreatened();
		}
		return val;
	}
}