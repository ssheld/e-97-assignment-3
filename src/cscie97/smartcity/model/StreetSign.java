package cscie97.smartcity.model;

/**
 * Author: Stephen Sheldon
 **/
public class StreetSign extends IotDevice implements Cloneable {

    /**
     * The text to be displayed on the street sign.
     */
    private String text;

    /**
     * @param uuid          The globally unique identifier of the IoT Device.
     * @param latitude      The current latitude of the IoT Device.
     * @param longitude     The current longitude of the IoT Device.
     * @param enabled       Whether the IoT device is enabled or disabled.
     * @param text          The current text to be displayed on the street sign.
     */
    public StreetSign(String uuid, double latitude, double longitude,
                      Enabled enabled, String text) {
        super(uuid, latitude, longitude, Status.READY, enabled);
        this.text = text;
    }

    public StreetSign(String uuid, Enabled enabled, String text) {
        super(uuid, enabled);
        this.text = text;
    }

    public StreetSign(String uuid, Enabled enabled) {
        super(uuid, enabled);
    }

    public StreetSign(String uuid, String text) {
        super(uuid);
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    /**
     * Clones a StreetSign object.
     * @return The cloned StreetSign object.
     */
    @Override
    protected Object clone() {
        StreetSign cloned;
        cloned = (StreetSign) super.clone();

        if (this.getText() != null) {
            cloned.setText(this.getText());
        }
        if (this.getLatestEvent() != null) {
            cloned.setLatestEvent((SensorEvent)this.getLatestEvent().clone());
        }
        return cloned;
    }

    @Override
    public String toString() {
        StringBuilder deviceString = new StringBuilder();

        deviceString.append("Device Type: Street Sign" + "\n" +
                "Device ID: " + this.getUuid() + "\n" +
                "Current Status: " + this.getCurrentStatus().toString() + "\n" +
                "Enabled: " + this.getEnabled().toString() + "\n" +
                "Latitude: " + this.getLocation().getLatitude() + "\n" +
                "Longitude: " + this.getLocation().getLongitude() + "\n" +
                "Current City: " + this.getCurrentCity() + "\n" +
                "Text: " + this.getText() + "\n");

        if (this.getLatestEvent() != null) {
            deviceString.append("Latest event: " +this.getLatestEvent().toString() + "\n");
        }

        return deviceString.toString();
    }
}
