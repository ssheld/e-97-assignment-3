package cscie97.smartcity.controller;

import cscie97.smartcity.model.*;

import java.util.*;
import java.util.logging.Level;

/**
 * Author: Stephen Sheldon
 **/
public class NaturalDisasterCommand implements Command {

    /**
     * Type of natural disaster command is responding to (fire|floor|earthquake|severe weather)
     */
    private String emergencyType;

    /**
     * City ID of city where natural disaster is occurring
     */
    private String cityId;

    /**
     * Device ID of device that reported emergency
     */
    private String deviceId;

    /**
     * Reference to Model Service
     */
    private CityModelService modelService;

    /**
     * Constructor for NaturalDisasterCommand, initializes everything needed to execute command.
     * @param event         The SensorEvent that the command will respond to.
     * @param modelService  A reference to the model service.
     * @throws CityModelServiceException
     */
    public NaturalDisasterCommand(SensorEvent event, CityModelService modelService) {
        this.modelService = modelService;
        this.cityId = event.getCityId();
        this.deviceId = event.getDeviceId();
        this.emergencyType = event.getValue();
    }

    /**
     * Generates announcement over all IoT devices in city for people to find shelter, then sends 1/2 robots
     * in city to location of emergency and then tells the remaining 1/2 robots in city to help people find shelter.
     * @throws CityModelServiceException
     */
    @Override
    public void execute() throws CityModelServiceException {

        Location emergencyLocation = modelService.getIotDevice(cityId, deviceId).getLocation();

        List<IotDevice> deviceList = modelService.getIotDevice(cityId);

        // Make announcement over all IoT Devices in the city using sensor output
        for (IotDevice device : deviceList) {
            String announcement = "There is a " + emergencyType + " in " + device.getCurrentCity() + " please find shelter immediately";
            // Create sensor output and send it to IoT device
            SensorOutput announcementOutput = new SensorOutput(device.getCurrentCity(), device.getUuid(),
                    announcement);
            modelService.createSensorOutput(announcementOutput);
        }

        // Locate robots
        List<Robot> sortedRobotDistanceList = ControllerUtils.locateRobots(emergencyLocation, modelService, cityId);

        // Send half the robots to the emergency
        for (int i = 0; i < sortedRobotDistanceList.size()/2; i++) {
            // Get robot
            Robot emergencyResponder = sortedRobotDistanceList.get(i);
            // Set the robots activity
            emergencyResponder.setActivity("Responding to " + emergencyType);
            // Update location of Robot
            emergencyResponder.setLocation(new Location(emergencyLocation.getLatitude(), emergencyLocation.getLongitude()));
            LoggerUtil.log(Level.INFO, "Sending robot " + sortedRobotDistanceList.get(i).getUuid() + " to address emergency " + emergencyType + " at lat " + emergencyLocation.getLatitude() + " long " + emergencyLocation.getLongitude(), true);
            // Update the robot in the model
            modelService.updateIotDevice(emergencyResponder);
        }

        // Send the remaining 1/2 robots to help people find shelter
        for (int i = sortedRobotDistanceList.size()/2; i < sortedRobotDistanceList.size(); i++) {
            Robot currentRobot = sortedRobotDistanceList.get(i);
            // Create sensor output and send to robot for processing
            SensorOutput robotSensorOutput = new SensorOutput(currentRobot.getCurrentCity(), currentRobot.getUuid(), "Helping people find shelter.");
            modelService.createSensorOutput(robotSensorOutput);
        }
    }
}
