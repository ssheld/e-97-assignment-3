package cscie97.smartcity.controller;

import cscie97.smartcity.ledger.LedgerException;
import cscie97.smartcity.ledger.LedgerService;
import cscie97.smartcity.ledger.Transaction;
import cscie97.smartcity.model.*;

import java.util.UUID;

/**
 * Author: Stephen Sheldon
 **/
public class BoardBusCommand implements Command {

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
     * BoardBusCommand Constructor method
     * @param event         Event object
     * @param modelService  Reference to City Model Service
     * @param ledgerService Reference to Ledger Service
     */
    public BoardBusCommand(SensorEvent event, CityModelService modelService, LedgerService ledgerService) {
        this.modelService = modelService;
        this.ledgerService = ledgerService;
        this.cityId = event.getCityId();
        this.deviceId = event.getDeviceId();
        this.personId = event.getPersonId();
    }

    /**
     * Acknowledge person who has boarded bus, if person is a resident with a ledger balance
     * greater than the bus fee plus the ledger transaction fee (10 units) then they are charged
     * the amount. If the person is a visitor then they ride the bus for free.
     */
    @Override
    public void execute() throws CityModelServiceException, LedgerException, ControllerServiceException {

        // Get Person
        Person person = modelService.getPerson(personId);

        // Get reporting device
        IotDevice reportingDevice = modelService.getIotDevice(cityId, deviceId);

        // Create sensor output acknowledging person
        SensorOutput announcement = new SensorOutput(reportingDevice.getCurrentCity(), reportingDevice.getUuid(), "Hello, good to see you " + person.getUuid());

        // Send sensor output to device
        modelService.createSensorOutput(announcement);

        // check if person is a resident
        if (person instanceof Resident) {
            Resident resident = (Resident) person;

            // Get bus fee
            Integer busFee = ((Vehicle) reportingDevice).getFee();

            // Check if resident has a positive account balance with sufficient funds.
            if (ledgerService.getAccountBalance(resident.getBlockchainAccountAddress()) < busFee+10) {
                SensorOutput sensorOutput = new SensorOutput(reportingDevice.getCurrentCity(), reportingDevice.getUuid(), "Sorry " + resident.getUuid() + " you have insufficient funds to ride the bus.");
                throw new ControllerServiceException("Board Bus", "Insufficient funds for resident to board bus");
            }

            // Create UUID
            String uniqueID = UUID.randomUUID().toString();

            // Get ledger account for city
            String cityLedgerAccount = modelService.getCity(cityId).getBlockchainAccount();

            // Otherwise person has enough of a balance so create a transaction
            Transaction transaction = ledgerService.createTransaction(uniqueID, busFee, 10, "Bus ride fee", resident.getBlockchainAccountAddress(), cityLedgerAccount);

            // Process the transaction
            ledgerService.processTransaction(transaction);
        }
    }
}
