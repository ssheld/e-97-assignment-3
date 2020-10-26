package cscie97.smartcity.controller;

import cscie97.smartcity.ledger.LedgerException;
import cscie97.smartcity.model.*;

import java.util.logging.Level;

/**
 * Author: Stephen Sheldon
 **/
public class PersonEntersCarCommand implements Command {

    /**
     * City ID
     */
    String cityId;

    /**
     * Person ID
     */
    String personId;

    /**
     * Car ID
     */
    String vehicleId;

    /**
     * Reference to model service
     */
    CityModelService modelService;

    /**
     * Constructor for PersonEntersCarCommand
     * @param event         Event object
     * @param modelService  Reference to model service
     */
    public PersonEntersCarCommand(SensorEvent event, CityModelService modelService) {
        this.cityId = event.getCityId();
        this.personId = event.getPersonId();
        this.vehicleId = event.getDeviceId();
        this.modelService = modelService;
    }

    /**
     * Associate the person's ledger account with the vehicle
     * @throws CityModelServiceException
     * @throws LedgerException
     */
    @Override
    public void execute() throws CityModelServiceException {

        // Get person who has entered vehicle
        Resident person = (Resident) modelService.getPerson(personId);

        // Get vehicle person has entered
        Vehicle vehicle = (Vehicle) modelService.getIotDevice(cityId, vehicleId);

        // Associate person's ledger account with the vehicle
        vehicle.setLedgerAccount(person.getBlockchainAccountAddress());

        // Update vehicle
        modelService.updateIotDevice(vehicle);

        // Log the event
        LoggerUtil.log(Level.INFO, person.getUuid() + " has entered car " + vehicle.getUuid(), true);
    }
}
