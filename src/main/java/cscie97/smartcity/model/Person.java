package cscie97.smartcity.model;

/**
 * Author: Stephen Sheldon
 **/
public abstract class Person implements Cloneable {

    /**
     * The globally unique identifier of a person.
     */
    private String uuid;

    /**
     * The biometric ID of a person.
     */
    private String biometricId;

    /**
     * The current city that the person is in.
     */
    public String currentCity;

    /**
     * The current latitude and longintude of the person.
     */
    private Location location;

    /**
     * Default constructor.
     */
    public Person() {

    }

    /**
     * Constructor for a Person.
     * @param personId     The globally unique identifier for a person.
     * @param biometricId  The biometric ID for a person.
     * @param latitude     The latitude of the person in degrees.
     * @param longitude    The longitude of the person in degrees.
     */
    public Person(String personId, String biometricId, double latitude, double longitude) {
        this.uuid = personId;
        this.biometricId = biometricId;
        this.location = new Location(latitude, longitude);
    }

    public Person(String personId, double latitude, double longitude) {
        this.uuid = personId;
        this.location = new Location(latitude, longitude);
    }

    public Person(String personId, String biometricId) {
        this.uuid = personId;
        this.biometricId = biometricId;
    }

    public Person(String uuid) {
        this.uuid = uuid;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getBiometricId() {
        return biometricId;
    }

    public void setBiometricId(String biometricId) {
        this.biometricId = biometricId;
    }

    public String getCurrentCity() {
        return currentCity;
    }

    public void setCurrentCity(String currentCity) {
        this.currentCity = currentCity;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    /**
     * Method to clone Person objects for deep copy.
     * @return  A deepy copy clone of Person object.
     */
    @Override
    protected Object clone() {
        Person cloned = null;
        try {
            cloned = (Person) super.clone();
        } catch(CloneNotSupportedException e) {
            System.out.println("Clone not supported exception in class Person.");
        }

        if (this.getUuid() != null)
            cloned.setUuid(this.getUuid());
        if (this.getCurrentCity() != null)
            cloned.setCurrentCity(this.getCurrentCity());
        if (this.getBiometricId() != null)
            cloned.setBiometricId(this.getBiometricId());
        if (this.getLocation() != null)
            cloned.setLocation((Location) this.getLocation().clone());

        return cloned;
    }
}