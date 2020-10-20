package cscie97.smartcity.model;

/**
 * Author: Stephen Sheldon
 **/
public class ParkingSpace extends IotDevice implements Cloneable {

    /**
     * The hourly rate that a user is charged for parking
     * in the parking space.
     */
    private Integer rate;

    /**
     * The account address of the owner of the vehicle that is
     * currently parked in the parking space.
     */
    private String vehicleAccountAddress;

    /**
     * Constructor for a ParkingSpace.
     * @param uuid           Globally unique identifier for the IoT Device.
     * @param enabled        Whether the IoT device is enabled or disabled.
     * @param rate           The hourly rate associated with the parking space.
     */
    public ParkingSpace(String uuid, double latitude, double longitude, Enabled enabled, Integer rate) {
        super(uuid, latitude, longitude, Status.READY, enabled);
        this.rate = rate;
    }

    public ParkingSpace(String uuid, Enabled enabled, Integer rate) {
        super(uuid, enabled);
        this.rate = rate;
    }

    public ParkingSpace(String uuid, Integer rate) {
        super(uuid);
        this.rate = rate;
    }

    public ParkingSpace(String uuid, Enabled enabled) {
        super(uuid, enabled);
    }

    public Integer getRate() {
        return rate;
    }

    public void setRate(Integer rate) {
        this.rate = rate;
    }

    public String getVehicleAccountAddress() {
        return vehicleAccountAddress;
    }

    public void setVehicleAccountAddress(String vehicleAccountAddress) {
        this.vehicleAccountAddress = vehicleAccountAddress;
    }

    /**
     * Clones a ParkingSpace object.
     * @return The cloned ParkingSpace object.
     */
    @Override
    protected Object clone() {
        ParkingSpace cloned;
        cloned = (ParkingSpace) super.clone();

        if (this.getRate() != null) {
            cloned.setRate(this.getRate());
        }
        if (this.getVehicleAccountAddress() != null) {
            cloned.setVehicleAccountAddress(this.getVehicleAccountAddress());
        }
        if (this.getLatestEvent() != null) {
            cloned.setLatestEvent((SensorEvent)this.getLatestEvent().clone());
        }

        return cloned;
    }

    @Override
    public String toString() {

        StringBuilder deviceString = new StringBuilder();

        deviceString.append("Device Type: Parking space" + "\n" +
                "Device ID: " + this.getUuid() + "\n" +
                "Current Status: " + this.getCurrentStatus().toString() + "\n" +
                "Enabled: " + this.getEnabled().toString() + "\n" +
                "Latitude: " + this.getLocation().getLatitude() + "\n" +
                "Longitude: " + this.getLocation().getLongitude() + "\n" +
                "Current City: " + this.getCurrentCity() + "\n" +
                "Rate: " + this.getRate() + "\n");

        if (this.getVehicleAccountAddress() != null) {
            deviceString.append(this.getVehicleAccountAddress() + "\n");
        }

        if (this.getLatestEvent() != null) {
            deviceString.append("Latest event: " + this.getLatestEvent().toString() + "\n");
        }
        return deviceString.toString();
    }
}
