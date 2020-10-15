package cscie97.smartcity.model;

/**
 * Author: Stephen Sheldon
 **/
public class SensorOutput implements Cloneable {

    /**
     * The city ID in which the sensor resides.
     */
    private String cityId;

    /**
     * The device ID that corresponds to the device with sensor.
     */
    private String deviceId;

    /**
     * The string value output.
     */
    private String value;

    public SensorOutput() {

    }

    /**
     * Constructor for sensor output
     * @param cityId   The city ID in which the sensor resides.
     * @param deviceId The device ID that corresponds to device with sensor.
     * @param value    The value for the sensor output.
     */
    public SensorOutput(String cityId, String deviceId, String value) {
        this.cityId = cityId;
        this.deviceId = deviceId;
        this.value = value;
    }

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    /**
     * Clones a SensorOutput object.
     * @return Returns the cloned SensorOutput object.
     */
    @Override
    protected Object clone() {

        SensorOutput cloned = null;

        try {
            cloned = (SensorOutput) super.clone();
        } catch (CloneNotSupportedException e) {
            System.out.println("CloneNotSupportedException in class SensorOutput");
        }

        if (this.getCityId() != null) {
            cloned.setCityId(this.getCityId());
        }
        if (this.getDeviceId() != null) {
            cloned.setDeviceId(this.getDeviceId());
        }
        if (this.getValue() != null) {
            cloned.setValue(this.getValue());
        }
        return cloned;
    }

    @Override
    public String toString() {
        StringBuilder sensorOutputString = new StringBuilder();

        sensorOutputString.append("Sensor Output: " + "\n");

        if (this.getCityId() != null) {
            sensorOutputString.append("City ID: " + this.getCityId() + "\n");
        }
        if (this.getDeviceId() != null) {
            sensorOutputString.append("Device ID: " + this.getDeviceId() + "\n");
        }
        if (this.getValue() != null) {
            sensorOutputString.append("Value: " + this.getValue() + "\n");
        }

        return sensorOutputString.toString();
    }
}
