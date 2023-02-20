import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;

import simplepdl.Process;
import reseauPetri.*;
import simplepdl.*;

public class Simplepdl2reseauPetri {

    //Construire les éléments du réseau de petri
    static ReseauPetriFactory factory;
    static PetriNet petri;
    		//On crée les places
    static Map<String, Place> Ready = new HashMap<String, Place>();
    static Map<String, Place> running = new HashMap<String, Place>();
    static Map<String, Place> Started = new HashMap<String, Place>();
    static Map<String, Place> finished = new HashMap<String, Place>();
    		//On crée les transitions
    static Map<String, Transition> Start = new HashMap<String, Transition>();
    static Map<String, Transition> Finish = new HashMap<String, Transition>();
    
    
    public static void main(String[] args) {
   //pas sure de packageinstance                          //On veut enregistrer le package simplepdl dans le registre d'Eclipse
    SimplepdlPackage packageInstanceSimplePDL = SimplepdlPackage.eINSTANCE;	
    
   //pas sure de packageinstance                                           //Enregistrer le package reseauPetri dans le registre d'Eclipse
    ReseauPetriPackage packageInstancePetriNet = ReseauPetriPackage.eINSTANCE;
    
    
   // Enregistrer l'extension ".xmi" pour pouvoir l'ouvrir avec "XMIResourceFactoryImpl"
 		Resource.Factory.Registry reg = Resource.Factory.Registry.INSTANCE;
 		Map<String, Object> m = reg.getExtensionToFactoryMap();
 		m.put("xmi", new XMIResourceFactoryImpl());
 		
 		
 	// Créer un objet resourceSetImpl qui contiendra une ressource EMF (le modèle)
 		ResourceSet resSet = new ResourceSetImpl();
 			
 	// Créer le modèle de sortie (PetriNet.xmi)
 		URI SortieURI = URI.createURI("out8.xmi");
 		Resource Sortie = resSet.createResource(SortieURI);
 		
 	// (le modèle) Créer un resourceSetImpl contenant une ressource EMF	
 		ResourceSet ressourceSetModel = new ResourceSetImpl();
 		
 	// Charger le modèle
 		URI URImodel = URI.createURI("My2.xmi");
 		Resource resource = ressourceSetModel.createResource(URImodel);
 		Resource simplepdl = resSet.getResource(URImodel, true);
 		
 		
 	// Récupération du premier élément du modèle (le racine)
 		Process process = (Process) simplepdl.getContents().get(0);
 		
 		
 		
 		
 		// Instanciation de la fabrique
	    factory = ReseauPetriFactory.eINSTANCE;
	    
	    // Créer le PetriNet
	    petri = factory.createPetriNet();
	    petri.setName(process.getName());
	    Sortie.getContents().add(petri);
	    
	    
	    
	    for (Object o : process.getProcessElements()) {
	    	
	    // On convertit WorkDefinition to Place
		if (o instanceof WorkDefinition) {
			WorkDef((WorkDefinition)o);
			}
			
	    // On convertit WorkSequence to Arc
		else if (o instanceof WorkSequence) {
			WorkSeq((WorkSequence)o);
			}

		// On convertit Ressource to Place
		else if (o instanceof Ressources) {
			ConvertRessources((Ressources)o);
			}
		}
	    
	 // On sauvegarde la ressource de sortie
	    try {
	    	Sortie.save(Collections.EMPTY_MAP);
	    } catch (Exception e) {
	    	e.printStackTrace();
	    }
	}
 	
    
    
    private static void ConvertRessources(Ressources ressources) {
		// On crée la Place Ressources
		Place PlaceRessource = factory.createPlace();

// Paramétrage des Places
		
		// On nomme la ressource
		PlaceRessource.setName("Ressource_" + ressources.getName());
		
		// Initialisation du nombre de jeton
		PlaceRessource.setNbJetons(ressources.getQuantity());
		
		// Ajout de la Place à mypetri
		petri.getPassages().add(PlaceRessource);
		
		// Liaison Ressource-WorkDefinition
		for (AllocationRessource d : ressources.getAllocationressource()) {
			
			// Ajout de l'arc entre la PlaceRessource et la transition start correspondante
			Arc arcDemande = factory.createArc();
			arcDemande.setSource(PlaceRessource);
			System.out.println(d.getWorkdefinition());
			arcDemande.setDestination(Start.get(d.getWorkdefinition().getName()));
			arcDemande.setJetonsConsom(d.getQuantiteRessources());
			petri.getArc().add(arcDemande);
			
			// Ajout de l'arc entre la PlaceRessource et la transition finish correspondante
			Arc arcRetour = factory.createArc();
			arcRetour.setSource(Finish.get(d.getWorkdefinition().getName()));
			arcRetour.setDestination(PlaceRessource);
			arcRetour.setJetonsConsom(d.getQuantiteRessources());
			petri.getArc().add(arcRetour);
		}
		
	}
    
    
    
