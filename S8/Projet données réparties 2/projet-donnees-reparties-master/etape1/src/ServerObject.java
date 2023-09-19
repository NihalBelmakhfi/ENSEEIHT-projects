import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.*;
import java.util.concurrent.Semaphore;

/**
 * A ServerObject is caracterized by a unique identifiant and a shared object
 */
public class ServerObject implements Serializable, ServerObject_itf {

    private static enum Lock {
        NOLOCK, READLOCK, WRITELOCK
    };

    private int id;
    private Object obj;
    private Lock lock;

    private ArrayList<Client_itf> readers;
    private Client_itf writer;

    // To avoid synchronization problems, we use semaphores
    private final Semaphore unEcrivain = new Semaphore(1, true);
    private final Semaphore pasdEcrivain = new Semaphore(1, true);
    private final Semaphore MutexReadWrite = new Semaphore(1, true);

    public ServerObject(Object obj, int id) {
        this.obj = obj;
        this.id = id;
        this.lock = Lock.NOLOCK;
        this.readers = new ArrayList<Client_itf>();
        this.writer = null;
    }

    @Override
    public Object lock_read(Client_itf client) {
        try {
            this.MutexReadWrite.acquire();
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        }
        try {
            try {
                this.pasdEcrivain.acquire();
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }

            if (this.lock == Lock.WRITELOCK) {
                System.out.println("Server : reducing lock, activating read mode");
                this.obj = this.writer.reduce_lock(id);
                this.readers.add(this.writer);
            }

            readers.add(client);
            this.writer = null;
            this.lock = Lock.READLOCK;

            this.pasdEcrivain.release();
            this.MutexReadWrite.release();
        } catch (RemoteException exception) {
            System.out.println("Server : lock_read error");
            exception.printStackTrace();
        }

        System.out.println("Server : lock_read executed successfully");
        return obj;
    }

    @Override
    public Object lock_write(Client_itf client) {
        try {
            this.MutexReadWrite.acquire();
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        }
        try {
            this.unEcrivain.acquire();
        } catch (InterruptedException e1) {
            System.out.println("Exception dans lock_write, Interrupted ");
            e1.printStackTrace();
        }

        if (this.lock == Lock.WRITELOCK && this.writer != client) {
            try {
                System.out.println("Server : Invalidating writer");
                this.obj = writer.invalidate_writer(this.id);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        if (this.lock == Lock.READLOCK) {
            this.readers.remove(client);
            for (Client_itf reader : this.readers) {
                try {
                    reader.invalidate_reader(this.id);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }
        this.writer = client;
        this.lock = Lock.WRITELOCK;
        this.readers.clear();

        this.unEcrivain.release();
        this.MutexReadWrite.release();
        System.out.println("Server : lock_write executed successfully");
        return obj;
    }

}
