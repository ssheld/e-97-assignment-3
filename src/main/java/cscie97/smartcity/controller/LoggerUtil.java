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
        logger.setUseParentHandlers(false);

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
    public static void log(Level level, String msg, Boolean printToConsole) {
        getLogger().log(level,msg);

        if (printToConsole) {
            System.out.println(msg);
        }
    }

    /**
     * Overloaded message to log exceptions that have an action and reason strings
     * @param level Log level
     * @param msg1  The action that was attempted
     * @param msg2  The reason the exception was thrown
     */
    public static void log(Level level, String msg1, String msg2) {
        StringBuilder errorString = new StringBuilder();

        errorString.append("Exception Thrown: ");
        errorString.append(msg1);
        errorString.append(" ");
        errorString.append(msg2);

        getLogger().log(level, errorString.toString());
    }


}
