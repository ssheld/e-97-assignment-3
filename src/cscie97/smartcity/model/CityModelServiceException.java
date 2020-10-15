package cscie97.smartcity.model;

/**
 * Author: Stephen Sheldon
 **/
public class CityModelServiceException extends Exception {
    /**
     * Action that was performed.
     */
    private String reason;

    /**
     * Reason for the exception.
     */
    private String action;

    /**
     * Constructor method.
     * @param reason Reason for the exception.
     * @param action Action that was performed.
     */
    public CityModelServiceException(String action, String reason) {
        this.action = action;
        this.reason = reason;
    }

    public String getReason() {
        return reason;
    }

    public String getAction() {
        return action;
    }

    @Override
    public String toString() {
        return "CityModelServiceException{" +
                "reason='" + reason + '\'' +
                ", action='" + action + '\'' +
                '}';
    }
}
