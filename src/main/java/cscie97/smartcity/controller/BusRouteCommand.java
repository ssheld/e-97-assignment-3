package cscie97.smartcity.controller;

import cscie97.smartcity.model.*;

/**
 * Author: Stephen Sheldon
 **/
public class BusRouteCommand implements Command {

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
     * Constructor for BusRouteCommand
     * @param event        The bus route event
     * @param modelService Reference to the model service
     */
    public BusRouteCommand(SensorEvent event, CityModelService modelService) {
        this.cityId = event.getCityId();
        this.deviceId = event.getDeviceId();
        this.modelService = modelService;

    }

    /**
     * Makes an announcement at the device to notify the individual asking
     * the question that the bus does go to central square.
     */
    @Override
    public void execute() throws CityModelServiceException {

        String announcement = "Yes, this bus goes to Central Square.";

        IotDevice device = modelService.getIotDevice(cityId, deviceId);

        // Generate sensor output at device
        SensorOutput sensorOutput = new SensorOutput(device.getCurrentCity(), device.getUuid(), announcement);
        modelService.createSensorOutput(sensorOutput);
    }
}
