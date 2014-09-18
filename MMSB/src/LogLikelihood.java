
public class LogLikelihood {
//	Graph G;
//	Communities C;
//	B B;
//	Node[] Nodes;
//	public LogLikelihood(Graph G, Communities C, B B, Node[] Nodes){
//		this.G = G;
//		this.C = C;
//		this.B = B;
//		this.Nodes = Nodes;
//	}
	
	public void calculateLL(Graph G, Communities C, B B, Node[] Nodes){
		double ll = 0.0;
		for (int i=0; i<G.N; i++){
			for (int j=0; j<G.N; j++){
				if (i==j) continue;
				boolean edgeStatus = G.isEdge(i, j);
				int c1 = C.getCommunity(Nodes[i], Nodes[j]);
				int c2 = C.getCommunity(Nodes[j], Nodes[i]);
				if (edgeStatus){
					ll += Math.log(B.getProb(c1,c2));
				}
				else{
					ll += Math.log((1-B.getProb(c1,c2)));
				}
			}
		}
		System.out.println("LogLikelihood: "+String.valueOf(ll));
	}

}
