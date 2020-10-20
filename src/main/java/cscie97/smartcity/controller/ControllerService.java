package cscie97.smartcity.controller;

import cscie97.smartcity.ledger.Ledger;
import cscie97.smartcity.model.Observer;
import cscie97.smartcity.model.SensorEvent;

/**
 * Author: Stephen Sheldon
 **/
public class ControllerService implements Observer {

    /**
     * Singleton implementation of ControllerService.
     */
    private static ControllerService controllerService;

    /**
     * Reference to Ledger Service.
     */
    private Ledger ledger;


    private ControllerService() {

    }

    /**
     * Single Factory Method to instantiate a single instance of ControllerService.
     * @return  The ControllerService object that was created.
     */
    public static ControllerService getInstance() {
        if (controllerService == null) {
            controllerService = new ControllerService();
        }
        return controllerService;
    }

    @Override
    public void update(SensorEvent event) {
        // Go through the process of checking what kind of sensor event we have
    }

    public void setLedger(Ledger ledger) {
        this.ledger = ledger;
    }
}
