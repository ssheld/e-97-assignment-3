package cscie97.smartcity.controller;

import cscie97.smartcity.model.*;

import java.util.*;
import java.util.logging.Level;

/**
 * Author: Stephen Sheldon
 **/
public class ControllerUtils {


    /**
     * Utility method to locate robots within a city
     * @param eventLocation  The location of the emergency
     * @param modelService       A reference to the model service
     * @param cityId             The city ID of the city in which the emergency is occurring
     * @return                   A list of robots in the city sorted by distance from emergency location
     * @throws CityModelServiceException
     */
    public static List<Robot> locateRobots(Location eventLocation, CityModelService modelService, String cityId) throws CityModelServiceException {

        // Get all devices in the city
        List<IotDevice> deviceList = modelService.getIotDevice(cityId);
        List<Robot> robotList = new ArrayList<>();
        Map<Robot, Double> robotDistanceMap = new HashMap<>();

        double distanceBetween;

        // Search for robots in the city
        for (IotDevice device : deviceList) {
            // If a robot is found, add it to our robot list
            if (device instanceof Robot) {
                robotList.add((Robot)device);
            }
        }

        // Check if no robots exists in the city
        if (robotList.isEmpty()) {
            // Log a warning
            LoggerUtil.log(Level.WARNING, "There are no robots currently in the city", false);
        }

        // Otherwise go through the robot list and calculate the distance the robots
        // are from the location of the emergency.
        for (Robot r : robotList) {
            distanceBetween = CityModelService.distance(eventLocation.getLatitude(), r.getLocation().getLatitude(),
                    eventLocation.getLongitude(), r.getLocation().getLongitude(), 0, 0);
            // Put in robot distance map

            robotDistanceMap.put(r, distanceBetween);
        }

        // Now sort the robot distance map based on values
        return sortRobots(robotDistanceMap);
    }

    /**
     * Sorts Robot IoT Devices by distance.
     * Reference: https://www.geeksforgeeks.org/sorting-a-hashmap-according-to-values/
     * @param robotDistanceMap A list of robots and their distance from emergency
     * @return A sorted Map of Robots and their distance from the emergency
     */
    private static List<Robot> sortRobots(Map<Robot, Double> robotDistanceMap) {
        // Create a list from the hashmap entries
        List<Map.Entry<Robot, Double>> list = new LinkedList<>(robotDistanceMap.entrySet());

        // Sort the list
        Collections.sort(list, new Comparator<Map.Entry<Robot,Double> >() {
            public int compare(Map.Entry<Robot, Double> r1,
                               Map.Entry<Robot, Double> r2) {
                return (r1.getValue().compareTo(r2.getValue()));
            }
        });

        // Convert List of map entries into a normal list of robots
        List<Robot> tempSortedRobotList = new ArrayList<>();
        for (Map.Entry<Robot, Double> entry : list) {
            tempSortedRobotList.add(entry.getKey());
        }

        return tempSortedRobotList;
    }
}
