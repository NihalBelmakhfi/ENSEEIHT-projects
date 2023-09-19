public interface SharedObject_itf {
	public void lock_read();

	public void lock_write();

	public void unlock();

	public void invalidate_reader();

	public Object invalidate_writer();

	public int getId();

	public Object getObjet();

	public void setId(int id);

	public void setObjet(Object objet);
}