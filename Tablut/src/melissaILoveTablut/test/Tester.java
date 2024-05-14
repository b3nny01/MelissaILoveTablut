package melissaILoveTablut.test;

/*
 * 
 * 
OOOBBBOOO
OOOOOOOOO
OOOOOWBOO
OOOWOOOOO
BOOBTKOOB
BOOWOBWOO
OOOOWOOOO
OOOOOOOOO
OOOOBOOOO

 * 
 * 
 * 
 * 
 * */

import java.util.BitSet;
import java.util.List;

import it.unibo.ai.didattica.competition.tablut.domain.Action;
import it.unibo.ai.didattica.competition.tablut.domain.GameAshtonTablut;
import it.unibo.ai.didattica.competition.tablut.domain.State;
import it.unibo.ai.didattica.competition.tablut.domain.StateBrandub;
import it.unibo.ai.didattica.competition.tablut.domain.State.Pawn;
import it.unibo.ai.didattica.competition.tablut.domain.State.Turn;
import melissaILoveTablut.MILTAction;
import melissaILoveTablut.MILTGame;
import melissaILoveTablut.MILTSearch;
import melissaILoveTablut.MILTState;
import melissaILoveTablut.MILTState.PieceType;
import melissaILoveTablut.heuristics.MILTBlackEvaluator;
import melissaILoveTablut.heuristics.MILTWhiteEvaluator;
import it.unibo.ai.didattica.competition.tablut.domain.StateTablut;

public class Tester {

	/*
OOOOOOOOO
OOWOOOOOO
OOOOBOOOO
OOOBBOOOO
BBOOTKBOB
BOWWWWOOO
OOOWWBOOO
OOOOBOOOO
OOOBBOOOO

	 * */
	
	private static List<String> lines=List.of(
			"OOOOOOOOO",
			"OOWOOOOOO",
			"OOOOBOOOO",
			"OOOBBOOOO",
			"BBOOTKBOB",
			"BOWWWWOOO",
			"OOOWWBOOO",
			"OOOOBOOOO",
			"OOOBBOOOO");
	
	private static State getStateFromLines(List<String> lines, State.Turn turn) throws IllegalStateException {
		State state = new StateTablut();
		State.Pawn[][] board = state.getBoard();

		if (lines.size() != 9)
			throw new IllegalArgumentException(String.format("Wrong number of lines: was %d, expected 9", lines.size()));

		for (int i = 0; i < lines.size(); i++) {
			String line = lines.get(i);
			if (line.length() != 9)
				throw new IllegalArgumentException(String.format("Wrong line length at line %d: was %d, expected 9", i, line.length()));
			for (int j = 0; j < line.length(); j++) {
				String pawnChar = "" + line.charAt(j);

				State.Pawn pawn = State.Pawn.fromString(pawnChar);
				board[i][j] = pawn;
			}
		}

		state.setTurn(turn);
		state.setBoard(board);

		return state;
	}
	
	public static void main(String[] args) {
		try {
			State state = getStateFromLines(lines,Turn.BLACK);
			GameAshtonTablut game = new GameAshtonTablut(state, 0, 0, "logs", "testerW", "testerB");
			Action action;
			//action = new Action("e4", "f4", Turn.BLACK);
			//state = game.checkMove(state, action);

			System.out.println(state.boardString());
			BitSet whites = new BitSet(MILTState.BOARD_SIZE * MILTState.BOARD_SIZE);
			BitSet blacks = new BitSet(MILTState.BOARD_SIZE * MILTState.BOARD_SIZE);
			BitSet king = new BitSet(MILTState.BOARD_SIZE * MILTState.BOARD_SIZE);

			king.clear();
			whites.clear();
			blacks.clear();
			Pawn p;
			for (int i = 0; i < MILTState.BOARD_SIZE; i++) {
				for (int j = 0; j < MILTState.BOARD_SIZE; j++) {
					p = state.getPawn(i, j);
					switch (p) {
					case KING -> {
						king.set(i * MILTState.BOARD_SIZE + j);
					}

					case WHITE -> {
						whites.set(i * MILTState.BOARD_SIZE + j);

					}

					case BLACK -> {
						blacks.set(i * MILTState.BOARD_SIZE + j);
					}
					default -> {
					}
					}
				}
			}
			System.out.println("starting test");
			MILTBlackEvaluator evaluator=new MILTBlackEvaluator();
			MILTState miltState = new MILTState(MILTState.Turn.BLACK, whites, blacks, king);
			System.out.println("whites under attack: " + miltState.getWhitePawnsThreatened());
			System.out.println("blacks under attack: " + miltState.getBlackPawnsThreatened());
			System.out.println("king threatened: " + miltState.isKingThreatened());
			System.out.println("terminal: " + miltState.isTerminal());
			MILTGame miltGame=new MILTGame();
			MILTSearch miltSearch=new MILTSearch(miltGame,1);
			MILTAction bestAction=miltSearch.makeDecision(miltState);
			miltState=miltState.apply(new MILTAction(PieceType.BLACK_PAWN,31,32));
			System.out.println("evaluation: "+miltState.evaluation(evaluator));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}