public interface ServerObject_itf {

    //Méthode pour que les clients peuvent demander un verrou en lecture d'un SharedObject
    //Le serveur est responsable de recevoir la demande lock_read du client et appelle 
    //cette méthode du ServerObject avec une référence au client afin de pouvoir communiquer 
    //avec lui par la suite dans le cas du besoin
    public Object lock_read(Client_itf client);

    //Méthode pour que les clients peuvent demander un verrou en écriture d'un SharedObject
    //Le serveur est responsable de recevoir la demande lock_write du client et appelle 
    //cette méthode du ServerObject avec une référence au client afin de pouvoir communiquer 
    //avec lui par la suite dans le cas du besoin
    public Object lock_write(Client_itf client);
    
}
