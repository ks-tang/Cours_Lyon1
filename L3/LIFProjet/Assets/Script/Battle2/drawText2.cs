using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;

public class drawText2 : MonoBehaviour
{
	public float speed = 5f;
	
	public GameObject objet;

	
    // Start is called before the first frame update
    void Start()
    {
        
		Text text = GetComponent<Text>();
		
		text.text = (objet.GetComponent <Data>().currentHealth).ToString();
		
		text.transform.position = new Vector2(objet.transform.position.x, objet.transform.position.y + 1);
    }

    // Update is called once per frame
    void Update()
    {
		Text text = GetComponent<Text>();
		
		text.text = (objet.GetComponent <Data>().currentHealth).ToString();
		
		text.transform.position = new Vector2(objet.transform.position.x, objet.transform.position.y + 1);
    }
}
