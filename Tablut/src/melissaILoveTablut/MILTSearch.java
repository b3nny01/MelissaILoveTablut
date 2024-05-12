package melissaILoveTablut;

import aima.core.search.adversarial.Game;
import aima.core.search.adversarial.IterativeDeepeningAlphaBetaSearch;
import melissaILoveTablut.MILTState.Turn;

public class MILTSearch extends IterativeDeepeningAlphaBetaSearch<MILTState, MILTAction, MILTState.Turn> {

	public MILTSearch(Game<MILTState, MILTAction, Turn> game, double utilMin, double utilMax, int time) {
		super(game, utilMin, utilMax, time);
	}

	@Override
	protected double eval(MILTState state, MILTState.Turn player) {
		super.eval(state, player);
		return game.getUtility(state, player);
	}

	@Override
	public MILTAction makeDecision(MILTState state) {
		MILTAction action = super.makeDecision(state);
		System.out.println("explored nodes: " + getMetrics().get(METRICS_NODES_EXPANDED));
		System.out.println("depth reached: " + getMetrics().get(METRICS_MAX_DEPTH));
		return action;
	}

}
