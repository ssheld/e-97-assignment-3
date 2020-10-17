package cscie97.smartcity.model;


import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Author: Stephen Sheldon
 **/
public class CityModelService implements Subject {

    /**
     * A map of all city objects being managed by the City Model Service.
     * cityMap uses the city ID of the City object for the key and
     * the City object for the value.
     */
    private Map<String, City> cityMap;

    /**
     * A map of all person objects being manged by the City Model Service.
     * personMap uses the person ID of the Person object for the key and
     * the Person object for the value.
     */
    private Map<String, Person> personMap;

    /**
     * A map of all IotDevice objects being managed by the City Model Service.
     * iotDeviceMap uses the IoT Device ID of the IotDevice object for the key and
     * the IotDevice object for the value.
     */
    private Map<String, IotDevice> iotDeviceMap;

    /**
     * A list of all observers that are registered to watch the Model Service subject.
     */
    private List<Observer> observerList;


    /**
     * Constructor for CityModelService
     */
    public CityModelService() {
        this.cityMap = new TreeMap<>();
        this.personMap = new TreeMap<>();
        this.iotDeviceMap = new TreeMap<>();
    }

    /**
     * Method to create a new City object. A city object once
     * created is added to the cityMap.
     * @param city The City object to be created.
     * @return     The City object that was created.
     */
    public City createCity(City city) throws CityModelServiceException {

        // Check to make sure that the new city has an identifier
        if (city.getUuid() == null) {
            throw new CityModelServiceException("define city", "The city ID is null.");
        }
        // Check to make sure that the new city is not trying to use a uuid already in use
        else if (cityMap.containsKey(city.getUuid())) {
            throw new CityModelServiceException("define city", "The identifier value you entered is already in use.");
        }
        // Check to make sure that a blockchain account has been specified
        else if (city.getBlockchainAccount() == null) {
            throw new CityModelServiceException("define city", "The blockchain account value is null.");
        }
        // Check to make sure a city name has been specified
        else if (city.getName() == null) {
            throw new CityModelServiceException("define city", "The city name is null.");
        }
        // Check to make sure location is not null
        else if (city.getLocation() == null) {
            throw new CityModelServiceException("define city", "The city's location is null.");
        }
        // Check to make sure a city's radius is greater than 0 meters
        else if (city.getRadius() <= 0) {
            throw new CityModelServiceException("define city", "The city has a radius value that is not a positive number greater than zero");
        }

        // Do a deep copy of the city.
        City clonedCity = (City) city.clone();

        // Add the city to the cityMap
        cityMap.put(clonedCity.getUuid(), clonedCity);

        // Return the original city since we don't want the user to have
        // a reference to the one we plan to persist.
        return city;
    }

