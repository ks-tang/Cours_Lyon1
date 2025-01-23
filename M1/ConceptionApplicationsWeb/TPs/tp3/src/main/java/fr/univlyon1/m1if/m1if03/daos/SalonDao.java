package fr.univlyon1.m1if.m1if03.daos;

import fr.univlyon1.m1if.m1if03.classes.Salon;
import fr.univlyon1.m1if.m1if03.classes.User;

import javax.naming.NameNotFoundException;
import java.io.Serializable;

import java.util.List;
import java.util.ArrayList;


public class SalonDao extends AbstractMapDao<Salon> {

    @Override
    protected Serializable getKeyForElement(Salon element) {
        return element.getId();
    }

    public Salon findByName(String name) throws NameNotFoundException {
        for(Salon salon: this.collection.values()) {
            if(salon.getNom().equals(name)) {
                return salon;
            }
        }
        throw new NameNotFoundException(name);
    }

    public List<Salon> getSalonsByOwner(User user) {
        List<Salon> salonsFromUser = new ArrayList<>();
        for(Salon salon : findAll()) {
            if(salon.getOwner().equals(user.getLogin())) {
                salonsFromUser.add(salon);
            }
        }
        return salonsFromUser;
    }

    public List<Salon> getSalonsByMember(User user) {
        List<Salon> salonsFromMember = new ArrayList<>();
        for(Salon salon : findAll()) {
            if(salon.hasMembre(user)) {
                salonsFromMember.add(salon);
            }
        }
        return salonsFromMember;
    }

    public List<Salon> getSalonsByNonMember(User user) {
        List<Salon> salonsFromNonMember = new ArrayList<>();
        for(Salon salon : findAll()) {
            if(!salon.hasMembre(user)) {
                salonsFromNonMember.add(salon);
            }
        }
        return salonsFromNonMember;
    }
    
}
