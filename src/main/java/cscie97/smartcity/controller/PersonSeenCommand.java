package cscie97.smartcity.controller;

import cscie97.smartcity.model.*;

import java.util.logging.Level;

/**
 * Author: Stephen Sheldon
 **/
public class PersonSeenCommand implements Command {

    /**
     * City ID of city where the event has occurred
     */
    private String cityId;

    /**
     * Device ID of device that reported the event
     */
    private String deviceId;

    /**
     * ID of person
     */
    private String personId;

    /**
     * Reference to Model Service
     */
    private CityModelService modelService;

    public PersonSeenCommand(SensorEvent event, CityModelService modelService) {
        this.cityId = event.getCityId();
        this.deviceId = event.getDeviceId();
        this.personId = event.getPersonId();
        this.modelService = modelService;
    }

    /**
     * Update the location of the person seen to the location in which the event occurred.
     */
    @Override
    public void execute() throws CityModelServiceException {

        // Get IoT device that reported event
        Location eventLocation = modelService.getIotDevice(cityId, deviceId).getLocation();
        Location updatedPersonLocation = new Location(eventLocation.getLatitude(), eventLocation.getLongitude());

        // Get person
        Person seenPerson = modelService.getPerson(personId);

        // Update person's location
        seenPerson.setLocation(updatedPersonLocation);

        // Log event
        LoggerUtil.log(Level.INFO, "Person " + seenPerson.getUuid() + " seen at lat " + eventLocation.getLatitude() + " long " + eventLocation.getLongitude(), true);

        // Send to model service to update
        modelService.updatePerson(seenPerson);
    }
}
