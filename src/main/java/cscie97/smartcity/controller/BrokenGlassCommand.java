package cscie97.smartcity.controller;

import cscie97.smartcity.model.*;

import java.util.List;
import java.util.logging.Level;

/**
 * Author: Stephen Sheldon
 **/
public class BrokenGlassCommand implements Command {

    /**
     * City ID of city where the event has occurred
     */
    private String cityId;

    /**
     * Device ID of device that reported the event
     */
    private String deviceId;

    /**
     * Reference to Model Service
     */
    private CityModelService modelService;

    /**
     * Constructor for BrokenGlassCommand
     * @param event        Event object
     * @param modelService Reference to City Model Service
     */
    public BrokenGlassCommand(SensorEvent event, CityModelService modelService) {
        this.cityId = event.getCityId();
        this.deviceId = event.getDeviceId();
        this.modelService = modelService;
    }

    /**
     * Send the Robot nearest to the broken glass to clean it up
     */
    @Override
    public void execute() throws CityModelServiceException {

        // Get reporting device
        IotDevice reportingDevice = modelService.getIotDevice(cityId, deviceId);

        // Get location of device
        Location glassLocation = reportingDevice.getLocation();

        // Compile list of robots distance from location
        List<Robot> sortedRobotDistanceList = ControllerUtils.locateRobots(glassLocation, modelService, cityId);

        // Get closest robot
        Robot closestRobot = sortedRobotDistanceList.get(0);

        // Update the robots location to that of the glass location
        closestRobot.setLocation(new Location(glassLocation.getLatitude(), glassLocation.getLongitude()));

        // Log to console and text file
        LoggerUtil.log(Level.INFO, "Sending Robot " + closestRobot.getUuid() + " to clean up glass at lat " + glassLocation.getLatitude() + " long " +
                glassLocation.getLongitude(), true);

        // Update the robot in the model service
        modelService.updateIotDevice(closestRobot);
    }
}
