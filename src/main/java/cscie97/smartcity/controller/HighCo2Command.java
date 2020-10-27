package cscie97.smartcity.controller;

import cscie97.smartcity.model.*;

import java.util.List;
import java.util.logging.Level;

/**
 * Author: Stephen Sheldon
 **/
public class HighCo2Command implements Command {

    /**
     * City ID of city where natural disaster is occurring
     */
    private String cityId;

    /**
     * Reference to City Model Service
     */
    private CityModelService modelService;

    /**
     * HighCo2Command Constructor method
     * @param event         Event object
     * @param modelService  Reference to City Model Service
     */
    public HighCo2Command(SensorEvent event, CityModelService modelService) {
        this.modelService = modelService;
        this.cityId = event.getCityId();
    }

    /**
     * Disable all cars within the city
     */
    @Override
    public void execute() throws CityModelServiceException {

        // Retrieve all devices in the city
        List<IotDevice> deviceList = modelService.getIotDevice(cityId);

        // Retrieve all cars
        for (IotDevice device : deviceList) {
            // Check if device is car
            if (device instanceof Vehicle && ((Vehicle) device).getVehicleType() == VehicleType.CAR) {
                // Turn off car
                device.setEnabled(Enabled.OFF);
                // Update in model service
                modelService.updateIotDevice(device);
                LoggerUtil.log(Level.INFO, "Turning off vehicle " + device.getUuid(), true);
           }
        }
    }
}
