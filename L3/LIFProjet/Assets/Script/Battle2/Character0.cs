using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.SceneManagement;
using UnityEngine.UI;

public class Character0 : MonoBehaviour
{
    public float speed = 5f;
	
	Rigidbody2D rb;
	
	Vector2 dir;
	
	Animator anim;

	public GameObject quest;

	public int nbCompteurInvoc;				//,n invocations restantes
	public GameObject texteInvocation;		//texte nb invocations restantes
	public GameObject texteHelp;			//texte d'invocation

	
	// Start is called before the first frame update
    void Start()
    {
        rb = GetComponent<Rigidbody2D>();
		
		anim = GetComponent<Animator>();

		nbCompteurInvoc = 1;				//initialisation nb invocations possibles
    }

    // Update is called once per frame
    void Update()
    {
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
	}

	//fonction d'activation du texte d'invocation
	public void invoquer()
	{
		texteHelp.SetActive(true);
		StartCoroutine("Help");
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

	IEnumerator Help()
	{
		yield return new WaitForSeconds(2);
		texteHelp.SetActive(false);
        
	}

}
