package it.unibo.ai.didattica.competition.tablut.client;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import it.unibo.ai.didattica.competition.tablut.domain.State.Turn;
import melissaILoveTablut.*;
import it.unibo.ai.didattica.competition.tablut.domain.StateTablut;
import it.unibo.ai.didattica.competition.tablut.domain.Game;
import it.unibo.ai.didattica.competition.tablut.domain.GameAshtonTablut;
import it.unibo.ai.didattica.competition.tablut.domain.GameModernTablut;
import it.unibo.ai.didattica.competition.tablut.domain.GameTablut;
import it.unibo.ai.didattica.competition.tablut.domain.State;
import it.unibo.ai.didattica.competition.tablut.domain.StateBrandub;
import it.unibo.ai.didattica.competition.tablut.domain.State.Pawn;

/**
 * 
 * @author Alessio Bennati
 *
 */
public class MILTClient extends TablutClient {

	private int game;

	public MILTClient(String player, String name, int gameChosen, int timeout, String ipAddress)
			throws UnknownHostException, IOException {
		super(player, name, timeout, ipAddress);
		game = gameChosen;
	}

	public MILTClient(String player, String name, int timeout, String ipAddress)
			throws UnknownHostException, IOException {
		this(player, name, 4, timeout, ipAddress);
	}

	public MILTClient(String player, int timeout, String ipAddress) throws UnknownHostException, IOException {
		this(player, "MelissaILoveTablut", 4, timeout, ipAddress);
	}

	public MILTClient(String player) throws UnknownHostException, IOException {
		this(player, "MelissaILoveTablut", 4, 60, "localhost");
	}

	private Entry<Integer, MILTAction> minMax(MILTState state, MILTAction actionDone, int depth, boolean maximizer) {

		if (depth == 0) {
			return Map.entry(state.evaluation(), actionDone);
		}
		int bestVal = maximizer ? Integer.MIN_VALUE : Integer.MAX_VALUE;
		MILTAction bestAction = null;
		for (MILTAction action : state.getAvailableActions()) {
			MILTState newState = state.apply(action);
			int newStateVal = minMax(newState, action, depth - 1, !maximizer).getKey();
			if (maximizer) {
				if (newStateVal >= bestVal) {
					bestVal = newStateVal;
					bestAction = action;
				}
			} else {
				if (newStateVal <= bestVal) {
					bestVal = newStateVal;
					bestAction = action;
				}

			}

		}
		for(int i=0;i<3-depth;i++) {
			System.out.print("\t");
		}
		System.out.println("evaluated "+bestAction+" value: "+bestVal);
		return Map.entry(bestVal, bestAction);
	}
	
	private Entry<Integer, List<MILTAction>> minMaxList(MILTState state, MILTAction actionDone, int depth, boolean maximizer) {

		List<MILTAction> resultList=new ArrayList<>();
		if (depth == 0 || state.isTerminal()) {
			return Map.entry(state.evaluation(), new ArrayList<MILTAction>());
		}
		int bestVal = maximizer ? Integer.MIN_VALUE : Integer.MAX_VALUE;
		MILTAction bestAction = null;
		List<MILTAction> tail=new ArrayList<>();
		for (MILTAction action : state.getAvailableActions()) {
			MILTState newState = state.apply(action);
			Entry<Integer, List<MILTAction>> result= minMaxList(newState, action, depth - 1, !maximizer);
			
			
			if (maximizer) {
				if (result.getKey() > bestVal || (result.getKey() == bestVal && (result.getValue().size()<tail.size() || resultList.size()==0)))  {
					bestVal = result.getKey();
					bestAction = action;
					tail = result.getValue();
				}
			} else {
				if (result.getKey() < bestVal || (result.getKey() == bestVal && (result.getValue().size()<tail.size() || resultList.size()==0))) {
					bestVal = result.getKey();
					bestAction = action;
					tail = result.getValue();
				}

			}

		}
		resultList=tail;
		resultList.add(0,bestAction);
		return Map.entry(bestVal, resultList);
	}

	public static void main(String[] args) throws UnknownHostException, IOException, ClassNotFoundException {
		int gametype = 4;
		String role = "";
		String name = "MelissaILoveTablut";
		String ipAddress = "localhost";
		int timeout = 60;
		// TODO: change the behavior?
		if (args.length < 1) {
			System.out.println("You must specify which player you are (WHITE or BLACK)");
			System.exit(-1);
		} else {
			System.out.println(args[0]);
			role = (args[0]);
		}
		if (args.length == 2) {
			System.out.println(args[1]);
			timeout = Integer.parseInt(args[1]);
		}
		if (args.length == 3) {
			ipAddress = args[2];
		}
		System.out.println("Selected client: " + args[0]);

		MILTClient client = new MILTClient(role, name, gametype, timeout, ipAddress);
		client.run();
	}

