package cscie97.smartcity.controller;

import cscie97.smartcity.ledger.LedgerException;
import cscie97.smartcity.ledger.LedgerService;
import cscie97.smartcity.model.*;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

/**
 * Author: Stephen Sheldon
 **/
public class ControllerService implements Observer {

    /**
     * Singleton implementation of ControllerService.
     */
    private static ControllerService controllerService;

    /**
     * Reference to model service.
     */
    private CityModelService cityModelService;

    /**
     * Reference to LedgerService Service.
     */
    private LedgerService ledgerService;

    /**
     * A map of cityID's and then a list of CO2 devices
     * that are currently above 1000
     */
    private Map<String, List<String>> co2CityMap;

    /**
     * Tracks if a city is currently in a high CO2 state
     */
//    private Map<String, Boolean> cityCo2State;


    private ControllerService() {
        co2CityMap = new LinkedHashMap<>();
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
        try {
            // Attempt to create the command to corresponds to the received event
            Command eventCommand = createCommand(event);
            // Attempt to execute the command
            eventCommand.execute();
        } catch (ControllerServiceException cse) {
            // Log the exception
            LoggerUtil.log(Level.SEVERE, cse.getAction(), cse.getReason(), false);
        } catch (CityModelServiceException cmse) {
            LoggerUtil.log(Level.SEVERE, cmse.getAction(), cmse.getReason(), false);
        } catch (LedgerException le) {
            LoggerUtil.log(Level.SEVERE, le.getAction(), le.getReason(), false);
        }
    }

    /**
     * Creates the command that corresponds to the SensorEvent that was received.
     * @param event The SensorEvent to process.
     * @return      A specific command object corresponding to the sensor event.
     * @throws ControllerServiceException
     * @throws CityModelServiceException
     */
    private Command createCommand(SensorEvent event) throws ControllerServiceException, CityModelServiceException {
        Command command = null;
        String[] splitCommand;
        String vehicleId;

        // Logic to device what kind of command to create
        switch (event.getValue().toLowerCase()) {
            case "fire", "flood", "earthquake", "severe weather" -> command = new NaturalDisasterCommand(event, cityModelService);
            case "traffic_accident" -> command = new TrafficAccidentCommand(event, cityModelService);
            case "littering" -> command = new LitterCommand(event, cityModelService, ledgerService);
            case "broken_glass_sound" -> command = new BrokenGlassCommand(event, cityModelService);
            case "person_seen" -> command = new PersonSeenCommand(event, cityModelService);
            case "does this bus go to central square?" -> command = new BusRouteCommand(event, cityModelService);
            case "person boards bus" -> command = new BoardBusCommand(event);
            case "what movies are showing tonight?" -> command = new MovieInfoCommand(event);
            case "person enters car" -> command = new PersonEntersCarCommand(event, cityModelService);
        }

        // Determine if CO2 Event
        if (event.getSensorType().equals(SensorType.CO2METER)) {
            String cityId = event.getCityId();
            String deviceId = event.getDeviceId();

            // Check if event is above 1000
            if (Integer.parseInt(event.getValue()) >= 1000) {
                // Check if any device from this city is currently above 1000
                if (co2CityMap.containsKey(cityId)) {
                    // If it is check to see if this device is already in our array
                    if (!co2CityMap.get(cityId).contains(deviceId)) {
                        // It doesn't contain the device so add it
                        co2CityMap.get(cityId).add(deviceId);
                        // Now check size of the array to see if a command needs to be generated
                        if (co2CityMap.get(cityId).size() >= 3) {
                            // Generate an event if it's greater than or equal to 3
                            command = new HighCo2Command(event);
                        }
                        // If it's not greater than or equal to three then do nothing
                    } else {
                        // It does contain the device so throw an exception
                        throw new ControllerServiceException("create-event", "The device with device ID " + event.getDeviceId() + " has already reported a high CO2 event.");
                    }
                } else {
                    // Create a new list of devices with high co2 for this city
                    List<String> deviceList = new ArrayList<>();
                    deviceList.add(deviceId);
                    // The co2CityMap doesn't contain the city yet so add the device list
                    co2CityMap.put(cityId, deviceList);
                }
            } else if (Integer.parseInt(event.getValue()) < 1000) {
                // Low Co2 Event detected
                if (co2CityMap.containsKey(cityId)) {
                    // LEFT OFF HERE
                }
            }
        }

        splitCommand = event.getValue().split(" (?=([^\"]*\"[^\"]*\")*[^\"]*$)");

        // Check if event is vehicle parked
        if (splitCommand[0].equalsIgnoreCase("vehicle")) {
            vehicleId = splitCommand[1];
            command = new ParkingEventCommand(event, vehicleId, cityModelService, ledgerService);
        }



        // Reserve movie tickets

        // Help me find my child

        // Vehicle parked

        // If command is null then it it's a event that the controller won't handle
        if (command == null) {
            throw new ControllerServiceException("create-event", "Unable to create a command response in controller for event " + event.getValue());
        }

        return command;
    }

    public void setCityModelService(CityModelService cityModelService) {
        this.cityModelService = cityModelService;
        // Register with model service
        this.cityModelService.registerObserver(this);
    }

    public void setLedgerService(LedgerService ledgerService) {
        this.ledgerService = ledgerService;
    }

}
