using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.SceneManagement;

public class etatBattle : MonoBehaviour
{

    public GameObject targetA;
    public GameObject targetB;
    public GameObject targetC;

    public GameObject wolfA;
    public GameObject wolfB;
    public GameObject wolfC;
    public GameObject wolfD;

    public List<GameObject> Wolves = new List<GameObject>();
    public List<GameObject> Monstres = new List<GameObject>();

    public GameObject textVictoire;
    //public GameObject textDefaite;

    // Start is called before the first frame update
    void Start()
    {
        Wolves.Add(wolfA);
        Wolves.Add(wolfB);
        Wolves.Add(wolfC);
        Wolves.Add(wolfD);

        Monstres.Add(targetA);
        Monstres.Add(targetB);
        Monstres.Add(targetC);
    }

    // Update is called once per frame
    void Update()
    {
        
    }

    


    //Si victoire alors attend 3 sec et charge la scène suivante
    public IEnumerator Victory()
	{
        textVictoire.SetActive(true);
		yield return new WaitForSeconds(3);
		SceneManager.LoadScene("Cave_3B");
	}

}

    
