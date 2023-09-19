import java.io.*;

public class SharedObject implements Serializable, SharedObject_itf {

    private static final long serialVersionUID = 1L;
    private boolean restrictedAccess;
    private Lock lock;

    public static enum Lock {
        NL, RLC, WLC, RLT, WLT, RLT_WLC
    };

    protected Object obj;
    protected int id;

    public SharedObject(Object o, int id) {
        super();
        this.restrictedAccess = false;
        this.obj = o;
        this.lock = Lock.NL;
        this.id = id;
    }

    @Override
    public int getId() {
        return this.id;
    }

    @Override
    public Object getObjet() {
        return this.obj;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    @Override
    public void setObjet(Object objet) {
        this.obj = objet;
    }

    public void setLock(Lock lock) {
        this.lock = lock;
    }

    @Override
    public void lock_read() {
        boolean changed = false;
        synchronized (this) {
            while (this.restrictedAccess) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            switch (lock) {
                case NL:
                    changed = true;
                    lock = Lock.RLT;
                    break;
                case RLC:
                    lock = Lock.RLT;
                    break;
                case WLC:
                    lock = Lock.RLT_WLC;
                    break;
                default:
                    break;
            }
            if (changed) {
                obj = Client.lock_read(id);
            }
        }
    }

    @Override
    public void lock_write() {
        boolean requesting = false;
        synchronized (this) {
            while (this.restrictedAccess) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            switch (lock) {
                case NL:
                    requesting = true;
                    lock = Lock.WLT;
                    break;
                case RLC:
                    requesting = true;
                    lock = Lock.WLT;
                    break;
                case WLC:
                    lock = Lock.WLT;
                    break;
                default:
                    break;
            }
        }
        if (requesting) {
            obj = Client.lock_write(id);
        }
    }

    @Override
    public synchronized void unlock() {
        switch (lock) {
            case RLT:
                lock = Lock.RLC;
                break;
            case WLT:
                if (Client.publish(id, obj)) {
                    lock = Lock.RLC;
                } else {
                    lock = Lock.WLC;
                }
                break;
            case RLT_WLC:
                lock = Lock.WLC;
                break;
            default:
                break;
        }
        try {
            notify();
        } catch (IllegalMonitorStateException e) {
            e.printStackTrace();
        }
    }

    public synchronized Object reduce_lock() {
        this.restrictedAccess = true;
        switch (lock) {
            case WLT:
                while (lock == Lock.WLT) {
                    try {
                        wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            case WLC:
                lock = Lock.RLC;
                break;
            case RLT_WLC:
                lock = Lock.RLT;
                break;
            default:
                break;
        }

        this.restrictedAccess = false;

        try {
            notify();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return obj;
    }

    public synchronized void invalidate_reader() {

        this.restrictedAccess = true;
        switch (lock) {
            case RLT:
                while (lock == Lock.RLT) {
                    try {
                        wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            case RLC:
                lock = Lock.NL;
                break;
            default:
                break;
        }

        this.restrictedAccess = false;
        try {
            notify();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public synchronized Object invalidate_writer() {
        this.restrictedAccess = true;
        switch (lock) {
            case WLT:
                while (lock == Lock.WLT) {
                    try {
                        wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            case WLC:
                lock = Lock.NL;
                break;
            case RLT_WLC:
                while (lock == Lock.RLT_WLC) {
                    try {
                        wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                lock = Lock.NL;
                break;
            default:
                break;
        }

        this.restrictedAccess = false;
        try {
            notify();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return obj;
    }

}
