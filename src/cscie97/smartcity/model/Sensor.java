package cscie97.smartcity.model;

/**
 * Author: Stephen Sheldon
 **/
public class Sensor {

    /**
     * The sensor type of the sensor (microphone|camera|co2sensor|thermometer)
     */
    private SensorType sensorType;

    /**
     * The constructor for the Sensor class.
     * @param sensorType  The type of sensor the sensor is.
     */
    public Sensor(SensorType sensorType) {
        this.sensorType = sensorType;
    }

    public SensorType getSensorType() {
        return sensorType;
    }
}
