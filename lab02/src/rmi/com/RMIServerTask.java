package rmi.com;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.Socket;

import rmi.reg.ReferenceObject;

/**
 * Thread to allow RMI Server to serve multiple requests concurrently.
 * 
 * @author Michael Wang - mhw1
 */
public class RMIServerTask implements Runnable {
	
	private ServerSocket ssock;
	private RORTable t;
	
	public RMIServerTask(ServerSocket ssock, RORTable t){
		this.ssock = ssock;
		this.t     = t;
	}

	@Override
	public void run() {
		try {
			while(true){
				//Read in message
				Socket sock = ssock.accept();
				InputStream         istream = sock.getInputStream();
				OutputStream        ostream = sock.getOutputStream();
				ObjectInputStream  oistream = new ObjectInputStream(istream);
				ObjectOutputStream oostream = new ObjectOutputStream(ostream);
				
				RMIInvocationMessage message = (RMIInvocationMessage) oistream.readObject();
				RMIResponseMessage response = null;
				
				//pull local copy of object from ror table
				Object obj = t.findObj(message.getObj());
				
				Class clazz = Class.forName(message.getTypeName());
				
				//get function from local object using reflections
				Serializable[] arguments = message.getParams();
				
				boolean      isVoid    = false;
				Serializable ret       = null;
				Object[]     newParams = message.getParams();
				
				//replace RORs with stubs
				for(int i = 0; i < newParams.length; i++){
					if(newParams[i] instanceof ReferenceObject){
						newParams[i] = ((ReferenceObject) newParams[i]).localize();
					}
				}
				
				Class[] parameterTypes = new Class[arguments.length];
				
				for (int i = 0; i < newParams.length; i++){
					if(newParams[i] instanceof Stub){
						parameterTypes[i] = newParams[i].getClass().getSuperclass();
					} else {
						parameterTypes[i] = newParams[i].getClass();
					}
				}
				
				//invoke method
				try {
					Method method = clazz.getDeclaredMethod(message.getFunc(), parameterTypes);
					
					if(method.getReturnType().equals(Void.TYPE)){
						isVoid = true;
						method.invoke(clazz.cast(obj), newParams);
					} else {
						ret = (Serializable) method.invoke(clazz.cast(obj), newParams); 
					}
				} catch (Exception e){
					//if invocation message throws an exception, send that exception back to the client
					
					System.err.println(e.getMessage());
					e.printStackTrace();
					
					response = new RMIResponseMessage(e);
					oostream.writeObject(response);
					
					oostream.close();
					oistream.close();
					ostream.close();
					istream.close();
					sock.close();
					continue;
				}
				
				//generate response message
				response = new RMIResponseMessage(ret, isVoid);
				
				//send response back to client
				oostream.writeObject(response);
				
				oostream.close();
				oistream.close();
				ostream.close();
				istream.close();
				sock.close();
			}
		} catch(Exception e){
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}

}
