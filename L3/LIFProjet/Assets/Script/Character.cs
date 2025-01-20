using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.SceneManagement;
using UnityEngine.UI;

public class Character : MonoBehaviour
{
    public float speed = 5f;
	
	Rigidbody2D rb;
	
	Vector2 dir;
	
	Animator anim;

	public GameObject quest;

	public int nbCompteurInvoc;				//nb invocations restantes
	public GameObject texteInvocation;		//texte nb invocations restantes
	public GameObject texteHelp;			//texte d'invocation

	private List<GameObject> Wolves;
    private List<GameObject> Monstres;

	public float updateInterval = 3f;
	public double lastInterval;

	bool start;

	public int soin;
	
	// Start is called before the first frame update
    void Start()
    {
        rb = GetComponent<Rigidbody2D>();
		
		anim = GetComponent<Animator>();

		Wolves = GetComponent<etatBattle>().Wolves;
        Monstres = GetComponent<etatBattle>().Monstres;

		start = true;
		
		soin = 50;
    }

    // Update is called once per frame
    void Update()
    {
		init();

        dir.x = Input.GetAxisRaw("Horizontal");
		dir.y = Input.GetAxisRaw("Vertical");
		
		rb.MovePosition(rb.position + dir * speed * Time.fixedDeltaTime);
		
		SetParam();

		// mis a jour affichage nb invocations restantes
		texteInvocation.GetComponent<Text>().text = "nb invocations restantes : " + nbCompteurInvoc.ToString();
    }
	
	void SetParam()
	{
		if(dir.x == 0 && dir.y ==0) //Idle
		{
			anim.SetInteger("dir", 0);
		}
		else if(dir.x > 0) //Droite
		{
			anim.SetInteger("dir", 3);
		}
		else if(dir.x < 0) //Gauche
		{
			anim.SetInteger("dir", 2);
		}
		else if(dir.y > 0) //Up
		{
			anim.SetInteger("dir", 4);
		}
		else if(dir.y < 0) //Down
		{
			anim.SetInteger("dir", 1);
		}
	}

	private void OnTriggerEnter2D (Collider2D collision)
	{
		if (collision.gameObject.name == "pnj_0")
		{
			//Destroy(collision.gameObject.GetComponent<CircleCollider2D>());
			quest.SetActive(true);
			StartCoroutine("HideQuest");
		}

		if (collision.gameObject.name == "pnj_1")
		{
			Destroy(collision.gameObject.GetComponent<CircleCollider2D>());
			quest.SetActive(true);
			rb.bodyType = RigidbodyType2D.Static;
			StartCoroutine("HideQuest");
			StartCoroutine("StartBattle");
		}

		if (collision.gameObject.name == "pnj_2")
		{
			quest.SetActive(true);
			rb.bodyType = RigidbodyType2D.Static;
			StartCoroutine("HideQuest");
			StartCoroutine("StartBattle2");
			
		}

		if (collision.gameObject.name == "pnj_3")
		{
			quest.SetActive(true);
			rb.bodyType = RigidbodyType2D.Static;
			StartCoroutine("HideQuest");
			StartCoroutine("StartBattle3");
			
		}

		if (collision.gameObject.name == "pnj_4")
		{
			quest.SetActive(true);
			rb.bodyType = RigidbodyType2D.Static;
			StartCoroutine("HideQuest");
			StartCoroutine("StartBattle4");	
		}
		
		if (collision.gameObject.name == "Coffre")
		{
			quest.SetActive(true);
		}
	}

	private void init()
	{
		if (start)
		{
			nbCompteurInvoc = Wolves.Count -1;	//initialisation nb invocations possibles
			start = false;
		}
	}

	//fonction d'invocation d'un loup
	public void invoquer()
	{
		float timeNow = Time.realtimeSinceStartup; //gestion du temps
		if( timeNow - lastInterval > updateInterval) // fais action toutes les temps défini
		{
			if(nbWolvesInvocked(Wolves) < 3)
			{
			texteHelp.SetActive(true);
			StartCoroutine("Help");

			if(nbCompteurInvoc > 0)
			{
				int random; //variable pour choisir quel loup sera invoqué aléatoirement
				do{
					random = Random.Range(0, Wolves.Count); //entre les loups encore en vie
				} while (Wolves[random].activeSelf);		//et qui n'ont pas encore été invoqués

				Wolves[random].SetActive(true);				//invoque un loup
				Wolves[random].GetComponent<Wolf4>().texte.SetActive(true); //et son texte PV
				nbCompteurInvoc--;
				
			}
			}
		lastInterval = timeNow;
        }

	}

	public void Heal(GameObject wolf)
	{
		wolf.GetComponent<Data>().currentHealth += soin;
	}


	private int nbWolvesInvocked(List<GameObject> list)
	{
		int result = 0;
		foreach (GameObject item in list)
		{
			if(item.activeSelf) result++;
		}
		return result;
	}

	IEnumerator HideQuest()
	{
		yield return new WaitForSeconds(5);
		
		quest.SetActive(false);
	}

	IEnumerator StartBattle()
	{
		yield return new WaitForSeconds(3);
		SceneManager.LoadScene("Battle1");
	}

	IEnumerator StartBattle2()
	{
		yield return new WaitForSeconds(3);
		SceneManager.LoadScene("Battle2");
	}

	IEnumerator StartBattle3()
	{
		yield return new WaitForSeconds(3);
		SceneManager.LoadScene("Battle3");
	}

	IEnumerator StartBattle4()
	{
		yield return new WaitForSeconds(3);
		SceneManager.LoadScene("Battle4");
	}

	IEnumerator Help()
	{
		yield return new WaitForSeconds(2);
		texteHelp.SetActive(false);
        
	}

	

}
