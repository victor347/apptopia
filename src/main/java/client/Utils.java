package client;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Utils {

    public final static String PROPS_FILE = "config.properties";
    public final static String MONGO_IMPORT_APPS_APPLE = "mongoimport -d apptopia -c apps_apple --numInsertionWorkers 4 --file ";
    public final static String MONGO_IMPORT_APPS_GOOGLE = "mongoimport -d apptopia -c apps_google --numInsertionWorkers 4 --file ";
    public final static String MONGO_IMPORT_PUBLISHER_APPLE = "mongoimport -d apptopia -c publishers_apple --numInsertionWorkers 4 --file ";
    public final static String MONGO_IMPORT_PUBLISHER_GOOGLE = "mongoimport -d apptopia -c publishers_google --numInsertionWorkers 4 --file ";
    public final static String MONGO_IMPORT_RATINGS_APPLE = "mongoimport -d apptopia -c ratings_apple --numInsertionWorkers 4 --file ";
    public final static String MONGO_IMPORT_RATINGS_GOOGLE = "mongoimport -d apptopia -c ratings_google --numInsertionWorkers 4 --file ";
    public final static String MONGO_IMPORT_APP_ESTIMATES_APPLE = "mongoimport -d apptopia -c app_estimates_apple --numInsertionWorkers 4 --file ";
    public final static String MONGO_IMPORT_APP_ESTIMATES_GOOGLE = "mongoimport -d apptopia -c app_estimates_google --numInsertionWorkers 4 --file ";
    public final static String MONGO_IMPORT_PUBLISHER_ESTIMATES_APPLE = "mongoimport -d apptopia -c publisher_estimates_apple --numInsertionWorkers 4 --file ";
    public final static String MONGO_IMPORT_PUBLISHER_ESTIMATES_GOOGLE = "mongoimport -d apptopia -c publisher_estimates_google --numInsertionWorkers 4 --file ";

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
}
