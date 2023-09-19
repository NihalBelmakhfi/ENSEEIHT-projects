using System.Collections;
using System.Collections.Generic;
using UnityEngine;


using System.Linq;
using System;
using UnityEngine.UI;

public class Trajectoire : MonoBehaviour
{

    //Listes des positions selon les différents axes :X,Y et Z
    List<float> X = new List<float>();      //Recuperer la position selon l'axe X
    List<float> Y = new List<float>();      //Recuperer la position selon l'axe Y
    List<float> Z = new List<float>();      //Recuperer la position selon l'axe Z

    //Liste des pas temporels
    List<float> T = new List<float>(); 
    List<float> tToEval = new List<float>(); 
    
    
    //Liste des quaternions representant l'orientation et la rotation dans l'espace tridimensionnel
    List<Quaternion> rotation = new List<Quaternion>();    //Recuperer la rotation 
    
    
    //Liste des positions des points interpolés
    List<float> Xres = new List<float>();           //La liste des nouveaux points interpoles selon l'axe des X
    List<float> Yres = new List<float>();           //La liste des nouveaux points interpoles selon l'axe des Y
    List<float> Zres = new List<float>();           //La liste des nouveaux points interpoles selon l'axe des Z

    // Pour le dessin de la courbe 
    private List<Vector3> P2DRAW = new List<Vector3>();

 
   // Pas d'échantillonnage 
    public float pas = 1/1000;
    //initialisation de i et j 
    int i = 0;
    int j = 0;

    float t = 0;
                 

    // Conteneur de la camera
    GameObject[] camera;
   // GameObject[] wolf;

    // Les différentes types de paramétrisation étudiées en TP1
    public enum EParametrisationType { Reguliere, Distance, RacineDistance, Tchebytcheff, None}
    //Parametrisation est par defaut de type Reguliere
    public EParametrisationType ParametrisationType = EParametrisationType.Reguliere;

    float compteur = 1.0f;       
    int v = 20; 

    //Parametrisation  Regulière
     (List<float>, List<float>) buildParametrisationReguliere(int nbElem, float pas)
    {
        // Vecteur des pas temporels
        List<float> T = new List<float>();
        // Echantillonage des pas temporels
        List<float> tToEval = new List<float>();

        // Construction des pas temporels
        for (int i = 0; i < nbElem; ++i) {
            T.Add(i);
            }

        // Construction des échantillons
        tToEval.Add(T.Min());
        int cp = 1;
        while (tToEval.Last() <= T.Max()) {
            tToEval.Add(T.Min() + cp * pas);
            cp++;
        }

        return (T, tToEval);
    }

     //Parametrisation Distance
    (List<float>, List<float>) buildParametrisationDistance(int nbElem, float pas)
    {
        // Vecteur des pas temporels
        List<float> T = new List<float>();
        // Echantillonage des pas temporels
        List<float> tToEval = new List<float>();

        // Construction des pas temporels
        // TODO !!
        //Il faut ajouter les T(i,j)
        T.Add(0);
        for (int i = 1; i < X.Count; ++i) {
            float dX = (X[i-1] - X[i]) * (X[i-1] - X[i]); 
            float dY = (Y[i-1] - Y[i]) * (Y[i-1] - Y[i]);
            T.Add(T.Last()+(float)Math.Sqrt(Math.Pow(dX, 2) + Math.Pow(dY, 2)));
        }

        // Construction des échantillons
        
         tToEval.Add(T.Min());
        int cp = 1;
        while (tToEval.Last() <= T.Max()) {
            tToEval.Add(T.Min() + cp*pas);
            cp++;
        }


        return (T, tToEval);
    }
    
    
    
    //Parametrisation Racine Distance
      (List<float>, List<float>) buildParametrisationRacineDistance(int nbElem, float pas)
    {
        // Vecteur des pas temporels
        List<float> T = new List<float>();
        // Echantillonage des pas temporels
        List<float> tToEval = new List<float>();

        // Construction des pas temporels
        // TODO !!
        //X.count = nbElem
        T.Add(0);
        for (int i = 1; i < X.Count; ++i) {
            float dX = (X[i-1] - X[i]) * (X[i-1] - X[i]); 
            float dY = (Y[i-1] - Y[i]) * (Y[i-1] - Y[i]);
            T.Add(T.Last() + (float)Math.Sqrt((float)Math.Sqrt(Math.Pow(dX, 2) + Math.Pow(dY, 2))));
        }


        // Construction des échantillons
        tToEval.Add(T.Min());
        int cp = 1;
        while (tToEval.Last() <= T.Max()) {
            tToEval.Add(T.Min() + cp*pas);
            cp++;
        }
        

        return (T, tToEval);
    }
    
    
    //Parametrisation Tchebycheff
    (List<float>, List<float>) buildParametrisationTchebycheff(int nbElem, float pas)
    {
        // Vecteur des pas temporels
        List<float> T = new List<float>();
        // Echantillonage des pas temporels
        List<float> tToEval = new List<float>();

        // Construction des pas temporels
        // TODO !!
          for (int i = 0; i < nbElem; ++i) {
            T.Add((float)Math.Cos(((2*i+1)*Math.PI)/(2*(nbElem-1) + 2)));
        }

        // Construction des échantillons
        // TODO !!
        
         tToEval.Add(T.Min());
        int cp = 1;
        while (tToEval.Last() <= T.Max()) {
            tToEval.Add(T.Min() + cp*pas);
            cp++;
        }

        return (T, tToEval);
    }
    

