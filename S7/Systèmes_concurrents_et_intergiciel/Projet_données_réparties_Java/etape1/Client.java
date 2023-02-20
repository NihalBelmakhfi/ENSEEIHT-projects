import java.rmi.*;
import java.rmi.server.UnicastRemoteObject;
import java.rmi.registry.*;
import java.util.HashMap;
import java.net.*;


public class Client extends UnicastRemoteObject implements Client_itf {
	
	//Les attributs de la classe Client

	//Instance de client à utiliser pour l'initialisation
	private static Client_itf monClient = null;
	//Choix de l'URL de serveur à appeler pour les lookup
	private static String URLDuServeur;
	//Le server auquel le client se connecte
	private static Server_itf monServeur = null;
	//Une table locale des SharedObject crée par les applications
	private static HashMap<Integer, SharedObject> sharedObjects;
	//Initialisation de registry sur laquelle on va faire les lookup
	private static Registry registry = null;


//=================================================//
//  Le constructeur de la classe				   //
//=================================================//

	public Client() throws RemoteException {
		super();
	}

//=================================================//
//  Initialisation du Client par les applications  //
//=================================================//

	// initialization of the client layer
	public static void init() {
		try {
			int port = Registry.REGISTRY_PORT;
			registry = LocateRegistry.getRegistry(port);
			String localHost = InetAddress.getLocalHost().getHostName();
			Client.URLDuServeur = "//" + localHost + ":" + port + "/monServeur";
			monServeur = (Server_itf) registry.lookup(URLDuServeur);
			monClient = new Client();
			sharedObjects = new HashMap<Integer, SharedObject>();
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

//============================================================//
//  Méthode lookup:chercher si le so était déjà crée avant	  //
//============================================================//

	// lookup in the name server
	public static SharedObject lookup(String name) {
		SharedObject objetCree = null ;
		// pour récupérer un objet partagé à partir d'un nom donné.
		//on va utiliser lock_read" pour verrouiller l'objet correspondant, 
		//puis on va créer un nouvel objet "SharedObject" à partir de cet 
		//objet et de l'identifiant. on déverrouillera ensuite l'objet 
		//verrouillé et on ajoutera le nouvel objet "SharedObject" à une table 
		//locale appelée "sharedObjects"
		try{
		int id = monServeur.lookup(name); // id associé à name
		//Si l'identifiant est valide (supérieur ou égal à zéro)
		if (id>=0) {
			// objetCree = new SharedObject(null, id);
			//on ajoute l'objet creé à la table 
			sharedObjects.put(id, objetCree);
		}
		System.out.println("Client: lookup effectué");
		
	}
 catch (RemoteException exp) {
	System.out.println("Premier lookup: objet pas trouvé");
}
return objetCree;
		
	}		
	
//============================================================//
//  Méthode register: enregistrement du so crée sur le server //
//============================================================//

	// binding in the name server
	public static void register(String name, SharedObject_itf so) {
		// il y a un "casting" (conversion) de l'objet SharedObject_itf 
		//en SharedObject, 
		//Ensuite, l'objet partagé est enregistré dans le serveur de noms
		// en utilisant la méthode "register" avec les paramètres "name" 
		//et l'ID de l'objet partagé.
		try {
			SharedObject soCast = (SharedObject) so;
			monServeur.register(name, soCast.getId());
			System.out.println("Client: register effectué");
		}
		catch (RemoteException exp) {
			//Dans le cas ou on catch une remote exception
			//C'est qu'on a crée un objet de trop
			//Alors il faut refaire le lookup et l'enlever le doublon
			//de notre table locale
			sharedObjects.remove(so.getId());
			int newid;
			try {
				newid = monServeur.lookup(name);
				SharedObject s = (SharedObject) so;
				sharedObjects.put(newid,s);
				so.setId(newid);
			} catch (RemoteException e) {
				e.printStackTrace();
			}
	}
}

//============================================================//
//  Méthode create: creation du so avec l'objet o			  //
//============================================================//

	// creation of a shared object
	public static SharedObject create(Object o) {
		//on crée un objet partagé et l'affecte à la variable "so", 
		//qui est initialement définie à null
		//appelle la méthode "create" sur l'objet "Server" en passant 
		//en paramètre l'objet "o". La méthode renvoie un "id" qui 
		//est utilisé pour créer un nouvel objet partagé en utilisant 
		//l'id et l'objet "o". Le nouvel objet partagé est ensuite ajouté 
		//à une HashMap appelée "sharedObjects" avec l'id en tant que clé et l'objet partagé en tant que valeur.
		SharedObject so = null;
		try{
			int id = monServeur.create(o);
			// so = new SharedObject(o, id);
			so = new SharedObject(o, id);
			//on ajoute à la hashmap
			sharedObjects.put(so.getId(), so);
			System.out.println("Client: create effectué");
		}
		catch (RemoteException exp) {
			System.out.println("Exception dans create");
			exp.printStackTrace();
	}
	return so;
}
	
//============================================================//
//  Méthode lock_read: obtention d'un verrou en écriture	  //
//============================================================//
	/**
	 * Verrouille le SharedObject d'identifiant id en lecture
	 * @param id l'identifiant de l'objet à verrouiller en lecture
	 * @return Object l'objet correspondant à l'identifiant 
	 */
	// request a read lock from the Server
	public static Object lock_read(int id) {
		// la variable "o" est affectée avec le résultat de l'appel à la méthode 
		//"lock_read" de l'objet "monServeur" en passant en paramètre "id" et "monClient"
		Object obj = null;
		try{
			obj = monServeur.lock_read(id, monClient);
		}
		catch (Exception exp) {
			System.out.println("Exception dans lovk_read");
			exp.printStackTrace();
		}
		return obj;
	}

//============================================================//
//  Méthode lock_write: obtention d'un verrou en écriture	  //
//============================================================//

	// request a write lock from the Server
	public static Object lock_write (int id) {
		//"o" est affectée avec le résultat de l'appel à la méthode "lock_write" 
		//de l'objet "monServeur" en passant en paramètre "id" et "monClient".
		Object obj = null;
		try{
			obj = monServeur.lock_write(id, monClient);
		}
		catch (RemoteException exp) {
			System.out.println("Exception dans lock_write");
			exp.printStackTrace();
		}
		return obj;
	}

//============================================================//
//  Méthode reduce_lock: passer du mode d'écriture en lecture //
//============================================================//

	// receive a lock reduction request from the Server
 @Override
	public Object reduce_lock(int id) throws java.rmi.RemoteException {
	//On applique un verrou de réduction sur un objet partagé localement 
	//qui porte un identifiant spécifique (id). La méthode utilise une 
	//fonction "reduce_lock()" pour verrouiller l'objet, puis elle 
	//retourne l'objet verrouillé. 
	return sharedObjects.get(id).reduce_lock();
	}

//============================================================//
//  Méthode invalidate_reader: invalider un lecteur			  //
//============================================================//

	// receive a reader invalidation request from the Server
 @Override
	public void invalidate_reader(int id) throws java.rmi.RemoteException {
	//La méthode récupère l'objet de la collection en utilisant l'identifiant "id" 
	//comme clé, puis appelle la méthode "invalidate_reader" sur cet objet
	sharedObjects.get(id).invalidate_reader();
	}

//============================================================//
//  Méthode invalidate_writer: invalider un écrivain		  //
//============================================================//

	// receive a writer invalidation request from the Server
 @Override
	public Object invalidate_writer(int id) throws java.rmi.RemoteException {
		//System.out.println(sharedObjects.get(id) == null);
		return sharedObjects.get(id).invalidate_writer(); 

	}
}
