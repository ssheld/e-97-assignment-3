package cscie97.smartcity.model;

/**
 * Author: Stephen Sheldon
 **/
public class Visitor extends Person implements Cloneable {

    /**
     * Constructor for Visitor class.
     * @param personId      The globally unique identifier for the person.
     * @param biometricId   The biometric ID of the person.
     * @param latitude      The latitude of the person.
     * @param longitude     The longitude of the person.
     */
    public Visitor(String personId, String biometricId, double latitude, double longitude) {
        super(personId, biometricId, latitude, longitude);
    }

    public Visitor(String personId, double latitude, double longitude) {
        super(personId, latitude, longitude);
    }
    public Visitor(String personId, String biometricId) {
        super(personId, biometricId);
    }

    /**
     * Method to clone a Visitor object.
     * @return The cloned visitor object.
     */
    @Override
    protected Object clone() {
        Visitor cloned;

        cloned = (Visitor) super.clone();

        if (this.getUuid() != null)
            cloned.setUuid(this.getUuid());
        if (this.getBiometricId() != null)
            cloned.setBiometricId(this.getBiometricId());
        if (this.getCurrentCity() != null)
            cloned.setCurrentCity(this.getCurrentCity());
        if (this.getLocation() != null)
            cloned.setLocation((Location)this.getLocation().clone());

        return cloned;
    }

    @Override
    public String toString() {
        return "\n" + "Person Type: Visitor" + "\n" +
                "Person ID: " + this.getUuid() + "\n" +
                "Biometric ID: " + this.getBiometricId() + "\n" +
                "Latitude: " + this.getLocation().getLatitude() + "\n" +
                "Longitude: " + this.getLocation().getLongitude() + "\n" +
                "Current City: " + this.getCurrentCity() + "\n";
    }
}
