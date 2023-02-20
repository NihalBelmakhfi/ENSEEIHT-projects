package Main;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;



public class Window2 extends JPanel{
		
	private JFrame window;
	private View_Quest window_view;
	
	
	Quest_wind modele;
	
	public ViwQuestWindow(Quest modele) {
		this.modele = modele;
		window_view = new View_Quest(this.modele);
		
		//build la vue et fenetre principale
		this.window = new JFrame("Journal de quête");
		this.window.setLocation(100,200);
		
		//build controleur(gestion events)
		this.window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		Container contenu = this.window.getContentPane();
		contenu.setLayout(new BoxLayout(contenu, BoxLayout.PAGE_AXIS));
		
		contenu.add(this.window_view);
		
		//afficher fenetre
		this.window.pack();  //redimensionner 
		this.window.setVisible(true);//afficher
	}
}
