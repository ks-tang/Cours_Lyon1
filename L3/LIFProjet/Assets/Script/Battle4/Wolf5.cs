using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;

public class Wolf5 : MonoBehaviour
{
    public float speed = 5f;
    Rigidbody2D rb;         //composant rigidbody
    Vector2 dir;
    Animator anim;      //composant d'animation
    Data data;          //composant qui gere les PV

    public GameObject invocatrice;          //perso    
    
    public GameObject texte;                // texte qui affiche les points de vie
    public bool isDead = false;             //booleen mort
    public bool askedHelp = false;
	public bool collab = false;

    public float updateInterval = 3f;
	public double lastInterval;

    private List<GameObject> Wolves;
    private List<GameObject> Monstres;
    
    public GameObject Interrogation;


    // Start is called before the first frame update
    void Start()
    {
        rb = GetComponent<Rigidbody2D>();
        anim = GetComponent<Animator>();
        data = GetComponent<Data>();

        data.maxHealth = 100;               //initialisation des points de vie max
        data.currentHealth = data.maxHealth; //points de vie courant

        data.force = 30;                         //initialisation des dégats par attaque

        Wolves = GameObject.Find("Perso").GetComponent<etatBattle2>().Wolves;
        Monstres = GameObject.Find("Perso").GetComponent<etatBattle2>().Monstres;

    }

    // Update is called once per frame
    void Update()
    {
        
        dir.x = Input.GetAxisRaw("Horizontal");
        dir.y = Input.GetAxisRaw("Vertical");

        rb.MovePosition(rb.position + dir * speed * Time.fixedDeltaTime);

        
        foreach(GameObject ennemi in Monstres)
        {
            CollisionEnnemi(ennemi);
        }
        

        //SetParam();

        estMort();          // vérifie si le loup est mort
        
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
                    ennemi.GetComponent<Monstre5>().TakeDamage(data.force); // fais dégats
                    ennemi.GetComponent<Animator>().SetInteger("attacked",1); //l'ennemi active l'animation de "est attaqué"
                    lastInterval = timeNow;
                }
            }
        }
    } 

    //Fonction de gestion des collisions
    private void OnCollisionEnter2D(Collision2D collision)
    {
        //Si un loup touche le personnage adverse alors on gagne
        if(collision.gameObject.name == "pnj_3")
        {
            StartCoroutine(invocatrice.GetComponent<etatBattle2>().Victory());
        }

        //Si un loup touche son invocatrice alors elle le soigne
        if(collision.gameObject.name == "Perso")
        {
            invocatrice.SendMessage("Heal", gameObject);
        }

        
    }

    //fonction de mise a jour des points de vie en cas d'attaque
    public void TakeDamage(int damage)
    {
        data.currentHealth = data.currentHealth-damage;
    }

    //vérifie si les points de vie du loup sont à zéro --> mort
    public void estMort()
    {
        if(data.currentHealth <= 0)
        {
            isDead = true;
            gameObject.SetActive(false);  //desactive le monstre
            texte.SetActive(false);               // desactive l'affichage des pdv
            gameObject.transform.position = new Vector2(-1000,-1000);
            Wolves.Remove(gameObject);
            Destroy(gameObject);                //detruis le personnage
        }
    }

    /// Calcul de la distance entre deux objets
    public float distance(GameObject perso1, GameObject perso2)
    {
        return Mathf.Sqrt( Mathf.Pow(perso1.transform.position.x - perso2.transform.position.x, 2) + Mathf.Pow(perso1.transform.position.y - perso2.transform.position.y, 2)  );

    }

    //Fonction qui s'active lorsqu'un allié a besoin d'aide
    //Le loup actuel va attaquer la cible de son allié
    public void HelpReceiver(GameObject ami)
    {
        Debug.Log(gameObject + " RECOIT APPEL A L'AIDE DE " + ami);

        if(distance(gameObject, gameObject.GetComponent<Pathfinding.AIDestinationSetter3>().targetFinal.parent.gameObject) < 2)
        {
            Debug.Log("NE PEUT PAS AIDER, OCCUPEEE");
        } else {

            gameObject.GetComponent<Pathfinding.AIDestinationSetter3>().targetFinal = ami.GetComponent<Pathfinding.AIDestinationSetter3>().targetFinal;
            ///gameObject.GetComponent<Pathfinding.AIDestinationSetter3>().targetFinal.position += new Vector3(1,0,0);
            collab = true; 
            ami.GetComponent<Wolf5>().collab = true;
        }
    }

	

}
