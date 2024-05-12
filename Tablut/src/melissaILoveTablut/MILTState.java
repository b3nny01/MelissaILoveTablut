package melissaILoveTablut;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

import heuristics.MILTBlackHeuristics;
import heuristics.MILTWhiteHeuristics;
import melissaILoveTablut.MILTState.Turn;

public class MILTState {

	public enum Turn {
		WHITE("W"), BLACK("B");

		private final String turn;

		private Turn(String s) {
			turn = s;
		}

		public boolean equalsTurn(String otherName) {
			return (otherName == null) ? false : turn.equals(otherName);
		}

		public String toString() {
			return turn;
		}
	}

	public enum PieceType {
		WHITE_PAWN("WP"), BLACK_PAWN("BP"), WHITE_KING("WK");

		private String name;

		private PieceType(String name) {
			this.name = name;
		}

		private String getName() {
			return name;
		}
	}

	public static final int BOARD_SIZE = 9;

	private static BitSet initEscapes() {
		BitSet result = new BitSet(BOARD_SIZE * BOARD_SIZE);
		result.set(1, 3);
		result.set(6, 8);
		result.set(9);
		result.set(17, 19);
		result.set(26);
		result.set(54);
		result.set(62, 64);
		result.set(71);
		result.set(73, 75);
		result.set(78, 80);
		return result;

	}

	private static BitSet initCampLeft() {
		BitSet result = new BitSet(BOARD_SIZE * BOARD_SIZE);
		result.set(27);
		result.set(36, 48);
		result.set(45);
		return result;
	}

	private static BitSet initCampRight() {
		BitSet result = new BitSet(BOARD_SIZE * BOARD_SIZE);
		result.set(35);
		result.set(43, 45);
		result.set(53);
		return result;
	}

	private static BitSet initCampUp() {
		BitSet result = new BitSet(BOARD_SIZE * BOARD_SIZE);
		result.set(3, 6);
		result.set(13);
		return result;
	}

	private static BitSet initCampDown() {
		BitSet result = new BitSet(BOARD_SIZE * BOARD_SIZE);
		result.set(67);
		result.set(75, 78);
		return result;
	}

	private static BitSet initCamps() {
		BitSet result = new BitSet(BOARD_SIZE * BOARD_SIZE);
		result.set(3, 6);
		result.set(13);
		result.set(27);
		result.set(35, 38);
		result.set(43, 46);
		result.set(53);
		result.set(67);
		result.set(75, 78);
		return result;
	}

	private static BitSet initThrone() {
		BitSet result = new BitSet(BOARD_SIZE * BOARD_SIZE);
		result.set(40);
		return result;
	}

	private static BitSet initAroundThrone() {
		BitSet result = new BitSet(BOARD_SIZE * BOARD_SIZE);
		result.set(31);
		result.set(39);
		result.set(41);
		result.set(49);
		return result;
	}

	private static BitSet initAroundThroneSurroundedLeft() {
		BitSet result = new BitSet(BOARD_SIZE * BOARD_SIZE);
		result.set(30);
		result.set(38);
		result.set(48);
		return result;
	};

	private static BitSet initAroundThroneSurroundedRight() {
		BitSet result = new BitSet(BOARD_SIZE * BOARD_SIZE);
		result.set(32);
		result.set(42);
		result.set(50);
		return result;
	};

	private static BitSet initAroundThroneSurroundedUp() {
		BitSet result = new BitSet(BOARD_SIZE * BOARD_SIZE);
		result.set(30);
		result.set(22);
		result.set(32);
		return result;
	};

	private static BitSet initAroundThroneSurroundedDown() {
		BitSet result = new BitSet(BOARD_SIZE * BOARD_SIZE);
		result.set(48);
		result.set(58);
		result.set(50);
		return result;
	};

	private static MILTState initInitialState() {

		// white setup
		BitSet whites = new BitSet(MILTState.BOARD_SIZE * MILTState.BOARD_SIZE);
		whites.set(22);
		whites.set(31);
		whites.set(38);
		whites.set(39);
		whites.set(41);
		whites.set(42);
		whites.set(49);
		whites.set(58);

		// black setup
		BitSet blacks = initCamps();

		// king setup
		BitSet king = initThrone();

		return new MILTState(Turn.WHITE, whites, blacks, king);
	}

