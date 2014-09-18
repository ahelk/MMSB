
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.BitSet;
public class Graph {
	public BitSet[] G;
	public int N;
	public Graph(String path){
		//populate adjacency matrix with graph
		try  {
	        Scanner reader = new Scanner(new FileInputStream(path));
			
			int numNodes = Integer.parseInt(reader.nextLine());
			this.N = numNodes;
			this.G = new BitSet[numNodes];
			String[] line;
			int index = 0;
			while (reader.hasNextLine()){
				this.G[index] = new BitSet(this.N);
				line = reader.nextLine().split(" ");
				for (int i=0; i<line.length; i++){
					if (!line[0].equals("")) G[index].set(Integer.parseInt(line[i])); 
				}
				index+=1;
			}
			reader.close();
			
		}
		catch (FileNotFoundException e){
		    // do stuff here..
		}
	}
	
	public boolean isEdge(int node1, int node2){
		return this.G[node1].get(node2);
	}
	
	public void setEdge(int edge1, int edge2){
		this.G[edge1].set(edge2);
	}
}
