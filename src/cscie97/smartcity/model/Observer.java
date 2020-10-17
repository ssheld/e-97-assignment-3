package cscie97.smartcity.model;

import cscie97.smartcity.model.SensorEvent;

/**
 * Author: Stephen Sheldon
 **/
public interface Observer {

    public void update(SensorEvent event);
}
