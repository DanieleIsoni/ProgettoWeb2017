/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unitn.buyhub.utils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 *
 * @author massimo
 */
public class PropertyHandler{

   private static PropertyHandler instance = null;

   private Properties props = null;

   private PropertyHandler(){
        props = new Properties();
        String propFileName = "config.properties";

        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(propFileName);

        if (inputStream != null) {
            try{  
                props.load(inputStream);
            }
            catch(IOException ex)
            {
                Log.error("Error reading properties file!"+ex.getMessage());

            }
        } else {
              
            Log.error("Error reading properties file!");

        }
   }

   public static synchronized PropertyHandler getInstance(){
       if (instance == null)
           instance = new PropertyHandler();
       return instance;
   }

   public String getValue(String propKey){
       return this.props.getProperty(propKey)!= null ?this.props.getProperty(propKey) : "";
   }
}