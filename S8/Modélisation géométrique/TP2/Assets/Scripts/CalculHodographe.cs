using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class CalculHodographe : MonoBehaviour
{
    // Nombre de subdivision dans l'algo de DCJ
    public int NombreDeSubdivision = 3;
    // Liste des points composant la courbe de l'hodographe
    private List<Vector3> ListePoints = new List<Vector3>();
    // Donnees i.e. points cliqués

    public GameObject Donnees;
    public GameObject particle;

    //////////////////////////////////////////////////////////////////////////
    // fonction : DeCasteljauSub                                            //
    // semantique : renvoie la liste des points composant la courbe         //
    //              approximante selon un nombre de subdivision données     //
    // params : - List<float> X : abscisses des point de controle           //
    //          - List<float> Y : odronnees des point de controle           //
    //          - int nombreDeSubdivision : nombre de subdivision           //
    // sortie :                                                             //
    //          - (List<float>, List<float>) : liste des abscisses et liste //
    //            des ordonnées des points composant la courbe              //
    //////////////////////////////////////////////////////////////////////////
    (List<float>, List<float>) DeCasteljauSub(List<float> X, List<float> Y, int nombreDeSubdivision)
    {
        List<float> XSortie = new List<float>();
        List<float> YSortie = new List<float>();
        List<float> xR = new List<float>();
        List<float> yR = new List<float>();
        List<float> xQ = new List<float>();
        List<float> yQ = new List<float>();
        
        if (nombreDeSubdivision <= 0){
            return (XSortie, YSortie); 
        }
        else{

            xQ.Add(X[0]);
            yQ.Add(Y[0]);
            xR.Add(X[X.Count-1]);
            yR.Add(Y[Y.Count-1]);

            for(int i=X.Count-2 ; i >= 0; --i) {
                for(int j=0; j < i; j++){
                    X[j] = (float)(X[j+1]/2 + X[j]/2);
                    Y[j] = (float)(Y[j+1]/2 + Y[j]/2);     
                }

                xQ.Add(X[0]);
                yQ.Add(Y[0]);

                xR.Add(X[i]);
                yR.Add(Y[i]);

            }

            xR.Reverse();
            yR.Reverse();

            List<float> xQ1 = new List<float>();
            List<float> yQ1 = new List<float>();
            List<float> xR1 = new List<float>();
            List<float> yR1 = new List<float>();
            (xQ1, yQ1) = DeCasteljauSub(xQ, yQ, nombreDeSubdivision - 1);
            (xR1, yR1) = DeCasteljauSub(xR, yR, nombreDeSubdivision - 1);

            xQ1.AddRange(xR1);
            yQ1.AddRange(yR1);

            return (xQ1, yQ1);
        
    }
    }


    //////////////////////////////////////////////////////////////////////////
    // fonction : Hodographe                                                //
    // semantique : renvoie la liste des vecteurs vitesses entre les paires //
    //              consécutives de points de controle                      //
    //              approximante selon un nombre de subdivision données     //
    // params : - List<float> X : abscisses des point de controle           //
    //          - List<float> Y : odronnees des point de controle           //
    //          - float Cx : offset d'affichage en x                        //
    //          - float Cy : offset d'affichage en y                        //
    // sortie :                                                             //
    //          - (List<float>, List<float>) : listes composantes des       //
    //            vecteurs vitesses sous la forme (Xs,Ys)                   //
    //////////////////////////////////////////////////////////////////////////
    (List<float>, List<float>) Hodographe(List<float> X, List<float> Y, float Cx = 1.5f, float Cy = 0.0f)
    {
        List<float> XSortie = new List<float>();
        List<float> YSortie = new List<float>();

        int n = X.Count; 
        for (int i=0;i<X.Count-1;i++){
            XSortie.Add(n*(X[i+1] - X[i]));
            YSortie.Add(n*(Y[i+1] - Y[i]));
        }

        float t = 0.5f; 
        for (int ligne=0 ; ligne < YSortie.Count; ligne++) {
            for(int col=0; col < YSortie.Count - ligne -1; col++){
                YSortie[col] = (1 - t) * YSortie[col] + t * YSortie[col+1];
                XSortie[col] = (1 - t) * XSortie[col] + t * XSortie[col+1];
            }
        }
        
        return (XSortie, YSortie);
    }

    //////////////////////////////////////////////////////////////////////////
    //////////////////////////// NE PAS TOUCHER //////////////////////////////
    //////////////////////////////////////////////////////////////////////////
    void Start()
    {
        
    }

    void Update()
    {
        if (Input.GetKeyDown(KeyCode.Return))
        {
            Instantiate(particle, new Vector3(1.5f, -4.0f, 0.0f), Quaternion.identity);
            var ListePointsCliques = GameObject.Find("Donnees").GetComponent<Points>();
            if (ListePointsCliques.X.Count > 0)
            {
                List<float> XSubdivision = new List<float>();
                List<float> YSubdivision = new List<float>();
                List<float> dX = new List<float>();
                List<float> dY = new List<float>();
                
                (dX, dY) = Hodographe(ListePointsCliques.X, ListePointsCliques.Y);

                (XSubdivision, YSubdivision) = DeCasteljauSub(dX, dY, NombreDeSubdivision);
                for (int i = 0; i < XSubdivision.Count; ++i)
                {
                    ListePoints.Add(new Vector3(XSubdivision[i], -4.0f, YSubdivision[i]));
                }
            }

        }
    }

    void OnDrawGizmosSelected()
    {
        Gizmos.color = Color.blue;
        for (int i = 0; i < ListePoints.Count - 1; ++i)
        {
            Gizmos.DrawLine(ListePoints[i], ListePoints[i + 1]);
        }
    }
}
