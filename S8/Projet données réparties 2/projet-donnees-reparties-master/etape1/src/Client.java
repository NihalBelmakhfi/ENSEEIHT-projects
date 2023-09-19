import java.rmi.*;
import java.rmi.server.UnicastRemoteObject;
import java.rmi.registry.*;
import java.util.HashMap;
import java.net.*;

public class Client extends UnicastRemoteObject implements Client_itf {

	private static Client_itf client;
	private static String serverURL;
	private static Server_itf server;
	private static HashMap<Integer, SharedObject> sharedObjects;
	private static Registry registry;
	private static Updater_itf updater;

	public Client() throws RemoteException {
		super();
	}

	public static void init() {
		try {
			int port = Registry.REGISTRY_PORT;
			registry = LocateRegistry.getRegistry(port);
			String localHost = InetAddress.getLocalHost().getHostName();
			Client.serverURL = "//" + localHost + ":" + port + "/monServeur";
			server = (Server_itf) registry.lookup(serverURL);
			client = new Client();
			sharedObjects = new HashMap<Integer, SharedObject>();
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void setUpdater(Updater_itf updater) {
		Client.updater = updater;
	}

	public static SharedObject lookup(String name) {
		SharedObject so = null;
		try {
			int id = server.lookup(name);
			so = sharedObjects.get(id);
			if (so == null) {
				so = new SharedObject(null, id);
				sharedObjects.put(id, so);
			}
			System.out.println("Client: lookup effectué");
		} catch (RemoteException exp) {
			System.out.println("Premier lookup: objet pas trouvé");
		}
		return so;
	}

	public static void subscribe(int id) {
		try {
			server.subscribe(id, client);
			System.out.println("Client: subscribe success");
		} catch (RemoteException e) {
			System.out.println("Client: subscribe failed: " + e.getMessage());
		}
	}

	public static void unsubscribe(int id) {
		try {
			server.unsubscribe(id, client);
			System.out.println("Client: unsubscribe success");
		} catch (RemoteException e) {
			System.out.println("Client: unsubscribe failed: " + e.getMessage());
		}
	}

	public static void register(String name, SharedObject so) {
		try {
			server.register(name, so.getId());
			System.out.println("Client: register success");
		} catch (RemoteException e) {
			System.out.println("Client: register failed: " + e.getMessage());
		}
	}

	public static SharedObject create(Object o) {
		SharedObject so = null;
		try {
			int id = server.create(o);
			so = new SharedObject(o, id);
			sharedObjects.put(id, so);
			System.out.println("Client: created shared object");
		} catch (RemoteException e) {
			System.out.println("Client: create failed");
			e.printStackTrace();
		}
		return so;
	}

	/**
	 * Verrouille le SharedObject d'identifiant id en lecture
	 * 
	 * @param id l'identifiant de l'objet à verrouiller en lecture
	 * @return Object l'objet correspondant à l'identifiant
	 */
	public static Object lock_read(int id) {
		Object obj = null;
		try {
			obj = server.lock_read(id, client);
		} catch (Exception e) {
			System.out.println("Client: lock_read failed");
			e.printStackTrace();
		}
		return obj;
	}

	public static Object lock_write(int id) {
		Object obj = null;
		try {
			obj = server.lock_write(id, client);
		} catch (RemoteException e) {
			System.out.println("Client: lock_write failed");
			e.printStackTrace();
		}
		return obj;
	}

	public static void publish(int id) {
		try {
			server.publish(id);
		} catch (RemoteException e) {
			System.out.println("Client: publish failed");
			e.printStackTrace();
		}
	}

	@Override
	public Object reduce_lock(int id) throws java.rmi.RemoteException {
		return sharedObjects.get(id).reduce_lock();
	}

	@Override
	public void invalidate_reader(int id) throws java.rmi.RemoteException {
		sharedObjects.get(id).invalidate_reader();
	}

	@Override
	public Object invalidate_writer(int id) throws java.rmi.RemoteException {
		return sharedObjects.get(id).invalidate_writer();

	}

	public void update() throws java.rmi.RemoteException {
		System.out.println("Client: update");
		if (updater != null) {
			updater.update();
		}
	}
}
