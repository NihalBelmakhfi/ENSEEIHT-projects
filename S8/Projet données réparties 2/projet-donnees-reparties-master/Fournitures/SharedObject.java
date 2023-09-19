import java.io.*;
import java.util.concurrent.Semaphore;
import java.util.Set;
import java.rmi.RemoteException;


public class SharedObject implements Serializable, SharedObject_itf {

    private static final long serialVersionUID = 1L;


    public void update(int v, Object valeur, WriteCallback wcb) {
        try {
            Client.monitor.feuVert(Client.getIdSite(),4); // ** Instrumentation
         	// ** attente quadruplée pour les ack, pour exhiber l'inversion de valeurs
         	// getIdSite identique à getSite, mais non Remote
         	
         	// suite de la méthode update... 
         	
    }

    public void reportValue(ReadCallback rcb) {
        try {
            Client.monitor.feuVert(Client.getIdSite(),1); // ** Instrumentation
            
         	// suite de la méthode reportValue... 
         	
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    // invoked by the user program on the client node
    // passage par Client pour que les écritures soient demandées en séquence sur le site
    public void write(Object o) {
        try {
            Client.monitor.signaler("DE",Client.getIdSite(),id); // ** Instrumentation
            Client.write(id,o);
            Client.monitor.signaler("TE",Client.getIdSite(),id); // ** Instrumentation
        } catch (RemoteException rex) {
            rex.printStackTrace();
        }
    }

    // pour simplifier (éviter les ReadCallBack actifs simultanés)
    // on évite les lectures concurrentes sur une même copie
    public synchronized Object read() {
        // déclarations méthode read....

        try {
            Client.monitor.signaler("DL",Client.getIdSite(),id); // ** Instrumentation
            
            //corps de la méthode read...
            
            Client.monitor.signaler("TL",Client.getIdSite(),id); // ** Instrumentation
            return obj;
        } catch (RemoteException rex) {
            rex.printStackTrace();
            return null;
        }
    }

}