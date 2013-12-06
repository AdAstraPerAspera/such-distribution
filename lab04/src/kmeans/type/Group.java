package kmeans.type;

public class Group <T> {
	private T value, mean;
	
	public Group<T> (T v, T mean) {
		this.value = v;
		this.mean = mean;
	}

	public T getVal () {
		return this.value;
	}
	
	public T getMean () {
		return this.mean;
	}
}