    /**
     * Method to create a person and add them to the personMap
     * @param person The person object to be created.
     * @return       The person object that was successfully created.
     * @throws CityModelServiceException
     */
    public Person createPerson(Person person) throws CityModelServiceException {

        Resident uniqueResident, clonedResident;
        Visitor uniqueVisitor, clonedVisitor;

        // Check to make sure global identifier is not null
        if (person.getUuid() == null) {
            throw new CityModelServiceException("define person", "The person's global identifier is null.");
        }
        // Check to make sure the global identifier is unique
        else if (personMap.containsKey(person.getUuid())) {
            throw new CityModelServiceException("define person", "The person's global identifier is not unique.");
        }
        // Check to make sure biometric ID is not null
        else if (person.getBiometricId() == null) {
            throw new CityModelServiceException("define person", "The person's biometric ID is null.");
        }
        // Check to make sure location is not null
        else if (person.getLocation() == null) {
            throw new CityModelServiceException("define person", "The city's location is null.");
        }

        // Set current city
        person.setCurrentCity(findCurrentCity(person.getLocation()));

        // Check if person is a resident
        if (person instanceof Resident) {

            // Check to make sure the resident has a name
            if (((Resident) person).getName() == null) {
                throw new CityModelServiceException("define person", "The resident has no name.");
            }
            // Check to make sure the resident has a phone number
            else if (((Resident) person).getPhoneNumber() == null) {
                throw new CityModelServiceException("define person", "The resident has no phone number.");
            }
            // Check to make sure the resident has a role
            else if (((Resident) person).getRole() == null) {
                throw new CityModelServiceException("define person", "The resident has assigned role.");
            }
            // Check to make sure the resident has a blockchain account address.
            else if (((Resident) person).getBlockchainAccountAddress() == null) {
                throw new CityModelServiceException("define person", "The resident has no blockchain account address");
            }

            // Now we know the resident is valid so clone it
            uniqueResident = (Resident) person;
            clonedResident = (Resident) uniqueResident.clone();

            // Add the cloned resident to our personMap
            personMap.put(clonedResident.getUuid(), clonedResident);
        }
        // Check if person is a visitor
        else if (person instanceof Visitor) {

            // If the person is a valid person then they are a valid visitor
            // so clone them.
            uniqueVisitor = (Visitor) person;
            clonedVisitor = (Visitor) uniqueVisitor.clone();

            // Add the cloned visitor to our personMap
            personMap.put(clonedVisitor.getUuid(), clonedVisitor);
        }
        // If it's neither then we have a problem
        else {
            throw new CityModelServiceException("define Person", "The person is neither a resident or a visitor.");
        }

        // Return the person object
        return person;
    }

    /**
     * Creates a new IoTDevice that's either a StreetLight, StreetSign, Robot, Vehicle, ParkingSpace, or InformationKiosk.
     * @param iotDevice The IoT Device to be created.
     * @return          The IoT Device that was successfully created.
     * @throws CityModelServiceException
     */
    public IotDevice createIotDevice(IotDevice iotDevice) throws CityModelServiceException {

        // Make sure Device ID isn't null
        if (iotDevice.getUuid() == null ) {
            throw new CityModelServiceException("Define IoTDevice", "The IoT device ID is null.");
        }
        // Check if the Device ID is globally unique
        else if (iotDeviceMap.containsKey(iotDevice.getUuid())) {
            throw new CityModelServiceException("Define IoTDevice", "The IoT Device ID is already in use.");
        }

        // Set current city
        iotDevice.setCurrentCity(findCurrentCity(iotDevice.location));

        // Check to see what kind of IoT Device we are creating
        if (iotDevice instanceof StreetLight) {

            // Clone the device to the proper subclass
            StreetLight streetLight = (StreetLight) iotDevice.clone();

            // Add the device to the iotDeviceMap
            iotDeviceMap.put(streetLight.getUuid(), streetLight);
        } else if (iotDevice instanceof StreetSign) {

            StreetSign streetSign = (StreetSign) iotDevice.clone();

            iotDeviceMap.put(streetSign.getUuid(), streetSign);
        } else if (iotDevice instanceof Vehicle) {

            Vehicle vehicle = (Vehicle) iotDevice.clone();

            iotDeviceMap.put(vehicle.getUuid(), vehicle);
        } else if (iotDevice instanceof InformationKiosk) {

            InformationKiosk informationKiosk = (InformationKiosk) iotDevice.clone();

            iotDeviceMap.put(informationKiosk.getUuid(), informationKiosk);
        } else if (iotDevice instanceof Robot) {

            Robot robot = (Robot) iotDevice.clone();

            iotDeviceMap.put(robot.getUuid(), robot);
        } else if (iotDevice instanceof ParkingSpace) {

            ParkingSpace parkingSpace = (ParkingSpace) iotDevice.clone();

            iotDeviceMap.put(parkingSpace.getUuid(), parkingSpace);
        }

        return iotDevice;
    }