     // LAGRANGE
      private float lagrange(float x, List<float> X, List<float> Y)
    {
    //Interpoller Y en fct de X
        float Li = 1.0f;
        float val_x = 0.0f;
        for (int i = 0; i < X.Count; ++i) {
            Li = 1;
            for (int j =0; j < X.Count; ++j) {
                if (i != j) {
                    Li = Li * (x-X[j])/(X[i]-X[j]);
                }
            }
            val_x = val_x + Li*Y[i];
        }
        return val_x;
          
    }
    


    
    //Adaptation de la fonction en introduisant l'axe Z et en renvoyant une mise a jour des positions selon les differents axes X,Y et Z
    (List<float>,List<float>,List<float>) applyLagrangeParametrisation(List<float> X, List<float> Y, List<float> Z, List<float> T, List<float> tToEval)
    {
        List<float> Xnew = new List<float>();
        List<float> Ynew = new List<float>();
        List<float> Znew = new List<float>();

        for (int i = 0; i < tToEval.Count; ++i) {
       
            float xpoint = lagrange(tToEval[i], T, X);
            float ypoint = lagrange(tToEval[i], T, Y);
            float zpoint = lagrange(tToEval[i], T, Z);
            Vector3 pos = new Vector3(xpoint, ypoint, zpoint);    
            Xnew.Add(xpoint);
            Ynew.Add(ypoint);
            Znew.Add(zpoint);
            P2DRAW.Add(pos);
        }  

        return (Xnew,Ynew,Znew); 
    }



    // Start is called before the first frame update
    void Start()
    {
        // Récupération des points de la trajectoire
        GameObject[] listeSpheres = GameObject.FindGameObjectsWithTag("Sphere");

        // Récupération des objets caméra
        camera = GameObject.FindGameObjectsWithTag("MainCamera");

      //  wolf = GameObject.FindGameObjectsWithTag("Wolf");
        
        // Remplissage des listes X,Y,Z, rotation par les coordonnées des points de la trajectoire
        for (int i = 0; i < listeSpheres.Count() ; i++) {
            X.Add(listeSpheres[i].transform.position[0]);  
            Y.Add(listeSpheres[i].transform.position[1]);
            Z.Add(listeSpheres[i].transform.position[2]);
            rotation.Add(listeSpheres[i].transform.rotation);
        }

        // Assurer la fermeture de la courbe
        X.Add(listeSpheres[0].transform.position[0]);
        Y.Add(listeSpheres[0].transform.position[1]);
        Z.Add(listeSpheres[0].transform.position[2]);
        rotation.Add(listeSpheres[0].transform.rotation);

                
        switch (ParametrisationType)
        {
            case EParametrisationType.Reguliere:
                (T, tToEval) = buildParametrisationReguliere(X.Count, pas);
                (Xres,Yres,Zres)  = applyLagrangeParametrisation(X,Y,Z,T,tToEval);
                break;
                
            case EParametrisationType.Distance:
                (T, tToEval) = buildParametrisationReguliere(X.Count, pas);
                (Xres,Yres,Zres)  = applyLagrangeParametrisation(X,Y,Z,T,tToEval);
                break;
                
            
            case EParametrisationType.RacineDistance:
                (T, tToEval) = buildParametrisationReguliere(X.Count, pas);
                (Xres,Yres,Zres)  = applyLagrangeParametrisation(X,Y,Z,T,tToEval);
                break;
    
            case EParametrisationType.Tchebytcheff:
                (T, tToEval) = buildParametrisationTchebycheff(X.Count, pas);
                (Xres,Yres,Zres)  = applyLagrangeParametrisation(X,Y,Z,T,tToEval);
                break;               
        }  
    }

    // Update is called once per frame
    void Update()
    {
            compteur = compteur - Time.deltaTime*v;
            if (X.Count > 0 && compteur < 0)  {
            // Suivi des positions des spheres
            Vector3 p = new Vector3(Xres[i], Yres[i],Zres[i]);
            foreach (GameObject c in camera) {
                c.transform.position = p;
            }
           //transform.position = p;
            if (i < tToEval.Count - 1) {
              i++;
            }
            else {
               i = 0;
            }

            // Suivi des rotations des spheres
            t = (tToEval[i] - T[j]) / (T[j+1] - T[j]);
            t = Math.Min(t, 1);  //Limite t à une valeur maximale de 1
            Quaternion q = Quaternion.Slerp(rotation[j], rotation[j+1],t);

           


            if (tToEval[i] >= T[j+1]) {
                j = (j+1)%(T.Count-1);
            }
            foreach (GameObject c in camera) {
                c.transform.rotation = q;
            }

            // foreach (GameObject w in wolf) {
              //  Quaternion wo = Quaternion.Slerp(q, w.transform.rotation, t);
              //  foreach (GameObject c in camera) {
              //  c.transform.rotation = wo;
              //    }                
          //  }
            
            //transform.rotation = q;
            compteur = 1;
            }
        } 


    // Tracer dde la trajectoire en rouge 
    void OnDrawGizmosSelected()
    {
        Gizmos.color = Color.red;
        for(int i = 0; i < P2DRAW.Count - 1; ++i)
        {
            Gizmos.DrawLine(P2DRAW[i], P2DRAW[i+1]);

        }

        //La fermeture de courbe
        if (P2DRAW.Count > 0) {
            Gizmos.DrawLine(P2DRAW[P2DRAW.Count-1], P2DRAW[0]);
        }
    }
}
