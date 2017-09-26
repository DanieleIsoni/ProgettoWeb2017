/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unitn.buyhub.utils;
import org.apache.log4j.Logger;
/**
 *
 * @author maxgiro96
 */
public class Log {
    private static Logger logger;
    
    public synchronized static void info(Object obj)
    {
         logger = Logger.getRootLogger();
         logger.info(obj);
    }
    
    public synchronized static void warn(Object obj)
    {
         logger = Logger.getRootLogger();
         logger.warn(obj);
    }
    
    public synchronized static void error(Object obj)
    {
         logger = Logger.getRootLogger();
         logger.error(obj);
    }
}