    /**
     * Creates a SensorOutput object
     * @param sensorOutput The sensor output object to create.
     * @return             The sensor output object that was created.
     */
    public SensorOutput createSensorOutput(SensorOutput sensorOutput) {

        // Clone the sensor output
        SensorOutput clonedSensorOutput = (SensorOutput) sensorOutput.clone();

        return (SensorOutput) clonedSensorOutput.clone();
    }

    /**
     * Creates a SensorEvent object.
     * @param sensorEvent The SensorEvent object to create.
     * @return            The created SensorEvent object.
     */
    public SensorEvent createSensorEvent(SensorEvent sensorEvent) {

        // Clone the sensor event
        SensorEvent clonedSensorEvent = (SensorEvent) sensorEvent.clone();

        // Check if this is a global sensor event or device specific
        if (sensorEvent.getDeviceId() == null) {
            // It's global so apply it to all devices within the cityId
            for (Map.Entry<String, IotDevice> entry : iotDeviceMap.entrySet()) {
                if (entry.getValue().getCurrentCity().equalsIgnoreCase(sensorEvent.getCityId())) {
                    entry.getValue().setLatestEvent(clonedSensorEvent);
                }
            }
        } else {
            // It's specific to a device ID so only apply it to that device
            iotDeviceMap.get((clonedSensorEvent.getCityId() + ":" + clonedSensorEvent.getDeviceId())).setLatestEvent(clonedSensorEvent);
        }

        return (SensorEvent) clonedSensorEvent.clone();
    }

    /**
     * Gets the person object with the specified personId.
     * @param personId The globally unique identifier of the person.
     * @return         The Person with the specified personId.
     * @throws CityModelServiceException
     */
    public Person getPerson(String personId) throws CityModelServiceException {

        if (!personMap.containsKey(personId)) {
            throw new CityModelServiceException("show person", "There is no person with the personId specified.");
        }

        // Check if person is a resident, if so return a copy of the resident.
        if (personMap.get(personId) instanceof Resident) {
            return (Resident) personMap.get(personId).clone();
        }
        // If the person isn't a resident then they're a visitor.
        else {
            return (Visitor) personMap.get(personId).clone();
        }
    }

    /**
     * Returns the City object corresponding to the given CityId
     * @param cityId The globally unique identifier of the city.
     * @return       The city object associated with the city ID.
     * @throws CityModelServiceException
     */
    public City getCity(String cityId) throws CityModelServiceException {
        City clonedCity;

        // Make sure the city exist
        if (!cityMap.containsKey(cityId)) {
            throw new CityModelServiceException("show city", "There is no city with the city ID of " + cityId);
        }

        clonedCity = (City) cityMap.get(cityId).clone();

        // Find all Iot Devices currently in city
        for (Map.Entry<String, IotDevice> entry : iotDeviceMap.entrySet()) {
            if (entry.getValue().getCurrentCity() != null) {
                // Check if the device is currently in the city
                if (entry.getValue().getCurrentCity().equalsIgnoreCase(clonedCity.getUuid())) {
                    // If the device is currently in the city add to list
                    clonedCity.addDevicesCurrentlyInCity((IotDevice) entry.getValue().clone());
                }
            }
        }

        // Find all Persons currently in city
        for (Map.Entry<String, Person> entry : personMap.entrySet()) {
            if (entry.getValue().getCurrentCity() != null) {
                // Check if the person is currently in the city
                if (entry.getValue().getCurrentCity().equalsIgnoreCase(clonedCity.getUuid())) {
                    clonedCity.addPerson((Person) entry.getValue().clone());
                }
            }
        }

        return clonedCity;
    }

