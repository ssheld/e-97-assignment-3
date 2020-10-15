package cscie97.smartcity.model;

/**
 * Author: Stephen Sheldon
 **/
public enum Role {
    ADULT {
        @Override
        public String toString() {
            return "Adult";
        }
    }, CHILD {
        @Override
        public String toString() {
            return "Child";
        }
    }, PUBLICADMINISTRATOR {
        @Override
        public String toString() {
            return "Public Administrator";
        }
    }
}
