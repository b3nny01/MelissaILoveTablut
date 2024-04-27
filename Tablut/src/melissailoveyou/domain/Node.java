package melissailoveyou.domain;

public record Node(MILTState startState,Action actionDone){
	public boolean isTerminal() {
		return true;
	}
	
	public Node[] getChilds() {
		return new Node[0];
	}

}
