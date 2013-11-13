package mrf.tasks;

import java.io.Serializable;
import java.util.concurrent.Callable;

public interface SerializableCallable extends Callable<Object>, Serializable {

}
