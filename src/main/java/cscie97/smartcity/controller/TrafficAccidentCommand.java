package cscie97.smartcity.controller;

import cscie97.smartcity.model.*;

import java.util.List;
import java.util.logging.Level;

/**
 * Author: Stephen Sheldon
 **/
public class TrafficAccidentCommand implements Command {

    /**
     * Type of natural disaster command is responding to (traffic accident)
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
     * Location of emergency
     */
    private Location emergencyLocation;

    /**
     * Reference to Model Service
     */
    private CityModelService modelService;

    /**
     * List of Robots sorted by distance from emergency
     */
    private List<Robot> sortedRobotDistanceList;

    /**
     * Constructor for TrafficAccidentCommand, initializes everything needed to execute command.
     * @param event         The SensorEvent that the command will respond to.
     * @param modelService  A reference to the model service.
     * @throws CityModelServiceException
     */
    public TrafficAccidentCommand(SensorEvent event, CityModelService modelService) throws CityModelServiceException {
        this.modelService = modelService;
        this.cityId = event.getCityId();
        this.deviceId = event.getDeviceId();
        this.emergencyType = event.getValue();
        this.emergencyLocation = modelService.getIotDevice(this.cityId, this.deviceId).getLocation();
    }

    /**
     * Make an announcement at reporting device, send two nearest robots to emergency to location of emergency.
     * @throws CityModelServiceException
     */
    @Override
    public void execute() throws CityModelServiceException {

        Robot robot1, robot2;

        IotDevice reportingDevice = modelService.getIotDevice(cityId, deviceId);
        // Generate announcement at the reporting device
        SensorOutput announcementOutput = new SensorOutput(reportingDevice.getCurrentCity(), reportingDevice.getUuid(), "Stay calm, help is on its way.");
        modelService.createSensorOutput(announcementOutput);

        // Get sorted list of robots
        sortedRobotDistanceList = ControllerUtils.locateRobots(emergencyLocation, modelService, cityId);
        LoggerUtil.log(Level.INFO, "Sending two nearest robots with ID's " + sortedRobotDistanceList.get(0), ", " + sortedRobotDistanceList.get(1) +
                " to address " + emergencyType + " at lat " + emergencyLocation.getLatitude() + " long " + emergencyLocation.getLongitude());

        // Get two closes robots to emergency
        robot1 = sortedRobotDistanceList.get(0);
        robot2 = sortedRobotDistanceList.get(1);

        robot1.setActivity("Responding to " + emergencyType);
        robot2.setActivity("Responding to " + emergencyType);

        // Update location of two nearest robots
        robot1.setLocation(new Location(emergencyLocation.getLatitude(), emergencyLocation.getLongitude()));
        robot2.setLocation(new Location(emergencyLocation.getLatitude(), emergencyLocation.getLongitude()));

        LoggerUtil.log(Level.INFO, "Sending robots " + robot1.getUuid() + " and " + robot2.getUuid() + " to address emergency " + emergencyType + " at lat " + emergencyLocation.getLatitude() + " long " + emergencyLocation.getLongitude(), true);

        modelService.updateIotDevice(robot1);
        modelService.updateIotDevice(robot2);
    }
}
