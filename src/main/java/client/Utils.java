package client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Created by victo on 17/06/2017.
 */
public class Utils {

    public final static String PROPSFILE = "config.properties";
    public final static String MONGO_IMPORT_APPS_APPLE = "mongoimport -d apptopia -c apps_apple --file ";
    public final static String MONGO_IMPORT_APPS_GOOGLE = "mongoimport -d apptopia -c apps_google --file ";
    public final static String MONGO_IMPORT_PUBLISHER_APPLE = "mongoimport -d apptopia -c publishers_apple --file ";
    public final static String MONGO_IMPORT_PUBLISHER_GOOGLE = "mongoimport -d apptopia -c publishers_google --file ";
    public final static String MONGO_IMPORT_RATINGS_APPLE = "mongoimport -d apptopia -c ratings_apple --file ";
    public final static String MONGO_IMPORT_RATINGS_GOOGLE = "mongoimport -d apptopia -c ratings_google --file ";
    public final static String MONGO_IMPORT_APP_ESTIMATES_APPLE = "mongoimport -d apptopia -c app_estimates_apple --file ";
    public final static String MONGO_IMPORT_APP_ESTIMATES_GOOGLE = "mongoimport -d apptopia -c app_estimates_google --file ";
    public final static String MONGO_IMPORT_PUBLISHER_ESTIMATES_APPLE = "mongoimport -d apptopia -c publisher_estimates_apple --file ";
    public final static String MONGO_IMPORT_PUBLISHER_ESTIMATES_GOOGLE = "mongoimport -d apptopia -c publisher_estimates_google --file ";

    private static final Logger log = LoggerFactory.getLogger(Utils.class);
    private static Properties props;

    public static Properties getProperties()
    {
        if(props == null)
        {
            props = new Properties();

            try {

                props.load(new FileInputStream(new File(PROPSFILE)));
            }
            catch (IOException e) {

                log.error("exception happened - here's what I know: ");
                e.printStackTrace();
            }
        }
        return props;
    }
}
