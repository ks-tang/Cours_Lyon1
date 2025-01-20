using UnityEngine;
using UnityEngine.SceneManagement;

public class LoadScene1 : MonoBehaviour
{
    private void OnTriggerEnter2D(Collider2D collision)
	{
		if(collision.CompareTag("Perso"))
		{
			SceneManager.LoadScene("Combat");
		}
	}
}

