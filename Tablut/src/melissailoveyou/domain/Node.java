package melissailoveyou.domain;

public record Node(MILTState startState,MILTAction actionDone) {
	public boolean isTerminal() {
		return true;
	}
	
	public Node[] getChilds() {
		return new Node[0];
	}

}
