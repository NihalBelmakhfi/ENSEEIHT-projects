import java.awt.*;
import java.awt.event.*;
import java.rmi.*;
import java.io.*;
import java.net.*;
import java.util.*;
import java.lang.*;
import java.rmi.registry.*;

public class Irc_synchrone extends Frame {
	public TextArea text;
	public TextField data;
	SharedObject sentence;
	static String myName;

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
		Irc_synchrone irc = new Irc_synchrone(s);
		Client.setUpdater(new Updater_itf() {
			public void update() {
				irc.sentence.lock_read();
				String s = ((Sentence) (irc.sentence.obj)).read();
				irc.sentence.unlock();
				irc.text.append(s + "\n");
			}
		});
	}

	public Irc_synchrone(SharedObject s) {

		setLayout(new FlowLayout());

		text = new TextArea(10, 60);
		text.setEditable(false);
		text.setForeground(Color.red);
		add(text);

		data = new TextField(60);
		add(data);

		Button writeButton = new Button("write");
		writeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Irc_synchrone irc = Irc_synchrone.this;
				String s = irc.data.getText();

				irc.sentence.lock_write();

				((Sentence) (irc.sentence.obj)).write(Irc_synchrone.myName + " wrote " + s);
				irc.data.setText("");

				irc.sentence.unlock();
			}
		});
		add(writeButton);
		Button readButton = new Button("read");

		readButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Irc_synchrone irc = Irc_synchrone.this;
				irc.sentence.lock_read();
				String s = ((Sentence) (irc.sentence.obj)).read();

				irc.sentence.unlock();

				irc.text.append(s + "\n");

			}
		});
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

		setSize(470, 300);
		text.setBackground(Color.black);
		show();

		sentence = s;
	}
}
