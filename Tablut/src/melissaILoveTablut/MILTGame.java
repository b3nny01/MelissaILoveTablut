package melissaILoveTablut;

import java.util.List;

import aima.core.search.adversarial.Game;
import melissaILoveTablut.MILTState.Turn;
import melissaILoveTablut.heuristics.MILTEvaluator;

public class MILTGame implements Game<MILTState,MILTAction,MILTState.Turn> {
	private MILTEvaluator evaluator;
	
	public MILTGame() {
		this.evaluator=new MILTEvaluator();
	}
	
	@Override
	public List<MILTAction> getActions(MILTState state) {
		return state.getAvailableActions();
	}

	@Override
	public MILTState getInitialState() {
		return MILTState.getInitialState();
	}

	@Override
	public MILTState.Turn getPlayer(MILTState state) {
		return state.getTurn();
	}

	@Override
	public MILTState.Turn[] getPlayers() {
		return MILTState.Turn.values();
	}

	@Override
	public MILTState getResult(MILTState state, MILTAction action) {
		return state.apply(action);
	}

	@Override
	public double getUtility(MILTState state, Turn turn) {
		return state.evaluation(evaluator);
	}

	@Override
	public boolean isTerminal(MILTState state) {
		return state.isTerminal();
	}

}
