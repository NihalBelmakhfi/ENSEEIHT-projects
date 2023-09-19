
public interface ReadCallback_itf extends java.rmi.Remote {

	public void response(int version, Object value) throws java.rmi.RemoteException;
	public Object getValue() throws java.rmi.RemoteException;

}