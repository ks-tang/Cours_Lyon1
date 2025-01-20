using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.SceneManagement;
using UnityEngine.UI;

public class Wolf2 : MonoBehaviour
{
    public float speed = 5f;
    Rigidbody2D rb;
    Vector2 dir;
    Animator anim;
    Data data;

    public GameObject ennemi;
    public GameObject ennemi2;

    public GameObject invocatrice;          //perso
    public GameObject ami1;                 //loup allié
    //public GameObject texteHelp;            
    
    public GameObject texte;                // texte qui affiche les points de vie
    public bool isDead = false;             //booleen mort
    public int force;                       //dégats par attaque

    public float updateInterval = 3f;
	public double lastInterval;

    // Start is called before the first frame update
    void Start()
    {
        rb = GetComponent<Rigidbody2D>();
        anim = GetComponent<Animator>();
        data = GetComponent<Data>();

        //nbCompteurHelp = 1;

        data.maxHealth = 100;               //initialisation des points de vie max
        data.currentHealth = data.maxHealth; //points de vie courant

        force = 30;                         //initialisation des dégats par attaque
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

        //texteInvocation.GetComponent<Text>().text = "nb invocations restantes : " + nbCompteurHelp.ToString();
        

        //Detection de collision ennemi et attaque
        CollisionEnnemi(ennemi);
        CollisionEnnemi(ennemi2);
        

        //SetParam();

        needHelp();         //si le loup a besoin d'aide
        estMort();          // vérifie si le loup est mort

        End();              //vérifie si le combat est terminée
        
    }

    //fonction qui change l'animation selon la direction du personnage
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

    //fonction de cas de collision avec un ennemi
    public void CollisionEnnemi(GameObject ennemi)
    {
        float distance = this.distance(gameObject, ennemi); //distance avec l'ennemi
        float timeNow = Time.realtimeSinceStartup; //gestion du temps

        //met à zero l'animation de l'ennemi
        ennemi.GetComponent<Animator>().SetInteger("attacked",0);

        if(ennemi.activeSelf){

            if (distance < 2) // si ennemi assez proche
            {
                if( timeNow - lastInterval > updateInterval) // fais action toutes les temps défini
				{
                    ennemi.GetComponent<Monstre2>().TakeDamage(force); // fais dégats
                    ennemi.GetComponent<Animator>().SetInteger("attacked",1); //l'ennemi active l'animation de "est attaqué"
                    lastInterval = timeNow;
                }
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

    //fonction de mise a jour des points de vie en cas d'attaque
    public void TakeDamage(int damage)
    {
        data.currentHealth = data.currentHealth-damage;
    }

    //si le loup a perdu la moitié de ses points de vie alors demande une autre invocation
    public void needHelp()
    {
        if (data.currentHealth <= (data.maxHealth/2) && invocatrice.GetComponent<Character0>().nbCompteurInvoc > 0)
        {
            invocatrice.SendMessage("invoquer");                // demande l'invocation d'un autre loup
            ami1.SetActive(true);                               //active le loup ami
            ami1.GetComponent<Wolf2>().texte.SetActive(true);   //active l'affiche des pv du loup ami

            invocatrice.GetComponent<Character0>().nbCompteurInvoc--;  //décrémente le nb invocation restant
        }
    }
    // pour la prochaine scène : initialiser tous les loups dans le perso
    //au lieu de créer les variables dans Wolf2


    //vérifie si les points de vie du loup sont à zéro --> mort
    public void estMort()
    {
        if(data.currentHealth <= 0)
        {
            isDead = true;
            gameObject.SetActive(false);  //desactive le monstre
            //gameObject.GetComponent<Monstre>().enabled = false;  //desactive le script
            texte.SetActive(false);               // desactive l'affichage des pdv
            gameObject.transform.position = new Vector2(-1000,-1000);
        }
    }

    /// Calcul de la distance entre deux objets
    public float distance(GameObject perso1, GameObject perso2)
    {
        return Mathf.Sqrt( Mathf.Pow(perso1.transform.position.x - perso2.transform.position.x, 2) + Mathf.Pow(perso1.transform.position.y - perso2.transform.position.y, 2)  );

    }

    /// Vérifie si le combat est terminé (victoire des loups)
    public void End()
    {
        //si les deux monstres sont morts et le personnage courant loup est vivant
        if(ennemi.GetComponent<Monstre2>().isDead && ennemi2.GetComponent<Monstre2>().isDead)
        {
            if(gameObject.activeSelf)
            {
                rb.bodyType = RigidbodyType2D.Static;
                
                StartCoroutine("Victory");
            }
            
        }
    }
    
    //Si victoire alors attend 3 sec et charge la scène suivante
    IEnumerator Victory()
	{
		yield return new WaitForSeconds(3);
		SceneManager.LoadScene("Cave_2B");
	}



}
