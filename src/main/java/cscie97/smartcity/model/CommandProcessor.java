package cscie97.smartcity.model;

import cscie97.smartcity.ledger.Account;
import cscie97.smartcity.ledger.LedgerService;
import cscie97.smartcity.ledger.LedgerException;
import cscie97.smartcity.ledger.Transaction;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Author: Stephen Sheldon
 **/
public class CommandProcessor {

    private static CityModelService cityModelService = null;

    private static LedgerService ledgerService = null;

    /**
     * Process a single command. The output of the command is formatted and displayed to stdout.
     * @param command Command to process
     * @param lineNumber The lineNumber in the text file of this command
     * @throws CommandProcessorException Throws on error in LedgerService
     */
    public static void processModelCommands(String[] command, int lineNumber) throws CommandProcessorException {

        Enabled enable;

        try {
            switch (command[0].toLowerCase()) {
                case "define":
                    switch (command[1].toLowerCase()) {
                        case "city":
                            // Create a new city here
                            City smartCity = new City(command[2], command[4], command[6], Double.parseDouble(command[8]),
                                    Double.parseDouble(command[10]), Double.parseDouble(command[12]));
                            cityModelService.createCity(smartCity);
                            break;
                        case "resident":
                            Role residentRole = null;

                            // Find the role of the resident
                            switch (command[10].toLowerCase()) {
                                case "adult":
                                    residentRole = Role.ADULT;
                                    break;
                                case "child":
                                    residentRole = Role.CHILD;
                                    break;
                                case "publicadministrator":
                                    residentRole = Role.PUBLICADMINISTRATOR;
                                    break;
                            }

                            // Create a resident
                            Resident resident = new Resident(command[2], command[4], command[6], command[8],
                                    residentRole, Double.parseDouble(command[12]), Double.parseDouble(command[14]), command[16]);
                            // Add Resident to CityModelService
                            cityModelService.createPerson(resident);
                            break;
                        case "visitor":
                            // Create a visitor
                            Visitor visitor = new Visitor(command[2], command[4], Double.parseDouble(command[6]), Double.parseDouble(command[8]));
                            cityModelService.createPerson(visitor);
                            break;
                        case "info-kiosk":
                            // create new information kiosk
                            enable = null;
                            if (command[8].equalsIgnoreCase("true")) {
                                enable = Enabled.ON;
                            }
                            InformationKiosk informationKiosk = new InformationKiosk(command[2], Double.parseDouble(command[4]), Double.parseDouble(command[6]), enable, command[10]);
                            cityModelService.createIotDevice(informationKiosk);
                            break;
                        case "street-sign":
                            // create new street sign
                            enable = null;
                            if (command[8].equalsIgnoreCase("true")) {
                                enable = Enabled.ON;
                            }
                            StreetSign streetSign = new StreetSign(command[2], Double.parseDouble(command[4]), Double.parseDouble(command[6]), enable, command[10]);
                            cityModelService.createIotDevice(streetSign);
                            break;
                        case "street-light":
                            // create a new street light
                            enable = null;
                            if (command[8].equalsIgnoreCase("true")) {
                                enable = Enabled.ON;
                            }
                            StreetLight streetLight = new StreetLight(command[2], Double.parseDouble(command[4]), Double.parseDouble(command[6]), enable, Integer.parseInt(command[10]));
                            cityModelService.createIotDevice(streetLight);
                            break;
                        case "parking-space":
                            // create a new parking space
                            enable = null;
                            if (command[8].equalsIgnoreCase("true")) {
                                enable = Enabled.ON;
                            }
                            ParkingSpace parkingSpace = new ParkingSpace(command[2], Double.parseDouble(command[4]), Double.parseDouble(command[6]), enable, Integer.parseInt(command[10]));
                            cityModelService.createIotDevice(parkingSpace);
                            break;
                        case "robot":
                            // create a new robot
                            enable = null;
                            if (command[8].equalsIgnoreCase("true")) {
                                enable = Enabled.ON;
                            }
                            Robot robot = new Robot(command[2], Double.parseDouble(command[4]), Double.parseDouble(command[6]), enable, command[10]);
                            cityModelService.createIotDevice(robot);
                            break;
                        case "vehicle":
                            // create a new vehicle
                            enable = null;
                            VehicleType vehicleType = null;
                            if (command[8].equalsIgnoreCase("true")) {
                                enable = Enabled.ON;
                            }
                            switch (command[10].toLowerCase()) {
                                case "car":
                                    vehicleType = VehicleType.CAR;
                                    break;
                                case "bus":
                                    vehicleType = VehicleType.BUS;
                                    break;
                            }

                            Vehicle vehicle = new Vehicle(command[2], Double.parseDouble(command[4]), Double.parseDouble(command[6]), enable, vehicleType, command[12],
                                    Integer.parseInt(command[14]), Integer.parseInt(command[16]));
                            cityModelService.createIotDevice(vehicle);
                            break;
                    }
                    break;
                case "update":
                    boolean containsEnable, containsBrightness, containsImageUri, containsText,
                            containsRate, containsLocation, containsActivity, containsFee,
                            containsBiometricId, containsName, containsPhoneNumber, containsRole, containsAccount, containsRadius;
                    switch (command[1].toLowerCase()) {
                        case "city":
                            containsName = Arrays.stream(command).anyMatch("name"::equals);
                            containsAccount = Arrays.stream(command).anyMatch("account"::equals);
                            containsRadius = Arrays.stream(command).anyMatch("radius"::equals);
                            containsLocation = Arrays.stream(command).anyMatch("lat"::equals);

                            // Create a new city update with UUID
                            City updatedCity = new City(command[2]);

                            // Update city's name
                            if (containsName) {
                                // Find name index
                                int nameIndex = findIndex(command, "name");
                                // Update name
                                updatedCity.setName(command[nameIndex + 1]);
                            }
                            // Update the city's blockchain account address
                            if (containsAccount) {
                                // Find account index
                                int accountIndex = findIndex(command, "account");
                                // Update account
                                updatedCity.setBlockchainAccount(command[accountIndex + 1]);
                            }
                            // Update the city's radius
                            if (containsRadius) {
                                // Get radius index
                                int radiusIndex = findIndex(command, "radius");
                                // Update radius
                                updatedCity.setRadius(Double.parseDouble(command[radiusIndex + 1]));
                            }
                            // Update the city's location
                            if (containsLocation) {
                                // Get lat index
                                int latIndex = findIndex(command, "lat");
                                // Get long index
                                int longIndex = findIndex(command, "long");
                                // Update city location
                                updatedCity.setLocation(new Location(Double.parseDouble(command[latIndex + 1]), Double.parseDouble(command[longIndex + 1])));
                            }

                            // Pass updated city to API
                            cityModelService.updateCity(updatedCity);
                            break;
                        case "resident":
                            // update resident
                            containsName = Arrays.stream(command).anyMatch("name"::equals);
                            containsBiometricId = Arrays.stream(command).anyMatch("bio-metric"::equals);
                            containsPhoneNumber = Arrays.stream(command).anyMatch("phone"::equals);
                            containsRole = Arrays.stream(command).anyMatch("role"::equals);
                            containsLocation = Arrays.stream(command).anyMatch("lat"::equals);
                            containsAccount = Arrays.stream(command).anyMatch("account"::equals);

                            // Create a new Resident with UUID
                            Resident updatedResident = new Resident(command[2]);


                            // Update the resident's name
                            if (containsName) {
                                // Find index of name
                                int nameIndex = findIndex(command, "name");
                                // Set updated name
                                updatedResident.setName(command[nameIndex + 1]);
                            }
                            // Update the resident's biometricId
                            if (containsBiometricId) {
                                // Find index of biometric ID
                                int biometricIdIndex = findIndex(command, "bio-metric");
                                // Set updated biometricId
                                updatedResident.setBiometricId(command[biometricIdIndex + 1]);
                            }
                            // Update the resident's phone number
                            if (containsPhoneNumber) {
                                // Find index of phone number
                                int phoneNumberIndex = findIndex(command, "phone");
                                // Set updated phone number
                                updatedResident.setPhoneNumber(command[phoneNumberIndex + 1]);
                            }
                            // Update the resident's role
                            if (containsRole) {
                                // Find the index of role
                                int roleIndex = findIndex(command, "role");
                                // Set updated role
                                Role updatedRole = null;
                                if (command[roleIndex + 1].equalsIgnoreCase("adult")) {
                                    updatedRole = Role.ADULT;
                                } else if (command[roleIndex + 1].equalsIgnoreCase("child")) {
                                    updatedRole = Role.CHILD;
                                } else if (command[roleIndex + 1].equalsIgnoreCase("publicadministrator")) {
                                    updatedRole = Role.PUBLICADMINISTRATOR;
                                }
                                updatedResident.setRole(updatedRole);
                            }
                            // Update the resident's location
                            if (containsLocation) {
                                // Find lat index
                                int latIndex = findIndex(command, "lat");
                                // Find long index
                                int longIndex = findIndex(command, "long");

                                // Set location
                                updatedResident.setLocation(new Location(Double.parseDouble(command[latIndex + 1]), Double.parseDouble(command[longIndex + 1])));
                            }
                            // Update the resident's account
                            if (containsAccount) {
                                // Find the account index
                                int accountIndex = findIndex(command, "account");
                                // Set account
                                updatedResident.setBlockchainAccountAddress(command[accountIndex + 1]);
                            }

                            // Pass updated Resident to API
                            cityModelService.updatePerson(updatedResident);

                            break;
                        case "visitor":
                            // update visitor
                            containsBiometricId = Arrays.stream(command).anyMatch("bio-metric"::equals);
                            containsLocation = Arrays.stream(command).anyMatch("lat"::equals);

                            // Check whether biometricId or Location has changed.
                            if (containsBiometricId && containsLocation) {
                                Visitor updatedVisitor = new Visitor(command[2], command[4], Double.parseDouble(command[6]), Double.parseDouble(command[8]));
                                cityModelService.updatePerson(updatedVisitor);
                            } else if (containsBiometricId) {
                                Visitor updatedVisitor = new Visitor(command[2], command[4]);
                                cityModelService.updatePerson(updatedVisitor);
                            } else if (containsLocation) {
                                Visitor updatedVisitor = new Visitor(command[2], Double.parseDouble(command[4]), Double.parseDouble(command[6]));
                                cityModelService.updatePerson(updatedVisitor);
                            }
                            break;
                        case "info-kiosk":
                            // update information kiosk
                            containsEnable = Arrays.stream(command).anyMatch("enabled"::equals);
                            containsImageUri = Arrays.stream(command).anyMatch("image"::equals);

                            if (containsEnable && containsImageUri) {
                                if (command[4].equalsIgnoreCase("true")) {
                                    enable = Enabled.ON;
                                } else {
                                    enable = Enabled.OFF;
                                }
                                InformationKiosk updatedInformationKiosk = new InformationKiosk(command[2], enable, command[6]);
                                cityModelService.updateIotDevice(updatedInformationKiosk);
                            } else if (containsImageUri) {
                                InformationKiosk updatedInformationKiosk = new InformationKiosk(command[2], command[4]);
                                cityModelService.updateIotDevice(updatedInformationKiosk);
                            } else if (containsEnable) {
                                if (command[4].equalsIgnoreCase("true")) {
                                    enable = Enabled.ON;
                                } else {
                                    enable = Enabled.OFF;
                                }
                                InformationKiosk updatedInformationKiosk = new InformationKiosk(command[2], enable);
                                cityModelService.updateIotDevice(updatedInformationKiosk);
                            }
                            break;
                        case "street-sign":
                            // update street sign
                            containsEnable = Arrays.stream(command).anyMatch("enabled"::equals);
                            containsText = Arrays.stream(command).anyMatch("text"::equals);

                            if (containsEnable && containsText) {
                                if (command[4].equalsIgnoreCase("true")) {
                                    enable = Enabled.ON;
                                } else {
                                    enable = Enabled.OFF;
                                }
                                StreetSign updatedStreetSign = new StreetSign(command[2], enable, command[6]);
                                cityModelService.updateIotDevice(updatedStreetSign);
                            } else if (containsText) {
                                StreetSign updatedStreetSign = new StreetSign(command[2], command[4]);
                                cityModelService.updateIotDevice(updatedStreetSign);
                            } else if (containsEnable) {
                                if (command[4].equalsIgnoreCase("true")) {
                                    enable = Enabled.ON;
                                } else {
                                    enable = Enabled.OFF;
                                }
                                StreetSign updatedStreetSign = new StreetSign(command[2], enable);
                                cityModelService.updateIotDevice(updatedStreetSign);
                            }
                            break;
                        case "street-light":
                            // update street light
                            containsEnable = Arrays.stream(command).anyMatch("enabled"::equals);
                            containsBrightness = Arrays.stream(command).anyMatch("brightness"::equals);

                            if (containsEnable && containsBrightness) {
                                if (command[4].equalsIgnoreCase("true")) {
                                    enable = Enabled.ON;
                                } else {
                                    enable = Enabled.OFF;
                                }
                                StreetLight updatedStreetLight = new StreetLight(command[2], enable, Integer.parseInt(command[6]));
                                cityModelService.updateIotDevice(updatedStreetLight);
                            } else if (containsBrightness) {
                                StreetLight updatedStreetLight = new StreetLight(command[2], Integer.parseInt(command[4]));
                                cityModelService.updateIotDevice(updatedStreetLight);
                            } else if (containsEnable) {
                                if (command[4].equalsIgnoreCase("true")) {
                                    enable = Enabled.ON;
                                } else {
                                    enable = Enabled.OFF;
                                }
                                StreetLight updatedStreetLight = new StreetLight(command[2], enable);
                                cityModelService.updateIotDevice(updatedStreetLight);
                            }
                            break;
                        case "parking-space":
                            // update parking space
                            containsEnable = Arrays.stream(command).anyMatch("enabled"::equals);
                            containsRate = Arrays.stream(command).anyMatch("rate"::equals);

                            if (containsEnable && containsRate) {
                                if (command[4].equalsIgnoreCase("true")) {
                                    enable = Enabled.ON;
                                } else {
                                    enable = Enabled.OFF;
                                }
                                ParkingSpace updatedParkingSpace = new ParkingSpace(command[2], enable, Integer.parseInt(command[6]));
                                cityModelService.updateIotDevice(updatedParkingSpace);
                            } else if (containsRate) {
                                ParkingSpace updatedParkingSpace = new ParkingSpace(command[2], Integer.parseInt(command[4]));
                                cityModelService.updateIotDevice(updatedParkingSpace);
                            } else if (containsEnable) {
                                if (command[4].equalsIgnoreCase("true")) {
                                    enable = Enabled.ON;
                                } else {
                                    enable = Enabled.OFF;
                                }
                                ParkingSpace updatedParkingSpace = new ParkingSpace(command[2], enable);
                                cityModelService.updateIotDevice(updatedParkingSpace);
                            }
                            break;
                        case "robot":
                            // update robot
                            containsLocation = Arrays.stream(command).anyMatch("lat"::equals);
                            containsEnable = Arrays.stream(command).anyMatch("enabled"::equals);
                            containsActivity = Arrays.stream(command).anyMatch("activity"::equals);

                            if (containsLocation && containsEnable && containsActivity) {
                                if (command[8].equalsIgnoreCase("true")) {
                                    enable = Enabled.ON;
                                } else {
                                    enable = Enabled.OFF;
                                }
                                Robot updatedRobot = new Robot(command[2], Double.parseDouble(command[4]), Double.parseDouble(command[6]),
                                        enable, command[10]);
                                cityModelService.updateIotDevice(updatedRobot);
                            } else if (containsLocation && containsEnable) {
                                if (command[8].equalsIgnoreCase("true")) {
                                    enable = Enabled.ON;
                                } else {
                                    enable = Enabled.OFF;
                                }
                                Robot updatedRobot = new Robot(command[2], Double.parseDouble(command[4]), Double.parseDouble(command[6]), enable);
                                cityModelService.updateIotDevice(updatedRobot);
                            } else if (containsLocation && containsActivity) {
                                Robot updatedRobot = new Robot(command[2], Double.parseDouble(command[4]), Double.parseDouble(command[6]), command[8]);
                                cityModelService.updateIotDevice(updatedRobot);
                            } else if (containsEnable && containsActivity) {
                                if (command[4].equalsIgnoreCase("true")) {
                                    enable = Enabled.ON;
                                } else {
                                    enable = Enabled.OFF;
                                }
                                Robot updatedRobot = new Robot(command[2], enable, command[6]);
                                cityModelService.updateIotDevice(updatedRobot);
                            } else if (containsEnable) {
                                if (command[8].equalsIgnoreCase("true")) {
                                    enable = Enabled.ON;
                                } else {
                                    enable = Enabled.OFF;
                                }
                                Robot updatedRobot = new Robot(command[2], enable);
                                cityModelService.updateIotDevice(updatedRobot);
                            } else if (containsLocation) {
                                Robot updatedRobot = new Robot(command[2], Double.parseDouble(command[4]), Double.parseDouble(command[6]));
                                cityModelService.updateIotDevice(updatedRobot);
                            } else if (containsActivity) {
                                Robot updatedRobot = new Robot(command[2], command[4]);
                                cityModelService.updateIotDevice(updatedRobot);
                            }
                            break;
                        case "vehicle":
                            // update vehicle
                            containsLocation = Arrays.stream(command).anyMatch("lat"::equals);
                            containsEnable = Arrays.stream(command).anyMatch("enabled"::equals);
                            containsActivity = Arrays.stream(command).anyMatch("activity"::equals);
                            containsFee = Arrays.stream(command).anyMatch("fee"::equals);

                            if (containsLocation && containsEnable && containsActivity && containsFee) {
                                if (command[8].equalsIgnoreCase("true")) {
                                    enable = Enabled.ON;
                                } else {
                                    enable = Enabled.OFF;
                                }
                                Vehicle updatedVehicle = new Vehicle(command[2], Double.parseDouble(command[4]), Double.parseDouble(command[6]),
                                        enable, command[10], Integer.parseInt(command[12]));
                                cityModelService.updateIotDevice(updatedVehicle);

                            } else if (containsLocation && containsEnable && containsActivity) {
                                if (command[8].equalsIgnoreCase("true")) {
                                    enable = Enabled.ON;
                                } else {
                                    enable = Enabled.OFF;
                                }
                                Vehicle updatedVehicle = new Vehicle(command[2], Double.parseDouble(command[4]), Double.parseDouble(command[6]),
                                        enable, command[10]);
                                cityModelService.updateIotDevice(updatedVehicle);
                            } else if (containsLocation && containsEnable && containsFee) {
                                if (command[8].equalsIgnoreCase("true")) {
                                    enable = Enabled.ON;
                                } else {
                                    enable = Enabled.OFF;
                                }
                                Vehicle updatedVehicle = new Vehicle(command[2], Double.parseDouble(command[4]), Double.parseDouble(command[6]),
                                        enable, command[10]);
                                cityModelService.updateIotDevice(updatedVehicle);
                            } else if (containsEnable && containsActivity && containsFee) {
                                if (command[4].equalsIgnoreCase("true")) {
                                    enable = Enabled.ON;
                                } else {
                                    enable = Enabled.OFF;
                                }
                                Vehicle updatedVehicle = new Vehicle(command[2], enable, command[6], Integer.parseInt(command[8]));
                                cityModelService.updateIotDevice(updatedVehicle);
                            } else if (containsLocation && containsEnable) {
                                if (command[8].equalsIgnoreCase("true")) {
                                    enable = Enabled.ON;
                                } else {
                                    enable = Enabled.OFF;
                                }
                                Vehicle updatedVehicle = new Vehicle(command[2], Double.parseDouble(command[4]), Double.parseDouble(command[6]),
                                        enable);
                                cityModelService.updateIotDevice(updatedVehicle);
                            } else if (containsLocation && containsActivity) {
                                Vehicle updatedVehicle = new Vehicle(command[2], Double.parseDouble(command[4]), Double.parseDouble(command[6]),
                                        command[8]);
                                cityModelService.updateIotDevice(updatedVehicle);
                            } else if (containsLocation && containsFee) {
                                Vehicle updatedVehicle = new Vehicle(command[2], Double.parseDouble(command[4]), Double.parseDouble(command[6]), Integer.parseInt(command[8]));
                                cityModelService.updateIotDevice(updatedVehicle);
                            } else if (containsEnable && containsActivity) {
                                if (command[4].equalsIgnoreCase("true")) {
                                    enable = Enabled.ON;
                                } else {
                                    enable = Enabled.OFF;
                                }
                                Vehicle updatedVehicle = new Vehicle(command[2], enable, command[6]);
                                cityModelService.updateIotDevice(updatedVehicle);
                            } else if (containsEnable && containsFee) {
                                if (command[4].equalsIgnoreCase("true")) {
                                    enable = Enabled.ON;
                                } else {
                                    enable = Enabled.OFF;
                                }
                                Vehicle updatedVehicle = new Vehicle(command[2], enable, Integer.parseInt(command[6]));
                                cityModelService.updateIotDevice(updatedVehicle);
                            } else if (containsActivity && containsFee) {
                                Vehicle updatedVehicle = new Vehicle(command[2], command[4], Integer.parseInt(command[6]));
                                cityModelService.updateIotDevice(updatedVehicle);
                            } else if (containsEnable) {
                                if (command[4].equalsIgnoreCase("true")) {
                                    enable = Enabled.ON;
                                } else {
                                    enable = Enabled.OFF;
                                }
                                Vehicle updatedVehicle = new Vehicle(command[2], enable);
                                cityModelService.updateIotDevice(updatedVehicle);
                            } else if (containsActivity) {
                                Vehicle updatedVehicle = new Vehicle(command[2], command[4]);
                                cityModelService.updateIotDevice(updatedVehicle);
                            } else if (containsFee) {
                                Vehicle updatedVehicle = new Vehicle(command[2], Integer.parseInt(command[4]));
                                cityModelService.updateIotDevice(updatedVehicle);
                            } else if (containsLocation) {
                                Vehicle updatedVehicle = new Vehicle(command[2], Double.parseDouble(command[4]), Double.parseDouble(command[6]));
                                cityModelService.updateIotDevice(updatedVehicle);
                            }
                            break;
                    }
                    break;
                case "show":
                    switch (command[1].toLowerCase()) {
                        case "city":
                            // Check if showing a specific city based on size of array
                            if (command.length == 3) {
                                City retrievedCity = cityModelService.getCity(command[2]);
                                System.out.println(retrievedCity.toString());
                            } else {
                                List<City> cityList = cityModelService.getCityList();
                                for (City city : cityList) {
                                    System.out.println(city.toString());
                                }
                            }
                            break;
                        case "person":
                            // show person
                            System.out.println("Details for Person: " + command[2]);
                            System.out.println(cityModelService.getPerson(command[2]).toString());
                            break;
                        case "device":
                            // Check if showing a specific device
                            String[] splitDeviceId = command[2].split(":");

                            if (splitDeviceId.length == 2) {
                                System.out.println("Retrieving device with ID: " + command[2]);
                                System.out.println(cityModelService.getIotDevice(splitDeviceId[0], splitDeviceId[1]));
                            } else {
                                List<IotDevice> deviceList;
                                System.out.println("Devices associated with City: " + command[2]);
                                deviceList = cityModelService.getIotDevice(command[2]);
                                for (IotDevice iotDevice : deviceList) {
                                    System.out.println(iotDevice.toString());
                                }
                            }
                            break;
                    }
                    break;
                case "create":

                    boolean containsSubject, containsDeviceId;
                    String[] splitDeviceId;

                    switch (command[1].toLowerCase()) {
                        case "sensor-event":
                            SensorEvent sensorEvent = new SensorEvent();

                            // Check if showing a specific device
                            splitDeviceId = command[2].split(":");
                            SensorType sensorType = null;

                            if (splitDeviceId.length == 2) {
                                // Set city
                                sensorEvent.setCityId(splitDeviceId[0]);
                                // Set device id
                                sensorEvent.setDeviceId(splitDeviceId[1]);
                            }

                            // Find the sensor type
                            if (command[4].equalsIgnoreCase("microphone")) {
                                sensorType = SensorType.MICROPHONE;
                            } else if (command[4].equalsIgnoreCase("thermometer")) {
                                sensorType = SensorType.THERMOMETER;
                            } else if (command[4].equalsIgnoreCase("co2meter")) {
                                sensorType = SensorType.CO2METER;
                            } else if (command[4].equalsIgnoreCase("camera")) {
                                sensorType = SensorType.CAMERA;
                            }

                            // Set sensor type
                            sensorEvent.setSensorType(sensorType);

                            // Set value
                            sensorEvent.setValue(command[6]);

                            // Check if subject is in command, if so set it
                            if (Arrays.stream(command).anyMatch("subject"::equals)) {
                                sensorEvent.setPersonId(command[8]);
                            }

                            // Pass to API and print out returned SensorEvent object.
                            System.out.println(cityModelService.createSensorEvent(sensorEvent).toString());
                            break;
                        case "sensor-output":
                            SensorOutput sensorOutput = new SensorOutput();

                            // Check if showing a specific device
                            splitDeviceId = command[2].split(":");

                            if (splitDeviceId.length == 2) {
                                // Set city
                                sensorOutput.setCityId(splitDeviceId[0]);
                                // Set device id
                                sensorOutput.setDeviceId(splitDeviceId[1]);
                            } else {
                                sensorOutput.setCityId(command[2]);
                            }
                            // Set sensor output
                            sensorOutput.setValue(command[6]);
                            // Pass sensor event to API and print it out.
                            System.out.println(cityModelService.createSensorOutput(sensorOutput).toString());
                            break;
                    }
                    break;
            }
        } catch (CityModelServiceException e) {
            throw new CommandProcessorException(e.getAction(), e.getReason(), lineNumber);
        }
    }

