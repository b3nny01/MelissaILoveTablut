package it.unibo.ai.didattica.competition.tablut.client;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;
import java.util.Random;

import it.unibo.ai.didattica.competition.tablut.domain.*;
import it.unibo.ai.didattica.competition.tablut.domain.State.Turn;
import melissailoveyou.domain.MILTState;
import it.unibo.ai.didattica.competition.tablut.domain.State.Pawn;

/**
 * 
 * @author Alessio Bennati
 *
 */
public class MelissaILoveTablutClient extends TablutClient {

	private int game;

	public MelissaILoveTablutClient(String player, String name, int gameChosen, int timeout, String ipAddress)
			throws UnknownHostException, IOException {
		super(player, name, timeout, ipAddress);
		game = gameChosen;
	}

	public MelissaILoveTablutClient(String player, String name, int timeout, String ipAddress)
			throws UnknownHostException, IOException {
		this(player, name, 4, timeout, ipAddress);
	}

	public MelissaILoveTablutClient(String player, int timeout, String ipAddress)
			throws UnknownHostException, IOException {
		this(player, "MelissaILoveTablut", 4, timeout, ipAddress);
	}

	public MelissaILoveTablutClient(String player) throws UnknownHostException, IOException {
		this(player, "MelissaILoveTablut", 4, 60, "localhost");
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

		MelissaILoveTablutClient client = new MelissaILoveTablutClient(role, name, gametype, timeout, ipAddress);
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
					MILTState miltState=new MILTState(MILTState.Turn.WHITE,king,whites,blacks);
					


					System.out.println("Mossa scelta: " + a.toString());
					try {
						this.write(a);
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
					int[] buf;
					for (int i = 0; i < state.getBoard().length; i++) {
						for (int j = 0; j < state.getBoard().length; j++) {
							if (state.getPawn(i, j).equalsPawn(State.Pawn.BLACK.toString())) {
								buf = new int[2];
								buf[0] = i;
								buf[1] = j;
								myPawns.add(buf);
							} else if (state.getPawn(i, j).equalsPawn(State.Pawn.EMPTY.toString())) {
								buf = new int[2];
								buf[0] = i;
								buf[1] = j;
								empty.add(buf);
							}
						}
					}

					int[] selected = null;

					boolean found = false;
					Action a = null;
					try {
						a = new Action("z0", "z0", State.Turn.BLACK);
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					;
					while (!found) {
						selected = myPawns.get(new Random().nextInt(myPawns.size() - 1));
						String from = this.getCurrentState().getBox(selected[0], selected[1]);

						selected = empty.get(new Random().nextInt(empty.size() - 1));
						String to = this.getCurrentState().getBox(selected[0], selected[1]);

						try {
							a = new Action(from, to, State.Turn.BLACK);
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}

						System.out.println("try: " + a.toString());
						try {
							rules.checkMove(state, a);
							found = true;
						} catch (Exception e) {

						}

					}

					System.out.println("Mossa scelta: " + a.toString());
					try {
						this.write(a);
					} catch (ClassNotFoundException | IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					myPawns.clear();
					empty.clear();

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
