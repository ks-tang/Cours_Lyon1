using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;

public class TargetDecision2 : MonoBehaviour
{
    private List<GameObject> Wolves;
    private List<GameObject> Monstres;

    // Start is called before the first frame update
    void Start()
    {
        //initialisation de l'etat du combat
        Wolves = GameObject.Find("Perso").GetComponent<etatBattle2>().Wolves;
        Monstres = GameObject.Find("Perso").GetComponent<etatBattle2>().Monstres;
    }

    // Update is called once per frame
    void Update()
    {
        if(gameObject.activeSelf)
        {

            //si il reste des ennemis à combattre
            if(Monstres.Count > 0)
            {

                //On trouve l'ennemi le plus proche parmi ceux qui ont le moins de PV
                List<GameObject> GroupeEnnemisFaible = CeuxQuiOntLeMoinsDePV(Monstres);
                GameObject PlusProcheDesFaibles = LePlusProcheDe(GroupeEnnemisFaible, gameObject);
                
                //On trouve l'ennemi le plus proche (tout court)
                GameObject EnnemiPlusProche = LePlusProcheDe(Monstres, gameObject);

                //On choisit qui attaquer entre les deux selon la distance
                if(distance(PlusProcheDesFaibles, EnnemiPlusProche) < 2)
                {
                    gameObject.GetComponent<Pathfinding.AIDestinationSetter3>().targetFinal = PlusProcheDesFaibles.transform;
                } else {
                    gameObject.GetComponent<Pathfinding.AIDestinationSetter3>().targetFinal = EnnemiPlusProche.transform;
                }


                //si l'ennemi a plus de PV que le loup
                if(gameObject.GetComponent<Pathfinding.AIDestinationSetter3>().targetFinal.GetComponent<Data>().currentHealth > gameObject.GetComponent<Data>().currentHealth)
                {
                    //et qu'il a au moins un allié
                    if(nbWolvesInBattle(Wolves) >= 2 )
                    {
                        
                        // le loup demande à un allié (le plus proche) de l'aider
                        GameObject loupPlusProche = LePlusProcheDe(Wolves, gameObject);
                        loupPlusProche.SendMessage("HelpReceiver", gameObject);


                    } else {
                        //si l'ennemi a plus de PV que le loup et que le loup est seul
                        //demande à l'invocatrice d'invoquer un autre loup
                        GameObject.Find("Perso").SendMessage("invoquer");
                    }
                }    
				
				needHelp(gameObject);
				needHeal(gameObject); 

            } else {
                //si tous les monstres adverses sont morts
                //les loups attaquent le personnage adversaire
                gameObject.GetComponent<Pathfinding.AIDestinationSetter3>().targetFinal = GameObject.Find("pnj_3").transform;
            }


                
        }

        Debug.Log(gameObject + " VA EN DIRECTION DE " + gameObject.GetComponent<Pathfinding.AIDestinationSetter3>().targetFinal);

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
                if(distance(item,cible) > 0) tmp = item;
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

    //fonction qui invoque un autre loup 
    private void needHelp(GameObject wolf)
    {
        //Si un des loups a perdu plus de 80% de ses PV
        if(wolf.GetComponent<Data>().currentHealth < (wolf.GetComponent<Data>().maxHealth /5) && wolf.activeSelf)
        {
            if(!wolf.GetComponent<Wolf5>().askedHelp)
            {
                GameObject.Find("Perso").SendMessage("invoquer");
                wolf.GetComponent<Wolf5>().askedHelp = true;
            }

        }
    }

    //fonction qui fait venir le loup vers le personnage
    private void needHeal(GameObject wolf)
    {
        //Si un des loups a perdu plus de 60% de ses PV
        if(wolf.GetComponent<Data>().currentHealth <= (wolf.GetComponent<Data>().maxHealth *0.4) && wolf.activeSelf) // fct evitement qui se deplace de 2 cases au dessus
        {
            wolf.GetComponent<Pathfinding.AIDestinationSetter3>().targetFinal = GameObject.Find("Perso").transform;
            
        }
    }

    //Fonction qui retourne le nombre de loup actifs dans le combat
    private int nbWolvesInBattle(List<GameObject> list)
	{
		int result = 0;
		foreach (GameObject item in list)
		{
			if(item.activeSelf) result++;
		}
		return result;
	}

   
   
}
