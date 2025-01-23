using UnityEngine;
using UnityEngine.SceneManagement;

public class GoCave5 : MonoBehaviour
{
    private void OnTriggerEnter2D(Collider2D collision)
	{
		if(collision.CompareTag("Perso"))
		{
			SceneManager.LoadScene("Cave_5");
		}
	}
}
