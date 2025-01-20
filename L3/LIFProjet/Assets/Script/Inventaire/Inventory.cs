using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;
using UnityEngine.SceneManagement;

public class Inventory : MonoBehaviour
{
	
	public List<Item> content = new List<Item>(); //liste du contenu de notre tableau
	
	public int contentId = 0;
	
	public Image itemUIImage;
	
	public List<Image> tabUIImage = new List<Image>();
	
	public static Inventory instance;
	
	public static Scene currentScene;
	
	
	private void Awake()
    {
        if(instance != null)
        {
            Debug.LogWarning("Il y a plus d'une instance de Inventory dans la scène");
            return;
        }

        instance = this;
			
		//DontDestroyOnLoad(this.gameObject);
    }
	
    // Start is called before the first frame update
    void Start()
    {
		UpdateInventoryUI();
    }

    // Update is called once per frame
    void Update()
    {
        
    }
	
	public void AddItem(Item item) 
	{
		bool ok =  false; //teste si on a trouvé dans notre tableau l'item
		if (content.Count == 0) 
		{
			content.Add(item);
			content[0].quantite++;
		}
		else 
		{
			int i = 0;
			
			while (i < content.Count || !ok) 
			{
				if(content[i].id == item.id) 
				{
					content[i].quantite++;
					ok = true;
				}
				i++;
			}
		}
		
		if (!ok)
		{
			content.Add(item);
			content[content.Count - 1].quantite++;
		}			
	}
	
	public void UseItem()
	{
		if (content.Count == 0)
			return;
			
		Item currentItem = content[contentId];
		content.Remove(currentItem);
		NextItem();
	}
	
	public void NextItem()
	{
		if (content.Count == 0)
			return;
			
		if(contentId < content.Count - 1)
			contentId++;
		else
			contentId = 0;				
			
		UpdateInventoryUI();
	}
	
	public void PreviousItem()
	{
		if (content.Count == 0)
			return;
			
		if(contentId > 0)
			contentId--;
		else 
			contentId = content.Count - 1;
		
		UpdateInventoryUI();
	}
	
	public void UpdateInventoryUI()
	{	
		/*if (content.Count > 0)
			itemUIImage.sprite = content[contentId].image;
		else
			itemUIImage.sprite = null;*/
			
		for (int i = 0; i < content.Count; i++) {
			tabUIImage[i].sprite = content[i].image;
		}
	}
	
	public void GoToInventory()
	{
		dataToStore.instance.Save(); 
		SceneManager.LoadScene("Inventaire");
	}
	
	public void ReturnToCurrentScene()
	{	
		SceneManager.LoadScene(dataToStore.instance.sceneName);
	}
}
