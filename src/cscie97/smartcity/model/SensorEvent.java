package cscie97.smartcity.model;

/**
 * Author: Stephen Sheldon
 **/
public class SensorEvent implements Cloneable {

    /**
     * The city ID of the city in which the sensor event takes place.
     */
    private String cityId;

    /**
     * The IoT Device ID that is associated with the event.
     */
    private String deviceId;

    /**
     * The sensor type associated with the sensor event.
     */
    private SensorType sensorType;

    /**
     * Value associated with sensor event.
     */
    private String value;

    /**
     * The personId of the person associated with the sensor event.
     */
    private String personId;

    public SensorEvent() {

    }

    /**
     * Constructor for SensorEvent.
     * @param cityId     The cityId associated with the City in which the sensor event occurs.
     * @param deviceId   The device ID of the IoT Device associated with sensor event.
     * @param sensorType The type of sensor associated with the sensor event.
     * @param value      The value associated with the sensor event.
     * @param personId   The personId of the person associated with the sensor event.
     */
    public SensorEvent(String cityId, String deviceId, SensorType sensorType,
                       String value, String personId) {
        this.cityId = cityId;
        this.deviceId = deviceId;
        this.sensorType = sensorType;
        this.value = value;
        this.personId = personId;
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

    public SensorType getSensorType() {
        return sensorType;
    }

    public void setSensorType(SensorType sensorType) {
        this.sensorType = sensorType;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getPersonId() {
        return personId;
    }

    public void setPersonId(String personId) {
        this.personId = personId;
    }

    protected Object clone() {
        SensorEvent cloned = null;
        try {
            cloned = (SensorEvent) super.clone();
        } catch (CloneNotSupportedException e) {
            System.out.println("Clone not supported exception in SensorEvent class.");
        }

        if (this.getCityId() != null) {
            cloned.setCityId(this.getCityId());
        }
        if (this.getDeviceId()!= null) {
            cloned.setDeviceId(this.getDeviceId());
        }
        if (this.getPersonId() != null) {
            cloned.setPersonId(this.getPersonId());
        }
        if (this.getSensorType() != null) {
            cloned.setSensorType(this.getSensorType());
        }
        if (this.getValue() != null) {
            cloned.setValue(this.getValue());
        }
        return cloned;
    }

    @Override
    public String toString() {
        StringBuilder sensorEventString = new StringBuilder();

        sensorEventString.append("Sensor Event: " + "\n");

        if (this.getCityId() != null) {
            sensorEventString.append("City ID: " + this.getCityId() + "\n");
        }
        if (this.getDeviceId() != null) {
            sensorEventString.append("Device ID" + this.getDeviceId() + "\n");
        }
        if (this.getSensorType() != null) {
            sensorEventString.append("Sensor Type: " + this.getSensorType() + "\n");
        }
        if (this.getValue() != null) {
            sensorEventString.append("Value: " + this.getValue() + "\n");
        }
        if (this.getPersonId() != null) {
            sensorEventString.append("Person Subject ID: " + this.getPersonId() + "\n");
        }
        return sensorEventString.toString();
    }
}
