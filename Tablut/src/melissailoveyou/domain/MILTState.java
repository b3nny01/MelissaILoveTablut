package melissailoveyou.domain;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

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
		result.set(7, 9);
		result.set(9);
		result.set(17, 19);
		result.set(26);
		result.set(54);
		result.set(62, 64);
		result.set(73, 75);
		result.set(78, 80);
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

	private static BitSet escapes = initEscapes();
	private static BitSet camps = initCamps();
	private static BitSet throne = initThrone();
	private static BitSet aroundThrone = initAroundThrone();
	private static BitSet aroundThroneSurroundedLeft = initAroundThroneSurroundedLeft();
	private static BitSet aroundThroneSurroundedRight = initAroundThroneSurroundedRight();
	private static BitSet aroundThroneSurroundedUp = initAroundThroneSurroundedUp();
	private static BitSet aroundThroneSurroundedDown = initAroundThroneSurroundedDown();

	private Turn turn;
	private BitSet king;
	private BitSet whites;
	private BitSet blacks;

	public MILTState(Turn turn, BitSet king, BitSet whites, BitSet blacks) {
		super();
		this.turn = turn;
		this.king = king;
		this.whites = whites;
		this.blacks = blacks;
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

	public MILTState apply(Action action) {
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
			int col = action.to() - row;

			// capture left
			if (col >= 2) {
				if (result.getBlacks().get(row * BOARD_SIZE + col - 1)) {
					if (result.getWhites().get(row + BOARD_SIZE + col - 2) || camps.get(row * BOARD_SIZE + col - 2)) {
						result.getBlacks().clear(row * BOARD_SIZE + col - 1);
					}
				}
			}

			// capture right
			if (col < BOARD_SIZE - 2) {
				if (result.getBlacks().get(row * BOARD_SIZE + col + 1)) {
					if (result.getWhites().get(row + BOARD_SIZE + col + 2) || camps.get(row * BOARD_SIZE + col + 2)) {
						result.getBlacks().clear(row * BOARD_SIZE + col + 1);
					}
				}

			}

			// capture up
			if (row >= 2) {
				if (result.getBlacks().get((row - 1) * BOARD_SIZE + col)) {
					if (result.getWhites().get((row - 2) * BOARD_SIZE + col)
							|| camps.get((row - 2) * BOARD_SIZE + col)) {
						result.getBlacks().clear((row - 1) * BOARD_SIZE + col);
					}
				}

			}

			// capture down
			if (row < BOARD_SIZE - 2) {
				if (result.getBlacks().get((row + 1) * BOARD_SIZE + col)) {
					if (result.getWhites().get((row + 2) * BOARD_SIZE + col)
							|| camps.get((row + 2) * BOARD_SIZE + col)) {
						result.getBlacks().clear((row + 1) * BOARD_SIZE + col);
					}
				}
			}

		}
		case WHITE_PAWN -> {
			result.getWhites().clear(action.from());
			result.getWhites().set(action.to());

			int row = action.to() / BOARD_SIZE;
			int col = action.to() - row;

			// capture left
			if (col >= 2) {
				if (result.getBlacks().get(row * BOARD_SIZE + col - 1)) {
					if (result.getWhites().get(row + BOARD_SIZE + col - 2)
							|| result.getKing().get(row + BOARD_SIZE + col - 2) || camps.get(row * BOARD_SIZE + col - 2)
							|| throne.get(row * BOARD_SIZE + col - 2)) {
						result.getBlacks().clear(row * BOARD_SIZE + col - 1);
					}
				}
			}

			// capture right
			if (col < BOARD_SIZE - 2) {
				if (result.getBlacks().get(row * BOARD_SIZE + col + 1)) {
					if (result.getWhites().get(row + BOARD_SIZE + col + 2)
							|| result.getKing().get(row + BOARD_SIZE + col + 2) || camps.get(row * BOARD_SIZE + col + 2)
							|| throne.get(row * BOARD_SIZE + col + 2)) {
						result.getBlacks().clear(row * BOARD_SIZE + col + 1);
					}
				}

			}

			// capture up
			if (row >= 2) {
				if (result.getBlacks().get((row - 1) * BOARD_SIZE + col)) {
					if (result.getWhites().get((row - 2) * BOARD_SIZE + col)
							|| result.getKing().get((row - 2) * BOARD_SIZE + col)
							|| camps.get((row - 2) * BOARD_SIZE + col) || throne.get((row - 2) * BOARD_SIZE + col)) {
						result.getBlacks().clear((row - 1) * BOARD_SIZE + col);
					}
				}

			}

			// capture down
			if (row < BOARD_SIZE - 2) {
				if (result.getBlacks().get((row + 1) * BOARD_SIZE + col)) {
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
			int col = action.to() - row;

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

	public List<Action> getAvailableActions() {
		List<Action> actions = new ArrayList<>();
		int i = -1;
		int row = -1;
		int col = -1;

		BitSet invalid = new BitSet(BOARD_SIZE * BOARD_SIZE);
		invalid.or(camps);
		invalid.or(throne);
		invalid.or(king);
		invalid.or(whites);
		invalid.or(blacks);

		if (turn == Turn.WHITE) {
			// white pawns
			for (i = whites.nextSetBit(0); i >= 0; i = whites.nextSetBit(i + 1)) {
				if (i == Integer.MAX_VALUE) {
					break;
				}
				row = i / BOARD_SIZE;
				col = i - row * BOARD_SIZE;

				// moving right
				for (int j = col; j < BOARD_SIZE; j++) {
					if (invalid.get(i + j)) {
						break;
					}
					actions.add(new Action(PieceType.WHITE_PAWN, i, i + j));
				}

				// moving left
				for (int j = col; j >= 0; j--) {
					if (invalid.get(i - j)) {
						break;
					}
					actions.add(new Action(PieceType.WHITE_PAWN, i, i - j));
				}

				// moving down
				for (int j = row; j < BOARD_SIZE; j++) {
					if (invalid.get(j * BOARD_SIZE + col)) {
						break;
					}
					actions.add(new Action(PieceType.WHITE_PAWN, i, j * BOARD_SIZE + col));
				}

				// moving up
				for (int j = row; j >= 0; j--) {
					if (invalid.get(j * BOARD_SIZE + col)) {
						break;
					}
					actions.add(new Action(PieceType.WHITE_PAWN, i, j * BOARD_SIZE + col));
				}
			}

			// king
			i = this.king.nextSetBit(0);
			if (i >= 0) {
				row = i / BOARD_SIZE;
				col = i - row * BOARD_SIZE;

				// moving right
				for (int j = col; j < BOARD_SIZE; j++) {
					if (invalid.get(i + j)) {
						break;
					}
					actions.add(new Action(PieceType.WHITE_KING, i, i + j));
				}

				// moving left
				for (int j = col; j >= 0; j--) {
					if (invalid.get(i - j)) {
						break;
					}
					actions.add(new Action(PieceType.WHITE_KING, i, i - j));
				}

				// moving down
				for (int j = row; j < BOARD_SIZE; j++) {
					if (invalid.get(j * BOARD_SIZE + col)) {
						break;
					}
					actions.add(new Action(PieceType.WHITE_KING, i, j * BOARD_SIZE + col));
				}

				// moving up
				for (int j = row; j >= 0; j--) {
					if (invalid.get(j * BOARD_SIZE + col)) {
						break;
					}
					actions.add(new Action(PieceType.WHITE_KING, i, j * BOARD_SIZE + col));
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

				// moving right
				for (int j = col; j < BOARD_SIZE; j++) {
					if (invalid.get(i + j)) {
						break;
					}
					actions.add(new Action(PieceType.BLACK_PAWN, i, i + j));
				}

				// moving left
				for (int j = col; j >= 0; j--) {
					if (invalid.get(i - j)) {
						break;
					}
					actions.add(new Action(PieceType.BLACK_PAWN, i, i - j));
				}

				// moving down
				for (int j = row; j < BOARD_SIZE; j++) {
					if (invalid.get(j * BOARD_SIZE + col)) {
						break;
					}
					actions.add(new Action(PieceType.BLACK_PAWN, i, j * BOARD_SIZE + col));
				}

				// moving up
				for (int j = row; j >= 0; j--) {
					if (invalid.get(j * BOARD_SIZE + col)) {
						break;
					}
					actions.add(new Action(PieceType.BLACK_PAWN, i, j * BOARD_SIZE + col));
				}
			}

		}
		return actions;
	}
	
	public List<MILTState> getChildren(){
		List<MILTState> result=new ArrayList<>();
		List<Action> actions=this.getAvailableActions();
		for(Action a : actions) {
			result.add(this.apply(a));
		}
		return result;
	}

}
