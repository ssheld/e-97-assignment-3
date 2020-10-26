package cscie97.smartcity.model;

import cscie97.smartcity.controller.LoggerUtil;

import java.util.logging.Level;

/**
 * Author: Stephen Sheldon
 **/
public abstract class IotDevice implements Cloneable {

    /**
     * Globally unique identifier for IoT device.
     */
    private String uuid;

    /**
     * Current status of the device (Ready|Offline).
     */
    Status currentStatus;

    /**
     * Stores whether the device is currently enabled or disabled.
     */
    Enabled enabled;

    /**
     * The current latitude and longitude of the IoT Device.
     */
    Location location;

    /**
     * The latest event that the device has processed.
     */
    private SensorEvent latestEvent;

    /**
     * The current city that the device is in.
     */
    private String currentCity;

    /**
     * Constructor for IotDevice
     * @param uuid           The globally unique identifier for an IoT Device.
     * @param currentStatus  The current status of the IoT Device.
     * @param enabled        Is IoT device enabled or disabled.
     */
    public IotDevice(String uuid, double latitude, double longitude, Status currentStatus, Enabled enabled) {
        this.uuid = uuid;
        this.location = new Location(latitude, longitude);
        this.currentStatus = currentStatus;
        this.enabled = enabled;
    }

    public IotDevice(String uuid, double latitude, double longitude, Enabled enabled) {
        this.uuid = uuid;
        this.location = new Location(latitude, longitude);
        this.enabled = enabled;
    }

    public IotDevice(String uuid, double latitude, double longitude) {
        this.uuid = uuid;
        this.location = new Location(latitude, longitude);
    }

    public IotDevice(String uuid, Enabled enabled) {
        this.uuid = uuid;
        this.enabled = enabled;
    }

    public IotDevice(String uuid) {
        this.uuid = uuid;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public Status getCurrentStatus() {
        return currentStatus;
    }

    public void setCurrentStatus(Status currentStatus) {
        this.currentStatus = currentStatus;
    }

    public Enabled getEnabled() {
        return enabled;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public void setEnabled(Enabled enabled) {
        this.enabled = enabled;
    }

    public SensorEvent getLatestEvent() {
        return latestEvent;
    }

    public void setLatestEvent(SensorEvent latestEvent) {
        this.latestEvent = latestEvent;
    }

    public String getCurrentCity() {
        return currentCity;
    }

    public void setCurrentCity(String currentCity) {
        this.currentCity = currentCity;
    }

    /**
     * Method to clone IoTDevice Objects
     * @return Cloned IotDevice object.
     */
    @Override
    protected Object clone() {
        IotDevice cloned = null;
        try {
            cloned = (IotDevice) super.clone();
        } catch (CloneNotSupportedException e) {
            LoggerUtil.log(Level.SEVERE, "Clone not supported exception in class IoTDevice.", false);
        }

        if (this.getUuid() != null) {
            cloned.setUuid(this.getUuid());
        }
        if (this.getLocation() != null) {
            cloned.setLocation((Location) this.getLocation().clone());
        }
        if (this.getCurrentCity() != null) {
            cloned.setCurrentCity(this.getCurrentCity());
        }
        if (this.getCurrentStatus() != null) {
            cloned.setCurrentStatus(this.getCurrentStatus());
        }
        if (this.getEnabled() != null) {
            cloned.setEnabled(this.getEnabled());
        }
        if (this.getLatestEvent() != null) {
            cloned.setLatestEvent((SensorEvent) this.getLatestEvent().clone());
        }

        return cloned;
    }

    /**
     * Processes sensor output by printing log
     * @param sensorOutput Sensor output to be processed by device
     */
    public void processSensorOutput(SensorOutput sensorOutput) {
        LoggerUtil.log(Level.INFO, "Device ID: " + sensorOutput.getDeviceId() + " " + sensorOutput.getValue(), true);
    }
}
