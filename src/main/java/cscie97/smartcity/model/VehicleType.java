package cscie97.smartcity.model;

/**
 * Author: Stephen Sheldon
 **/
public enum VehicleType {
    CAR {
        @Override
        public String toString() {
            return "Car";
        }
    }, BUS {
        @Override
        public String toString() {
            return "Bus";
        }
    }
}