	public static final BitSet escapes = initEscapes();
	public static final BitSet campLeft = initCampLeft();
	public static final BitSet campRight = initCampRight();
	public static final BitSet campUp = initCampUp();
	public static final BitSet campDown = initCampDown();
	public static final BitSet camps = initCamps();
	public static final BitSet throne = initThrone();
	public static final BitSet aroundThrone = initAroundThrone();
	public static final BitSet aroundThroneSurroundedLeft = initAroundThroneSurroundedLeft();
	public static final BitSet aroundThroneSurroundedRight = initAroundThroneSurroundedRight();
	public static final BitSet aroundThroneSurroundedUp = initAroundThroneSurroundedUp();
	public static final BitSet aroundThroneSurroundedDown = initAroundThroneSurroundedDown();
	public static final MILTState initialState = initInitialState();

	private Turn turn;
	private BitSet king;
	private BitSet whites;
	private BitSet blacks;
	private List<MILTAction> availableActions;
	private int whitePawnsThreatened;
	private int blackPawnsThreatened;
	private boolean kingThreatened;

	private boolean checkKingThreatened(int row, int col) {

		// trono
		if (this.king.intersects(throne)) {
			BitSet aroundThroneAndBlacks = (BitSet) throne.clone();
			aroundThroneAndBlacks.and(this.blacks);
			if (aroundThroneAndBlacks.cardinality() == 3) {
				return true;
			}
		}

		// attorno al trono
		else if (this.king.intersects(aroundThrone)) {

			BitSet surroundedThroneAndBlack = (BitSet) aroundThroneSurroundedLeft.clone();
			surroundedThroneAndBlack.and(this.blacks);
			if (surroundedThroneAndBlack.cardinality() == 2) {
				return true;
			}

			surroundedThroneAndBlack = (BitSet) aroundThroneSurroundedRight.clone();
			surroundedThroneAndBlack.and(this.blacks);
			if (surroundedThroneAndBlack.cardinality() == 2) {
				return true;
			}

			surroundedThroneAndBlack = (BitSet) aroundThroneSurroundedUp.clone();
			surroundedThroneAndBlack.and(this.blacks);
			if (surroundedThroneAndBlack.cardinality() == 2) {
				return true;
			}

			surroundedThroneAndBlack = (BitSet) aroundThroneSurroundedDown.clone();
			surroundedThroneAndBlack.and(this.blacks);
			if (surroundedThroneAndBlack.cardinality() == 2) {
				return true;
			}
		}

		// vicino barriera/campo + posizione generica
		else {
			if (checkRow(this.king.nextSetBit(0)) || checkColumn(this.king.nextSetBit(0)))
				return true;
		}

		return false;

	}

	private boolean checkRow(int position) {

		int row = position / BOARD_SIZE;
		int col = position - row * BOARD_SIZE;

		// capture left
		if (col >= 2) {
			if (this.getBlacks().get(row + BOARD_SIZE + col - 2) || camps.get(row * BOARD_SIZE + col - 2)) {
				return true;
			}
		}

		// capture right
		if (col < BOARD_SIZE - 2) {
			if (this.getBlacks().get(row + BOARD_SIZE + col + 2) || camps.get(row * BOARD_SIZE + col + 2)) {
				return true;
			}

		}

		return false;
	}

	private boolean checkColumn(int position) {

		int row = position / BOARD_SIZE;
		int col = position - row * BOARD_SIZE;

		// capture up
		if (row >= 2) {
			if (this.getBlacks().get((row - 2) * BOARD_SIZE + col) || camps.get((row - 2) * BOARD_SIZE + col)) {
				return true;
			}
		}

		// capture down
		if (row < BOARD_SIZE - 2) {
			if (this.getBlacks().get((row + 2) * BOARD_SIZE + col) || camps.get((row + 2) * BOARD_SIZE + col)) {
				return true;
			}
		}

		return false;
	}

