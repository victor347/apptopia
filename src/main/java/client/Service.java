package client;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

public class Service {

    private static final Logger log = LogManager.getLogger(Service.class.getName());
    RestTemplate restTemplate;
    Login login;

    public Service(RestTemplate restTemplate) {

        this.restTemplate = restTemplate;
        login();
    }

    protected void login(){

        log.info("Authenticating...");

        try {
            login = restTemplate.postForObject(Utils.getProperties().getProperty("host-apptopia")+
                    Utils.getProperties().getProperty("login-service")+
                    "?client="+Utils.getProperties().getProperty("client-apptopia")+
                    "&secret="+Utils.getProperties().getProperty("secret-apptopia"),
                    null, Login.class);
            log.info("Successful authentication in apptopia API!");
        }
        catch (RestClientException e) {
            log.error("Exception happened - here's what I know: ");
            log.error(e);
            Utils.sleep(Integer.parseInt(Utils.getProperties().getProperty("retry-time")));
            login();
        }
    }

    protected Bulk[] getDataDumps(String service, String dataset, String interval, String dateFrom, String date_to){

        log.info("Getting data dumps URL's...");

        ResponseEntity<Bulk []> response = null;
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", login.getToken());
            HttpEntity<String> entity = new HttpEntity<String>(null,headers);

            response = restTemplate.exchange(Utils.getProperties().getProperty("host-apptopia")+
                            service+"?dataset="+dataset+
                            "&date_from="+dateFrom+
                            "&date_to="+date_to+
                            (interval.equals("") ? "" : ("&interval="+interval))
                    , HttpMethod.GET,
                    entity, Bulk[].class);
            log.info("URL's successfully obteined...");
        }
        catch (RestClientException e) {
            log.error("Exception happened - here's what I know: ");
            log.error(e);
            Utils.sleep(Integer.parseInt(Utils.getProperties().getProperty("retry-time")));
            getDataDumps(service, dataset, interval, dateFrom, date_to);
        }

        return response.getBody();
    }
}