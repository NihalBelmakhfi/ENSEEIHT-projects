import java.net.InetAddress;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Set;

public class Client extends UnicastRemoteObject implements Client_itf {
    public static Moniteur monitor;
    private static HashMap<Integer, SharedObject> sharedObjects;
    private static String nomSite;
    private static Registry registry;
    private static String serverURL;
    private static Server_itf server;
    private static Client_itf client;
    private static Set<Client_itf> clients;

    public Client() throws RemoteException {
        super();
    }

    public static void init(String nom) {
        try {
            int port = Registry.REGISTRY_PORT;
            registry = LocateRegistry.getRegistry(port);
            String localHost = InetAddress.getLocalHost().getHostName();
            Client.serverURL = "//" + localHost + ":" + port + "/Server";
            server = (Server_itf) registry.lookup(serverURL);
            client = new Client();
            sharedObjects = new HashMap<Integer, SharedObject>();
            nomSite = nom;
            clients = server.addClient(client);
            monitor = server.getMonitor();
            System.out.println("Client " + nom + " initialised");
        } catch (Exception e) {
            System.out.println("Client" + nom + " initialisation exception: " + e.getMessage());
        } 
    }

    public Object getObj(String name) throws java.rmi.RemoteException {
        int id = server.lookup(name);
        SharedObject so = sharedObjects.get(id);
        return so.obj;
    }

    public void initSO(int idObj, Object valeur) throws java.rmi.RemoteException {
        SharedObject so = new SharedObject(valeur, idObj);
        sharedObjects.put(idObj, so);
        System.out.println("Client " + nomSite + " added object " + idObj + " to its local list");
    }

    public static void write(int id, Object o) {
        try {
            server.write(id, o);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /* Read for a regular register */
    public static Object readRegular(int id) throws RemoteException {
        ReadCallback rc = new ReadCallback(clients.size() / 2 + 1);
        for (Client_itf client : clients) {
            Thread t = new Thread(new Runnable() {
                public void run() {
                    try {
                        client.reportValue(id, rc);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            t.start();
        }
        return rc.getValue();
    }

    /* Read for an atomic register */
     public static Object read(int id) throws RemoteException {
        ReadCallback rc = new ReadCallback(clients.size() / 2 + 1);
        for (Client_itf client : clients) {
            Thread t = new Thread(new Runnable() {
                public void run() {
                    try {
                        client.reportValue(id, rc);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            t.start();
        }
        Object value = rc.getValue();
        server.broadcast(id, value, -1);
        return value;

    }

    public void reportValue(int idObj, ReadCallback_itf rcb) throws java.rmi.RemoteException {
        SharedObject so = sharedObjects.get(idObj);
        so.reportValue(rcb);
    }

    public void update(int idObj, int version, Object valeur, WriteCallback_itf wcb) throws java.rmi.RemoteException {
        SharedObject so = sharedObjects.get(idObj);
        so.update(version, valeur, wcb);
    }

    public String getSite() throws java.rmi.RemoteException {
        return nomSite;
    }

    public int getVersion(String name) throws java.rmi.RemoteException {
        int id = server.lookup(name);
        SharedObject so = sharedObjects.get(id);
        return so.getVersion();
    }

    public static SharedObject lookup(String objname) {
        int id;
        SharedObject so = null;
        try {
            id = server.lookup(objname);
            so = sharedObjects.get(id);
        } catch (Exception e) {
            System.out.println("Client " + nomSite + " lookup exception: " + e.getMessage());
        }
        return so;
    }

    public static String getIdSite() {
        return nomSite;
    }

    public static SharedObject publish(String name, Object o, boolean reset) {
        int id = 0;
        try {
            id = server.publish(name, o, reset);
        } catch (Exception e) {
            System.out.println("Client " + nomSite + " publish exception: " + e.getMessage());
        }
        SharedObject so = sharedObjects.get(id);
        return so;
    }

}
