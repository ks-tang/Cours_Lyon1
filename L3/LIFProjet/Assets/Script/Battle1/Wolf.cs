using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.SceneManagement;

public class Wolf : MonoBehaviour
{
    public float speed = 5f;
    Rigidbody2D rb;
    Vector2 dir;
    Animator anim;

    public GameObject ennemi;

    public int maxHealth = 100;
    public int currentHealth;
    public GameObject texte;
    public bool isDead = false;
    public int force = 30;

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

        if(ennemi.activeSelf){
            ennemi.GetComponent<Animator>().SetInteger("attacked",0);
            CollisionEnnemi(ennemi);
        }

        //SetParam();
        estMort();
        if(gameObject.activeSelf && ennemi.GetComponent<Monstre>().isDead)
        {
            rb.bodyType = RigidbodyType2D.Static;
            StartCoroutine("Victory");
        }

        
    }

    void SetParam()
    {
        if(dir.x == 0 && dir.y == 0)
        {
            anim.SetInteger("direction",0);
        }
        else if(dir.x > 0)
        {
            anim.SetInteger("direction",3);
        }
        else if(dir.x < 0)
        {
            anim.SetInteger("direction",4);
        }
        else if(dir.y > 0)
        {
            anim.SetInteger("direction",1);
        }
        else if(dir.y < 0)
        {
            anim.SetInteger("direction",2);
        }
    }

    public void CollisionEnnemi(GameObject ennemi)
    {
        float distance = this.distance(gameObject, ennemi); //distance avec l'ennemi
        float timeNow = Time.realtimeSinceStartup; //gestion du temps

        if (distance < 2) // si ennemi assez proche
        {
            if( timeNow - lastInterval > updateInterval) // fais action toutes les temps défini
				{
                    ennemi.GetComponent<Monstre>().TakeDamage(force); // fais dégats
                    ennemi.GetComponent<Animator>().SetInteger("attacked",1);
                    lastInterval = timeNow;
                }
        }
    } 

    /*
    private void OnCollisionEnter2D(Collision2D collision)
    {

        Monstre monstre = collision.transform.GetComponent<Monstre>();
        monstre.TakeDamage(35);

        //recul
        
        float diffX = gameObject.transform.position.x - ennemi.transform.position.x;
        float diffY = gameObject.transform.position.y - ennemi.transform.position.y;
        
        transform.position = new Vector2(transform.position.x+diffX, transform.position.y+diffY);

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
            texte.SetActive(false);               // desactive l'affichage des pdv

            ennemi.GetComponent<Animator>().SetInteger("attacked",0); //desactive l'attaque ennemi
            gameObject.transform.position = new Vector2(-1000,-1000);
        }
    }

    public float distance(GameObject perso1, GameObject perso2)
    {
        return Mathf.Sqrt( Mathf.Pow(perso1.transform.position.x - perso2.transform.position.x, 2) + Mathf.Pow(perso1.transform.position.y - perso2.transform.position.y, 2)  );

    }

    

    IEnumerator Victory()
	{
		yield return new WaitForSeconds(3);
		SceneManager.LoadScene("Cave_1B");
	}

}
