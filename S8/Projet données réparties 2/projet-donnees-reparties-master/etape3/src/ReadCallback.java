import java.rmi.server.UnicastRemoteObject;

public class ReadCallback extends UnicastRemoteObject implements ReadCallback_itf {

    private int nbResponses;
    private final int NBRESPONSESMIN;
    private Object value;
    private int version;

    /**
     * Constructeur de ReadCallback
     * @param nbResponsesMin entier positif représentant le nombre de réponses minimum attendues pour le bon fonctionnement (forcement plus de la moitié des clients)
     */
    public ReadCallback(int nbResponsesMin) throws java.rmi.RemoteException {
        super();
        this.nbResponses = 0;
        this.NBRESPONSESMIN = nbResponsesMin;
        version = -1;
    }

    @Override
    public synchronized void response(int version, Object value) throws java.rmi.RemoteException {
        this.nbResponses++;
        if (version > this.version){
            this.version = version;
            this.value = value;
        }
        if (this.nbResponses >= this.NBRESPONSESMIN){
            this.notify();
            System.out.println("Minimum number of responses (" + NBRESPONSESMIN +  ") for the read received");
        }
    }

    @Override
    public synchronized Object getValue() throws java.rmi.RemoteException {
        while (this.nbResponses < this.NBRESPONSESMIN){
            try {
                System.out.println("Waiting for enough (" + NBRESPONSESMIN + ") responses to end the read");
                this.wait();
            } catch (InterruptedException e) {
                System.out.println("ReadCallback interrupted while waiting for enough (" + NBRESPONSESMIN + ") responses to end the read");
            }
        }
        return this.value;
    }
}
