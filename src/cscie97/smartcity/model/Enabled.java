package cscie97.smartcity.model;

/**
 * Author: Stephen Sheldon
 **/
public enum Enabled {
    ON {
       @Override
       public String toString() {
           return "On";
       }
    },
    OFF {
        @Override
        public String toString() {
            return "Off";
        }
    }
}
