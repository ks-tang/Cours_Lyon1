using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class Monstre5 : MonoBehaviour
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
        
        data.maxHealth = 200;
        data.currentHealth = data.maxHealth;

        data.force = 20;

        Wolves = GameObject.Find("Perso").GetComponent<etatBattle2>().Wolves;
        Monstres = GameObject.Find("Perso").GetComponent<etatBattle2>().Monstres;

    }

    // Update is called once per frame
    void Update()
    {

        dir.x = Input.GetAxisRaw("Horizontal");
        dir.y = Input.GetAxisRaw("Vertical");

        rb.MovePosition(rb.position + dir * speed * Time.fixedDeltaTime);

        //SetParam();

        

        foreach(GameObject ennemi in Wolves)
        {
            CollisionEnnemi(ennemi);
        }
        
        //vérifie si mort
        estMort();

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
                    ennemi.GetComponent<Wolf5>().TakeDamage(data.force); //fais dégats
                    ennemi.GetComponent<Animator>().SetInteger("attacked",1); //animation des dégats
                    lastInterval = timeNow;
                }
                
            }
        } 

        

    } 


    //Si un monstre touche le personnage adverse alors il gagne
    private void OnCollisionEnter2D(Collision2D collision)
    {
        if(collision.gameObject.name == "Perso")
        {
            StartCoroutine(invocateur.GetComponent<Adversaire>().Lose2());
        }

        if(collision.gameObject.name == "pnj_3")
        {
            invocateur.SendMessage("Heal", gameObject);
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
            Monstres.Remove(gameObject);
            Destroy(gameObject);

        }
    }


    public float distance(GameObject perso1, GameObject perso2)
    {
        return Mathf.Sqrt( Mathf.Pow(perso1.transform.position.x - perso2.transform.position.x, 2) + Mathf.Pow(perso1.transform.position.y - perso2.transform.position.y, 2)  );

    }

}
