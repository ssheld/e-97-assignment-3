package cscie97.smartcity.model;

/**
 * Author: Stephen Sheldon
 **/
public class Resident extends Person implements Cloneable {

    /**
     * The name of the resident.
     */
    private String name;

    /**
     * The phone number of the resident.
     */
    private String phoneNumber;

    /**
     * The role of the resident.
     */
    private Role role;

    /**
     * The blockchain account address of the resident.
     */
    private String blockchainAccountAddress;

    public Resident(String uuid) {
        super(uuid);
    }

    /**
     * The constructor for a resident.
     * @param personId      The globally unique identifier for the person.
     * @param biometricId   The biometric ID of the person.
     * @param latitude      The latitude of the person.
     * @param longitude     The longitude of the person.
     * @param name          The name of the resident.
     * @param phoneNumber   The phone number of the resident.
     * @param role          The role of the resident (child|adult|public administrator).
     * @param blockchainAccountAddress    The blockchain account address of the resident.
     */
    public Resident(String personId, String name, String biometricId, String phoneNumber, Role role,
                    double latitude, double longitude, String blockchainAccountAddress) {
        super(personId, biometricId, latitude, longitude);
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.role = role;
        this.blockchainAccountAddress = blockchainAccountAddress;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getBlockchainAccountAddress() {
        return blockchainAccountAddress;
    }

    public void setBlockchainAccountAddress(String blockchainAccountAddress) {
        this.blockchainAccountAddress = blockchainAccountAddress;
    }

    /**
     * Method to clone a Resident object.
     * @return Cloned copy of resident.
     */
    @Override
    protected Object clone() {
        Resident cloned;
        cloned = (Resident) super.clone();

        cloned.setUuid(this.getUuid());
        cloned.setBiometricId(this.getBiometricId());
        cloned.setCurrentCity(this.getCurrentCity());
        cloned.setLocation((Location)this.getLocation().clone());
        cloned.setName(this.getName());
        cloned.setPhoneNumber(this.getPhoneNumber());
        cloned.setRole(this.getRole());
        cloned.setBlockchainAccountAddress(this.getBlockchainAccountAddress());

        return cloned;
    }

    @Override
    public String toString() {
        return  "\n" + "Person Type: Resident" + "\n" +
                "Person ID: " + this.getUuid() + "\n" +
                "Biometric ID: " + this.getBiometricId() + "\n" +
                "Latitude: " + this.getLocation().getLatitude() + "\n" +
                "Longitude: " + this.getLocation().getLongitude() + "\n" +
                "Current City: " + this.getCurrentCity() + "\n" +
                "Name: " + this.getName() + "\n" +
                "Phone Number: " + this.getPhoneNumber() + "\n" +
                "Role: " + this.getRole().toString() + "\n" +
                "Blockchain Account Address: " + this.getBlockchainAccountAddress() + "\n";
    }
}
