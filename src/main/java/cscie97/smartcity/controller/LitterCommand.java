package cscie97.smartcity.controller;

import cscie97.smartcity.ledger.LedgerException;
import cscie97.smartcity.ledger.LedgerService;
import cscie97.smartcity.ledger.Transaction;
import cscie97.smartcity.model.*;

import java.util.List;
import java.util.UUID;
import java.util.logging.Level;

/**
 * Author: Stephen Sheldon
 **/
public class LitterCommand implements Command {

    /**
     * City ID of city where natural disaster is occurring
     */
    private String cityId;

    /**
     * Device ID of device that reported emergency
     */
    private String deviceId;

    /**
     * ID of person who littered
     */
    private String personId;

    /**
     * Reference to Model Service
     */
    private CityModelService modelService;

    /**
     * Reference to Ledger Service
     */
    private LedgerService ledgerService;

    /**
     * LitterCommand Constructor method
     * @param event         Litter event
     * @param modelService  Reference to model service
     * @param ledgerService Reference to litter service
     * @throws CityModelServiceException
     */
    public LitterCommand(SensorEvent event, CityModelService modelService, LedgerService ledgerService) throws CityModelServiceException {
        this.modelService = modelService;
        this.ledgerService = ledgerService;
        this.cityId = event.getCityId();
        this.deviceId = event.getDeviceId();
        this.personId = event.getPersonId();
    }

    /**
     * IoT device announces over speaker to please not litter, a robot is sent to location
     * of litter event to clean up and the person's account is charged 50 units
     */
    @Override
    public void execute() throws CityModelServiceException, LedgerException {

        List<Robot> sortedRobotDistanceList;

        // Get location of litter
        Location litterLocation = modelService.getIotDevice(cityId, deviceId).getLocation();

        // Get reporting IoT Device
        IotDevice device = modelService.getIotDevice(cityId, deviceId);

        // Create sensor output
        SensorOutput litterWarning = new SensorOutput(device.getCurrentCity(), device.getUuid(), "Please do not litter.");

        // Retrieve list of robots in city sorted by distance from event location
        sortedRobotDistanceList = ControllerUtils.locateRobots(litterLocation, modelService, cityId);

        // Get closest robot
        Robot nearestRobot = sortedRobotDistanceList.get(0);

        // Update the robots location
        nearestRobot.setLocation(new Location(litterLocation.getLatitude(), litterLocation.getLongitude()));
        // Update the robots activity
        nearestRobot.setActivity("Clean garbage at lat " + litterLocation.getLatitude() + " long " + litterLocation.getLongitude());

        LoggerUtil.log(Level.INFO, "Sending robot with ID " + nearestRobot.getUuid() + " to clean up litter at lat "
                    + litterLocation.getLatitude() + " long " + litterLocation.getLongitude(), true);

        // Move then nearest robot to the event location to clean up litter
        modelService.updateIotDevice(nearestRobot);

        // Create UUID
        String uniqueID = UUID.randomUUID().toString();

        // Get Person who littered
        Person litterbug = modelService.getPerson(personId);

        // Check if person is a visitor
        if (litterbug instanceof Visitor) {
            // Visitors don't have ledger accounts
            LoggerUtil.log(Level.INFO, "Visitor " + litterbug.getUuid() + " has been caught littering at lat " +
                    litterLocation.getLatitude() + " long " + litterLocation.getLongitude(), true);
            throw new CityModelServiceException("Litter event", "Unable to charge visitor littering fine. Visitor has no ledger account.");
        }

        Resident residentLitterbug = (Resident) litterbug;

        // Get ledger account of the person who littered
        String litterbugLedgerAccount = residentLitterbug.getBlockchainAccountAddress();

        // Get the ledger account of the city
        String cityLedgerAccount = modelService.getCity(cityId).getBlockchainAccount();

        // Charge the person's account 50 units for litter
        Transaction transaction = ledgerService.createTransaction(uniqueID, 50, 10, "Fee for littering", litterbugLedgerAccount, cityLedgerAccount);

        // Log to console and text file
        LoggerUtil.log(Level.INFO, "Resident " + litterbug.getUuid() + " has been caught littering at lat " +
                litterLocation.getLatitude() + " long " + litterLocation.getLongitude() + " and will be charged 50 units.", true);

        // Process the transaction
        ledgerService.processTransaction(transaction);
    }
}
