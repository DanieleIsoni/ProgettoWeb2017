package it.unitn.buyhub.utils;
import org.apache.log4j.Logger;
/**
 * A simple logging class, a wrapper on log4j
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
