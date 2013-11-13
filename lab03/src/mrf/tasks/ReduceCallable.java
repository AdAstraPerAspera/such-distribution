package mrf.tasks;

import java.io.File;
import java.util.List;

public interface ReduceCallable extends SerializableCallable {
	public void getFiles(List<File> f);
}
