
public class Communities {
	public int[][] C;
	public Communities(int N, int K){
		C = new int[N][N];
	}
	
	public void addCommunity(Node initiator, Node receiver, int community){
		int N1 = initiator.index;
		int N2 = receiver.index;
		this.C[N1][N2] = community;
	}
	
	public int getCommunity(Node initiator, Node receiver){
		int N1 = initiator.index;
		int N2 = receiver.index;
		return this.C[N1][N2];
	}
}
