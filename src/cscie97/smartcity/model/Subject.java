package cscie97.smartcity.model;

import cscie97.smartcity.controller.Observer;

import java.util.List;

/**
 * Author: Stephen Sheldon
 **/
public abstract class Subject {

    private List<Observer> observerList;

    /**
     * Register an observer with this subject.
     * @param o  The Observer object to register.
     */
    public void registerObserver(Observer o) {
        observerList.add(o);
    }

    /**
     * Remove an observer from watching this subject.
     * @param o  The observer to remove from watching the subject.
     */
    public void removeObserver(Observer o) {
        observerList.remove(o);
    }

    /**
     * Notify all the observers of a new SensorEvent.
     * @param event  The SensorEvent to notify observers of.
     */
    public void notifyObservers(SensorEvent event) {
        for (Observer observer : observerList) {
            observer.update(event);
        }
    }
}
