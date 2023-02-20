import java.net.InetAddress;
import java.net.UnknownHostException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.concurrent.Semaphore;



public class Server extends UnicastRemoteObject implements Server_itf {

	//cette collection stocke des paires dont les clés sont des chaînes 
	//de caractères (String) et les valeurs sont des entiers (Integer).
	private static HashMap<String, Integer> nomServeur;

	private static HashMap<Integer, ServerObject> serverObjects;

	//Définition d'un sémaphore pour éviter des problèmes de synchronization
    private final Semaphore registerlock = new Semaphore(1,true);

	private final Semaphore createlock = new Semaphore(1,true);

	private int increment = 0;

//============================================================//
//  Constructeur de la classe Server						  //
//============================================================//

	public Server() throws RemoteException {
		super();
		nomServeur = new HashMap<String, Integer>();
		serverObjects = new HashMap<Integer, ServerObject>();
	}

//============================================================//
// Méthode lookup: cherche un nom de so sur la table des noms //
//============================================================//

	public int lookup(String name) throws java.rmi.RemoteException{
		System.out.println("Server : lookup demand received");
		//on cherche l'objet partagé associé au nom	
		//on trouve l'id de name
		Integer id = nomServeur.get(name);
		//on verifie si ça existe
		if (id != null) {
			int resultat = (int) id;
			return resultat;
		}
		else{
			throw new RemoteException();
		}
	}

//============================================================//
//  Méthode register: ajoute un ServerObject à la table	  //
//============================================================//

	public void register(String name, int id) throws java.rmi.RemoteException{
		try {
			registerlock.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("Server : register demand received");
		if (nomServeur.containsKey(name)){
			serverObjects.remove(id);
			registerlock.release();
			throw new RemoteException();
		}
		else {
			nomServeur.put(name, id);
			registerlock.release();
		}
	}

//============================================================//
//  Méthode nouveauId: attribue un id unique à chaque so	  //
//============================================================//

	public int nouveauId(){
	//Cette méthode retourne un identifiant unique pour un objet partagé.
	//On utilise la méthode "size()" de l'objet "serverObjects" qui est une
	//collection de type HashMap pour obtenir le nombre d'éléments dans la collection.
	//On ajoute 1 à ce nombre pour obtenir un identifiant unique.
		this.increment = this.increment + 1;
		int id = this.increment;
		return id;
	}

//============================================================//
//  Méthode create: crée un nouveau serverObject dans la table//
//============================================================//

	public int create(Object o) throws java.rmi.RemoteException{
	//La méthode crée un nouvel objet "ServerObject" en utilisant 
	//l'objet "o" passé en paramètre et en appelant la méthode 
	//"nouveauId()" pour obtenir un identifiant unique pour 
	//l'objet. Ensuite, la méthode enregistre l'objet "ServerObject" 
	//dans un objet "serverObjects" (qui est une collection de type HashMap) 
	//en utilisant l'identifiant unique comme clé et l'objet "ServerObject" 
	//comme valeur.	
		try {
			createlock.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("Server : create demand received");
		int id = nouveauId();
		ServerObject servobj = new ServerObject(o,id);
		serverObjects.put(id, servobj);
		createlock.release();
		return id;
	}

//==============================================================================//
// Méthode lock_read: demande un verrou en lecture pour un so d'identifiant id  //
//==============================================================================//

	public Object lock_read(int id, Client_itf client) throws java.rmi.RemoteException{
		//permet à un client de verrouiller en lecture un objet partagé sur le serveur.
		//"Client_itf" représente le client qui souhaite verrouiller l'objet.
		//Récupère l'objet "ServerObject" associé à l'identifiant "id" en utilisant 
		//la méthode "get" de l'objet "serverObjects" qui est une collection de type 
		//HashMap.
		System.out.println("Server : lock_read demand received");
		ServerObject so = serverObjects.get(id);
		//on Appelle la méthode "lock_read" sur l'objet "ServerObject" en lui 
		//passant l'objet "client" en paramètre.
		return so.lock_read(client);
	}

//==============================================================================//
// Méthode lock_write: demande un verrou en écriture pour un so d'identifiant id//
//==============================================================================//

	public Object lock_write(int id, Client_itf client) throws java.rmi.RemoteException{
		//permet à un client de verrouiller en écriture un objet partagé sur le serveur
		System.out.println("Server : lock_write demand received");
		ServerObject so = serverObjects.get(id); 
		return so.lock_write(client);

	}

//==================================================================================//
// Méthode main: crée la registry et un server qui accepte les demandes des clients //
//==================================================================================//

	public static void main(String[] args) throws UnknownHostException {
		System.out.println("----------------On lance le serveur----------------");
		try{
			//port et URL
			int port = Registry.REGISTRY_PORT;
			//on crée a registry
			Registry registry = LocateRegistry.createRegistry(port);
			Server monServeur = new Server();

			String URLDuServeur = "//"
							+ InetAddress.getLocalHost().getHostName() + ":"
							+ port + "/monServeur";
			//rediriger une URL de serveur (URLDuServeur) vers un objet serveur 
			//spécifique (monServeur). Cela permet de rediriger les requêtes entrantes 
			//pour cette URL vers l'objet serveur spécifié, plutôt que vers l'objet 
			//monServeur associé à cette URL par défaut.
			registry.rebind(URLDuServeur, monServeur);
		}

		catch(Exception exp){
			System.out.println("exception dans la classe main");
		}
		
		System.out.println("----------------Le serveur est pret-----------------");

	}


}
