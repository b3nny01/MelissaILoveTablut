package melissailoveyou.domain;

public class MinMaxChooser {
	
	public static int minMax(Node node,int depth,boolean maximizer) {
		if(depth==0 || node.isTerminal())
		{
			return heuristic(node);
		}
		if(maximizer) {
			int value=Integer.MIN_VALUE;
			for(Node child : node.getChilds()) {
				value=Math.max(value, minMax(child,depth-1,false));
			}
			return value;
		}else {
			int value=Integer.MAX_VALUE;
			for(Node child : node.getChilds()) {
				value=Math.min(value, minMax(child,depth-1,true));
			}
			return value;
		}
	}
	
	public static int heuristic(Node node) {
		return 0;
		
	}
	
	

}