    /**
     * Method to get all cities maintained by the CityModelService
     * @return A list of all cities maintained by the CityModelSerivce
     */
    public List<City> getCityList() {
        List<City> cityList = new LinkedList<>();
        City clonedCity;

        for (Map.Entry<String, City> entry : cityMap.entrySet()) {
            clonedCity = (City) entry.getValue().clone();

            // Check what devices are currently in the city
            for (Map.Entry<String, IotDevice> device : iotDeviceMap.entrySet()) {
                // Check if the device is currently in the city
                if (device.getValue().getCurrentCity() != null) {
                    if (device.getValue().getCurrentCity().equals(clonedCity.getUuid())) {
                        // If the device is currently in the city add to list
                        clonedCity.addDevicesCurrentlyInCity((IotDevice) device.getValue().clone());
                    }
                }
            }
            cityList.add(clonedCity);
        }
        return cityList;
    }

    /**
     * Gets all IoT devices within a city with the specified cityId.
     * @param cityId  The city ID of the City to return IoT devices from.
     * @return        A list of all IoT devices associated with city with cityId
     * @throws CityModelServiceException
     */
    public List<IotDevice> getIotDevice(String cityId) throws CityModelServiceException {

        IotDevice clonedDevice;
        City clonedCity;
        String uuid;
        String[] splitUuid;
        List<IotDevice> devicesAssociatedWithCity = new LinkedList<>();

        // Make sure the city exist
        if (!cityMap.containsKey(cityId)) {
            throw new CityModelServiceException("show device", "There is no city with the city ID of " + cityId);
        }

        clonedCity = (City) cityMap.get(cityId).clone();

        // Now iterate through all devices and see which ones are associated with this city
        for (Map.Entry<String, IotDevice> entry : iotDeviceMap.entrySet()) {
            uuid = entry.getValue().getUuid();
            splitUuid = uuid.split(":");

            if (splitUuid[0].equalsIgnoreCase(clonedCity.getUuid())) {
                // This device is associated with city so add to list
                clonedDevice = (IotDevice) entry.getValue().clone();
                devicesAssociatedWithCity.add(clonedDevice);
            }
        }

        return devicesAssociatedWithCity;
    }

    /**
     * Gets a specific IoT device within a city with the specified cityId.
     * @param cityId    The cityId corresponding to the city in which the IoT Device resides.
     * @param deviceId  The deviceId of the device to return.
     * @return          The IoT Device with the corresponding deviceId.
     * @throws CityModelServiceException
     */
    public IotDevice getIotDevice(String cityId, String deviceId) throws CityModelServiceException {

        StringBuilder fullDeviceId = new StringBuilder();

        fullDeviceId.append(cityId);
        fullDeviceId.append(":");
        fullDeviceId.append(deviceId);

       // Make sure the city exist
        if (!cityMap.containsKey(cityId)) {
            throw new CityModelServiceException("show device", "There is no city with the city ID of " + cityId);
        }
        // Make sure the device exist
        else if (!iotDeviceMap.containsKey(fullDeviceId.toString())) {
            throw new CityModelServiceException("show device", "There is no device with device ID of " + fullDeviceId);
        }
        return (IotDevice) iotDeviceMap.get(fullDeviceId.toString()).clone();
    }



    /**
     * Citation: https://stackoverflow.com/questions/3694380/calculating-distance-between-two-points-using-latitude-longitude
     *
     * Calculate distance between two points in latitude and longitude taking
     * into account height difference. If you are not interested in height
     * difference pass 0.0. Uses Haversine method as its base.
     *
     * lat1, lon1 Start point lat2, lon2 End point el1 Start altitude in meters
     * el2 End altitude in meters
     * @return Distance in kilometers
     */
    public static double distance(double lat1, double lat2, double lon1,
                                  double lon2, double el1, double el2) {

        final int R = 6371; // Radius of the earth

        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = R * c; // leave in kilometers

        double height = el1 - el2;

        distance = Math.pow(distance, 2) + Math.pow(height, 2);

        return Math.sqrt(distance);
    }

