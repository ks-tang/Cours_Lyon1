using UnityEngine;

public class Coffre : MonoBehaviour
{
	private bool triggers;
	
	public Animator animator;
	
	public Item item;
	
	public GameObject texteOuverture;
    // Start is called before the first frame update
    void Start()
    {
        
    }

    // Update is called once per frame
    void Update()
    {
        if(triggers)
			OpenChest();
    }
	
	void OpenChest()
	{
		animator.SetTrigger("OpenChest"); 
		Inventory.instance.AddItem(item);
        Inventory.instance.UpdateInventoryUI();
		GetComponent<BoxCollider2D>().enabled = false;
		texteOuverture.SetActive(true);
	}
	
	private void OnTriggerEnter2D(Collider2D collision)
	{
		if(collision.CompareTag("Perso"))
			triggers = true;
	}
	
	private void OnTriggerExit2D(Collider2D collision)
	{
		if(collision.CompareTag("Perso"))
			triggers = false;
	}

}
