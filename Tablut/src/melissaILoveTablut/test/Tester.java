//package melissaILoveTablut.test;
//
//import java.io.IOException;
//import java.util.BitSet;
//
//import it.unibo.ai.didattica.competition.tablut.domain.Action;
//import it.unibo.ai.didattica.competition.tablut.domain.GameAshtonTablut;
//import it.unibo.ai.didattica.competition.tablut.domain.State;
//import it.unibo.ai.didattica.competition.tablut.domain.State.Pawn;
//import it.unibo.ai.didattica.competition.tablut.domain.State.Turn;
//import melissaILoveTablut.MILTState;
//import it.unibo.ai.didattica.competition.tablut.domain.StateTablut;
//
//public class Tester {
//
//	public static void main(String[] args) {
//		try {
//			State state = new StateTablut();
//			GameAshtonTablut game = new GameAshtonTablut(state, 0, 0, "logs", "testerW", "testerB");
//			Action action;	
//			state.setTurn(Turn.WHITE);
//		
//			action = new Action("c5", "c4", Turn.WHITE);
//			state = game.checkMove(state, action);
//			
//			action = new Action("b5", "c5", Turn.BLACK);
//			state = game.checkMove(state, action);
//			
//			action = new Action("e5", "d5", Turn.WHITE);
//			state = game.checkMove(state, action);
//			
//			action = new Action("d1", "d4", Turn.BLACK);
//			state = game.checkMove(state, action);
//			
//			action = new Action("d5", "c5", Turn.WHITE);
//			state = game.checkMove(state, action);
//			
//			action = new Action("i4", "f4", Turn.BLACK);
//			state = game.checkMove(state, action);
//			
//			
//
//			System.out.println(state.boardString());
//			BitSet whites = new BitSet(MILTState.BOARD_SIZE * MILTState.BOARD_SIZE);
//			BitSet blacks = new BitSet(MILTState.BOARD_SIZE * MILTState.BOARD_SIZE);
//			BitSet king = new BitSet(MILTState.BOARD_SIZE * MILTState.BOARD_SIZE);
//
//			king.clear();
//			whites.clear();
//			blacks.clear();
//			Pawn p;
//			for (int i = 0; i < MILTState.BOARD_SIZE; i++) {
//				for (int j = 0; j < MILTState.BOARD_SIZE; j++) {
//					p = state.getPawn(i, j);
//					switch (p) {
//					case KING -> {
//						king.set(i * MILTState.BOARD_SIZE + j);
//					}
//
//					case WHITE -> {
//						whites.set(i * MILTState.BOARD_SIZE + j);
//
//					}
//
//					case BLACK -> {
//						blacks.set(i * MILTState.BOARD_SIZE + j);
//					}
//					default -> {
//					}
//					}
//				}
//			}
//			System.out.println("starting test");
//			int kingPos=king.nextSetBit(0);
//			MILTState miltState = new MILTState(MILTState.Turn.BLACK, whites, blacks, king);
//			System.out.println("whites under attack: " + miltState.getWhitePawnsThreatened());
//			System.out.println("blacks under attack: " + miltState.getBlackPawnsThreatened());
//			System.out.println("king threatened: " + miltState.isKingThreatened());
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
//
//}
