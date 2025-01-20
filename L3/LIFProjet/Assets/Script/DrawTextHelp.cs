using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;

public class DrawTextHelp : MonoBehaviour
{

    public GameObject personnage;
    Text text;
    
    // Start is called before the first frame update
    void Start()
    {
        text = GetComponent<Text>();
        text.transform.position = new Vector2(personnage.transform.position.x, personnage.transform.position.y + 1);
    }

    // Update is called once per frame
    void Update()
    {
        text.transform.position = new Vector2(personnage.transform.position.x, personnage.transform.position.y + 1);
    }
}
