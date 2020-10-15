package cscie97.smartcity.model;

/**
 * Author: Stephen Sheldon
 **/
public class Vehicle extends IotDevice implements Cloneable {

    /**
     * The vehicle type of the vehicle (car|bus).
     */
    private VehicleType vehicleType;

    /**
     * The activity associated with the vehicle.
     */
    private String activity;

    /**
     * The capacity of the vehicle.
     */
    private Integer capacity;

    /**
     * The fee associated with utilizing the vehicle.
     */
    private Integer fee;

    /**
     * Constructor for a vehicle object.
     * @param uuid          The globally unique identifier for the IoT Device.
     * @param latitude      The current latitude of the IoT Device.
     * @param longitude     The current longitude of the IoT Device.
     * @param enabled       Whether the IoT device is enabled or disabled.
     * @param vehicleType   The type of vehicle (bus|car).
     * @param activity      The activity associated with the vehicle.
     * @param capacity      The capacity of the vehicle.
     * @param fee           The fee associated with using the vehicle.
     */
    public Vehicle(String uuid, double latitude, double longitude,
                   Enabled enabled, VehicleType vehicleType,
                   String activity, Integer capacity, Integer fee) {
        super(uuid, latitude, longitude, Status.READY, enabled);
        this.vehicleType = vehicleType;
        this.activity = activity;
        this.capacity = capacity;
        this.fee = fee;
    }

    public Vehicle(String uuid, double latitude, double longitude,
                   Enabled enabled, String activity, Integer fee) {
        super(uuid, latitude, longitude, enabled);
        this.activity = activity;
        this.fee = fee;
    }

    public Vehicle(String uuid, double latitude, double longitude, Enabled enabled, String activity) {
        super(uuid, latitude, longitude, enabled);
        this.activity = activity;
    }

    public Vehicle(String uuid, double latitude, double longitude, Enabled enabled, Integer fee) {
        super(uuid, latitude, longitude, enabled);
        this.fee = fee;
    }

    public Vehicle(String uuid, Enabled enabled, String activity, Integer fee) {
        super(uuid, enabled);
        this.activity = activity;
        this.fee = fee;
    }

    public Vehicle(String uuid, double latitude, double longitude, Enabled enabled) {
        super(uuid, latitude, longitude, enabled);
    }

    public Vehicle(String uuid, double latitude, double longitude, String activity) {
        super(uuid, latitude, longitude);
        this.activity = activity;
    }

    public Vehicle(String uuid, double latitude, double longitude, Integer fee) {
        super(uuid, latitude, longitude);
        this.fee = fee;
    }

    public Vehicle(String uuid, Enabled enabled, String activity) {
        super(uuid, enabled);
        this.activity = activity;
    }

    public Vehicle(String uuid, Enabled enabled, Integer fee) {
        super(uuid, enabled);
        this.fee = fee;
    }

    public Vehicle(String uuid, String activity, Integer fee) {
        super(uuid);
        this.activity = activity;
        this.fee = fee;
    }

    public Vehicle(String uuid, double latitude, double longitude) {
        super(uuid, latitude, longitude);
    }

    public Vehicle(String uuid, Enabled enabled) {
        super(uuid, enabled);
    }

    public Vehicle(String uuid, String activity) {
        super(uuid);
        this.activity = activity;
    }

    public Vehicle(String uuid, Integer fee) {
        super(uuid);
        this.fee = fee;
    }

    public VehicleType getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(VehicleType vehicleType) {
        this.vehicleType = vehicleType;
    }

    public String getActivity() {
        return activity;
    }

    public void setActivity(String activity) {
        this.activity = activity;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public Integer getFee() {
        return fee;
    }

    public void setFee(int fee) {
        this.fee = fee;
    }

    /**
     * Clone method used to clone a Vehicle object.
     * @return A cloned vehicle object.
     */
    @Override
    protected Object clone() {
        Vehicle cloned;
        cloned = (Vehicle) super.clone();

        if (this.getActivity() != null) {
            cloned.setActivity(this.getActivity());
        }
        if (this.getCapacity() != null) {
            cloned.setCapacity(this.getCapacity());
        }
        if (this.getFee() != null) {
            cloned.setFee(this.getFee());
        }
        if (this.getVehicleType() != null) {
            cloned.setVehicleType(this.getVehicleType());
        }
        if (this.getLatestEvent() != null) {
            cloned.setLatestEvent((SensorEvent)this.getLatestEvent().clone());
        }

        return cloned;
    }

    @Override
    public String toString() {
        StringBuilder deviceString = new StringBuilder();

        deviceString.append("Device Type: Vehicle" + "\n" +
                "Device ID: " + this.getUuid() + "\n" +
                "Current Status: " + this.getCurrentStatus().toString() + "\n" +
                "Enabled: " + this.getEnabled().toString() + "\n" +
                "Latitude: " + this.getLocation().getLatitude() + "\n" +
                "Longitude: " + this.getLocation().getLongitude() + "\n" +
                "Current City: " + this.getCurrentCity() + "\n" +
                "Vehicle Type: " + this.getVehicleType().toString() + "\n" +
                "Activity: " + this.getActivity() + "\n" +
                "Capacity: " + this.getCapacity() + "\n" +
                "Fee: " + this.getFee() + "\n");

        if (this.getLatestEvent() != null) {
            deviceString.append("Latest event: " + this.getLatestEvent().toString() + "\n");
        }
        return deviceString.toString();
    }
}
