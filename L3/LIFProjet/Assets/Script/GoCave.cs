using UnityEngine;
using UnityEngine.SceneManagement;

public class GoCave : MonoBehaviour
{
    private void OnTriggerEnter2D(Collider2D collision)
	{
		if(collision.CompareTag("Perso"))
		{
			SceneManager.LoadScene("Cave_1");
		}
	}
}