	private boolean isBlackThreatened(int pos, int row, int col) {
		if (camps.get((row * BOARD_SIZE) + col)) {
			return false;
		}
		int newPos = -1;
		// right threat
		if (col > 0) {
			if (whites.get(pos - 1) || king.get(pos - 1) || camps.get(pos - 1) || throne.get(pos - 1)) {
				for (int j = 2; j < BOARD_SIZE - col; j++) {
					newPos = pos + j;
					if (blacks.get(newPos) || throne.get(newPos) || camps.get(newPos)) {
						break;
					} else if (whites.get(newPos) || king.get(newPos)) {
						return true;
					}
				}
			}
		}
		// left threat
		if (col < BOARD_SIZE - 1) {
			if (whites.get(pos + 1) || king.get(pos + 1) || camps.get(pos + 1) || throne.get(pos + 1)) {
				for (int j = 2; j <= col; j++) {
					newPos = pos - j;
					if (blacks.get(newPos) || throne.get(newPos) || camps.get(newPos)) {
						break;
					} else if (whites.get(newPos) || king.get(newPos)) {
						return true;
					}
				}
			}
		}

		// down threat
		if (row > 0) {
			if (whites.get(pos - BOARD_SIZE) || king.get(pos - BOARD_SIZE) || camps.get(pos - BOARD_SIZE)
					|| throne.get(pos - BOARD_SIZE)) {
				for (int j = 2; j < BOARD_SIZE - row; j++) {
					newPos = (row + j) * BOARD_SIZE + col;
					if (blacks.get(newPos) || throne.get(newPos) || camps.get(newPos)) {
						break;
					} else if (whites.get(newPos) || king.get(newPos)) {
						return true;
					}
				}
			}
		}

		// up threat
		if (row < BOARD_SIZE - 1) {
			if (whites.get(pos + BOARD_SIZE) || king.get(pos + BOARD_SIZE) || camps.get(pos + BOARD_SIZE)
					|| throne.get(pos + BOARD_SIZE)) {
				for (int j = 2; j <= row; j++) {
					newPos = (row - j) * BOARD_SIZE + col;
					if (blacks.get(newPos) || throne.get(newPos) || camps.get(newPos)) {
						break;
					} else if (whites.get(newPos) || king.get(newPos)) {
						return true;
					}
				}
			}
		}

		return false;
	}

	private boolean isWhiteThreatened(int pos, int row, int col) {
		int newPos = -1;
		// right threat
		if (col > 0) {
			if (blacks.get(pos - 1) || camps.get(pos - 1) || throne.get(pos - 1)) {
				for (int j = 2; j < BOARD_SIZE - col; j++) {
					newPos = pos + j;
					if (whites.get(newPos) || throne.get(newPos) || camps.get(newPos)) {
						break;
					} else if (blacks.get(newPos)) {
						return true;
					}
				}
			}
		}
		// left threat
		if (col < BOARD_SIZE - 1) {
			if (blacks.get(pos + 1) || camps.get(pos + 1) || throne.get(pos + 1)) {
				for (int j = 2; j <= col; j++) {
					newPos = pos - j;
					if (whites.get(newPos) || throne.get(newPos) || camps.get(newPos)) {
						break;
					} else if (blacks.get(newPos)) {
						return true;
					}
				}
			}
		}

		// down threat
		if (row > 0) {
			if (blacks.get(pos - BOARD_SIZE) || camps.get(pos - BOARD_SIZE) || throne.get(pos - BOARD_SIZE)) {
				for (int j = 2; j < BOARD_SIZE - row; j++) {
					newPos = (row + j) * BOARD_SIZE + col;
					if (whites.get(newPos) || throne.get(newPos) || camps.get(newPos)) {
						break;
					} else if (blacks.get(newPos)) {
						return true;
					}
				}
			}
		}

		// up threat
		if (row < BOARD_SIZE - 1) {
			if (blacks.get(pos + BOARD_SIZE) || camps.get(pos + BOARD_SIZE) || throne.get(pos + BOARD_SIZE)) {
				for (int j = 2; j <= row; j++) {
					newPos = (row - j) * BOARD_SIZE + col;
					if (whites.get(newPos) || throne.get(newPos) || camps.get(newPos)) {
						break;
					} else if (blacks.get(newPos)) {
						return true;
					}
				}
			}
		}

		return false;
	}