    /**
     * Returns the current city in which the given location falls in.
     * @param location The latitude and longitude to find what city it belongs to.
     * @return         The city ID of the city that the latitude and longitude are in.
     */
    private String findCurrentCity(Location location) {

        double cityLat, cityLong, latitude, longitude, cityRadius, distanceBetween;
        String currentCity = null;

        latitude = location.getLatitude();
        longitude = location.getLongitude();

        // Iterate through cities to find which this location falls in.
        for (Map.Entry<String, City> entry : cityMap.entrySet()) {

            cityLat = entry.getValue().getLocation().getLatitude();
            cityLong = entry.getValue().getLocation().getLongitude();
            cityRadius = entry.getValue().getRadius();

            // Calculate distance between the city and location.
            distanceBetween = distance(cityLat, latitude, cityLong, longitude, 0.0, 0.0);

            // Check if the distance is less than or equal to radius
            if (distanceBetween <= cityRadius) {
                // Set the currentCity
                currentCity = entry.getValue().getUuid();
                break;
            }
        }
        return currentCity;
    }

    /**
     * Updates a Person (Visitor or Resident)
     * @param person The Person object with updated values.
     * @return       The updated Person object.
     * @throws CityModelServiceException
     */
    public Person updatePerson(Person person) throws CityModelServiceException {
        // First check if the person exists
        if (!personMap.containsKey(person.getUuid())) {
            throw new CityModelServiceException("update person", "Person does not exist with ID " + person.getUuid());
        }

        // Check if Person is a Resident
        if (person instanceof Resident) {

            // Clone the person to the Resident subclass
            Resident clonedResident = (Resident) person.clone();

            // Get the Resident object to be updated
            Resident updatedResident = (Resident) personMap.get(clonedResident.getUuid());

            // Check if name has been updated
            if (clonedResident.getName() != null) {
                // Set updated name
                updatedResident.setName(clonedResident.getName());
            }
            // Check if biometric ID has been updated
            if (clonedResident.getBiometricId() != null) {
                // Set updated biometricId
                updatedResident.setBiometricId(clonedResident.getBiometricId());
            }
            // Check if phone number has been updated
            if (clonedResident.getPhoneNumber() != null) {
                // Update phone number
                updatedResident.setPhoneNumber(clonedResident.getPhoneNumber());
            }
            // Check if role has been updated
            if (clonedResident.getRole() != null) {
                // Set updated role
                updatedResident.setRole(clonedResident.getRole());
            }
            // Check if Location has been updated
            if (clonedResident.getLocation() != null) {
                // Set updated location
                updatedResident.setLocation(clonedResident.getLocation());
                // Update the current city
                updatedResident.setCurrentCity(findCurrentCity(updatedResident.getLocation()));
            }
            // Check if account has been updated
            if (clonedResident.getBlockchainAccountAddress() != null) {
                // Set updated account
                updatedResident.setBlockchainAccountAddress(clonedResident.getBlockchainAccountAddress());
            }

            // Return updated resident
            return (Resident) updatedResident.clone();
        }
        // Check if Person is a Visitor
        else if (person instanceof Visitor) {

            // Clone the person to the visitor subclass
            Visitor clonedVisitor = (Visitor) person.clone();

            // Get the Visitor object to be updated
            Visitor updatedVisitor = (Visitor) personMap.get(clonedVisitor.getUuid());

            // Check if biometric ID has been updated
            if (clonedVisitor.getBiometricId() != null) {
                // If so then update it
                updatedVisitor.setBiometricId(clonedVisitor.getBiometricId());
            }
            // Check if location has been updated
            if (clonedVisitor.getLocation() != null) {
                // Set new location
                updatedVisitor.setLocation(clonedVisitor.getLocation());
                // Then find it's new current city
                updatedVisitor.setCurrentCity(findCurrentCity(updatedVisitor.getLocation()));
            }
            return (Visitor) updatedVisitor.clone();
        }

        return person;
    }

