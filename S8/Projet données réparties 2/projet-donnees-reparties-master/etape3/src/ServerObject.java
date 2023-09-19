public class ServerObject {
    private Object obj;
    private int version;

    public ServerObject(Object obj){
        this.obj = obj;
        this.version = 0;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public int getVersion(){
        return this.version;
    }

    public Object getObject(){
        return this.obj;
    }

    public void setObject(Object obj) {
        this.obj = obj;
    }


    
}