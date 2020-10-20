package cscie97.smartcity.model;

/**
 * Author: Stephen Sheldon
 **/
public class Location implements Cloneable {

    /**
     * The latitude in degrees of the location object.
     */
    private double latitude;

    /**
     * The longitude in degrees of the location object.
     */
    private double longitude;

    /**
     * Constructor for location objects
     *
     * @param latitude  latitude in degrees of location object
     * @param longitude longitude in degrees of location object
     */
    public Location(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    /**
     * Clone method for cloning location objects for deep copies.
     * @return Cloned copy of Location.
     */
    @Override
    protected Object clone() {
        Location cloned = null;
        try {
            cloned = (Location) super.clone();
        } catch (CloneNotSupportedException e) {
            System.out.println("Clone not supported exception in Location class.");
        }

        cloned.setLatitude(this.getLatitude());
        cloned.setLongitude(this.getLongitude());

        return cloned;
    }
}
