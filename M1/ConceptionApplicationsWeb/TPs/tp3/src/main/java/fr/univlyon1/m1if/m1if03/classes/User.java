package fr.univlyon1.m1if.m1if03.classes;

//import java.util.HashMap;
//import java.util.Map;
//import java.util.Set;

/**
 * Utilisateur du chat.<br>
 * Cette classe pourra être améliorée dans la suite de l'UE...
 */
public class User {
    private final String login;
    private String name;
    //private final Map<String, String> ownedSalons;

    public User(String login, String name) {
        this.login = login;
        this.name = name;
        //this.ownedSalons = new HashMap<>();
    }

    public String getLogin() {
        return login;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    //<editor-fold desc="Gestion des salons de l'utilisateur">
    /* 
    public void createSalon(String nom) {
        Salon newSalon = new Salon(nom, this.login);
        this.ownedSalons.put(nom, newSalon.getId());

    }
    
    public Set<String> getAllNomsSalons() {
        return this.ownedSalons.keySet();
    }

    public Salon getSalon(String nom) {
        return this.ownedSalons.get(nom);
    }

    public void removeSalon(String nom) {
        this.ownedSalons.remove(nom);
    }*/
    
    //</editor-fold>
}
