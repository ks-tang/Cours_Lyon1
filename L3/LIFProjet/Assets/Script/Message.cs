using System.Collections;
using System.Collections.Generic;
using UnityEngine;


public enum Action 
{
	deplacement,
	attaque
}

public class ContenuMessage 
{
	public int date;
	public Action action;
	public string detailsAction;
	
}

public class Message : MonoBehaviour
{
	public enum Performatif 
	{
		request,
		inform
	}

	public int idEmetteur;
	public int idReceveur;
	public Performatif performatif;
	public ContenuMessage contenuMessage;
	
	
	public Message(int _idEmetteur, int _idReceveur, Performatif _performatif, ContenuMessage _contenuMessage) 
	{
		idEmetteur = _idEmetteur;
		idReceveur = _idReceveur;
		performatif = _performatif;
		contenuMessage = _contenuMessage;
	}
}
