using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;

public class drawText : MonoBehaviour
{
	public float speed = 5f;
	
	public GameObject wolf;

	
    // Start is called before the first frame update
    void Start()
    {
        
		Text text = GetComponent<Text>();
		
		text.text = (wolf.GetComponent <Wolf>().currentHealth).ToString();
		
		text.transform.position = new Vector2(wolf.transform.position.x, wolf.transform.position.y + 1);
    }

    // Update is called once per frame
    void Update()
    {
		Text text = GetComponent<Text>();
		
		text.text = (wolf.GetComponent <Wolf>().currentHealth).ToString();
		
		text.transform.position = new Vector2(wolf.transform.position.x, wolf.transform.position.y + 1);
    }
}
