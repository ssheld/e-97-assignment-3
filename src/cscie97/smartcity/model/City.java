package cscie97.smartcity.model;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Author: Stephen Sheldon
 **/
public class City implements Cloneable {

    /**
     * The globally unique identifier of the City.
     */
    private String uuid;

    /**
     * The name of the city.
     */
    private String name;

    /**
     * The blockchain account associated with the city that
     * is used for receiving and sending money.
     */
    private String blockchainAccount;

    /**
     * The radius in kilometers of the city.
     */
    private Double radius;

    /**
     * The latitude and longitude of the city.
     */
    private Location location;

    /**
     * A map of all Persons currently in this city.
     */
    private Map<String, Person> personMap;

    /**
     * List of all devices currently in the city
     */
    List<IotDevice> devicesCurrentlyInCity;

    /**
     * The City constructor for creation of a new city.
     * @param cityId    The globally unique identifier of the city.
     * @param name      The name of the city.
     * @param account   The blockchain account associated with the city.
     * @param latitude  The latitude in degrees of the city.
     * @param longitude The longitude in degrees of the city.
     * @param radius    The radius in miles of the city.
     */
    public City(String cityId, String name, String account, double latitude, double longitude, double radius) {
        this.uuid = cityId;
        this.name = name;
        this.blockchainAccount = account;
        this.location = new Location(latitude, longitude);
        this.radius = radius;

        // Initialize our personMap
        personMap = new TreeMap<>();

        // Initialize device lists
        devicesCurrentlyInCity = new LinkedList<>();
    }

    public City(String cityId) {
        this.uuid = cityId;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBlockchainAccount() {
        return blockchainAccount;
    }

    public void setBlockchainAccount(String blockchainAccount) {
        this.blockchainAccount = blockchainAccount;
    }

    public Double getRadius() {
        return radius;
    }

    public void setRadius(Double radius) {
        this.radius = radius;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public void addPerson(Person person) {
        personMap.put(person.getUuid(), person);
    }

    public void addDevicesCurrentlyInCity(IotDevice iotDevice) {
        devicesCurrentlyInCity.add(iotDevice);
    }

    public void setPersonMap(Map<String, Person> personMap) {
        this.personMap = personMap;
    }

    public void setDevicesCurrentlyInCity(List<IotDevice> devicesCurrentlyInCity) {
        this.devicesCurrentlyInCity = devicesCurrentlyInCity;
    }

    /**
     * Clone method for cloning City objects for deep copies.
     * @return Cloned copy of City.
     */
    @Override
    protected Object clone() {

        City cloned = null;
        Map<String, Person> clonedPersonMap = new TreeMap<>();
        List<IotDevice> clonedDeviceList = new LinkedList<>();
        Person clonedPerson;

        try {
            cloned = (City) super.clone();
        } catch (CloneNotSupportedException e) {
            System.out.println("Clone not supported exception in City class.");
        }

        cloned.setUuid(this.getUuid());
        cloned.setName(this.getName());
        cloned.setBlockchainAccount(this.getBlockchainAccount());
        cloned.setRadius(this.getRadius());
        cloned.setLocation((Location)this.location.clone());

        for (IotDevice iotDevice : devicesCurrentlyInCity) {
            clonedDeviceList.add((IotDevice) iotDevice.clone());
        }

        // Clone all persons in personMap
        for (Map.Entry<String, Person> entry : personMap.entrySet()) {
            clonedPerson = (Person) entry.getValue().clone();
            clonedPersonMap.put(clonedPerson.getUuid(), clonedPerson);
        }

        cloned.setPersonMap(clonedPersonMap);
        cloned.setDevicesCurrentlyInCity(clonedDeviceList);

        return cloned;
    }

    @Override
    public String toString() {
        StringBuilder personString = new StringBuilder();
        StringBuilder devicesInsideCityString = new StringBuilder();
        StringBuilder cityString = new StringBuilder();

        // Create string of all persons
        for (Map.Entry<String, Person> entry : personMap.entrySet()) {
            personString.append(entry.getValue().toString());
            personString.append("\n");
        }

        cityString.append("City ID: " + this.getUuid()  + "\n" +
                "City Name: " + this.getName() + "\n" +
                "City Latitude: " + this.getLocation().getLatitude() + "\n" +
                "City Longitude: " + this.getLocation().getLongitude() + "\n" +
                "Persons in City: " + "\n" +
                personString.toString() + "\n");

        cityString.append("IoT Devices currently within the city limits: " + "\n");

        if (!devicesCurrentlyInCity.isEmpty()) {

            for (IotDevice iotDevice : devicesCurrentlyInCity) {
                devicesInsideCityString.append(iotDevice.clone());
                devicesInsideCityString.append("\n");
            }
            // Then append to cityString
            cityString.append(devicesInsideCityString);
        }
        return cityString.toString();
    }
}
