package it.unitn.buyhub.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * This class is a singleton object to keep the properties object, avoiding to
 * reload it everytime a property is needed
 *
 * @author Massimo Girondi
 */
public class PropertyHandler {

    private static PropertyHandler instance = null;

    private Properties props = null;

    private PropertyHandler() {
        props = new Properties();
        String propFileName = "config.properties";

        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(propFileName);

        if (inputStream != null) {
            try {
                props.load(inputStream);
            } catch (IOException ex) {
                Log.error("Error reading properties file!" + ex.getMessage());

            }
        } else {

            Log.error("Error reading properties file!");

        }
    }

    /**
     * Returns the actual instance, creating it if not present
     *
     * @return The PropertyHandler actual instance
     */
    public static synchronized PropertyHandler getInstance() {
        if (instance == null) {
            instance = new PropertyHandler();
        }
        return instance;
    }

    /**
     * To retrieve the property value
     *
     * @param propKey The name of the property
     * @return The value of the property. Empty string if the property is not
     * present or it is empty
     */
    public String getValue(String propKey) {
        return this.props.getProperty(propKey) != null ? this.props.getProperty(propKey) : "";
    }
}
