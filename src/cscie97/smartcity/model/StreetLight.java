package cscie97.smartcity.model;

/**
 * Author: Stephen Sheldon
 **/
public class StreetLight extends IotDevice implements Cloneable{

    /**
     * The current brightness of the streetlight.
     */
    private Integer brightness;

    /**
     * The constructor for StreetLight.
     * @param uuid          The globally unique identifier of the IoT Device.
     * @param latitude      The current latitude of the IoT Device.
     * @param longitude     The current longitude of the IoT Device.
     * @param enabled       Whether the IoT device is enabled or disabled.
     * @param brightness    The brightness of the streetlight.
     */
    public StreetLight(String uuid, double latitude, double longitude,
                       Enabled enabled, Integer brightness) {
        super(uuid, latitude, longitude, Status.READY, enabled);
        this.brightness = brightness;
    }

    public StreetLight(String uuid, Enabled enabled, Integer brightness) {
        super(uuid, enabled);
        this.brightness = brightness;
    }

    public StreetLight(String uuid, Enabled enabled) {
        super(uuid, enabled);
    }

    public StreetLight(String uuid, Integer brightness) {
        super(uuid);
        this.brightness = brightness;
    }

    public Integer getBrightness() {
        return brightness;
    }

    public void setBrightness(Integer brightness) {
        this.brightness = brightness;
    }

    /**
     * Clones a StreetLight object.
     * @return The cloned StreetLight object.
     */
    @Override
    protected Object clone() {
        StreetLight cloned;
        cloned = (StreetLight) super.clone();

        if (this.getBrightness() != null) {
            cloned.setBrightness(this.getBrightness());
        }
        if (this.getLatestEvent() != null) {
            cloned.setLatestEvent((SensorEvent)this.getLatestEvent().clone());
        }

        return cloned;
    }

    @Override
    public String toString() {
        StringBuilder deviceString = new StringBuilder();

        deviceString.append("Device Type: Streetlight" + "\n" +
                "Device ID: " + this.getUuid() + "\n" +
                "Current Status: " + this.getCurrentStatus().toString() + "\n" +
                "Enabled: " + this.getEnabled().toString() + "\n" +
                "Latitude: " + this.getLocation().getLatitude() + "\n" +
                "Longitude: " + this.getLocation().getLongitude() + "\n" +
                "Current City: " + this.getCurrentCity() + "\n" +
                "Brightness: " + this.getBrightness() + "\n");

        if (this.getLatestEvent() != null) {
            deviceString.append("Latest event: " + this.getLatestEvent().toString() + "\n");
        }
        return deviceString.toString();
    }
}
