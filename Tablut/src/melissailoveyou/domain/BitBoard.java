package melissailoveyou.domain;

import java.util.BitSet;

public class BitBoard {
	
	private BitSet board;
	private int boardSize;

	public BitBoard(int boardSize) {
		super();
		this.boardSize=boardSize;
		this.board = new BitSet(boardSize);
	}
	
	public BitBoard() {
		this(9);
	}

	public BitSet getBoard() {
		return board;
	}
	
	public int getBoardSize() {
		return boardSize;
	}
	
	

}
