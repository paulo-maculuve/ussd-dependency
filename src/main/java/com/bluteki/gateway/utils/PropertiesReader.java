package com.bluteki.gateway.utils;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

public class PropertiesReader {
    private static final Logger LOGGER = Logger.getLogger(PropertiesReader.class.getName());

    private Properties properties;
    private InputStream input;
    private BufferedReader reader;

    public PropertiesReader(String fileName)  {
        properties = new Properties();
        input = PropertiesReader.class.getClassLoader().getResourceAsStream(fileName);

        if (input == null) {
            LOGGER.info("Property file not found: " + fileName);
            return;
        }

        reader = new BufferedReader(new InputStreamReader(input));
        try {
            properties.load(reader);
        } catch (IOException e) {
            LOGGER.info("Error loading the properties file: " + fileName, e);
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
                if (input != null) {
                    input.close();
                }
            } catch (IOException e) {
                LOGGER.info("Error closing the file reader", e);
            }
        }
    }

    public String getProperty(String propertyName) {
        return properties.getProperty(propertyName);
    }
}
