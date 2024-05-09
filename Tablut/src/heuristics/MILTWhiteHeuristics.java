package heuristics;

import java.util.BitSet;
import java.util.List;

import melissaILoveTablut.MILTAction;
import melissaILoveTablut.MILTState;

public class MILTWhiteHeuristics {
	
	
	private MILTState state;
	

	private final int WHITE_ALIVE = 0; 
    private final int BLACK_ALIVE = 1;
    private final int KING_SAFE = 2;
    private final int WHITE_MOVEMENT = 3;

    private final Double[] gameWeights;

    public MILTWhiteHeuristics(MILTState state) {
        this.state = state;

        gameWeights = new Double[4];

        gameWeights[WHITE_ALIVE] = 10.0;
        gameWeights[BLACK_ALIVE] = 10.0;
        gameWeights[KING_SAFE] = 10.0;
        gameWeights[WHITE_MOVEMENT] = 10.0;
    }
    
    
    public double evaluation() {
        double stateValue = 0.0;
        
        if (state.canKingBeCaptured(state))
            return Double.NEGATIVE_INFINITY;
        
        int whitePawns = state.getWhites().cardinality();
        int blackPawns = state.getBlacks().cardinality();
        
        // pesi per il numero di pedine vive di ogni colore
        double whitesAlive = whitePawns * 1;
        double blacksAlive = blackPawns * 1;

        double kingMov = evalKingMovement();
        double kingEsc = evalKingEscapes(state);
        double pawnsMov = getPawnsMovement();

        stateValue += whitesAlive * gameWeights[WHITE_ALIVE];
        stateValue += blacksAlive * gameWeights[BLACK_ALIVE];

        stateValue += (kingEsc + kingMov) * gameWeights[KING_SAFE];

        stateValue += pawnsMov * gameWeights[WHITE_MOVEMENT];

        return stateValue;
    }
    
    
    private double evalKingMovement() {
        int val = state.getKingMovements(state);
        if (val == 0)
            return 0.5;
        if (val == 1)
            return 1.0;

        return 1.2;
    }
    
    
    private int getPawnsMovement() {
        int safe = 0;
        
        //TO DO: aggiungere pesi per le righe e le colonne favorevoli
        BitSet board = state.getWhites();
        for (int i = 0; i < board.cardinality(); i++) {    
        	safe += state.canPawnBeCaptured(state, i) ? 0 : 1;
        }

        return safe;
    }
    
    
    private double evalKingEscapes(MILTState state) {
        List<MILTAction> escapes = state.getKingEscapes(state);
        int numEsc = escapes.size();
        
        if (!state.canKingBeCaptured(state)) {
        	if (numEsc > 1 && !state.canKingBeCaptured(state))
            return Double.POSITIVE_INFINITY;

	        // check if an enemy can block an escape
	        else if (numEsc == 1) {
	        	MILTAction action = escapes.get(0);
	        	int end = action.to();
	            // Up escape
	            if (end < 8) {
	                //check
	            	//return 0.0;
	                return 10.0;
	            }
	            // Down escape
	            if (end > 72) {
	                //check
	            	//return 0.0;
	                return 10.0;
	            }
	            // Left escape
	            if (end == 8 || end == 18 || end == 54 || end == 63) {
	                //check
	            	//return 0.0;
	                return 10.0;
	            }
	            // Right escape
	            if (end == 17 || end == 26 || end == 62 || end == 71) {
	                //check
	            	//return 0.0;
	                return 10.0;
	            }
        	}
        }
        
        return 0.0;
    }
}