	private void initAvaliableActionsAndStats() {
		this.availableActions = new ArrayList<>();
		this.whitePawnsThreatened = 0;
		this.blackPawnsThreatened = 0;

		int i = -1;
		int row = -1;
		int col = -1;
		int newPos = -1;

		BitSet whiteInvalid = new BitSet(BOARD_SIZE * BOARD_SIZE);
		whiteInvalid.or(throne);
		whiteInvalid.or(king);
		whiteInvalid.or(whites);
		whiteInvalid.or(camps);
		whiteInvalid.or(blacks);

		BitSet blackBaseInvalid = new BitSet(BOARD_SIZE * BOARD_SIZE);
		blackBaseInvalid.or(throne);
		blackBaseInvalid.or(king);
		blackBaseInvalid.or(whites);

		BitSet blackInvalid = new BitSet(BOARD_SIZE * BOARD_SIZE);

		if (turn == Turn.WHITE) {
			// king
			i = this.king.nextSetBit(0);
			if (i >= 0) {
				row = i / BOARD_SIZE;
				col = i - row * BOARD_SIZE;

				// moving right
				for (int j = 1; j < BOARD_SIZE - col; j++) {
					newPos = i + j;
					if (whiteInvalid.get(newPos)) {
						break;
					}
					availableActions.add(new MILTAction(PieceType.WHITE_KING, i, newPos));
				}

				// moving left
				for (int j = 1; j <= col; j++) {
					newPos = i - j;
					if (whiteInvalid.get(newPos)) {
						break;
					}
					availableActions.add(new MILTAction(PieceType.WHITE_KING, i, newPos));
				}

				// moving down
				for (int j = 1; j < BOARD_SIZE - row; j++) {
					newPos = (row + j) * BOARD_SIZE + col;
					if (whiteInvalid.get(newPos)) {
						break;
					}
					availableActions.add(new MILTAction(PieceType.WHITE_KING, i, newPos));
				}

				// moving up
				for (int j = 1; j <= row; j++) {
					newPos = (row - j) * BOARD_SIZE + col;
					if (whiteInvalid.get(newPos)) {
						break;
					}
					availableActions.add(new MILTAction(PieceType.WHITE_KING, i, newPos));
				}
			}

			// white pawns
			for (i = whites.nextSetBit(0); i >= 0; i = whites.nextSetBit(i + 1)) {
				if (i == Integer.MAX_VALUE) {
					break;
				}
				row = i / BOARD_SIZE;
				col = i - row * BOARD_SIZE;

				if (isWhiteThreatened(i, row, col)) {
					whitePawnsThreatened++;
				}

				// moving right
				for (int j = 1; j < BOARD_SIZE - col; j++) {
					newPos = i + j;
					if (whiteInvalid.get(newPos)) {
						break;
					}
					availableActions.add(new MILTAction(PieceType.WHITE_PAWN, i, newPos));
				}

				// moving left
				for (int j = 1; j <= col; j++) {
					newPos = i - j;
					if (whiteInvalid.get(newPos)) {
						break;
					}
					availableActions.add(new MILTAction(PieceType.WHITE_PAWN, i, newPos));
				}

				// moving down
				for (int j = 1; j < BOARD_SIZE - row; j++) {
					newPos = (row + j) * BOARD_SIZE + col;
					if (whiteInvalid.get(newPos)) {
						// check threat up
						if (j < BOARD_SIZE - row - 1 && blacks.get(newPos) && (!camps.get(newPos))) {
							if (whites.get(newPos - BOARD_SIZE) || camps.get(newPos - BOARD_SIZE)
									|| throne.get(newPos - BOARD_SIZE) || king.get(newPos - BOARD_SIZE)) {
								whitePawnsThreatened++;
							}
						}
						break;
					}
					availableActions.add(new MILTAction(PieceType.WHITE_PAWN, i, newPos));
				}

				// moving up
				for (int j = 1; j <= row; j++) {
					newPos = (row - j) * BOARD_SIZE + col;
					if (whiteInvalid.get(newPos)) {
						break;
					}
					availableActions.add(new MILTAction(PieceType.WHITE_PAWN, i, newPos));
				}
			}

		} else {

			// black pawns
			for (i = blacks.nextSetBit(0); i >= 0; i = blacks.nextSetBit(i + 1)) {
				if (i == Integer.MAX_VALUE) {
					break;
				}
				row = i / BOARD_SIZE;
				col = i - row * BOARD_SIZE;
				if (isBlackThreatened(i, row, col)) {
					blackPawnsThreatened++;
				}

				// restore blackInvalid bitboard
				blackInvalid.clear();
				blackInvalid.or(blackBaseInvalid);
				blackInvalid.or(camps);
				// if startPos is inside a camp set that camp's positions to valid
				if (campRight.get(i)) {
					blackInvalid.xor(campRight);
				} else if (campLeft.get(i)) {
					blackInvalid.xor(campLeft);
				} else if (campUp.get(i)) {
					blackInvalid.xor(campUp);
				} else if (campDown.get(i)) {
					blackInvalid.xor(campDown);
				}
				// else set every camp's position to invalid
				blackInvalid.or(blacks);

				// moving right
				for (int j = 1; j < BOARD_SIZE - col; j++) {
					newPos = i + j;
					if (blackInvalid.get(newPos)) {
						break;
					}
					availableActions.add(new MILTAction(PieceType.BLACK_PAWN, i, i + j));
				}

				// moving left
				for (int j = 1; j <= col; j++) {
					newPos = i - j;
					if (blackInvalid.get(newPos)) {
						break;
					}
					availableActions.add(new MILTAction(PieceType.BLACK_PAWN, i, newPos));
				}

				// moving down
				for (int j = 1; j < BOARD_SIZE - row; j++) {
					newPos = (row + j) * BOARD_SIZE + col;
					if (blackInvalid.get(newPos)) {
						break;
					}
					availableActions.add(new MILTAction(PieceType.BLACK_PAWN, i, newPos));
				}

				// moving up
				for (int j = 1; j <= row; j++) {
					newPos = (row - j) * BOARD_SIZE + col;
					if (blackInvalid.get(newPos)) {
						break;
					}
					availableActions.add(new MILTAction(PieceType.BLACK_PAWN, i, newPos));
				}
			}

		}
	}

