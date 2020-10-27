package cscie97.smartcity.controller;

import cscie97.smartcity.model.*;
import java.util.List;
import java.util.logging.Level;

/**
 * Author: Stephen Sheldon
 **/
public class MissingChildCommand implements Command {

    /**
     * City ID of city where the event has occurred
     */
    private String cityId;

    /**
     * Device ID of device that reported the event
     */
    private String deviceId;

    /**
     * ID of person looking for missing person
     */
    private String personId;

    /**
     * ID of missing person
     */
    private String missingPersonId;

    /**
     * Reference to Model Service
     */
    private CityModelService modelService;

    /**
     * List of Robots sorted by distance from emergency
     */
    private List<Robot> sortedRobotDistanceList;

    /**
     * MissingChildCommand Constructor method
     * @param event           Event object
     * @param missingPersonId ID of missing person
     * @param modelService    Reference to City Model Service
     */
    public MissingChildCommand(SensorEvent event, String missingPersonId, CityModelService modelService) {
        this.modelService = modelService;
        this.cityId = event.getCityId();
        this.deviceId = event.getDeviceId();
        this.personId = event.getPersonId();
        this.missingPersonId = missingPersonId;
    }

    /**
     * Locates the missing person, notifies the requester and then retrieves the person using a robot.
     */
    @Override
    public void execute() throws CityModelServiceException {

        // Retrieve person
        Person person = modelService.getPerson(missingPersonId);

        // Retrieve reporting person
        Person reportingPerson = modelService.getPerson(personId);

        // Get reporting IoT Device
        IotDevice reportingDevice = modelService.getIotDevice(cityId, deviceId);

        // Retrieve person location
        Location personLocation = person.getLocation();

        // Create sensor output
        SensorOutput announcement = new SensorOutput(reportingDevice.getCurrentCity(), reportingDevice.getUuid(), "Person " + person.getUuid() + " is at lat " +
                personLocation.getLatitude() + " long " + personLocation.getLongitude());

        // Send sensor output to device
        modelService.createSensorOutput(announcement);

        // Find robot nearest to person
        // Get sorted list of robots
        sortedRobotDistanceList = ControllerUtils.locateRobots(personLocation, modelService, cityId);

        // Make sure there's a robot to help move person
        if (sortedRobotDistanceList.isEmpty()) {
            LoggerUtil.log(Level.INFO, "There are no robots in the city to help retrieve the missing person", true);
            throw new CityModelServiceException("Retrieve Missing Person", "There are no robots in the city to retrieve person.");
        }

        Robot robot = sortedRobotDistanceList.get(0);

        // Get reporting person location
        Location reportingPersonLocation = reportingPerson.getLocation();

        LoggerUtil.log(Level.INFO, "Robot " + robot.getUuid() + " is retrieving " + person.getUuid(), true);

        // Update person location
        person.setLocation(new Location(reportingPersonLocation.getLatitude(), reportingPersonLocation.getLongitude()));
        modelService.updatePerson(person);

        // Update robot location
        robot.setLocation(new Location(reportingPersonLocation.getLatitude(), reportingPersonLocation.getLongitude()));
        modelService.updateIotDevice(robot);
    }
}
