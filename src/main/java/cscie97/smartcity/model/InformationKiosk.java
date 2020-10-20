package cscie97.smartcity.model;

/**
 * Author: Stephen Sheldon
 **/
public class InformationKiosk extends IotDevice implements Cloneable {

    /**
     * The image URI of the image to be displayed on the
     * information kiosk.
     */
    private String imageUri;

    /**
     * Constructor for an InformationKiosk.
     * @param uuid          The globally unique identifier for the IoT Device.
     * @param latitude      The current latitude of the IoT Device.
     * @param longitude     The current longitude of the IoT Device.
     * @param enabled       Whether the IoT device is enabled or disabled.
     * @param imageUri      The image URI of the image to be dispalyed on the information kiosk.
     */
    public InformationKiosk(String uuid, double latitude, double longitude,
                            Enabled enabled, String imageUri) {
        super(uuid, latitude, longitude, Status.READY, enabled);
        this.imageUri = imageUri;
    }

    public InformationKiosk(String uuid, Enabled enabled, String imageUri) {
        super(uuid, enabled);
        this.imageUri = imageUri;
    }

    public InformationKiosk(String uuid, Enabled enabled) {
        super(uuid, enabled);
    }

    public InformationKiosk(String uuid, String imageUri) {
        super(uuid);
        this.imageUri = imageUri;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    /**
     * Clones a InformationKiosk object.
     * @return The cloned InformationKiosk object.
     */
    @Override
    protected Object clone() {
        InformationKiosk cloned;
        cloned = (InformationKiosk) super.clone();

        if (this.getImageUri() != null) {
            cloned.setImageUri(this.getImageUri());
        }
        if (this.getLatestEvent() != null) {
            cloned.setLatestEvent((SensorEvent)this.getLatestEvent().clone());
        }

        return cloned;
    }

    @Override
    public String toString() {
        StringBuilder deviceString = new StringBuilder();

        deviceString.append("Device Type: Information Kiosk" + "\n" +
                "Device ID: " + this.getUuid() + "\n" +
                "Current Status: " + this.getCurrentStatus().toString() + "\n" +
                "Enabled: " + this.getEnabled().toString() + "\n" +
                "Latitude: " + this.getLocation().getLatitude() + "\n" +
                "Longitude: " + this.getLocation().getLongitude() + "\n" +
                "Current City: " + this.getCurrentCity() + "\n" +
                "Text: " + this.getImageUri() + "\n");

        if (this.getLatestEvent() != null) {
            deviceString.append("Latest event: " + this.getLatestEvent().toString() + "\n");
        }
        return deviceString.toString();
    }
}