	public MILTState(Turn turn, BitSet king, BitSet whites, BitSet blacks) {
		super();
		this.turn = turn;
		this.king = king;
		this.whites = whites;
		this.blacks = blacks;
		this.initAvaliableActionsAndStats();

	}

	public Turn getTurn() {
		return turn;
	}

	public BitSet getKing() {
		return king;
	}

	public BitSet getWhites() {
		return whites;
	}

	public BitSet getBlacks() {
		return blacks;
	}

	public void setTurn(Turn turn) {
		this.turn = turn;
	}

	public void setKing(BitSet king) {
		this.king = king;
	}

	public void setWhites(BitSet whites) {
		this.whites = whites;
	}

	public void setBlacks(BitSet blacks) {
		this.blacks = blacks;
	}

	public int getWhitePawnsThreatened() {
		return whitePawnsThreatened;
	}

	public void setWhitePawnsThreatened(int whitePawnsThreatened) {
		this.whitePawnsThreatened = whitePawnsThreatened;
	}

	public int getBlackPawnsThreatened() {
		return blackPawnsThreatened;
	}

	public void setBlackPawnsThreatened(int blackPawnsThreatened) {
		this.blackPawnsThreatened = blackPawnsThreatened;
	}

	public MILTState apply(MILTAction action) {
		MILTState result = new MILTState(turn, (BitSet) king.clone(), (BitSet) whites.clone(), (BitSet) blacks.clone());

		switch (turn) {
		case WHITE -> {
			result.setTurn(Turn.BLACK);
		}
		case BLACK -> {
			result.setTurn(Turn.WHITE);
		}
		}

		switch (action.pieceType()) {
		case WHITE_KING -> {
			result.getKing().clear(action.from());
			result.getKing().set(action.to());

			int row = action.to() / BOARD_SIZE;
			int col = action.to() - row * BOARD_SIZE;

			// capture left
			// aggiungi thrones cattura king
			if (col >= 2) {
				if (result.getBlacks().get(row * BOARD_SIZE + col - 1) && !camps.get(row * BOARD_SIZE + col - 1)) {
					if (result.getWhites().get(row + BOARD_SIZE + col - 2) || camps.get(row * BOARD_SIZE + col - 2)
							|| throne.get(row * BOARD_SIZE + col - 2)) {
						result.getBlacks().clear(row * BOARD_SIZE + col - 1);
					}
				}
			}

			// capture right
			if (col < BOARD_SIZE - 2) {
				if (result.getBlacks().get(row * BOARD_SIZE + col + 1) && !camps.get(row * BOARD_SIZE + col + 1)) {
					if (result.getWhites().get(row + BOARD_SIZE + col + 2) || camps.get(row * BOARD_SIZE + col + 2)
							|| throne.get(row * BOARD_SIZE + col + 2)) {
						result.getBlacks().clear(row * BOARD_SIZE + col + 1);
					}
				}

			}

			// capture up
			if (row >= 2) {
				if (result.getBlacks().get((row - 1) * BOARD_SIZE + col) && !camps.get((row - 1) * BOARD_SIZE + col)) {
					if (result.getWhites().get((row - 2) * BOARD_SIZE + col) || camps.get((row - 2) * BOARD_SIZE + col)
							|| throne.get((row - 2) * BOARD_SIZE + col)) {
						result.getBlacks().clear((row - 1) * BOARD_SIZE + col);
					}
				}

			}

			// capture down
			if (row < BOARD_SIZE - 2) {
				if (result.getBlacks().get((row + 1) * BOARD_SIZE + col) && !camps.get((row + 1) * BOARD_SIZE + col)) {
					if (result.getWhites().get((row + 2) * BOARD_SIZE + col) || camps.get((row + 2) * BOARD_SIZE + col)
							|| throne.get((row + 2) * BOARD_SIZE + col)) {
						result.getBlacks().clear((row + 1) * BOARD_SIZE + col);
					}
				}
			}

		}
		case WHITE_PAWN -> {
			result.getWhites().clear(action.from());
			result.getWhites().set(action.to());

			int row = action.to() / BOARD_SIZE;
			int col = action.to() - row * BOARD_SIZE;

			// capture left
			if (col >= 2) {
				if (result.getBlacks().get(row * BOARD_SIZE + col - 1) && !camps.get(row * BOARD_SIZE + col - 1)) {
					if (result.getWhites().get(row + BOARD_SIZE + col - 2)
							|| result.getKing().get(row + BOARD_SIZE + col - 2) || camps.get(row * BOARD_SIZE + col - 2)
							|| throne.get(row * BOARD_SIZE + col - 2)) {
						result.getBlacks().clear(row * BOARD_SIZE + col - 1);
					}
				}
			}

			// capture right
			if (col < BOARD_SIZE - 2) {
				if (result.getBlacks().get(row * BOARD_SIZE + col + 1) && !camps.get(row * BOARD_SIZE + col + 1)) {
					if (result.getWhites().get(row + BOARD_SIZE + col + 2)
							|| result.getKing().get(row + BOARD_SIZE + col + 2) || camps.get(row * BOARD_SIZE + col + 2)
							|| throne.get(row * BOARD_SIZE + col + 2)) {
						result.getBlacks().clear(row * BOARD_SIZE + col + 1);
					}
				}

			}

			// capture up
			if (row >= 2) {
				if (result.getBlacks().get((row - 1) * BOARD_SIZE + col) && !camps.get((row - 1) * BOARD_SIZE + col)) {
					if (result.getWhites().get((row - 2) * BOARD_SIZE + col)
							|| result.getKing().get((row - 2) * BOARD_SIZE + col)
							|| camps.get((row - 2) * BOARD_SIZE + col) || throne.get((row - 2) * BOARD_SIZE + col)) {
						result.getBlacks().clear((row - 1) * BOARD_SIZE + col);
					}
				}

			}

			// capture down
			if (row < BOARD_SIZE - 2) {
				if (result.getBlacks().get((row + 1) * BOARD_SIZE + col) && !camps.get((row + 1) * BOARD_SIZE + col)) {
					if (result.getWhites().get((row + 2) * BOARD_SIZE + col)
							|| result.getKing().get((row + 2) * BOARD_SIZE + col)
							|| camps.get((row + 2) * BOARD_SIZE + col) || throne.get((row + 2) * BOARD_SIZE + col)) {
						result.getBlacks().clear((row + 1) * BOARD_SIZE + col);
					}
				}
			}

		}
		case BLACK_PAWN -> {
			result.getBlacks().clear(action.from());
			result.getBlacks().set(action.to());

			int row = action.to() / BOARD_SIZE;
			int col = action.to() - row * BOARD_SIZE;

			// capture pawns
			// capture left
			if (col >= 2) {
				if (result.getWhites().get(row * BOARD_SIZE + col - 1)) {
					if (result.getBlacks().get(row + BOARD_SIZE + col - 2) || camps.get(row * BOARD_SIZE + col - 2)
							|| throne.get(row * BOARD_SIZE + col - 2)) {
						result.getWhites().clear(row * BOARD_SIZE + col - 1);
					}
				}
			}

			// capture right
			if (col < BOARD_SIZE - 2) {
				if (result.getWhites().get(row * BOARD_SIZE + col + 1)) {
					if (result.getBlacks().get(row + BOARD_SIZE + col + 2) || camps.get(row * BOARD_SIZE + col + 2)
							|| camps.get(row * BOARD_SIZE + col + 2)) {
						result.getWhites().clear(row * BOARD_SIZE + col + 1);
					}
				}

			}

			// capture up
			if (row >= 2) {
				if (result.getWhites().get((row - 1) * BOARD_SIZE + col)) {
					if (result.getBlacks().get((row - 2) * BOARD_SIZE + col) || camps.get((row - 2) * BOARD_SIZE + col)
							|| throne.get((row - 2) * BOARD_SIZE + col)) {
						result.getWhites().clear((row - 1) * BOARD_SIZE + col);
					}
				}

			}

			// capture down
			if (row < BOARD_SIZE - 2) {
				if (result.getWhites().get((row + 1) * BOARD_SIZE + col)) {
					if (result.getBlacks().get((row + 2) * BOARD_SIZE + col) || camps.get((row + 2) * BOARD_SIZE + col)
							|| throne.get((row + 2) * BOARD_SIZE + col)) {
						result.getWhites().clear((row + 1) * BOARD_SIZE + col);
					}
				}
			}

			// capture king
			if (king.intersects(throne)) {
				if (aroundThrone.get(action.to())) {
					BitSet aroundThroneAndBlacks = (BitSet) throne.clone();
					aroundThroneAndBlacks.and(result.getBlacks());
					if (aroundThroneAndBlacks.cardinality() == 4) {
						result.getKing().clear();
					}

				}

			} else if (king.intersects(aroundThrone)) {
				if (aroundThroneSurroundedLeft.get(action.to())) {
					BitSet surroundedThroneAndBlack = (BitSet) aroundThroneSurroundedLeft.clone();
					surroundedThroneAndBlack.and(result.getBlacks());
					if (surroundedThroneAndBlack.cardinality() == 3) {
						result.getKing().clear();
					}
				}
				if (aroundThroneSurroundedRight.get(action.to())) {
					BitSet surroundedThroneAndBlack = (BitSet) aroundThroneSurroundedRight.clone();
					surroundedThroneAndBlack.and(result.getBlacks());
					if (surroundedThroneAndBlack.cardinality() == 3) {
						result.getKing().clear();
					}
				}
				if (aroundThroneSurroundedUp.get(action.to())) {
					BitSet surroundedThroneAndBlack = (BitSet) aroundThroneSurroundedUp.clone();
					surroundedThroneAndBlack.and(result.getBlacks());
					if (surroundedThroneAndBlack.cardinality() == 3) {
						result.getKing().clear();
					}
				}
				if (aroundThroneSurroundedDown.get(action.to())) {
					BitSet surroundedThroneAndBlack = (BitSet) aroundThroneSurroundedDown.clone();
					surroundedThroneAndBlack.and(result.getBlacks());
					if (surroundedThroneAndBlack.cardinality() == 3) {
						result.getKing().clear();
					}
				}

			} else {
				// capture left
				if (col >= 2) {
					if (result.getKing().get(row * BOARD_SIZE + col - 1)) {
						if (result.getBlacks().get(row + BOARD_SIZE + col - 2)
								|| camps.get(row * BOARD_SIZE + col - 2)) {
							result.getKing().clear();
						}
					}
				}

				// capture right
				if (col < BOARD_SIZE - 2) {
					if (result.getKing().get(row * BOARD_SIZE + col + 1)) {
						if (result.getBlacks().get(row + BOARD_SIZE + col + 2)
								|| camps.get(row * BOARD_SIZE + col + 2)) {
							result.getKing().clear();
						}
					}

				}

				// capture up
				if (row >= 2) {
					if (result.getKing().get((row - 1) * BOARD_SIZE + col)) {
						if (result.getBlacks().get((row - 2) * BOARD_SIZE + col)
								|| camps.get((row - 2) * BOARD_SIZE + col)) {
							result.getKing().clear();
						}
					}

				}

				// capture down
				if (row < BOARD_SIZE - 2) {
					if (result.getKing().get((row + 1) * BOARD_SIZE + col)) {
						if (result.getBlacks().get((row + 2) * BOARD_SIZE + col)
								|| camps.get((row + 2) * BOARD_SIZE + col)) {
							result.getKing().clear();
						}
					}
				}
			}

		}
		}
		return result;
	}

