public class TestMix {
   
    public static void main(String argv[]) {
		
		if (argv.length != 2) {
			System.out.println("java TestMix <nb d'écritures> <Nom du client>");
			return;
		}
		int N = Integer.parseInt(argv[0]);
        int nomClient =Integer.parseInt(argv[1]);;

    	// initialize the system
		Client.init();
		
		// look up the client_i object in the name server
		// if not found, create it, and register it in the name server
		SharedObject s = Client.lookup("client_i");
		if (s == null) {
            Integer monint = 0;
			s = Client.create(monint);
			Client.register("client_i", s);
		}

        for(int i =0 ; i< N/2 ; i++) {
            //Premier cas d'erreur, un client essaye d'écrire alors qu'il y avait un lecteur
            System.out.println("Début écriture 1 pour le client" + nomClient);
            s.lock_write();
            Integer ancienneVal = (Integer) s.obj;
            s.obj = ancienneVal + 1;
            s.unlock();

            //Deuxième cas d'erreur, un client essaye de lire alors qu'il y avait un écrivain
            System.out.println("Début lecture pour le client" + nomClient);
            s.lock_read();
            System.out.println("Valeur Lise" + s.obj);
            s.unlock();
        }

    }
}
