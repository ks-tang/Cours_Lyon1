using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;

public class TargetDecision : MonoBehaviour
{

    private List<GameObject> Wolves;
    private List<GameObject> Monstres;

    // Start is called before the first frame update
    void Start()
    {
        

        Wolves = GetComponent<etatBattle>().Wolves;
        Monstres = GetComponent<etatBattle>().Monstres;

    }

    // Update is called once per frame
    void Update()
    {
        foreach(GameObject wolf in Wolves)
        {
            if(wolf.activeSelf)
            {
                

                //si il reste des ennemis à combattre
                if(Monstres.Count > 0)
                {

                    //attaque le plus faible
                    GameObject EnnemiPlusFaible = CeluiQuiALeMoinsDePV(Monstres);
                    wolf.GetComponent<Pathfinding.AIDestinationSetter3>().targetFinal = EnnemiPlusFaible.transform;

                    
                    //Si un groupe d'ennemis ont le meme nombre de PV et sont faibles
                    //on crée une liste de ces ennemis et le loup attaque le plus proche
                    List<GameObject> GroupeEnnemisFaible = CeuxQuiOntLeMoinsDePV(Monstres);
                    if(GroupeEnnemisFaible.Count > 0)
                    {
                        wolf.GetComponent<Pathfinding.AIDestinationSetter3>().targetFinal = LePlusProcheDe(GroupeEnnemisFaible, wolf).transform;
                    }
                    
                    //si l'ennemi a plus de PV que le loup
                    if(wolf.GetComponent<Pathfinding.AIDestinationSetter3>().targetFinal.GetComponent<Data>().currentHealth > wolf.GetComponent<Data>().currentHealth)
                    {
                        // le loup demande à un autre loup (le plus proche) de l'aider
                        GameObject loupPlusProche = LePlusProcheDe(Wolves, wolf);
                        loupPlusProche.GetComponent<Pathfinding.AIDestinationSetter3>().targetFinal = wolf.GetComponent<Pathfinding.AIDestinationSetter3>().targetFinal;

                        //si l'ennemi a moins de PV que les deux loup réunis
                        if(wolf.GetComponent<Data>().currentHealth + loupPlusProche.GetComponent<Data>().currentHealth 
                        < wolf.GetComponent<Pathfinding.AIDestinationSetter3>().targetFinal.GetComponent<Data>().currentHealth)
                        {
                            //les autres loups attaquent l'ennemi le plus proche d'eux
                            List<GameObject> AutresLoups = new List<GameObject>(Wolves);
                            AutresLoups.Remove(wolf);
                            AutresLoups.Remove(loupPlusProche);

                            List<GameObject> AutresMonstres = new List<GameObject>(Monstres);
                            AutresMonstres.Remove(wolf.GetComponent<Pathfinding.AIDestinationSetter3>().targetFinal.parent.gameObject);

                            foreach(GameObject loup in AutresLoups)
                            {
                                loup.GetComponent<Pathfinding.AIDestinationSetter3>().targetFinal = LePlusProcheDe(AutresMonstres,loup).transform;
                            }
                            
                        } else {
                            gameObject.SendMessage("invoquer");
                        }
                        
                    }


                } else {
                    //les loups attaquent le personnage adversaire
                    wolf.GetComponent<Pathfinding.AIDestinationSetter3>().targetFinal = GameObject.Find("pnj_3").transform;
                }



                needHelp(wolf);

                needHeal(wolf);
            
            }
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

    //fonction qui invoque un autre loup 
    private void needHelp(GameObject wolf)
    {
        //Si un des loups a perdu plus de la moitié de ses PV
        if(wolf.GetComponent<Data>().currentHealth < (wolf.GetComponent<Data>().maxHealth /2) && wolf.activeSelf)
        {
            if(!wolf.GetComponent<Wolf4>().askedHelp)
            {
                gameObject.SendMessage("invoquer");
                wolf.GetComponent<Wolf4>().askedHelp = true;
            }

        }
    }

    //fonction qui fait venir le loup vers le personnage
    private void needHeal(GameObject wolf)
    {
        //Si un des loups a perdu plus de 80% de ses PV
        if(wolf.GetComponent<Data>().currentHealth <= (wolf.GetComponent<Data>().maxHealth /4) && wolf.activeSelf)
        {
            wolf.GetComponent<Pathfinding.AIDestinationSetter3>().targetFinal = GameObject.Find("Perso").transform;

        }
    }


}
