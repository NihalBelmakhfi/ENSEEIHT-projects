import java.io.*;


public class SharedObject implements Serializable, SharedObject_itf {

    //Définition des attributs
    private static final long serialVersionUID = 1L;
    protected boolean accesPossible;
    protected int lock;
    // NL : no local lock := 0
    // RLC : read lock cached (not taken) := 1
    // WLC : write lock cached := 2
    // RLT : read lock taken := 3
    // WLT : write lock taken  := 4
    // RLT_WLC : read lock taken and write lock cached    := 5
    protected Object obj;
    protected int id;

//=================================================//
//  Définition des setters and getters de la classe//
//=================================================//
    
    public int getId() {
        return this.id;
    }
    public Object getObjet() {
        return this.obj;
    }

    public void setId(int id) {
        this.id = id;
    }
    public void setObjet(Object objet) {
        this.obj = objet;
    }

//=================================================//
//  Le constructeur de la classe				   //
//=================================================//

    public SharedObject(Object o, int id) {
        super();
        this.accesPossible = false;
        this.obj = o;
        this.lock = 0;
        this.id = id;
    }

//============================================================//
//  Méthode lock_read: obtention d'un verrou en écriture	  //
//============================================================//

    // invoked by the user program on the client node
    public void lock_read() {
        boolean modifier = false;
        synchronized (this) {
            while (this.accesPossible) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }        
            switch(lock) {
                case 0:
                    modifier = true;
                    lock = 3;
                    break;
                case 1:
                    lock = 3;
                    break;
                case 2:
                    lock = 5;
                    break;
                default:
                    break;
            }
            if (modifier) {
                obj = Client.lock_read(id);
            }
        }
	}

//============================================================//
//  Méthode lock_write: obtention d'un verrou en écriture	  //
//============================================================//

	// invoked by the user program on the client node
    public void lock_write() {
        boolean demander = false;
        synchronized (this) {
            while (this.accesPossible) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            switch (lock) {
                case 0:
                    demander = true;
                    lock = 4;
                    break;
                case 1:
                    demander = true;
                    lock = 4;
                    break;
                case 2:
                    lock = 4;
                    break;
                default:
                    break;
            }
        } 
        if (demander) {
            obj = Client.lock_write(id);
        }
    }

//============================================================//
//  Méthode libérer le verrou après un lock_read ou lock_write//
//============================================================//

    // invoked by the user program on the client node
    public synchronized void unlock() {
        switch(lock) {
            case 3:
                lock = 1;
                break;
            case 4:
                lock = 2;
                break;
            case 5:
                lock = 2;
                break;
            default:
                break;
        }
        try {
            notify();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//============================================================//
//  Méthode reduce_lock: passer du mode d'écriture en lecture //
//============================================================//

    // callback invoked remotely by the server
    public synchronized Object reduce_lock() {
        this.accesPossible = true;
        switch (lock) {
            case 4:
            while (lock == 4) {
                try{
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            case 2:
                lock = 1;
                break;
            case 5:
                lock = 3;
                break;
            default:
                break;
        }
        
        this.accesPossible = false;

        try {
            notify();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return obj;
    }

//============================================================//
//  Méthode invalidate_reader: invalider un lecteur			  //
//============================================================//

    // callback invoked remotely by the server
    public synchronized void invalidate_reader() {
        
        this.accesPossible = true;
        switch (lock) {
            case 3:
                while (lock == 3) {
                    try {
                        wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                
            case 1:
                lock = 0;
                break;
            default:
                break;
        }

        this.accesPossible = false;
        try {
            notify();
        } catch (Exception e) {
            e.printStackTrace();
        }   
    }

//============================================================//
//  Méthode invalidate_writer: invalider un écrivain		  //
//============================================================//

    public synchronized Object invalidate_writer() {
        this.accesPossible = true;
        switch (lock) {
            case 4:
                while (lock == 4) {
                    try {
                        wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            case 2:
                lock = 0;
                break;
            case 5:
                while (lock == 5) {
                    try {
                        wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                lock = 0;
                break;
            default:
                break;
        }

        this.accesPossible = false;
        try {
            notify();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return obj;
    }


}

