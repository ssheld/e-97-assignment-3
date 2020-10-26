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
public class ParkingEventCommand implements Command {

    /**
     * City ID of city where the event has occurred
     */
    private String cityId;

    /**
     * Device ID of device that reported the event
     */
    private String deviceId;

    /**
     * ID of vehicle that parked in parking space
     */
    private String vehicleId;

    /**
     * Reference to Model Service
     */
    private CityModelService modelService;

    /**
     * Reference to Ledger Service
     */
    LedgerService ledgerService;

    /**
     * Constructor for ParkingEventCommand
     * @param event         The event object
     * @param vehicleId     The vehicle ID of the vehicle parked in parking space
     * @param modelService  A reference to the City Model Service
     * @param ledgerService A reference to the Ledger Service
     */
    public ParkingEventCommand(SensorEvent event, String vehicleId, CityModelService modelService, LedgerService ledgerService) {
        this.cityId = event.getCityId();
        this.deviceId = event.getDeviceId();
        this.vehicleId = vehicleId.split(":")[1];
        this.modelService = modelService;
        this.ledgerService = ledgerService;
    }

    /**
     * Charge the account associated with the vehicle for parking for 1 hour
     * @throws CityModelServiceException
     */
    @Override
    public void execute() throws CityModelServiceException, LedgerException, ControllerServiceException {
        // Get the parking space device
        ParkingSpace parkingSpace = (ParkingSpace)modelService.getIotDevice(cityId, deviceId);
        Location parkingSpaceLocation = parkingSpace.getLocation();

        // Get the vehicle
        Vehicle vehicle = (Vehicle)modelService.getIotDevice(cityId, vehicleId);

        // Verify that there is a ledger account associated with vehicle
        if (vehicle.getLedgerAccount() == null) {
            throw new ControllerServiceException("parking space event", "The car doesn't have an associated ledger account");
        }

        // Update location of vehicle
        vehicle.setLocation(new Location(parkingSpaceLocation.getLatitude(), parkingSpaceLocation.getLongitude()));

        // Get the rate for the parking space
        Integer parkingFee = parkingSpace.getRate();

        // Get the account associated with the vehicle
        String vehicleAccount = vehicle.getLedgerAccount();

        // Update location of vehicle to that of parking space
        modelService.updateIotDevice(parkingSpace);

        // Create UUID
        String uniqueID = UUID.randomUUID().toString();

        // Create transaction to charge account
        Transaction transaction = ledgerService.createTransaction(uniqueID, parkingFee, 10, "Parking Space fee",  vehicleAccount, modelService.getCity(cityId).getBlockchainAccount());

        // Process the transaction
        ledgerService.processTransaction(transaction);

        LoggerUtil.log(Level.INFO, "Vehicle " + vehicleId + " has parked at " + parkingSpace.getUuid(), true);
    }
}
