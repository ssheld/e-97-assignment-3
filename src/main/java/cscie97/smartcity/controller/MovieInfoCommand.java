package cscie97.smartcity.controller;

import cscie97.smartcity.model.*;

/**
 * Author: Stephen Sheldon
 **/
public class MovieInfoCommand implements Command {

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
     * MovieInfoCommand Constructor method
     * @param event        Event object
     * @param modelService Reference to City Model Service
     */
    public MovieInfoCommand(SensorEvent event, CityModelService modelService) {
        this.modelService = modelService;
        this.cityId = event.getCityId();
        this.deviceId = event.getDeviceId();
    }

    /**
     * Generate a sensor output to notify individual of movie time then update the kiosk image URI.
     * @throws CityModelServiceException
     */
    @Override
    public void execute() throws CityModelServiceException {

        // Get reporting device
        InformationKiosk reportingDevice = (InformationKiosk) modelService.getIotDevice(cityId, deviceId);

        // Create sensor output announcement
        SensorOutput announcement = new SensorOutput(reportingDevice.getCurrentCity(), reportingDevice.getUuid(), "Casablanca is showing at 9 pm.");

        // Create the sensor output in model service
        modelService.createSensorOutput(announcement);

        // Update the kiosk to display casablanca image
        String casablancaURI = "https://en.wikipedia.org/wiki/Casablanca_(film)#/media/File:CasablancaPoster-Gold.jpg";
        reportingDevice.setImageUri(casablancaURI);

        // Update the kiosk in the model service
        modelService.updateIotDevice(reportingDevice);
    }
}
