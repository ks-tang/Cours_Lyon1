using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;
using UnityEngine.SceneManagement;

public class scrollText : MonoBehaviour
{
	public float speed = 5f;
	
    // Start is called before the first frame update
    void Start()
    {
		
    }

    // Update is called once per frame
    void Update()
    {
		if(Input.GetKeyDown(KeyCode.Return))
			StartCoroutine("End");
		else if(GetComponent<RectTransform>().anchoredPosition.y < -120)
			transform.Translate(0, speed * Time.deltaTime, 0);
    }
	
	IEnumerator End()
	{
		yield return new WaitForSeconds(1);

		SceneManager.LoadScene("Village");
	}
}