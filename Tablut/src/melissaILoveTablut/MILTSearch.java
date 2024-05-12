package melissaILoveTablut;

import aima.core.search.adversarial.Game;
import aima.core.search.adversarial.IterativeDeepeningAlphaBetaSearch;
import melissaILoveTablut.MILTState.Turn;
import melissaILoveTablut.heuristics.MILTEvaluator;

public class MILTSearch extends IterativeDeepeningAlphaBetaSearch<MILTState, MILTAction, MILTState.Turn> {
	
	public MILTSearch(Game<MILTState, MILTAction, Turn> game,int time) {
		super(game, Integer.MIN_VALUE, Integer.MAX_VALUE, time);

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
		System.out.println("choosen: "+action);
		System.out.println("evaluated: "+this.eval(state, state.getTurn()));
		return action;
	}

}
