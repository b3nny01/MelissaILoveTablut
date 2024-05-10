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
    private final int WHITE_SAFE = 3;
    private final int WHITE_MOVEMENT = 4;

    private final Double[] gameWeights;

    public MILTWhiteHeuristics(MILTState state) {
        this.state = state;

        gameWeights = new Double[5];

        gameWeights[WHITE_ALIVE] = 10.0;
        gameWeights[BLACK_ALIVE] = 10.0;
        gameWeights[KING_SAFE] = 10.0;
        gameWeights[WHITE_SAFE] = 10.0;
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
        double pawnsSafety = evalPawnsSafety();
        double pawnsMov = evalPawnsMovement();

        stateValue += whitesAlive * gameWeights[WHITE_ALIVE];
        stateValue -= blacksAlive * gameWeights[BLACK_ALIVE];

        stateValue += (kingEsc + kingMov) * gameWeights[KING_SAFE];
        stateValue += pawnsSafety * gameWeights[WHITE_SAFE];

        stateValue += (pawnsMov / whitePawns) * gameWeights[WHITE_MOVEMENT];

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
    
    
    private int evalPawnsSafety() {
        int safe = 0;
        int j = 0;
        
        //TO DO: aggiungere pesi per le righe e le colonne favorevoli
        BitSet board = state.getWhites();
        for (int i = 0; i < board.cardinality(); i++) {    
        	safe += state.canPawnBeCaptured(board.nextSetBit(j)) ? 0 : 1;
        	j = board.nextSetBit(j);
        }

        return safe;
    }
    
    
    private int evalPawnsMovement() {
    	int mov = 0;
    	int j = 0;
        
        //TO DO: aggiungere pesi per le righe e le colonne favorevoli
        BitSet board = state.getWhites();
        for (int i = 0; i < board.cardinality(); i++) {    
        	mov += state.getPawnsMovements(board.nextSetBit(j));
        	j = board.nextSetBit(j);
        }
    	
    	return mov;
    }
    
    
    private double evalKingEscapes(MILTState state) {
        List<MILTAction> escapes = state.getKingEscapes(state);
        int numEsc = escapes.size();
        
        if (!state.canKingBeCaptured(state)) {
        	if (numEsc > 1)
            return Double.POSITIVE_INFINITY;

	        // check if an enemy can block an escape
	        else if (numEsc == 1) {
	        	return 10.0;
        	}
        }
        return 0.0;
    }
}
