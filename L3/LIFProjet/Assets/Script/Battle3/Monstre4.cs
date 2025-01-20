using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class Monstre4 : MonoBehaviour
{
    public float speed = 5f;
    Rigidbody2D rb;
    Vector2 dir;
    Animator anim;
    Data data;

    public GameObject invocateur;

    public GameObject text;
    public bool isDead = false;

    public float updateInterval = 3f;
	public double lastInterval;

    private List<GameObject> Wolves;
    private List<GameObject> Monstres;

    // Start is called before the first frame update
    void Start()
    {
        rb = GetComponent<Rigidbody2D>();
        anim = GetComponent<Animator>();
        data = GetComponent<Data>();
        
        data.maxHealth = 100;
        data.currentHealth = data.maxHealth;

        data.force = 20;

        Wolves = GameObject.Find("Perso").GetComponent<etatBattle>().Wolves;
        Monstres = GameObject.Find("Perso").GetComponent<etatBattle>().Monstres;
    }

    // Update is called once per frame
    void Update()
    {
        
        dir.x = Input.GetAxisRaw("Horizontal");
        dir.y = Input.GetAxisRaw("Vertical");

        //Vector2 currentPos = transform.position;
        //dir = new Vector2(ennemi1.transform.position.x, ennemi1.transform.position.y);

        rb.MovePosition(rb.position + dir * speed * Time.fixedDeltaTime);

        //transform.position = Vector2.MoveTowards(currentPos, dir, Time.deltaTime * speed);

        //SetParam();

        
        foreach(GameObject ennemi in Wolves)
        {
            CollisionEnnemi(ennemi);
        }
        
        //vérifie si mort
        estMort();

        //vérifie si fin du jeu (victoire monstres)
        //End();
    }

    void SetParam()
    {
        if(dir.x == 0 && dir.y == 0)
        {
            anim.SetInteger("direction",0);
        }
        else if(dir.x > 0)
        {
            anim.SetInteger("direction",1);
        }
        else if(dir.x < 0)
        {
            anim.SetInteger("direction",2);
        }
        else if(dir.y > 0)
        {
            anim.SetInteger("direction",3);
        }
        else if(dir.y < 0)
        {
            anim.SetInteger("direction",4);
        }
    }

    
    public void CollisionEnnemi(GameObject ennemi)
    {
        float distance = this.distance(gameObject, ennemi); //distance avec l'ennemi
        float timeNow = Time.realtimeSinceStartup; //gestion du temps

        //désactive l'animation de l'attaque ennemi
        ennemi.GetComponent<Animator>().SetInteger("attacked",0);
    
        if(ennemi.activeSelf)
        {
            if( timeNow - lastInterval > updateInterval) // fais action toutes les temps défini
			{
                if (distance < 2) // si ennemi assez proche
                {
                ennemi.GetComponent<Wolf4>().TakeDamage(data.force); //fais dégats
                ennemi.GetComponent<Animator>().SetInteger("attacked",1); //animation des dégats
                lastInterval = timeNow;
                }
                
            }
        } 

        

    } 

    /*
    private void OnCollisionEnter2D(Collision2D collision)
    {
        float timeNow = Time.realtimeSinceStartup; //gestion du temps

        Wolf3 wolf = collision.transform.GetComponent<Wolf3>();

        //met à zero l'animation de l'ennemi
        wolf.GetComponent<Animator>().SetInteger("attacked",0);

        if( timeNow - lastInterval > updateInterval) // fais action toutes les temps défini
		{
            wolf.TakeDamage(data.force);
            wolf.GetComponent<Animator>().SetInteger("attacked",1); //animation des dégats
            lastInterval = timeNow;
        }
        
  
    } 
    */

    //Si un monstre touche le personnage adverse alors il gagne
    private void OnCollisionEnter2D(Collision2D collision)
    {
        if(collision.gameObject.name == "Perso")
        {
            StartCoroutine(invocateur.GetComponent<Adversaire>().Lose());
        }
    }


    public void TakeDamage(int damage)
    {
        data.currentHealth = data.currentHealth-damage;
    }

    public void estMort()
    {
        if(data.currentHealth <= 0)
        {
            isDead = true;
            gameObject.SetActive(false);  //desactive le monstre
            text.SetActive(false);               // desactive l'affichage des pdv
            gameObject.transform.position = new Vector2(-1000,-1000);
            //Destroy(gameObject);
            Monstres.Remove(gameObject);

        }
    }


    public float distance(GameObject perso1, GameObject perso2)
    {
        return Mathf.Sqrt( Mathf.Pow(perso1.transform.position.x - perso2.transform.position.x, 2) + Mathf.Pow(perso1.transform.position.y - perso2.transform.position.y, 2)  );

    }

}
