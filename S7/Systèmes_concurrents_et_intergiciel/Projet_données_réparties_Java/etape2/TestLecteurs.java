public class TestLecteurs {
    public static void main(String argv[]) {
		
		if (argv.length != 1) {
			System.out.println("java TestLecteurs <nb d'écritures>");
			return;
		}
		int N = Integer.parseInt(argv[0]);

    	// initialize the system
		Client.init();
		
		// look up the lecteur_i object in the name server
		// if not found, create it, and register it in the name server
		SharedObject s = Client.lookup("lecteur_i");
		if (s == null) {
            Integer monint = 1;
			s = Client.create(monint);
			Client.register("lecteur_i", s);
		}

        //On lock notre shared object en ecriture, on écrit quelque chose dedans, puis on le unlock
        for(int i =0 ; i<= N ; i++) {
            // lock the object in write mode
            s.lock_read();
            Integer ancienneVal1 = (Integer) s.obj;
            try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				System.out.println("Exception dans le système: Thread not working properly");
				e.printStackTrace();
			}
			Integer ancienneVal2 = (Integer) s.obj;

			//On compare les deux valeurs après une certaine période 
			if (ancienneVal1 != ancienneVal2) {
				System.out.println("Il y a une erreur de lecture");
			}
		
            // unlock the object
            s.unlock();
        }
		System.out.println("Lecture bien effectué, valeur lise: " + s.obj);
    }
}