    /**
     * Updates an IotDevice
     * @param iotDevice The IotDevice object with updated values.
     * @return          The updated IotDevice object.
     * @throws CityModelServiceException
     */
    public IotDevice updateIotDevice(IotDevice iotDevice) throws CityModelServiceException {
        // First check if the Iot Device exists
        if (!iotDeviceMap.containsKey(iotDevice.getUuid())) {
            throw new CityModelServiceException("update IoT Device", "The following IoT Device does not exist " + iotDevice.getUuid());
        }

        // Check to see what kind of IoT Device we are creating
        if (iotDevice instanceof StreetLight) {

            // Clone the device to the proper subclass
            StreetLight clonedStreetLight = (StreetLight) iotDevice.clone();

            // Get the Streetlight object to update
            StreetLight updatedStreetLight = (StreetLight) iotDeviceMap.get(clonedStreetLight.getUuid());

            if (clonedStreetLight.getBrightness() != null) {
                updatedStreetLight.setBrightness(clonedStreetLight.getBrightness());
            }
            if (clonedStreetLight.getEnabled() != null) {
                updatedStreetLight.setEnabled(clonedStreetLight.getEnabled());
            }

            return (StreetLight) updatedStreetLight.clone();

        } else if (iotDevice instanceof StreetSign) {

            // Clone the device to the proper subclass
            StreetSign clonedStreetSign = (StreetSign) iotDevice.clone();

            // Get StreetSign object to update
            StreetSign updatedStreetSign = (StreetSign) iotDeviceMap.get(clonedStreetSign.getUuid());

            if (clonedStreetSign.getText() != null) {
                updatedStreetSign.setText(clonedStreetSign.getText());
            }
            if (clonedStreetSign.getEnabled() != null) {
                updatedStreetSign.setEnabled(clonedStreetSign.getEnabled());
            }
            return (StreetSign) updatedStreetSign.clone();

        } else if (iotDevice instanceof Vehicle) {

            // Clone the device to the proper subclass
            Vehicle clonedVehicle = (Vehicle) iotDevice.clone();

            // Get the Vehicle object ot update
            Vehicle updatedVehicle = (Vehicle) iotDeviceMap.get(clonedVehicle.getUuid());

            // Check if location has changed
            if (clonedVehicle.getLocation() != null) {
                updatedVehicle.setLocation((Location) clonedVehicle.getLocation().clone());
                // Update the current city
                updatedVehicle.setCurrentCity(findCurrentCity(updatedVehicle.getLocation()));
            }
            // Check if enabled has changed
            if (clonedVehicle.getEnabled() != null) {
                updatedVehicle.setEnabled(clonedVehicle.getEnabled());
            }
            // Check if activity has changed
            if (clonedVehicle.getActivity() != null) {
                updatedVehicle.setActivity(clonedVehicle.getActivity());
            }
            // Check if fee has changed
            if (clonedVehicle.getFee() != null) {
                updatedVehicle.setFee(clonedVehicle.getFee());
            }

            // Return updated Vehicle object
            return (Vehicle) updatedVehicle.clone();

        } else if (iotDevice instanceof InformationKiosk) {

            // Clone the device to the proper subclass
            InformationKiosk clonedInformationKiosk = (InformationKiosk) iotDevice.clone();

            // Get the information kiosk object to update
            InformationKiosk updatedInformationKiosk = (InformationKiosk) iotDeviceMap.get(clonedInformationKiosk.getUuid());

            if (clonedInformationKiosk.getImageUri() != null) {
                updatedInformationKiosk.setImageUri(clonedInformationKiosk.getImageUri());
            }
            if (clonedInformationKiosk.getEnabled() != null) {
                updatedInformationKiosk.setEnabled(clonedInformationKiosk.getEnabled());
            }

            return (InformationKiosk) updatedInformationKiosk.clone();

        } else if (iotDevice instanceof Robot) {

            // Clone the device to the proper subclass
            Robot clonedRobot = (Robot) iotDevice.clone();

            // Get the Robot object to update
            Robot updatedRobot = (Robot) iotDeviceMap.get(clonedRobot.getUuid());

            // Check if the location has changed
            if (clonedRobot.getLocation() != null) {
                updatedRobot.setLocation((Location) clonedRobot.getLocation().clone());
                // If we update the location of the Robot then we need to find it's current city
                updatedRobot.setCurrentCity(findCurrentCity(updatedRobot.getLocation()));
            }
            // Check if enabled has changed
            if (clonedRobot.getEnabled() != null) {
                updatedRobot.setEnabled(clonedRobot.getEnabled());
            }
            // Check if activity has changed
            if (clonedRobot.getActivity() != null) {
                updatedRobot.setActivity(clonedRobot.getActivity());
            }

            return (Robot) updatedRobot.clone();

        } else if (iotDevice instanceof ParkingSpace) {

            // Clone device from proper subclass
            ParkingSpace clonedParkingSpace = (ParkingSpace) iotDevice.clone();

            // Get the ParkingSpace object to update
            ParkingSpace updatedParkingSpace = (ParkingSpace) iotDeviceMap.get(clonedParkingSpace.getUuid());

            if (clonedParkingSpace.getRate() != null) {
                updatedParkingSpace.setRate(clonedParkingSpace.getRate());
            }
            if (clonedParkingSpace.getEnabled() != null) {
                updatedParkingSpace.setEnabled(clonedParkingSpace.getEnabled());
            }

            return (ParkingSpace) updatedParkingSpace.clone();

        }
        // Get IoT device
        return iotDevice;
    }

