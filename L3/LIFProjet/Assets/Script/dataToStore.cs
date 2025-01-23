using UnityEngine;
using UnityEngine.SceneManagement;

public class dataToStore : MonoBehaviour 
{
	public const int SIZE = 32;
	
    public string sceneName = "";
	
	//public int[] tabInventaire = new int[SIZE];

    private static string sceneNameKey = "SCENE_NAME";
	
	//private static int tabInventaireKey = "TAB_INVENTAIRE";
	
	public static dataToStore instance;
	
	private void Awake()
    {
        if(instance != null)
        {
            Debug.LogWarning("Il y a plus d'une instance de Inventory dans la scène");
            return;
        }

        instance = this;
    }
	
	public void Start()
	{
		Load();
	}
	
	public void Update()
	{
		if(Input.GetKeyDown(KeyCode.S))
			Save();
	}

    public void Save()
    {
		Scene currentScene = SceneManager.GetActiveScene();
		
		sceneName = currentScene.name;
		
        PlayerPrefs.SetString(sceneNameKey, sceneName);
		
        PlayerPrefs.Save();
    }

    public void Load()
    {
        if (PlayerPrefs.HasKey(sceneNameKey))
        {
            sceneName = PlayerPrefs.GetString(sceneNameKey);
        }
		/*if (PlayerPrefs.HasKey(tabInventaireKey))
        {
            tabInventaire = PlayerPrefs.GetInt(tabInventaireKey);
        }*/
    }

    public void Delete()
    {
        PlayerPrefs.DeleteAll();
    }
}
