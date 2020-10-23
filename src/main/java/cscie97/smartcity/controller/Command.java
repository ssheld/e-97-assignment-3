package cscie97.smartcity.controller;

import cscie97.smartcity.model.CityModelServiceException;

/**
 * Author: Stephen Sheldon
 **/
public interface Command {
    void execute() throws CityModelServiceException;
}
