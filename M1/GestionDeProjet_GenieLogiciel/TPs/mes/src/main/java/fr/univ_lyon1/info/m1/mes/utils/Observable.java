package fr.univ_lyon1.info.m1.mes.utils;

import java.util.ArrayList;
import java.util.List;

public class Observable {
    private List<Observer> observers;

    public Observable() {
        this.observers = new ArrayList<>();
    }

    public void addObserver(final Observer obs) {
        observers.add(obs);
    }

    public void removeObserver(final Observer obs) {
        observers.remove(obs);
    }

    public void notifyObservers() {
        List<Observer> list = new ArrayList<Observer>(observers);
        for (Observer obs : list) {
            obs.update();
        }
    }
}