	public List<MILTAction> getAvailableActions() {
		return this.availableActions;
	}

	public boolean isTerminal() {
		return escapes.intersects(king) || king.isEmpty();
	}

	public int evaluation() {
		return switch (this.turn) {
		case WHITE -> new MILTWhiteHeuristics(this).evaluation();
		case BLACK -> -new MILTBlackHeuristics(this).evaluation();
		};
	}

	public boolean isKingThreatened() {
		return kingThreatened;
	}

	public boolean hasKingEscapes() {

		int i = -1;
		int row = -1;
		int col = -1;
		int newPos = -1;

		BitSet whiteInvalid = new BitSet(BOARD_SIZE * BOARD_SIZE);
		whiteInvalid.or(throne);
		whiteInvalid.or(king);
		whiteInvalid.or(whites);
		whiteInvalid.or(camps);
		whiteInvalid.or(blacks);

		// king
		i = this.king.nextSetBit(0);
		if (i >= 0) {
			row = i / BOARD_SIZE;
			col = i - row * BOARD_SIZE;

			// moving right
			for (int j = 1; j < BOARD_SIZE - col; j++) {
				newPos = i + j;
				if (whiteInvalid.get(newPos)) {
					break;
				}
				if (escapes.get(newPos)) {
					return true;
				}
			}

			// moving left
			for (int j = 1; j <= col; j++) {
				newPos = i - j;
				if (whiteInvalid.get(newPos)) {
					break;
				}
				if (escapes.get(newPos)) {
					return true;
				}
			}

			// moving down
			for (int j = 1; j < BOARD_SIZE - row; j++) {
				newPos = (row + j) * BOARD_SIZE + col;
				if (whiteInvalid.get(newPos)) {
					break;
				}
				if (escapes.get(newPos)) {
					return true;
				}
			}

			// moving up
			for (int j = 1; j <= row; j++) {
				newPos = (row - j) * BOARD_SIZE + col;
				if (whiteInvalid.get(newPos)) {
					break;
				}
				if (escapes.get(newPos)) {
					return true;
				}
			}

		}
		return false;
	}

	public int getKingMovements(MILTState state) {
		int count = 4;

		BitSet invalid = new BitSet(BOARD_SIZE * BOARD_SIZE);
		invalid.or(camps);
		invalid.or(throne);
		invalid.or(king);
		invalid.or(whites);
		invalid.or(blacks);

		int i = this.king.nextSetBit(0);
		int row = this.king.nextSetBit(0) / BOARD_SIZE;
		int col = this.king.nextSetBit(0) - row * BOARD_SIZE;

		// moving right
		if (col < BOARD_SIZE - 1) {
			if (invalid.get(i + 1)) {
				count--;
			}
		}
		// moving left
		if (col > 0) {
			if (invalid.get(i - 1)) {
				count--;
			}
		}
		// moving down
		if (row < BOARD_SIZE - 1) {
			if (invalid.get((row + 1) * BOARD_SIZE + col)) {
				count--;
			}
		}
		// moving up
		if (row > 0) {
			if (invalid.get((row - 1) * BOARD_SIZE + col)) {
				count--;
			}
		}

		return count;
	}

}
