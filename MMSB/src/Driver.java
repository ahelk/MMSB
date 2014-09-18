
public class Driver {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String path = "/Users/ahmed/Dropbox/Graph/JavaImplementation/MMSB/input/input.txt";
		int K = 5;
		double topicAlpha = 1;
		double bAlpha = 1;
		double bBeta = 1;
		System.out.println("testing");
		MMSB x = new MMSB(path, K, topicAlpha, bAlpha, bBeta);
		x.MCMC(1000, 10);

	}

}
