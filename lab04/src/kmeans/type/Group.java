package kmeans.type;

public class Group <T> {
	private T value, mean;
	
	public Group (T v, T m) {
		this.value = v;
		this.mean = mean;
	}

	public T getVal () { return value; }
	
	public T getMean () { return this.mean; }
}
