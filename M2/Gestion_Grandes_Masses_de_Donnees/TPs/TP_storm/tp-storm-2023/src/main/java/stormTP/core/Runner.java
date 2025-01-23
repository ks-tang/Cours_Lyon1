package stormTP.core;

import javax.json.Json;
import javax.json.JsonObjectBuilder;

public class Runner{
	
	long id = -1;
	String typeR = null;
	long top = -1;
	int position = -1;
	int nbDevant = -1;
	int nbDerriere = -1;
	int total = -1;
	
	String nom = "";
	
	
	public Runner(){
		
	}
	
	public Runner(long id, String name, String t, int before,  int total, int position, long top){
		this.id = id;
		this.typeR = typeR;
		this.nom = name;
		this.nbDevant = before;
		this.total = total;
		this.position = position;
		this.top = top;
	}


	public long getId() {
		return id;
	}


	public void setId(long id) {
		this.id = id;
	}


	public long getTop() {
		return top;
	}


	public void setTop(long top) {
		this.top = top;
	}


	public int getPosition() {
		return position;
	}


	public void setPosition(int position) {
		this.position = position;
	}


	public String getNom() {
		return nom;
	}


	public void setNom(String nom) {
		this.nom = nom;
	}



	public int getNbDevant() {
		return nbDevant;
	}


	public void setNbDevant(int nbDevant) {
		this.nbDevant = nbDevant;
	}


	public int getTotal() {
		return total;
	}


	public void setTotal(int total) {
		this.total = total;
	}
		
	
	
	
	
	public String getJSON_V1(){
		JsonObjectBuilder r = null;
		r = Json.createObjectBuilder();
		/* construction de l'objet JSON résultat */
		r.add("id", this.id);
		r.add("type", this.typeR);
		r.add("top", this.top);
		r.add("nom", this.nom);
		r.add("position", this.position);
        r.add("nbDevant", this.nbDevant);
        r.add("total", this.total);
       
        return r.build().toString();
	}
	
	

	
}
