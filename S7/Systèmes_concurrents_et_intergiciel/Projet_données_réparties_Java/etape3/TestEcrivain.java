//Le principe de ce test est de lancer N fois des lock_write sur un Shared Object s
//Ce test représente un seule client écrivain
//Donc on a écrit un script bash (executer.sh) qui permet de lancer ce test 10 fois
//C'est une simulation de 10 clients en compétition pour un seule shared object s
//A chaque fois qu'on obtient effectivement s en lockwrite, on incrémente sa valeur
//Comme ça a la fin, on peut savoir le nombre de requetes qui n'était ps pris en compte

//Par exemple si on choisi N = 1000, et on lance le test à partir de 10 terminaux, alors
//On doit avoir une valeur final de s de 10000
//Si on a que 9500 par exemple, alors 500 lock_write ont échoué et on a un problème.

public class TestEcrivain {

    public static void main(String argv[]) {
		
		if (argv.length != 1) {
			System.out.println("java TestEcrivain <nb d'écritures>");
			return;
		}
		int N = Integer.parseInt(argv[0]);

    	// initialize the system
		Client.init();
		
		// look up the ecrivain_i object in the name server
		// if not found, create it, and register it in the name server
		SharedObject s = Client.lookup("ecrivain_i");
		if (s == null) {
            Integer monint = 0;
			s = Client.create(monint);
			Client.register("ecrivain_i", s);
		}

        //On lock notre shared object en ecriture, on écrit quelque chose dedans, puis on le unlock
        for(int i =0 ; i< N ; i++) {
            // lock the object in write mode
            s.lock_write();
            Integer ancienneVal = (Integer) s.obj;
			s.obj = ancienneVal + 1;

			//System.out.println("Le nombre d'écriture courant:" + s.obj);
            // unlock the object
            s.unlock();
        }
		System.out.println("Le nombre d'écriture effectué est:" + s.obj);
    }
}
