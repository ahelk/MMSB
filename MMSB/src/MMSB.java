import java.util.Random;
public class MMSB {
	Graph G;
	B B;
	Communities C;
	int N;
	int K;
	Node[] Nodes;
	double topicAlpha;
	double bAlpha;
	double bBeta;
	Random rand;
	// evaluation
	LogLikelihood p;
	public MMSB(String path, int K, double topicAlpha, double bAlpha, double bBeta){
		this.K = K;
		// read input graph data
		String inputPath = path;
		System.out.println("Loading input graph.");
		this.G = new Graph(inputPath);
		System.out.println("Input graph loaded.");
		this.N = G.N;
		this.Nodes = new Node[this.N];
		// instantiate communities object
		this.C = new Communities(this.N, this.K);
		// instantiate Bernoulli matrix
		this.B = new B(this.K, bAlpha, bBeta);
		this.topicAlpha = topicAlpha;
		this.bAlpha = bAlpha;
		this.bBeta = bBeta;
		// populate these objects
		System.out.println("Reading Input and Populating Structures");
		rand = new Random();
		this.populate();
		this.p = new LogLikelihood();
	}
	private void populate(){
		// fill up nodes array
		for (int i=0; i<this.N; i++){
			this.Nodes[i] = new Node(this.N, this.K, this.topicAlpha, i);
		}
		Random rand = new Random();
		// populate community and node memberships
		for (int i=0; i<this.N; i++){
			for (int j=0; j<this.N; j++){
				if (i==j) continue;
				int community = rand.nextInt(this.K);
				this.C.addCommunity(this.Nodes[i], this.Nodes[j], community);
				this.Nodes[i].addTopic(community);
			}
		}
		// Populate Bernoulli Matrix
		for (int i=0; i<this.N; i++){
			for (int j=0; j<this.N; j++){
				if (i==j) continue;
				int initiator = C.getCommunity(Nodes[i], Nodes[j]);
				int receiver = C.getCommunity(Nodes[j], Nodes[i]);
				if (this.G.isEdge(i, j)){
					B.addLink(initiator, receiver);
				}
				else {
					B.addNoLink(initiator, receiver);
				}
			}
		}
		System.out.println("Population Complete");
	}
	private double[] sampleDistribution(double[] dist1, double[] dist2, boolean edgeStatus1, boolean edgeStatus2){
		double[] newDist = new double[this.K*this.K];
		double total = 0;
		for (int i=0; i< newDist.length; i++){
			 int c1 = i/this.K;
			 int c2 = i%this.K;
			 double link1Prob;
			 double link2Prob;
			 if (edgeStatus1){
				 link1Prob = this.B.getProb(c1,c2);
			 }
			 else{
				 link1Prob = (1-this.B.getProb(c1,c2));
			 }
			 if (edgeStatus2){
				 link2Prob = this.B.getProb(c2,c1);
			 }
			 else{
				 link2Prob = (1-this.B.getProb(c2,c1));
			 }

			 newDist[i] = dist1[c1]*dist2[c2]*link1Prob*link2Prob;
			 //System.out.println(newDist[i]);
			 total+=newDist[i];
			
		}
		newDist[0] = newDist[0]/total;
		for (int i=1; i<newDist.length;i++){
			newDist[i]=(newDist[i]/total)+newDist[i-1];
		}
		return newDist;
		
	}
	private int[] binarySample(double[] distribution){
		double val = this.rand.nextDouble();
		int min = 0;
		int max = distribution.length-1;
		int[] results = new int[2];
		while (max >= min){
			int mid = min + (max-min)/2;
			if (distribution[mid]>=val){
				if(mid == 0 || distribution[mid-1] < val){
					results[0] = (mid)/this.K;
					results[1] = (mid)%this.K;
					break;
				}
				else{
					max = mid - 1;
				}
			}

			else{
				min = mid+1;
			}
		}

		return results;		
	}
	private int[] sample(double[] distribution){
		double val = this.rand.nextDouble();
		int[] results = new int[2];
		for (int i=0; i<distribution.length; i++){
			if (distribution[i] >= val){
				results[0] = i/this.K;
				results[1] = i%this.K;
				break;
			}
		}
		return results;
		
	}
	
	public void MCMC(int iterations, int calculateLLEveryNIterations){
		for (int index=0; index<iterations; index++){
			if (index  %calculateLLEveryNIterations == 0) this.perplexity();
			for (int i=0; i<this.N; i++){
				Node initiator = this.Nodes[i];
				//System.out.println(initiator.toString());
				for (int j=0; j<this.N; j++){
					if (i==j) continue;
					Node receiver = this.Nodes[j];
					
					int initiatorIndex = initiator.index;
					int receiverIndex = receiver.index;
					
					int initiatorCommunity = this.C.getCommunity(initiator, receiver);
					int receiverCommunity = this.C.getCommunity(receiver, initiator);
					
					boolean edgeStatus1 = G.isEdge(initiatorIndex, receiverIndex);
					boolean edgeStatus2 = G.isEdge(receiverIndex, initiatorIndex);
					// remove these nodes from consideration
					initiator.removeTopic(initiatorCommunity);
					receiver.removeTopic(receiverCommunity);
					// remove these links / no links from B
					if (edgeStatus1){
						this.B.removeLink(initiatorCommunity, receiverCommunity);
					}
					else{
						this.B.removeNoLink(initiatorCommunity, receiverCommunity);
					}
					if (edgeStatus2){
						this.B.removeLink(receiverCommunity, initiatorCommunity);
					}
					else{
						this.B.removeNoLink(receiverCommunity, initiatorCommunity);
					}
					// sample new communities
					double[] dist1 = initiator.distribution();
					double[] dist2 = receiver.distribution();
					// construct distribution
					double[] dist = this.sampleDistribution(dist1, dist2, edgeStatus1, edgeStatus2);
					// sample two new communities
					int[] communities = this.binarySample(dist);
					int newCommunity1 = communities[0];
					int newCommunity2 = communities[1];
					// add the communities to each node
					initiator.addTopic(newCommunity1);
					receiver.addTopic(newCommunity2);
					// add this to communities for future resampling
					this.C.addCommunity(initiator, receiver, newCommunity1);
					this.C.addCommunity(receiver, initiator, newCommunity2);
					// add this to B matrix

					if (edgeStatus1){
						this.B.addLink(newCommunity1, newCommunity2);
					}
					else{
						this.B.addNoLink(newCommunity1, newCommunity2);
					}
					if (edgeStatus2){
						this.B.addLink(newCommunity2, newCommunity1);
					}
					else{
						this.B.addNoLink(newCommunity2, newCommunity1);
					}			
					
				}
			}
		}
	}
	private void perplexity(){
		this.p.calculateLL(this.G, this.C, this.B, this.Nodes);
	}
}
