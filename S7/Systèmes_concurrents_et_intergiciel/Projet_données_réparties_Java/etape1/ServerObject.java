import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.*;
import java.util.concurrent.Semaphore;

public class ServerObject implements Serializable, ServerObject_itf{

    //Définition des attributs de la classe
    //Un ServerObject est responsable d'un seul SharedObject
    //Il est définit alors par son identifiant, 
    //et l'objet partagé dont il est responsable

    //L'identifiant du Server_Object
    protected int id;

    //L'objet partagé sous la supérvision du Server_Object
    private Object obj;

    //Pour un serveur, on a besoin de savoir si le verrou est en écriture 
    //ou en lecture ou il n'y a pas de verrou, alors, on définit une 
    //variable enumération qui spécifie le type de verrou en question
    public enum typeVerrou {NoLock, ReadLock, WriteLock};

    //On définit une variable de ce type
    private typeVerrou Verrou;

    //On a besoin de connaitre qui est le client qui était en ecriture
    //pour pouvoir lui envoyer une requete de reduce_lock() et le mettre en mode lecteur
    //Et on a besoin également des lecteurs, alors on définit une ArrayList des lecteurs
    //Et une variable pour l'écrivain puisqu'on a un seule ecrivain à la fois
    private ArrayList<Client_itf> mesLecteurs;

    private Client_itf monEcrivain;

    //Définition d'un sémaphore pour éviter des problèmes de synchronization
    private final Semaphore unEcrivain = new Semaphore(1,true);

    private final Semaphore pasdEcrivain = new Semaphore(1,true);

    private final Semaphore MutexReadWrite = new Semaphore(1,true);


//============================================================//
//  Constructeur de la classe Server						  //
//============================================================//

    //On définit le constructeur de la classe
    public ServerObject(Object obj, int id){
        this.obj = obj;
        this.id = id;
        //On n'a pas de verrou au début, donc on initialise le Verrou à NoLock
        this.Verrou = typeVerrou.NoLock;
        //On initialise la liste de lecteur
        this.mesLecteurs = new ArrayList<Client_itf>();
        //On initialise notre écrivain à null
        this.monEcrivain = null;
    }

//==============================================================================//
// Méthode lock_read: demande un verrou en lecture pour un so d'identifiant id  //
//==============================================================================//
    
    @Override
    public Object lock_read(Client_itf client) {
        //On définit une exclusion mutuelle entre l(aquisitio des lock_read et lock_write
        try {
            this.MutexReadWrite.acquire();
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        }
        try {
            //On décrémente le sémaphore pour éviter des problèmes de synchronization
            try {
                this.pasdEcrivain.acquire();
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }

            //le traitement dépends si on a un client qui écrit ou pas
            //On peut savoir ceci à partir de type de verrou
            if(this.Verrou == typeVerrou.WriteLock) {
                //On récupère l'écrivain
                Client_itf writer = monEcrivain;

                //Et puis on envoie une requete de reduce_lock(),
                //qui permet de passer vde mode écriture en lecture
                System.out.println("Server : reducing lock, activating read mode");
                this.obj = writer.reduce_lock(id);

                //On ajoute notre ecrivain aux lecteurs
                mesLecteurs.add(writer);
            }

            //Maintenant on ajoute le client en tous cas au liste des lecteurs
            mesLecteurs.add(client);

            //Et on change le type du Verrou, en réinitialisant la valeur de l'écrivai à null
            this.monEcrivain = null;
            this.Verrou = typeVerrou.ReadLock;

            //Maintenant on réincrémente le sémaphore
            this.pasdEcrivain.release();
            this.MutexReadWrite.release();
        }
        catch(RemoteException exception){
            System.out.println("Server : lock_read error");
            exception.printStackTrace();
        }

        //On retourne la version mise-à-jour de notre objet
        System.out.println("Server : lock_read executed successfully");
        return obj;
    }

//==============================================================================//
// Méthode lock_write: demande un verrou en écriture pour un so d'identifiant id//
//==============================================================================//

    @Override
    public Object lock_write(Client_itf client){
        //On définit une exclusion mutuelle entre l(aquisitio des lock_read et lock_write
        try {
            this.MutexReadWrite.acquire();
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        }
        //On décrémente le sémaphore pour éviter des problèmes de synchronization
        try {
            this.unEcrivain.acquire();
        } catch (InterruptedException e1) {
            System.out.println("Exception dans lock_write, Interrupted ");
            e1.printStackTrace();
        }

        //Le traitement effectué dépends du type de Verrou le moment ou on recoie la demande de lock_write
        //Dans le cas ou on a un Verrou en écriture, cela veut dire qu'on a déjà un écrivain
        //Alors on envoie un invalidate_writer à notre écrivain courant
        if(this.Verrou == typeVerrou.WriteLock && this.monEcrivain != client){
            //On récupère l'écrivain
            Client_itf writer = this.monEcrivain;
            //Et puis on envoie un invalidate_writer() à ce dernier
            try {
                System.out.println("Server : Invalidating writer");
                this.obj = writer.invalidate_writer(this.id);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        //Et dans le cas ou le Verrou est en lecture, 
        //on doit envoyer un invalidate_reader à tous les lecteurs
        if(this.Verrou == typeVerrou.ReadLock){
            //On enlève le client qui fait la demande de la liste de lecteurs
            this.mesLecteurs.remove(client);
            //On invalide tous les lecteurs dans la liste
            for(Client_itf unlecteur: this.mesLecteurs){
                try {
                    unlecteur.invalidate_reader(this.id);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }
        //Après ce traitement particulier des cas ou le Verrou est pris, on traite le cas général
        //On change l'écrivain
        this.monEcrivain = client;
        //On change le type de verrou
        this.Verrou = typeVerrou.WriteLock;
        //On vide notre liste de lecteurs puisqu'on a plus de lecteurs
        this.mesLecteurs.clear();

        //Maintenant on réincrémente le sémaphore
        this.unEcrivain.release();
        this.MutexReadWrite.release();
        //On retourne la version mise-à-jour de notre objet
        System.out.println("Server : lock_write executed successfully");
        return obj;
    }

    
}
