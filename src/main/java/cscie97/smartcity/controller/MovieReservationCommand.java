package cscie97.smartcity.controller;

import cscie97.smartcity.ledger.LedgerException;
import cscie97.smartcity.ledger.LedgerService;
import cscie97.smartcity.ledger.Transaction;
import cscie97.smartcity.model.*;

import java.util.UUID;
import java.util.logging.Level;

/**
 * Author: Stephen Sheldon
 **/
public class MovieReservationCommand implements Command {

    /**
     * City ID of city where the event has occurred
     */
    private String cityId;

    /**
     * Device ID of device that reported the event
     */
    private String deviceId;

    /**
     * Person ID of person who is requested movie reservation
     */
    private String personId;

    /**
     * Reference to Model Service
     */
    private CityModelService modelService;

    /**
     * Reference to Ledger Service
     */
    LedgerService ledgerService;

    /**
     * MovieReservationCommand Constructor method
     * @param event         Event object
     * @param modelService  Reference to City Model Service
     * @param ledgerService Reference to Ledger Service
     */
    public MovieReservationCommand(SensorEvent event, CityModelService modelService, LedgerService ledgerService) {
        this.modelService = modelService;
        this.ledgerService = ledgerService;
        this.cityId = event.getCityId();
        this.deviceId = event.getDeviceId();
        this.personId = event.getPersonId();
    }

    /**
     * Checks for a positive ledger account balance, if person is a Resident charges person
     * 10 units and makes an announcement confirming their reservation
     * @throws CityModelServiceException
     */
    @Override
    public void execute() throws CityModelServiceException, LedgerException, ControllerServiceException {

        // Get reporting device
        IotDevice reportingDevice = modelService.getIotDevice(cityId, deviceId);

        // Get the person
        Person person = modelService.getPerson(personId);

        // Check if person is a resident
        if (person instanceof Resident) {
            Resident resident = (Resident) person;

            // Create UUID
            String uniqueID = UUID.randomUUID().toString();

            // Check to see if Resident has a balance greater than or equal to 20. Ticket fee
            // is 10 and service fee is 10.
            if (ledgerService.getAccountBalance(resident.getBlockchainAccountAddress()) < 20) {
                LoggerUtil.log(Level.INFO, "Sorry Resident " + resident.getUuid() + " but your ledger balance needs to be greater than 20 reserve movie tickets.", true);
                throw new ControllerServiceException("Movie Reservation", "Resident has ledger balance below 20.");
            }

            // Get ledger account for city
            String cityLedgerAccount = modelService.getCity(cityId).getBlockchainAccount();

            // Create transaction
            Transaction transaction = ledgerService.createTransaction(uniqueID, 10, 10, "Movie reservation fee", resident.getBlockchainAccountAddress(), cityLedgerAccount);

            // Process the transaction
            ledgerService.processTransaction(transaction);
        }
        // Create sensor output to notify person that their seats have been reserved.
        SensorOutput sensorOutput = new SensorOutput(reportingDevice.getCurrentCity(), reportingDevice.getUuid(), "Your seats are reserved; please arrive a few minutes early.");
        modelService.createSensorOutput(sensorOutput);
    }
}
