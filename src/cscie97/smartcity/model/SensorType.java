package cscie97.smartcity.model;

/**
 * Author: Stephen Sheldon
 **/
public enum SensorType {
    MICROPHONE {
        @Override
        public String toString() {
            return "Microphone";
        }
    }, CAMERA {
        @Override
        public String toString() {
            return "Camera";
        }
    }, THERMOMETER {
        @Override
        public String toString() {
            return "Thermometer";
        }
    }, CO2METER {
        @Override
        public String toString() {
            return "CO2 Meter";
        }
    }
}
