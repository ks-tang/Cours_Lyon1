using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.SceneManagement;

public class Adversaire : MonoBehaviour
{
    public GameObject textDefaite;

    private List<GameObject> Wolves;
    private List<GameObject> Monstres;

    private int soin = 70;

    // Start is called before the first frame update
    void Start()
    {
        Wolves = GameObject.Find("Perso").GetComponent<etatBattle>().Wolves;
        Monstres = GameObject.Find("Perso").GetComponent<etatBattle>().Monstres;
    }

    // Update is called once per frame
    void Update()
    {
        
    }

    public void Heal(GameObject monstre)
	{
        if(monstre.GetComponent<Data>().currentHealth < monstre.GetComponent<Data>().maxHealth)
        {
            monstre.GetComponent<Data>().currentHealth += soin;
        }
		
	}

    public IEnumerator Lose()
	{
        textDefaite.SetActive(true);
		yield return new WaitForSeconds(3);
		SceneManager.LoadScene("Battle3");
	}

    public IEnumerator Lose2()
	{
        textDefaite.SetActive(true);
		yield return new WaitForSeconds(3);
		SceneManager.LoadScene("Battle4");
	}
}
