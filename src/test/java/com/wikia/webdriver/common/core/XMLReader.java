package com.wikia.webdriver.common.core;

import com.wikia.webdriver.common.core.configuration.Configuration;
import com.wikia.webdriver.common.logging.PageObjectLogging;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.XMLConfiguration;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class XMLReader {

  private static final File defaultConfigFile = new File(Configuration.getCredentialsFilePath());

  private XMLReader() {

  }

  /**
   * method used to get credentials from configuration xml
   */
  public static String getValue(File file, String key) {

    /*try {
      XMLConfiguration xml = new XMLConfiguration(file);
      return xml.getString(key);
    } catch (ConfigurationException e) {
      PageObjectLogging.log("Error while reading XML config", e, false);

      return e.getMessage();
    }*/

    Properties prop = new Properties();
    InputStream input = null;

    try {

      input = new FileInputStream(file);

      // load a properties file
      prop.load(input);

      // get the property value and print it out
      System.out.println("value for " + key + " = " + prop.getProperty(key));
      if(StringUtils.isEmpty(prop.getProperty(key)) ) {
        System.out.println("Can not find value for " + key);
      }
      return prop.getProperty(key);

    } catch (IOException ex) {
      ex.printStackTrace();
    } finally {
      if (input != null) {
        try {
          input.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
    System.out.println("Can not find value for " + key);
    return "";
  }

  public static String getValue(String key) {
    return getValue(defaultConfigFile, key);
  }
}
