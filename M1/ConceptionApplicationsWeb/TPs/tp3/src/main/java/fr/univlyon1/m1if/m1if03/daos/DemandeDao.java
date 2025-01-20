package fr.univlyon1.m1if.m1if03.daos;

import fr.univlyon1.m1if.m1if03.classes.Demande;
import java.io.Serializable;
import javax.naming.NameAlreadyBoundException;
import java.util.List;
import java.util.ArrayList;

public class DemandeDao extends AbstractMapDao<Demande> {
    private static int entier = 0;
    
    @Override
    protected Serializable getKeyForElement(Demande element) {
        return element.getId();
    }

    public void createDemande(String user, String destinataire, String salon, String action) {
        try {
            this.add(new Demande(entier, user, destinataire, salon, action));
            entier++;
        } catch (NameAlreadyBoundException e) {
            System.out.println(e.toString());
        }
    }

    public void createDemande(Demande d) {
        try {
            Demande demande = new Demande(entier, d.getUser(), d.getDestinataire(), d.getSalon(), d.getAction());
            entier++;
            this.add(demande);
        } catch(NameAlreadyBoundException e) {
            System.out.println(e);
        }
    }

    public Demande construitDemande(String user, String destinataire, String salon, String action) {
        Demande d = new Demande(entier, user, destinataire, salon, action);
        return d;
    }

    public List<Demande> getDemandesByUser(String user) {
        List<Demande> demandesByUser = new ArrayList<>();
        for(Demande demande : findAll()) {
            if(user.equals(demande.getUser())) {
                demandesByUser.add(demande);
            }
        }
        return demandesByUser;
    }

    public List<Demande> getDemandesByDestinataire(String user) {
        List<Demande> demandesByDestinataire = new ArrayList<>();
        for(Demande demande : findAll()) {
            if(demande.getDestinataire().equals(user)) {
                demandesByDestinataire.add(demande);
            }
        }
        return demandesByDestinataire;
    }

    public List<Demande> getPendingDemandesByDestinataire(String user) {
        List<Demande> demandesByDestinataire = new ArrayList<>();
        for(Demande demande : findAll()) {
            if(demande.getDestinataire().equals(user) && demande.getState().equals("En cours")) {
                demandesByDestinataire.add(demande);
            }
        }
        return demandesByDestinataire;
    }

    
}

