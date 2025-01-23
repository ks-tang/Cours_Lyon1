using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;

public class TargetDecisionMonster2 : MonoBehaviour
{

    private List<GameObject> Wolves;
    private List<GameObject> Monstres;


    // Start is called before the first frame update
    void Start()
    {
        Wolves = GameObject.Find("Perso").GetComponent<etatBattle2>().Wolves;
        Monstres = GameObject.Find("Perso").GetComponent<etatBattle2>().Monstres;
    }

    // Update is called once per frame
    void Update()
    {
        
        if(gameObject.activeSelf)
        {
                

            if(Wolves.Count > 0 )
            {
                
                //ennemi le plus faible
                GameObject EnnemiPlusFaible =  CeluiQuiALeMoinsDePV(Wolves);

                //ennemi le plus proche
                GameObject EnnemiPlusProche = LePlusProcheDe(Wolves, gameObject);

                //si distance négligeable entre les deux
                //attaque le plus faible sinon le plus proche
                if(distance(EnnemiPlusFaible, EnnemiPlusProche) < 2 )
                {
                    gameObject.GetComponent<Pathfinding.AIDestinationSetter3>().targetFinal = EnnemiPlusFaible.transform;
                } else {
                    gameObject.GetComponent<Pathfinding.AIDestinationSetter3>().targetFinal = EnnemiPlusProche.transform;
                }

                //si l'ennemi a plus de PV que lui
                if(gameObject.GetComponent<Pathfinding.AIDestinationSetter3>().targetFinal.GetComponent<Data>().currentHealth > gameObject.GetComponent<Data>().currentHealth)
                {
                    //demande à un autre monstre (le plus proche) de l'aider
                    GameObject MonstrePlusProche = LePlusProcheDe(Monstres, gameObject);
                    MonstrePlusProche.GetComponent<Pathfinding.AIDestinationSetter3>().targetFinal = gameObject.GetComponent<Pathfinding.AIDestinationSetter3>().targetFinal;

                }

            } else {
                //si tous les ennemis sont morts
                //attaque l'adversaire
                gameObject.GetComponent<Pathfinding.AIDestinationSetter3>().targetFinal = GameObject.Find("Perso").transform;
            }


            needHeal(gameObject);

        }
        
    }



    /// Calcul de la distance entre deux objets
    public float distance(GameObject perso1, GameObject perso2)
    {
        return Mathf.Sqrt( Mathf.Pow(perso1.transform.position.x - perso2.transform.position.x, 2) + Mathf.Pow(perso1.transform.position.y - perso2.transform.position.y, 2)  );

    }

    ///Fonction qui renvoie le personnage qui a le moins de PV dans une liste
    public GameObject CeluiQuiALeMoinsDePV (List<GameObject> list)
    {
        GameObject tmp = list[0];

        foreach(GameObject item in list)
        {
            if(item.GetComponent<Data>().currentHealth < tmp.GetComponent<Data>().currentHealth && item.activeSelf)
            {
                tmp = item;
            }
        }
        return tmp;
    }

    //Fonction qui retourne une liste des personnage qui ont le moins de PV
    //parmi la liste en argument
    public List<GameObject> CeuxQuiOntLeMoinsDePV (List<GameObject> list)
    {
        List<GameObject> resultat = new List<GameObject>();
        int tmp = CeluiQuiALeMoinsDePV(list).GetComponent<Data>().currentHealth;

        foreach(GameObject item in list)
        {
            if(item.GetComponent<Data>().currentHealth == tmp)
            {
                resultat.Add(item);
            }
        }
        return resultat;
    }


    ///Fonction qui calcule le personnage, parmi une liste, le plus proche d'une cible définie
    public GameObject LePlusProcheDe (List<GameObject> list, GameObject cible)
    {
        GameObject tmp = list[0];

        foreach(GameObject item in list)
        {
            if(distance(item, cible) < distance(tmp, cible) && item.activeSelf)
            {
                tmp = item;
            }
        }
        return tmp;
    }

    ///Fonction qui renvoie vrai si tous les éléments d'une liste ont le meme nombre de PV
    public bool PVegaux (List<GameObject> list)
    {
        int tmp1 = list[0].GetComponent<Data>().currentHealth;
        int tmp2 = CeluiQuiALeMoinsDePV(list).GetComponent<Data>().currentHealth;

        return (tmp1==tmp2);
    }


    //fonction qui fait venir le monstre vers le personnage
    private void needHeal(GameObject monstre)
    {
        //Si un des monstre a perdu plus de 75% de ses PV
        if(monstre.GetComponent<Data>().currentHealth <= (monstre.GetComponent<Data>().maxHealth /4) && monstre.activeSelf)
        {
            monstre.GetComponent<Pathfinding.AIDestinationSetter3>().targetFinal = GameObject.Find("pnj_3").transform;

        }
    }

}
