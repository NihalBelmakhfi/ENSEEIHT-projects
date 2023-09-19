import java.net.InetAddress;
import java.net.UnknownHostException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Semaphore;

public class Server extends UnicastRemoteObject implements Server_itf {

	private HashMap<String, Integer> serverName;

	private HashMap<Integer, ServerObject> serverObjects;

	private int lastID;

	private HashMap<Integer, Set<Client_itf>> subscribers;

	// To avoid concurrent access to the register method
	private final Semaphore registerlock = new Semaphore(1, true);

	private final Semaphore createlock = new Semaphore(1, true);

	public Server() throws RemoteException {
		super();
		serverName = new HashMap<String, Integer>();
		serverObjects = new HashMap<Integer, ServerObject>();
		subscribers = new HashMap<Integer, Set<Client_itf>>();
		lastID = 0;
	}

	public int lookup(String name) throws java.rmi.RemoteException {
		System.out.println("Server: lookup demand received");
		if (serverName.containsKey(name)) {
			return serverName.get(name);
		} else {
			throw new RemoteException();
		}
	}

	public void track(int id, Client_itf client) throws java.rmi.RemoteException {
		System.out.println("Server: track demand received");
		if (!serverObjects.containsKey(id)) {
			throw new RemoteException("Server: track failed: object not found");
		}
		if (subscribers.containsKey(id)) {
			if (!subscribers.get(id).contains(client)) {
				subscribers.get(id).add(client);
			}
		} else {
			Set<Client_itf> subscribedClients = new HashSet<Client_itf>();
			subscribedClients.add(client);
			subscribers.put(id, subscribedClients);
		}
	}

	public void leave_track(int id, Client_itf client) throws java.rmi.RemoteException {
		System.out.println("Server: untrack demand received");
		if (subscribers.containsKey(id)) {
			subscribers.get(id).remove(client);
		}
	}

	public boolean publish(int id, Object object, Client_itf publisher) throws java.rmi.RemoteException {
		System.out.println("Server: publish demand received");
		if (!subscribers.containsKey(id) || subscribers.get(id).size() == 0
				|| (subscribers.get(id).size() == 1 && subscribers.get(id).contains(publisher))) {
			return false;
		}
		Set<Client_itf> subscribedClients = subscribers.get(id);
		for (Client_itf subscriber : subscribedClients) {
			subscriber.update(id, object);
		}
		serverObjects.get(id).setObject(object);
		serverObjects.get(id).setLock(ServerObject.Lock.READLOCK);
		serverObjects.get(id).setWriter(null);
		HashSet<Client_itf> readers = new HashSet<Client_itf>(subscribers.get(id));
		readers.add(publisher);
		serverObjects.get(id).setReaders(readers);
		return true;

	}

	public void register(String name, int id) throws java.rmi.RemoteException {
		try {
			registerlock.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("Server: register demand received");
		if (serverName.containsKey(name)) {
			registerlock.release();
			throw new RemoteException("Name already exists");
		} else {
			serverName.put(name, id);
			registerlock.release();
		}
	}

	public int create(Object o) throws java.rmi.RemoteException {
		try {
			createlock.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("Server: create demand received");
		int id = lastID++;
		ServerObject servObj = new ServerObject(o, id);
		serverObjects.put(id, servObj);
		createlock.release();
		return id;
	}

	public Object lock_read(int id, Client_itf client) throws java.rmi.RemoteException {
		System.out.println("Server : lock_read demand received");
		ServerObject so = serverObjects.get(id);
		return so.lock_read(client);
	}

	public Object lock_write(int id, Client_itf client) throws java.rmi.RemoteException {
		System.out.println("Server : lock_write demand received");
		ServerObject so = serverObjects.get(id);
		return so.lock_write(client);

	}

	public static void main(String[] args) throws UnknownHostException {
		System.out.println("----------------Starting the server----------------");
		try {
			int port = Registry.REGISTRY_PORT;
			Registry registry = LocateRegistry.createRegistry(port);
			Server server = new Server();

			String serverURL = "//"
					+ InetAddress.getLocalHost().getHostName() + ":"
					+ port + "/monServeur";
			registry.rebind(serverURL, server);
		}

		catch (Exception e) {
			System.out.println("Server exception: " + e.getMessage());
		}

		System.out.println("----------------Server ready-----------------");
	}

}
