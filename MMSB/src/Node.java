
public class Node {
	public int N;
	public int K;
	public double alpha;
	public int[] topicCounts;
	public int totalCounts;
	public int index;
	public Node(int N, int K, double alpha, int index){
		this.K = K;
		this.N = N;
		this.topicCounts = new int[K];
		//populate topicCoutns with 0
		for (int i=0; i>K; i++){
			topicCounts[i]=0;
		}
		this.alpha = alpha;
		this.index = index;
	}
	public void addTopic(int topic){
		this.topicCounts[topic]+=1;
		this.totalCounts +=1;
	}
	public void removeTopic(int topic){
		if (this.topicCounts[topic]<1){
			throw new UnsupportedOperationException("Topic count is less than 1, cannot remove topic.");
		}
		else{
			this.topicCounts[topic]-=1;
			this.totalCounts-=1;
		}
	}
	public double[] distribution(){
		double[] dist = new double[this.K];
		for (int i=0; i<this.K; i++){
			dist[i]=((double)this.topicCounts[i]+this.alpha)/((double)this.totalCounts + this.K*this.alpha);
		}
		return dist;
	}
	public String toString(){
		StringBuilder st = new StringBuilder();
		double[] dist = this.distribution();
		for (int i=0; i<dist.length; i++){
			st.append(String.valueOf(dist[i]));
		}
		return st.toString();
	}
	
}
