package heuristics;

import java.util.BitSet;

import melissaILoveTablut.MILTState;

public class MILTBlackHeuristics {
	/*
	 * scala -100 - +100
	 * 
	 */

	/*
	 * private static final int WHITE_ALIVE = 0; private static final int
	 * BLACK_ALIVE = 1; private static final int KING_SAFE = 2; private static final
	 * int WHITE_SAFE = 3; private static final int WHITE_MOVEMENT = 4;
	 */

	private static final int WHITE_PAWN_VAL = 20;
	private static final int BLACK_PAWN_VAL = 10;
	private static final int UNSAFE_WHITE_PAWN_VAL = 10;
	private static final int UNSAFE_BLACK_PAWN_VAL = 5;

	private static final int WHITES_OPENING_THRESHOLD = 5;
	private static final int WHITES_MIDDLEGAME_THRESHOLD = 3;

	private MILTState state;
	private int whitePawns;
	private int blackPawns;

	public MILTBlackHeuristics(MILTState state) {
		this.state = state;
		this.whitePawns = state.getWhites().cardinality();
		this.blackPawns = state.getBlacks().cardinality();

	}

	public int evaluation() {
		int stateValue = 0;
		
		if(state.isTerminal()) {
			if(state.getKing().cardinality()!=0) {
				return 1000;
			}
			if(state.getKing().intersects(MILTState.escapes)) {
				return  -1000;
			}
		}
		
		if (state.isKingThreatened())
			return 1000;

		if (state.hasKingEscapes()) {
			return -30;
		}

	

		// pesi per il numero di pedine vive di ogni colore
		int whitePawnsVal = this.whitePawns * WHITE_PAWN_VAL;
		int blackPawnsVal = this.blackPawns * BLACK_PAWN_VAL;
		int kingPosVal = evalKingPos();
		int whitePawnsSafety = this.state.getWhitePawnsThreatened() * UNSAFE_WHITE_PAWN_VAL;
		int blackPawnsSafety = this.state.getBlackPawnsThreatened() * UNSAFE_BLACK_PAWN_VAL;

		stateValue = 2*((blackPawnsVal - blackPawnsSafety) - (whitePawnsVal - whitePawnsSafety)) + kingPosVal;

		return stateValue;
	}

	private int evalKingPos() {
		int val = 0;

//		if (this.whitePawns >= WHITES_OPENING_THRESHOLD) {
//			// opening
//			if (state.getKing().intersects(MILTState.throne)) {
//				val = 0;
//			} else if (state.getKing().intersects(MILTState.aroundThrone)) {
//				val = 5;
//			} else {
//				val = 10;
//			}
//
//		} else if (this.whitePawns >= WHITES_MIDDLEGAME_THRESHOLD) {
//			// middle-game
//			if (state.getKing().intersects(MILTState.throne)) {
//				val = 0;
//			} else if (state.getKing().intersects(MILTState.aroundThrone)) {
//				val = 10;
//			} else {
//				val = 15;
//			}
//		} else {
//			// endgame
//			if (state.getKing().intersects(MILTState.throne)) {
//				val = 5;
//			} else if (state.getKing().intersects(MILTState.aroundThrone)) {
//				val = 15;
//			} else {
//				val = 20;
//			}
//		}
		if (state.getKing().intersects(MILTState.throne)) {
			val = 0;
		} else if (state.getKing().intersects(MILTState.aroundThrone)) {
			val = 5;
		} else {
			val = 10;
		}
		return val;
	}
}