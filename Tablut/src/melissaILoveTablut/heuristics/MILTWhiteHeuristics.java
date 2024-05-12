package melissaILoveTablut.heuristics;

import java.util.BitSet;
import java.util.List;

import melissaILoveTablut.MILTAction;
import melissaILoveTablut.MILTState;

public class MILTWhiteHeuristics {

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

	private static final int BLACKS_OPENING_THRESHOLD = 10;
	private static final int BLACKS_MIDDLEGAME_THRESHOLD = 5;

	private MILTState state;
	private int whitePawns;
	private int blackPawns;
	private int kingPos;

	public MILTWhiteHeuristics(MILTState state) {
		this.state = state;
		this.whitePawns = state.getWhites().cardinality();
		this.blackPawns = state.getBlacks().cardinality();
		this.kingPos = state.getKing().nextSetBit(0);

	}

	public int evaluation() {
		int stateValue = 0;

		if(state.isTerminal()) {
			if(state.getKing().cardinality()!=0) {
				return -1000;
			}
			if(state.getKing().intersects(MILTState.escapes)) {
				return  1000;
			}
		}
		
		if (state.hasKingEscapes()) {
			return 1000;
		}

		if (state.isKingThreatened())
			return -1000;
		// VALORE MOLTO NEGATVO DA DECIDERE

		// pesi per il numero di pedine vive di ogni colore
		int whitePawnsVal = this.whitePawns * WHITE_PAWN_VAL;
		int blackPawnsVal = this.blackPawns * BLACK_PAWN_VAL;
		int kingPosVal = evalKingPos();
		int whitePawnsSafety = this.state.getWhitePawnsThreatened() * UNSAFE_WHITE_PAWN_VAL;
		int blackPawnsSafety = this.state.getBlackPawnsThreatened() * UNSAFE_BLACK_PAWN_VAL;
		// int whiteKingSafety=this.state

		stateValue = 2*((whitePawnsVal - whitePawnsSafety) - (blackPawnsVal - blackPawnsSafety)) + kingPosVal;

		return stateValue;
	}

	private int evalKingPos() {
		int val = 0;

//		if (this.blackPawns >= BLACKS_OPENING_THRESHOLD) {
//			// opening
//			if (state.getKing().intersects(MILTState.throne)) {
//				val = 20;
//			} else if (state.getKing().intersects(MILTState.aroundThrone)) {
//				val = 10;
//			} else {
//				val = -5;
//			}
//
//		} else if (this.blackPawns >= BLACKS_MIDDLEGAME_THRESHOLD) {
//			// middle-game
//			if (state.getKing().intersects(MILTState.throne)) {
//				val = 10;
//			} else if (state.getKing().intersects(MILTState.aroundThrone)) {
//				val = 20;
//			} else {
//				val = 0;
//			}
//		} else {
//			// endgame
//			if (state.getKing().intersects(MILTState.throne)) {
//				val = -5;
//			} else if (state.getKing().intersects(MILTState.aroundThrone)) {
//				val = 0;
//			} else {
//				val = 10;
//			}
//		}
		
		if (state.getKing().intersects(MILTState.throne)) {
			val = 10;
		} else if (state.getKing().intersects(MILTState.aroundThrone)) {
			val = 5;
		} else {
			val = 0;
		}
		return val;
	}
}