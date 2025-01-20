using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class Combat : MonoBehaviour
{
	public Perso perso;
	
	public Message msg;
	
	public string answer;
    // Start is called before the first frame update
    void Start()
    {
        
    }

    // Update is called once per frame
    void Update()
    {
        action();
    }
	
	public void action()
	{
		if (!perso.estDispo)
			answer = "no";
			

		/*else if (perso.estDispo)
			if(msg.performatif == request)
				switch (msg.action)
				{
					case deplacement :
						answer = "Je me déplace en "; // + deplacement.x + " " + deplacement.y
						//movePerso(x, y);
						break;
						
					case attaque :
						answer = "J'attaque l'ennemi "; // + id target;
						//attack(target);
						break;
				}		*/
	}
	
	public void buildStrategy()
	{
		
	}
}
