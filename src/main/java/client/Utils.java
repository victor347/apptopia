package client;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import java.io.*;
import java.util.Properties;

public class Utils {

    public final static String PROPS_FILE = "config.properties";
    public final static String MONGO_IMPORT_APPS_APPLE = "mongoimport -d apptopia -c apps_apple --numInsertionWorkers " + getProperties().getProperty("workers") + " --file ";
    public final static String MONGO_IMPORT_APPS_GOOGLE = "mongoimport -d apptopia -c apps_google --numInsertionWorkers " + getProperties().getProperty("workers") + " --file ";
    public final static String MONGO_IMPORT_PUBLISHER_APPLE = "mongoimport -d apptopia -c publishers_apple --numInsertionWorkers " + getProperties().getProperty("workers") + " --file ";
    public final static String MONGO_IMPORT_PUBLISHER_GOOGLE = "mongoimport -d apptopia -c publishers_google --numInsertionWorkers " + getProperties().getProperty("workers") + " --file ";
    public final static String MONGO_IMPORT_RATINGS_APPLE = "mongoimport -d apptopia -c ratings_apple --numInsertionWorkers " + getProperties().getProperty("workers") + " --file ";
    public final static String MONGO_IMPORT_RATINGS_GOOGLE = "mongoimport -d apptopia -c ratings_google --numInsertionWorkers " + getProperties().getProperty("workers") + " --file ";
    public final static String MONGO_IMPORT_APP_ESTIMATES_APPLE = "mongoimport -d apptopia -c app_estimates_apple --numInsertionWorkers " + getProperties().getProperty("workers") + " --file ";
    public final static String MONGO_IMPORT_APP_ESTIMATES_GOOGLE = "mongoimport -d apptopia -c app_estimates_google --numInsertionWorkers " + getProperties().getProperty("workers") + " --file ";
    public final static String MONGO_IMPORT_PUBLISHER_ESTIMATES_APPLE = "mongoimport -d apptopia -c publisher_estimates_apple --numInsertionWorkers " + getProperties().getProperty("workers") + " --file ";
    public final static String MONGO_IMPORT_PUBLISHER_ESTIMATES_GOOGLE = "mongoimport -d apptopia -c publisher_estimates_google --numInsertionWorkers " + getProperties().getProperty("workers") + " --file ";

    private static final Logger log = LogManager.getLogger(Utils.class.getName());
    private static Properties props;

    public static Properties getProperties()
    {
        if(props == null)
        {
            props = new Properties();

            try {

                props.load(new FileInputStream(new File(PROPS_FILE)));
            }
            catch (IOException e) {

                log.error("Exception happened - here's what I know: " + e);
            }
        }
        return props;
    }

    protected static void sleep(int time){

        try {
            log.info("Waiting: " + time + " millis");
            Thread.sleep(time);
        }
        catch (InterruptedException e) {
            log.error("Exception happened - here's what I know: ");
            log.error(e);
        }
    }

    protected static void editProperty(String key, String value){

        try {
            FileOutputStream out = new FileOutputStream(PROPS_FILE);
            getProperties().setProperty(key, value);
            getProperties().store(out, null);
            out.close();
            log.info("Property " + key + " set to " + value);
        }
        catch (FileNotFoundException e) {
            log.error("Exception happened - here's what I know: ");
            log.error(e);
        } catch (IOException e) {
            log.error("Exception happened - here's what I know: ");
            log.error(e);
        }
    }
}
