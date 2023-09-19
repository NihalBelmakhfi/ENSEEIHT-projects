public interface Client_itf extends java.rmi.Remote {

	public void initSO(int idObj, Object valeur) throws java.rmi.RemoteException;
	public void reportValue(int idObj, ReadCallback_itf rcb) throws java.rmi.RemoteException;
	public void update(int idObj, int version, Object valeur, WriteCallback_itf wcb) throws java.rmi.RemoteException;
	public String getSite() throws java.rmi.RemoteException;
	public Object getObj(String name) throws java.rmi.RemoteException;
	public int getVersion(String name) throws java.rmi.RemoteException;
}
