package cscie97.smartcity.model;

/**
 * Author: Stephen Sheldon
 **/
public interface Subject {


    /**
     * Register an observer with this subject.
     * @param o  The Observer object to register.
     */
    void registerObserver(Observer o);

    /**
     * Remove an observer from watching this subject.
     * @param o  The observer to remove from watching the subject.
     */
    void removeObserver(Observer o);

    /**
     * Notify all the observers of a new SensorEvent.
     * @param event  The SensorEvent to notify observers of.
     */
    void notifyObservers(SensorEvent event);
}