    private static void WorkSeq(WorkSequence workSeq) {
		// Création de l'arc
		Arc arc = factory.createArc();
		
		switch (workSeq.getLinkType()) {
		case START_TO_START :
			arc.setSource(Started.get(workSeq.getPredecessor().getName()));
			arc.setDestination(Start.get(workSeq.getSuccessor().getName()));
			break;
		case START_TO_FINISH :
			arc.setSource(Started.get(workSeq.getPredecessor().getName()));
			arc.setDestination(Finish.get(workSeq.getSuccessor().getName()));
			break;
		case FINISH_TO_START :
			arc.setSource(finished.get(workSeq.getPredecessor().getName()));
			arc.setDestination(Start.get(workSeq.getSuccessor().getName()));
			break;
		case FINISH_TO_FINISH :
			arc.setSource(finished.get(workSeq.getPredecessor().getName()));
			arc.setDestination(Finish.get(workSeq.getSuccessor().getName()));
			break;
		default: break;
		
		}
		arc.setType(ArcType.READ_ARC);
		arc.setJetonsConsom(1);
		
		// Ajout de l'arc à petri
		petri.getArc().add(arc);
		
	}
    
    
    
    private static void WorkDef(WorkDefinition workDef) {
		// Création des Places
		Place PlaceReady = factory.createPlace();
		Place PlaceStarted = factory.createPlace();
		Place PlaceFinished = factory.createPlace();
		Place PlaceRunning= factory.createPlace();
		
		// Création des Transitions
		Transition TransitionStart = factory.createTransition();
		Transition TransitionFinish = factory.createTransition();
		
		// Création des Arcs
		Arc Arc_Ready_Start = factory.createArc();
		Arc Arc_Start_Started = factory.createArc();	
		Arc Arc_Start_Running = factory.createArc();
		Arc Arc_Running_Finish = factory.createArc();
		Arc Arc_Finish_Finished = factory.createArc();
		
			// Paramétrage des Places
		// Determination du nom des Places
		PlaceReady.setName(workDef.getName() + "_ready");
		PlaceRunning.setName(workDef.getName() + "_running");
		PlaceStarted.setName(workDef.getName() + "_started");
		PlaceFinished.setName(workDef.getName() + "_finished");
		
		
		// Initialisation du jeton des Places
		PlaceReady.setNbJetons(1);
		PlaceStarted.setNbJetons(0);
		PlaceRunning.setNbJetons(0);
		PlaceFinished.setNbJetons(0);

		
			// Paramétrage des Transitions
		// Determination du nom des Transitions
		TransitionStart.setName(workDef.getName() + "_start");
		TransitionFinish.setName(workDef.getName() + "_finish");

		
			// Paramétrage des Arcs
		// Determination de la source et la destination de chaque Arc
		Arc_Ready_Start.setSource(PlaceReady);
		Arc_Ready_Start.setDestination(TransitionStart);
		
		Arc_Running_Finish.setSource(PlaceRunning);
		Arc_Running_Finish.setDestination(TransitionFinish);
		
		Arc_Start_Started.setSource(TransitionStart);
		Arc_Start_Started.setDestination(PlaceStarted);
		
		Arc_Start_Running.setSource(TransitionStart);
		Arc_Start_Running.setDestination(PlaceRunning);
		
		Arc_Finish_Finished.setSource(TransitionFinish);
		Arc_Finish_Finished.setDestination(PlaceFinished);
		
		// Determination du type de chaque Arc
		Arc_Ready_Start.setType(ArcType.CLASSIC);
		Arc_Running_Finish.setType(ArcType.CLASSIC);
		Arc_Start_Started.setType(ArcType.CLASSIC);
		Arc_Start_Running.setType(ArcType.CLASSIC);
		Arc_Finish_Finished.setType(ArcType.CLASSIC);
		
		// Determination des jetons de chaque Arc
		Arc_Ready_Start.setJetonsConsom(1);;
		Arc_Running_Finish.setJetonsConsom(1);
		Arc_Start_Started.setJetonsConsom(1);
		Arc_Start_Running.setJetonsConsom(1);
		Arc_Finish_Finished.setJetonsConsom(1);
	
		
		// Ajout des Places à mypetri
		petri.getPassages().add(PlaceReady);
		petri.getPassages().add(PlaceStarted);
		petri.getPassages().add(PlaceRunning);
		petri.getPassages().add(PlaceFinished);
		
		// Ajout des Transitions à mypetri
		petri.getPassages().add(TransitionStart);
		petri.getPassages().add(TransitionFinish);
		
		// Ajout des Arcs à mypetri
		petri.getArc().add(Arc_Ready_Start);
		petri.getArc().add(Arc_Running_Finish);
		petri.getArc().add(Arc_Start_Started);
		petri.getArc().add(Arc_Start_Running);
		petri.getArc().add(Arc_Finish_Finished);
		
		// Mise à jour des maps
		Ready.put(workDef.getName(), PlaceReady);
	    Started.put(workDef.getName(), PlaceStarted);
	    running.put(workDef.getName(), PlaceRunning);
	    finished.put(workDef.getName(), PlaceFinished);
	    Start.put(workDef.getName(), TransitionStart);
	    Finish.put(workDef.getName(), TransitionFinish);
		
	}

}
    
    
    

