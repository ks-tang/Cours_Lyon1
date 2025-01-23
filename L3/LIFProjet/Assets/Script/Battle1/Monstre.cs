using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class Monstre : MonoBehaviour
{
    public float speed = 5f;
    Rigidbody2D rb;
    Vector2 dir;
    Animator anim;

    public GameObject ennemi;

    public int maxHealth = 100;
    public int currentHealth;
    public GameObject text;
    public bool isDead = false;
    public int force = 20;

    public float updateInterval = 3f;
	public double lastInterval;

    // Start is called before the first frame update
    void Start()
    {
        rb = GetComponent<Rigidbody2D>();
        anim = GetComponent<Animator>();
        
        currentHealth = maxHealth;

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

        if(ennemi.activeSelf){
            ennemi.GetComponent<Animator>().SetInteger("attacked",0);
            CollisionEnnemi(ennemi);
        }
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

    
            if( timeNow - lastInterval > updateInterval) // fais action toutes les temps défini
			{
                if (distance < 2) // si ennemi assez proche
                {
                ennemi.GetComponent<Wolf>().TakeDamage(force); //fais 20 dégats
                ennemi.GetComponent<Animator>().SetInteger("attacked",1);
                lastInterval = timeNow;
                }
                
            }
            
    } 

    /*
    private void OnCollisionEnter2D(Collision2D collision)
    {
        
        Wolf wolf = collision.transform.GetComponent<Wolf>();
        wolf.TakeDamage(20);

        //recul
        
        float diffX = gameObject.transform.position.x - wolf.transform.position.x;
        float diffY = gameObject.transform.position.y - wolf.transform.position.y;
        
        transform.position = new Vector2(transform.position.x-diffX, transform.position.y-diffY);        
    } 
    */

    public void TakeDamage(int damage)
    {
        currentHealth = currentHealth-damage;
    }

    public void estMort()
    {
        if(currentHealth <= 0)
        {
            isDead = true;
            gameObject.SetActive(false);  //desactive le monstre
            //gameObject.GetComponent<Monstre>().enabled = false;  //desactive le script
            text.SetActive(false);               // desactive l'affichage des pdv

            ennemi.GetComponent<Animator>().SetInteger("attacked",0); //desactive l'attaque ennemi
            gameObject.transform.position = new Vector2(-1000,-1000);


        }
    }

    public float distance(GameObject perso1, GameObject perso2)
    {
        return Mathf.Sqrt( Mathf.Pow(perso1.transform.position.x - perso2.transform.position.x, 2) + Mathf.Pow(perso1.transform.position.y - perso2.transform.position.y, 2)  );

    }
    
}