	@Override
	public void run() {

		try {
			this.declareName();
		} catch (Exception e) {
			e.printStackTrace();
		}

		State state;

		Game rules = null;
		switch (this.game) {
		case 1:
			state = new StateTablut();
			rules = new GameTablut();
			break;
		case 2:
			state = new StateTablut();
			rules = new GameModernTablut();
			break;
		case 3:
			state = new StateBrandub();
			rules = new GameTablut();
			break;
		case 4:
			state = new StateTablut();
			state.setTurn(State.Turn.WHITE);
			rules = new GameAshtonTablut(99, 0, "garbage", "fake", "fake");
			System.out.println("Ashton Tablut game");
			break;
		default:
			System.out.println("Error in game selection");
			System.exit(4);
		}

		System.out.println("You are player " + this.getPlayer().toString() + "!");

		BitSet whites = new BitSet(MILTState.BOARD_SIZE * MILTState.BOARD_SIZE);
		BitSet blacks = new BitSet(MILTState.BOARD_SIZE * MILTState.BOARD_SIZE);
		BitSet king = new BitSet(MILTState.BOARD_SIZE * MILTState.BOARD_SIZE);

		while (true) {
			try {
				this.read();
			} catch (ClassNotFoundException | IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				System.exit(1);
			}
			System.out.println("Current state:");
			state = this.getCurrentState();
			System.out.println(state.toString());
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
			}

			king.clear();
			whites.clear();
			blacks.clear();

			if (this.getPlayer().equals(Turn.WHITE)) {
				// Mio turno
				if (this.getCurrentState().getTurn().equals(StateTablut.Turn.WHITE)) {
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
					MILTState miltState = new MILTState(MILTState.Turn.WHITE, king, whites, blacks);
					Entry<Integer,List<MILTAction>> result=minMaxList(miltState,null,4,true);
					MILTAction choosen = result.getValue().get(0);
					int evaluation = result.getKey();

					System.out.println("Mossa scelta: " + choosen.toString());
					System.out.println("Valuate "+result.getValue());
					try {
						this.write(choosen.toAction());
					} catch (ClassNotFoundException | IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
				// Turno dell'avversario
				else if (state.getTurn().equals(StateTablut.Turn.BLACK)) {
					System.out.println("Waiting for your opponent move... ");
				}
				// ho vinto
				else if (state.getTurn().equals(StateTablut.Turn.WHITEWIN)) {
					System.out.println("YOU WIN!");
					System.exit(0);
				}
				// ho perso
				else if (state.getTurn().equals(StateTablut.Turn.BLACKWIN)) {
					System.out.println("YOU LOSE!");
					System.exit(0);
				}
				// pareggio
				else if (state.getTurn().equals(StateTablut.Turn.DRAW)) {
					System.out.println("DRAW!");
					System.exit(0);
				}

			} else {

				// Mio turno
				if (this.getCurrentState().getTurn().equals(StateTablut.Turn.BLACK)) {
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
					MILTState miltState = new MILTState(MILTState.Turn.BLACK, king, whites, blacks);
					Entry<Integer,List<MILTAction>> result=minMaxList(miltState,null,4,false);
					MILTAction choosen = result.getValue().get(0);
					int evaluation = result.getKey();

					System.out.println("Mossa scelta: " + choosen.toString());
					System.out.println("Valuate "+result.getValue());
					try {
						this.write(choosen.toAction());
					} catch (ClassNotFoundException | IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}

				else if (state.getTurn().equals(StateTablut.Turn.WHITE)) {
					System.out.println("Waiting for your opponent move... ");
				} else if (state.getTurn().equals(StateTablut.Turn.WHITEWIN)) {
					System.out.println("YOU LOSE!");
					System.exit(0);
				} else if (state.getTurn().equals(StateTablut.Turn.BLACKWIN)) {
					System.out.println("YOU WIN!");
					System.exit(0);
				} else if (state.getTurn().equals(StateTablut.Turn.DRAW)) {
					System.out.println("DRAW!");
					System.exit(0);
				}

			}
		}

	}
}