    /**
     * Process a single command. The output of the command is formatted and displayed to stdout.
     * @param command Command to process
     * @param lineNumber The lineNumber in the text file of this command
     * @throws CommandProcessorException Throws on error in LedgerService
     */
    public static void processLedgerCommands(String[] command, int lineNumber) throws CommandProcessorException {

        try {
            switch (command[0].toLowerCase()) {
                case "create-ledger":
                    ledgerService = LedgerService.getInstance();
                    break;
                case "create-account":
                    if (ledgerService == null) {
                        throw new LedgerException("create-account", "No ledgerService has been created.");
                    }
                    ledgerService.createAccount(command[1]);
                    break;
                case "process-transaction":
                    Transaction transaction = ledgerService.createTransaction(command[1], Integer.valueOf(command[3]), Integer.valueOf(command[5]), command[7], command[9], command[11]);
                    ledgerService.processTransaction(transaction);
                    break;
                case "get-account-balance":
                    System.out.println(ledgerService.getAccountBalance(command[1]));
                    break;
                case "get-account-balances":
                    for (Map.Entry<String, Account> entry : ledgerService.getAccountBalances().entrySet()) {
                        System.out.println(entry.getValue().toString());
                    }
                    break;
                case "get-block":
                    System.out.println(ledgerService.getBlock(Integer.valueOf(command[1])).toString());
                    break;
                case "get-transaction":
                    System.out.println(ledgerService.getTransaction(command[1]).toString());
                    break;
                case "validate":
                    ledgerService.validate();
                    break;
                case "in-order":
                    ledgerService.getBlock(1).getMerkleTree().inOrder();
                    break;
            }
        } catch (LedgerException e) {
            throw new CommandProcessorException(e.getAction(), e.getReason(), lineNumber);
        }
    }

