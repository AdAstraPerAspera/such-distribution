package kmeans.type;

import java.io.Serializable;

public class Group <T> implements Serializable{
	private T value, mean;
	
	public Group (T v, T m) {
		this.value = v;
		this.mean = m;
	}

	public T getVal () { return value; }
	
	public T getMean () { return this.mean; }
}
