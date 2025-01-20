
using UnityEngine;

[CreateAssetMenu(fileName = "Item", menuName = "Inventory/Item")]

public class Item : ScriptableObject
{
    public int id;
	
	public string name;
	
	public int quantite;
	
	public string description;
	
	public Sprite image;
	
	public int hp;
	
	public int dmg;
	
	public int res; // resistance
}