    /**
     * Helper method to find index of an optional string value in command.
     * @param  arr  The String array to search.
     * @param  word The word to find the index of.
     * @return index of word String inside String arr
     */
    public static int findIndex(String[] arr, String word) {
        // Check if array is null
        if (arr == null) {
            return -1;
        }

        // Search array for index
        for (int i = 0; i < arr.length; i++) {
            if (arr[i].equalsIgnoreCase(word)) {
                return i;
            }
        }

        // Case where word does not exist in array
        return -1;
    }

    /**
     * Process a set of commands provided within the given command files.
     * @param ledgerFile The ledger file script to be processed.
     * @param modelFile  The model file script to be processed.
     */
    public static void processCommandFile(String ledgerFile, String modelFile) {
        processLedgerFile(ledgerFile);
        processModelFile(modelFile);
    }

    /**
     * Process a set of commands provided within the Model Service Script.
     * @param modelFile The Model Service script to be processed.
     */
    private static void processModelFile(String modelFile) {
        LineNumberReader lineNumberReader;
        String[] words;

        cityModelService = CityModelService.getInstance();

        try {
            lineNumberReader = new LineNumberReader(new FileReader(modelFile));

            String line;

            while ((line = lineNumberReader.readLine()) != null) {
                // Regex to split on space unless we hit quotations marks.
                words = line.split(" +(?=([^\"]*\"[^\"]*\")*[^\"]*$)");

                // Remove residual quotations marks from strings
                for (int i = 0; i < words.length; i++) {
                    words[i] = words[i].replaceAll("\"", "");
                }

                // Check if this is a comment in the script file
                try {
                    if (!words[0].equals("#") && words.length != 1) {
                        processModelCommands(words, lineNumberReader.getLineNumber());
                    }
                } catch (CommandProcessorException c) {
                    System.out.println(c.getCommand() + " " + c.getReason() + " Line number: " + c.getLineNumber());
                }
            }
        } catch (FileNotFoundException f) {
            System.out.println("File not found exception.");
        } catch (IOException i) {
            System.out.println("IO exception");
        }
    }

    /**
     * Process a set of commands provided within the given LedgerService Service script.
     * @param ledgerFile The LedgerService Service script to be processed.
     */
    private static void processLedgerFile(String ledgerFile) {

        LineNumberReader lineNumberReader;
        String[] words;

        try {
            lineNumberReader = new LineNumberReader(new FileReader(ledgerFile));

            String line;

            while ((line = lineNumberReader.readLine()) != null) {
                // Regex to split on space unless we hit quotations marks.
                words = line.split(" (?=([^\"]*\"[^\"]*\")*[^\"]*$)");

                // Remove residual quotations marks from strings
                for (int i = 0; i < words.length; i++) {
                    words[i] = words[i].replaceAll("\"", "");
                }

                // Check if this is a comment in the script file
                try {
                    if (!words[0].equals("#")) {
                        processLedgerCommands(words, lineNumberReader.getLineNumber());
                    }
                } catch (CommandProcessorException c) {
                    System.out.println(c.getCommand() + " " + c.getReason() + " Line number: " + c.getLineNumber());
                }
            }
        } catch (FileNotFoundException f) {
            System.out.println("File not found exception.");
        } catch (IOException i) {
            System.out.println("IO exception");
        }
    }
}
