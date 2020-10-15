package cscie97.smartcity.model;

/**
 * Author: Stephen Sheldon
 **/
public class Robot extends IotDevice implements Cloneable {

    /**
     * The activity associated with the robot.
     */
    private String activity;

    /**
     * The constructor for the Robot class.
     * @param uuid          The globally unique identifier for the IoT Device.
     * @param latitude      The current latitude of the IoT Device.
     * @param longitude     The current longitude of the IoT Device.
     * @param enabled       Whether the IoT device is enabled or disabled.
     * @param activity      The activity associated with the robot.
     */
    public Robot(String uuid, double latitude, double longitude,
                 Enabled enabled, String activity) {
        super(uuid, latitude, longitude, Status.READY, enabled);
        this.activity = activity;
    }

    public Robot(String uuid, double latitude, double longitude, Enabled enabled) {
        super(uuid, latitude, longitude, enabled);
    }

    public Robot(String uuid, double latitude, double longitude, String activity) {
        super(uuid, latitude, longitude);
        this.activity = activity;
    }

    public Robot(String uuid, Enabled enabled, String activity) {
        super(uuid, enabled);
        this.activity = activity;
    }

    public Robot(String uuid, double latitude, double longitude) {
        super(uuid, latitude, longitude);
    }

    public Robot(String uuid, Enabled enabled) {
        super(uuid, enabled);
    }

    public Robot(String uuid, String activity) {
        super(uuid);
        this.activity = activity;
    }

    public String getActivity() {
        return activity;
    }

    public void setActivity(String activity) {
        this.activity = activity;
    }

    /**
     * Clones a Robot object.
     * @return The cloned Robot object.
     */
    @Override
    protected Object clone() {
        Robot cloned;
        cloned = (Robot) super.clone();


        if (this.getActivity() != null) {
            cloned.setActivity(this.getActivity());
        }
        if (this.getLatestEvent() != null) {
            cloned.setLatestEvent((SensorEvent)this.getLatestEvent().clone());
        }

        return cloned;
    }

    @Override
    public String toString() {
        StringBuilder deviceString = new StringBuilder();

        deviceString.append("Device Type: Robot" + "\n" +
                "Device ID: " + this.getUuid() + "\n" +
                "Current Status: " + this.getCurrentStatus().toString() + "\n" +
                "Enabled: " + this.getEnabled().toString() + "\n" +
                "Latitude: " + this.getLocation().getLatitude() + "\n" +
                "Longitude: " + this.getLocation().getLongitude() + "\n" +
                "Current City: " + this.getCurrentCity() + "\n" +
                "Activity: " + this.getActivity() + "\n");

        if (this.getLatestEvent() != null) {
            deviceString.append("Latest event: " + this.getLatestEvent().toString() + "\n");
        }
        return deviceString.toString();
    }
}
