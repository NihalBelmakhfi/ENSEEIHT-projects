public interface WriteCallback_itf extends java.rmi.Remote {

	void response() throws java.rmi.RemoteException;

	void waitResponses() throws java.rmi.RemoteException;
	
}
