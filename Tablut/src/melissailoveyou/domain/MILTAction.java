package melissailoveyou.domain;

import java.io.IOException;

public record MILTAction(MILTState.PieceType pieceType,int from, int to) {
    public it.unibo.ai.didattica.competition.tablut.domain.Action toAction() throws IOException{
    	
    	System.out.println("linearized: "+from+" "+to);
    	
        int row=from/MILTState.BOARD_SIZE +1;
        int col=from-(row-1)*MILTState.BOARD_SIZE;

        String fromStr= Character.toString((char)('a'+col)) + row;

        row=to/MILTState.BOARD_SIZE +1;
        col=to-(row-1)*MILTState.BOARD_SIZE;

        String toStr=Character.toString((char)('a'+col))+row;
        
        System.out.println("from "+ fromStr+" to "+toStr);

        it.unibo.ai.didattica.competition.tablut.domain.StateTablut.Turn turn=switch(pieceType){
            case BLACK_PAWN-> it.unibo.ai.didattica.competition.tablut.domain.StateTablut.Turn.BLACK;
            default -> it.unibo.ai.didattica.competition.tablut.domain.StateTablut.Turn.WHITE;
        };
        
        return new it.unibo.ai.didattica.competition.tablut.domain.Action(fromStr,toStr,turn);
    }
}
