package cscie97.smartcity.model;

/**
 * Author: Stephen Sheldon
 **/
public enum Status {
    READY {
        @Override
        public String toString() {
            return "Ready";
        }
    }, OFFLINE {
        @Override
        public String toString() {
            return "Offline";
        }
    }
}
