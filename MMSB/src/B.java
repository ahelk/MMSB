
public class B {
	int K;
	int countLinks[][];
	int countNoLinks[][];
	double alpha;
	double beta;
	public B(int K, double alpha, double beta){
		this.K = K;
		this.countLinks = new int[K][K];
		this.countNoLinks = new int[K][K];
		this.alpha = alpha;
		this.beta = beta;
		
	}
	
	public void addLink(int community1, int community2){
		this.countLinks[community1][community2] +=1;
	}
	
	public void addNoLink(int community1, int community2){
		this.countNoLinks[community1][community2]+=1;
	}
	
	public void removeLink(int community1, int community2){
		if (this.countLinks[community1][community2]<1){
			throw new UnsupportedOperationException("Link count is less than 1, cannot remove link.");
		}
		this.countLinks[community1][community2] -=1;
	}
	
	public void removeNoLink(int community1, int community2){
		
		if (this.countNoLinks[community1][community2]<1){
			throw new UnsupportedOperationException("Link count is less than 1, cannot remove noLink.");
		}
		this.countNoLinks[community1][community2]-=1;
	}
	
	public double getProb(int community1, int community2){
		double prob = ((double) this.countLinks[community1][community2]+this.alpha)
				/((double)this.countNoLinks[community1][community2] + this.beta + 
						this.countLinks[community1][community2] + this.alpha );
		return prob;
	}
	
}
