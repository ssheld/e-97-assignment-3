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
     * Co2 trigger, tracks high co2 levels
     */
    private int highCo2Level;

    /**
     * Co2 trigger, tracks low co2 levels
     */
    private int lowCo2Level;


    private ControllerService() {
        co2CityMap = new LinkedHashMap<>();
        highCo2Level = 0;
        lowCo2Level = 0;
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
            case "person boards bus" -> command = new BoardBusCommand(event, cityModelService, ledgerService);
            case "what movies are showing tonight?" -> command = new MovieInfoCommand(event, cityModelService);
            case "person enters car" -> command = new PersonEntersCarCommand(event, cityModelService);
        }

        // Determine if CO2 Event
        if (event.getSensorType().equals(SensorType.CO2METER)) {
            String cityId = event.getCityId();
            String deviceId = event.getDeviceId();

            // Check if event is above 1000
            if (Integer.parseInt(event.getValue()) >= 1000) {
                // Increment highCo2Level
                highCo2Level++;
                // Check if it's 3
                if (highCo2Level >= 3) {
                    command = new HighCo2Command(event, cityModelService);
                    // reset the trigger
                    highCo2Level = 0;
                }
            } else if (Integer.parseInt(event.getValue()) < 1000){
                // Increment lowCo2Level
                lowCo2Level++;
                // Check if it's 3
                if (lowCo2Level >= 3) {
                    command = new LowCo2Command(event, cityModelService);
                    // reset the trigger
                    lowCo2Level = 0;
                }
            }
        }

        splitCommand = event.getValue().split(" (?=([^\"]*\"[^\"]*\")*[^\"]*$)");

        // Check if event is parking space event
        if (splitCommand[0].equalsIgnoreCase("vehicle")) {
            vehicleId = splitCommand[1];
            command = new ParkingEventCommand(event, vehicleId, cityModelService, ledgerService);
        }

        // Check if event is movie reservation event
        if (splitCommand[0].equalsIgnoreCase("reserve")) {
            command = new MovieReservationCommand(event, cityModelService, ledgerService);
        }

        // Check if event is search for missing person
        if (splitCommand[0].equalsIgnoreCase("can")) {
            command = new MissingPersonCommand(event, splitCommand[7], cityModelService);
        }

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
