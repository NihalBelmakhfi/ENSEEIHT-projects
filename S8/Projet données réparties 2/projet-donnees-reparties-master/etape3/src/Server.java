import java.net.InetAddress;
import java.net.UnknownHostException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class Server extends UnicastRemoteObject implements Server_itf {
    Set<Client_itf> clients;
    private final int nbClients;
    int nbActiveClients;
    private HashMap<String, Integer> sharedObjectNames;
    private HashMap<Integer, ServerObject> serverObjects;
    private Moniteur moniteur;
    private Object lockMoniteur;

    public Server(int nbClients) throws RemoteException {
        super();
        this.nbClients = nbClients;
        this.nbActiveClients = 0;
        this.sharedObjectNames = new HashMap<String, Integer>();
        this.serverObjects = new HashMap<Integer, ServerObject>();
        this.clients = new HashSet<Client_itf>();
        this.lockMoniteur = new Object();
    }

    // architecture nécessaire au démarrage : serveur dans registry,
    // tous sites enregistrés auprès du serveur
    // enregistre un site et récupère la liste complète (cardinal connu au
    // lancement)
    public synchronized Set<Client_itf> addClient(Client_itf client) throws RemoteException {
        if (nbActiveClients == nbClients) {
            System.out.println("Server: all clients are already connected, client not added");
            return clients;
        }
        clients.add(client);
        nbActiveClients++;
        System.out.println("Server: client added");
        while (nbActiveClients < nbClients) {
            System.out.println("Server: client waiting for all clients to connect");
            try {
                this.wait();
            } catch (InterruptedException e) {
                System.out.println("Server: client interrupted while waiting for all clients to connect");
            }
        }
        this.notifyAll();
        return clients;
    }

    // nommage des objets partagés
    public synchronized int publish(String name, Object o, boolean reset) throws RemoteException {
        int id;
        if (!sharedObjectNames.containsKey(name)) {
            ServerObject so = new ServerObject(o);
            id = serverObjects.size();
            sharedObjectNames.put(name, id);
            serverObjects.put(id, so);

            clients.forEach((client) -> {
                try {
                    client.initSO(id, o);
                } catch (RemoteException e) {
                    System.out.println("Error publish: initSO");
                }

            });

        } else {
            id = sharedObjectNames.get(name);

            if (reset) {
                ServerObject so = new ServerObject(o);
                serverObjects.put(id, so);

                clients.forEach((client) -> {
                    try {
                        client.initSO(id, o);
                    } catch (RemoteException e) {
                        System.out.println("Error publish: initSO");

                    }
                });

            }
        }

        return id;
    }

    public int lookup(String name) throws java.rmi.RemoteException {
        return sharedObjectNames.get(name);
    }

    // le serveur centralise (-> ordonne) les écritures. Renvoie le numéro de
    // version
    public synchronized int write(int idObjet, Object valeur) throws RemoteException {
        ServerObject so = serverObjects.get(idObjet);
        int version = so.getVersion() + 1;
        System.out.println("Server: write on object " + idObjet + " with version " + version);
        so.setObject(valeur);
        so.setVersion(version);
        broadcast(idObjet, valeur, version);
        return version;
    }

    public synchronized void broadcast(int idObjet, Object valeur, int version) throws RemoteException {
        final int v = version < 0 ? serverObjects.get(idObjet).getVersion() : version;
        WriteCallback wcb = new WriteCallback(nbClients / 2 + 1);
        for (Client_itf client : clients) {
            Thread t = new Thread(new Runnable() {
                public void run() {
                    try {
                        client.update(idObjet, v, valeur, wcb);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            t.start();
        }
        wcb.waitResponses();
    }

    @Override
    public String[] list() throws RemoteException {
        return (String[]) sharedObjectNames.keySet().toArray();
    }

    @Override
    public Set<Client_itf> setMonitor(Moniteur m) throws RemoteException {
        this.moniteur = m;
        synchronized (this) {
            try {
                System.out.println("Server: monitor waiting for all clients to connect");
                while (nbClients > nbActiveClients) {
                    this.wait();
                }
                System.out.println("Server: monitor ready");
            } catch (InterruptedException exc) {
                System.out.println("Error setMonitor");
            }
        }
        if (moniteur != null) {
            synchronized (lockMoniteur) {
                System.out.println("Server: monitor set");
                lockMoniteur.notifyAll();
            }
        }
        return clients;
    }

    @Override
    public Moniteur getMonitor() throws RemoteException {
        synchronized (lockMoniteur) {
            while (moniteur == null) {
                try {
                    System.out.println("Waiting for monitor to be set");
                    lockMoniteur.wait();
                } catch (InterruptedException e) {
                    System.out.println("Waiting for monitor to be set interrupted");
                }
            }
        }
        return moniteur;
    }

    public static void main(String[] args) throws UnknownHostException {
        System.out.println("----------------Starting the server----------------");
        if (args.length != 1) {
            System.out.println("Usage: java Server <nbClients>");
            System.exit(0);
        }
        try {
            int port = Registry.REGISTRY_PORT;
            Registry registry = LocateRegistry.createRegistry(port);
            int nbClients = Integer.parseInt(args[0]);
            if (nbClients < 1) {
                System.out.println("Usage: java Server <nbClients>");
                System.exit(0);
            }
            Server server = new Server(nbClients);

            String serverURL = "//"
                    + InetAddress.getLocalHost().getHostName() + ":"
                    + port + "/Server";

            registry.rebind(serverURL, server);
        }

        catch (Exception e) {
            System.out.println("Server exception: " + e.getMessage());
            System.out.println("Usage: java Server <nbClients>");
            System.exit(0);
        }

        System.out.println("----------------Server ready-----------------");
    }

}
