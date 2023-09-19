import java.awt.*;
import java.awt.event.*;
import java.rmi.*;
import java.io.*;
import java.net.*;
import java.util.*;
import java.lang.*;
import java.rmi.registry.*;

public class Irc extends Frame {
	public TextArea text;
	public TextField data;
	SharedObject sentence;
	static String myName;
	private Label led;

	public static void main(String argv[]) {

		if (argv.length != 1) {
			System.out.println("java Irc <name>");
			return;
		}
		myName = argv[0];

		// initialize the system
		Client.init();

		// look up the IRC object in the name server
		// if not found, create it, and register it in the name server
		SharedObject s = Client.lookup("IRC");
		if (s == null) {
			s = Client.create(new Sentence());
			Client.register("IRC", s);
		}
		// create the graphical part
		Irc irc = new Irc(s);
		Client.setUpdater(new Updater_itf() {
			public void update() {
				irc.setLed(Color.red);
			}
		});
	}

	void setLed(Color c) {
		led.setForeground(c);
	}

	public Irc(SharedObject s) {

		setLayout(new FlowLayout());

		text = new TextArea(10, 60);
		text.setEditable(false);
		text.setForeground(Color.red);
		add(text);

		data = new TextField(60);
		add(data);

		Button writeButton = new Button("write");
		writeButton.addActionListener(new writeListener(this));
		add(writeButton);
		Button readButton = new Button("read");
		readButton.addActionListener(new readListener(this));
		add(readButton);
		Button subscribeButton = new Button("subscribe");
		subscribeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Client.subscribe(sentence.id);
			}
		});
		add(subscribeButton);
		Button unsubscribeButton = new Button("unsubscribe");
		unsubscribeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Client.unsubscribe(sentence.id);
			}
		});
		add(unsubscribeButton);

		led = new Label("•");
		led.setForeground(Color.green);
		add(led);

		setSize(470, 300);
		text.setBackground(Color.black);
		show();

		sentence = s;
	}
}

class readListener implements ActionListener {
	Irc irc;

	public readListener(Irc i) {
		irc = i;
	}

	public void actionPerformed(ActionEvent e) {

		// lock the object in read mode
		irc.sentence.lock_read();

		// invoke the method
		String s = ((Sentence) (irc.sentence.obj)).read();

		// unlock the object
		irc.sentence.unlock();

		// display the read value
		irc.text.append(s + "\n");
		irc.setLed(Color.green);

	}
}

class writeListener implements ActionListener {
	Irc irc;

	public writeListener(Irc i) {
		irc = i;
	}

	public void actionPerformed(ActionEvent e) {

		// get the value to be written from the buffer
		String s = irc.data.getText();

		// lock the object in write mode
		irc.sentence.lock_write();

		// invoke the method
		((Sentence) (irc.sentence.obj)).write(Irc.myName + " wrote " + s);
		irc.data.setText("");

		// unlock the object
		irc.sentence.unlock();

	}
}
