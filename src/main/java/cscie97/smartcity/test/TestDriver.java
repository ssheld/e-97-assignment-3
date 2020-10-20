package cscie97.smartcity.test;

import cscie97.smartcity.controller.ControllerService;
import cscie97.smartcity.model.CommandProcessor;

/**
 * Author: Stephen Sheldon
 **/
public class TestDriver {

    public static void main(String[] args) {

        // Pass our command process the specified file name
        CommandProcessor.processCommandFile(args[0], args[1]);

    }
}
