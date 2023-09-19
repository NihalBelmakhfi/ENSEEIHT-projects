import java.io.*;
import java.rmi.RemoteException;


public class SharedObject implements Serializable {

    private static final long serialVersionUID = 1L;
	private int id;
	protected Object obj;
    private int version;


    public SharedObject(Object valeur, int id, int version) {
        this.obj = valeur;
        this.id = id;
        this.version = version;
	}

    public SharedObject(Object valeur, int id) {
        this(valeur, id, 0);
	}

	public void update(int v, Object valeur, WriteCallback_itf wcb) {
        try {
            Client.monitor.feuVert(Client.getIdSite(),4); // ** Instrumentation
         	// ** attente quadruplée pour les ack, pour exhiber l'inversion de valeurs
            if (v > version) {
         	    version = v;
                obj = valeur;
            }
            wcb.response(); 

		} catch (Exception ex) {
            ex.printStackTrace();
        } 
	}

    public void reportValue(ReadCallback_itf rcb) {
        try {
            Client.monitor.feuVert(Client.getIdSite(),1); // ** Instrumentation
            rcb.response(version, obj);  
         	
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
            obj = Client.read(id);
            Client.monitor.signaler("TL",Client.getIdSite(),id); // ** Instrumentation
            return obj;
        } catch (RemoteException rex) {
            rex.printStackTrace();
            return null;
        }
    }

    public int getVersion() {
        return version;
    }

}