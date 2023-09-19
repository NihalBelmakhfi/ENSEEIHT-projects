import java.rmi.server.UnicastRemoteObject;

public class WriteCallback extends UnicastRemoteObject implements WriteCallback_itf {

    private int nbResponses;
    private final int NBRESPONSESMIN;

    /**
     * Constructeur de WriteCallback
     * @param nbResponsesMin entier positif représentant le nombre de réponses minimum attendues pour le bon fonctionnement (forcement plus de la moitié des clients)
     */
    public WriteCallback(int nbResponsesMin) throws java.rmi.RemoteException{
        super();
        this.nbResponses = 0;
        this.NBRESPONSESMIN = nbResponsesMin;
    }

    @Override
    public synchronized void response() throws java.rmi.RemoteException {
        System.out.println("Response to write received");
        this.nbResponses++;
        if (this.nbResponses >= this.NBRESPONSESMIN) {
            this.notify();
            System.out.println("Minimum number of responses (" + NBRESPONSESMIN +  ") for the write received");
        }
    }

    @Override
    public synchronized void waitResponses() throws java.rmi.RemoteException {
        while (this.nbResponses < this.NBRESPONSESMIN) {
            System.out.println("Waiting for enough (" + NBRESPONSESMIN + ") responses to end the write");
            try {
                this.wait();
            } catch (InterruptedException e) {
                System.out.println("WriteCallback interrupted while waiting for enough (" + NBRESPONSESMIN + ") responses to end the write");
            }
        }
    }

}