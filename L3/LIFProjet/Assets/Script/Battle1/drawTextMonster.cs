using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;

public class drawTextMonster : MonoBehaviour
{
	public float speed = 5f;
	
	public GameObject monstre;

	
    // Start is called before the first frame update
    void Start()
    {
        
		Text text = GetComponent<Text>();
		
		text.text = (monstre.GetComponent <Monstre>().currentHealth).ToString();
		
		text.transform.position = new Vector2(monstre.transform.position.x, monstre.transform.position.y + 1);
    }

    // Update is called once per frame
    void Update()
    {
		Text text = GetComponent<Text>();
		
		text.text = (monstre.GetComponent <Monstre>().currentHealth).ToString();
		
		text.transform.position = new Vector2(monstre.transform.position.x, monstre.transform.position.y + 2);
    }
}
