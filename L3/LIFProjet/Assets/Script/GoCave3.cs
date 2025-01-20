using UnityEngine;
using UnityEngine.SceneManagement;

public class GoCave3 : MonoBehaviour
{
    private void OnTriggerEnter2D(Collider2D collision)
	{
		if(collision.CompareTag("Perso"))
		{
			SceneManager.LoadScene("Cave_3");
		}
	}
}