    /**
     * Method to update a city. If location of the city changes then person and IoT current cities are recalculated.
     * @param city The city object to update.
     * @return     The updated city object.
     * @throws CityModelServiceException
     */
    public City updateCity(City city) throws CityModelServiceException {

        // Clone city
        City clonedCity = (City) city.clone();

        // Check if city exists
        if (!cityMap.containsKey(clonedCity.getUuid())) {
            throw new CityModelServiceException("update city", "No city exists with city ID of " + clonedCity.getUuid());
        }

        // Get the city to update
        City updatedCity = cityMap.get(clonedCity.getUuid());

        // Update name
        if (clonedCity.getName() != null) {
            updatedCity.setName(clonedCity.getName());
        }
        // Update blockchain account address
        if (clonedCity.getBlockchainAccount() != null) {
            updatedCity.setBlockchainAccount(clonedCity.getBlockchainAccount());
        }
        // Update radius
        if (clonedCity.getRadius() != null) {
            updatedCity.setRadius(clonedCity.getRadius());
        }
        // Update location
        if (clonedCity.getLocation() != null) {
            updatedCity.setLocation(clonedCity.getLocation());
        }

        // If either radius or location have changed then we need to find the current cities for all IoT devices and persons
        for (Map.Entry<String, Person> entry : personMap.entrySet()) {
            entry.getValue().setCurrentCity(findCurrentCity(entry.getValue().getLocation()));
        }
        for (Map.Entry<String, IotDevice> entry : iotDeviceMap.entrySet()) {
            entry.getValue().setCurrentCity(findCurrentCity(entry.getValue().getLocation()));
        }

        // Return updated city
        return (City) updatedCity.clone();
    }

    /**
     * Register an observer with this subject.
     * @param o  The Observer object to register.
     */
    public void registerObserver(Observer o) {
        observerList.add(o);
    }

    /**
     * Remove an observer from watching this subject.
     * @param o  The observer to remove from watching the subject.
     */
    public void removeObserver(Observer o) {
        observerList.remove(o);
    }

    /**
     * Notify all the observers of a new SensorEvent.
     * @param event  The SensorEvent to notify observers of.
     */
    public void notifyObservers(SensorEvent event) {
        for (Observer observer : observerList) {
            observer.update(event);
        }
    }
}
