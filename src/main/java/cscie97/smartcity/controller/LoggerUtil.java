package cscie97.smartcity.controller;

import java.io.IOException;
import java.util.logging.*;

/**
 * Author: Stephen Sheldon
 *
 * Reference: https://stackoverflow.com/questions/20737880/java-logging-through-multiple-classes
 **/
public class LoggerUtil {
    private static Logger logger;
    private Handler fileHandler;
    private Formatter plainText;

    /**
     * Private constructor method.
     * @throws IOException
     */
    private LoggerUtil() throws IOException {
        logger = Logger.getLogger(LoggerUtil.class.getName());

        // Create a file handler
        fileHandler = new FileHandler("log.txt", true);

        // Create the formatter and set formatting
        plainText = new SimpleFormatter();
        fileHandler.setFormatter(plainText);
        logger.addHandler(fileHandler);
    }

    /**
     * Singleton factory implementation to get a Logger object
     * @return A new logger object if none exists, otherwise the logger
     *         object that does exist.
     */
    private static Logger getLogger() {
        if (logger == null) {
            try {
                new LoggerUtil();
            } catch (IOException e) {
                System.out.println("IO Exception when attempting to create logger.");
            }
        }
        return logger;
    }

    /**
     * Public facing method that takes log level and log string.
     * @param level Log Level
     * @param msg   Log message
     */
    public static void log(Level level, String msg) {
        getLogger().log(level,msg);
        System.out.println(msg);
    }


}
