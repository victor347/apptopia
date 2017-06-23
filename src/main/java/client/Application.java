package client;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import java.io.*;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Calendar;

@SpringBootApplication
public class Application {

	//private static final Logger log = LoggerFactory.getLogger(Application.class);
	private static final Logger log = LogManager.getLogger(Application.class.getName());
	Login login;

	public static void main(String args[]) {

		SpringApplication.run(Application.class);
	}

	@Bean
	public RestTemplate restTemplate(RestTemplateBuilder builder) {

		return builder.build();
	}

	@Bean
	public CommandLineRunner run(RestTemplate restTemplate)  {
		return args -> {

			Bulk[] bulk = null;

			login = login(restTemplate);
			log.info(login.toString());

			if(Boolean.parseBoolean(Utils.getProperties().getProperty("process-apps-apple"))){

				bulk = getDataDumps(restTemplate,
						Utils.getProperties().getProperty("apple-data-dumps-service"),
						"app_general_data",
						"",
						getDate(2),
						getDate(2));
				log.info("Numero de archivos a descargar en Apps Apple " + bulk.length);

				importFilesMongo(bulk, "AppsApple-"+bulk.length+"-"+getDate(0), "\\part", Utils.MONGO_IMPORT_APPS_APPLE, Integer.parseInt(Utils.getProperties().getProperty("start-apps-apple")), Integer.parseInt(Utils.getProperties().getProperty("end-apps-apple")));
			}

			if(Boolean.parseBoolean(Utils.getProperties().getProperty("process-apps-google"))){

				bulk = getDataDumps(restTemplate,
						Utils.getProperties().getProperty("google-data-dumps-service"),
						"app_general_data",
						"",
						getDate(2),
						getDate(2));
				log.info("Numero de archivos a descargar en Apps Google " + bulk.length);

				importFilesMongo(bulk, "AppsGoogle-"+bulk.length+"-"+getDate(0), "\\part", Utils.MONGO_IMPORT_APPS_GOOGLE, Integer.parseInt(Utils.getProperties().getProperty("start-apps-google")), Integer.parseInt(Utils.getProperties().getProperty("end-apps-google")));
			}

			if(Boolean.parseBoolean(Utils.getProperties().getProperty("process-publishers-apple"))){

				bulk = getDataDumps(restTemplate,
						Utils.getProperties().getProperty("apple-data-dumps-service"),
						"publisher_general_data",
						"",
						getDate(2),
						getDate(2));
				log.info("Numero de archivos a descargar en Publishers Apple " + bulk.length);

				importFilesMongo(bulk, "PublishersApple-"+bulk.length+"-"+getDate(0), "\\part", Utils.MONGO_IMPORT_PUBLISHER_APPLE, Integer.parseInt(Utils.getProperties().getProperty("start-publisher-apple")), Integer.parseInt(Utils.getProperties().getProperty("end-publisher-apple")));
			}

			if(Boolean.parseBoolean(Utils.getProperties().getProperty("process-publishers-google"))){

				bulk = getDataDumps(restTemplate,
						Utils.getProperties().getProperty("google-data-dumps-service"),
						"publisher_general_data",
						"",
						getDate(2),
						getDate(2));
				log.info("Numero de archivos a descargar en Publisher Google " + bulk.length);

				importFilesMongo(bulk, "PublishersGoogle-"+bulk.length+"-"+getDate(0), "\\part", Utils.MONGO_IMPORT_PUBLISHER_GOOGLE, Integer.parseInt(Utils.getProperties().getProperty("start-publisher-google")), Integer.parseInt(Utils.getProperties().getProperty("end-publisher-google")));
			}

			if(Boolean.parseBoolean(Utils.getProperties().getProperty("process-ratings-apple"))){

				bulk = getDataDumps(restTemplate,
						Utils.getProperties().getProperty("apple-data-dumps-service"),
						"daily_app_ratings",
						"",
						"2017-01-01",
						getDate(0));
				log.info("Numero de archivos a descargar en Ratings Apple " + bulk.length);

				importFilesMongo(bulk, "RatingsApple-"+bulk.length+"-"+getDate(0), "\\part", Utils.MONGO_IMPORT_RATINGS_APPLE, Integer.parseInt(Utils.getProperties().getProperty("start-ratings-apple")), Integer.parseInt(Utils.getProperties().getProperty("end-ratings-apple")));
			}

			if(Boolean.parseBoolean(Utils.getProperties().getProperty("process-ratings-google"))){

				bulk = getDataDumps(restTemplate,
						Utils.getProperties().getProperty("google-data-dumps-service"),
						"daily_app_ratings",
						"",
						"2017-01-01",
						getDate(0));
				log.info("Numero de archivos a descargar en Ratings Google " + bulk.length);

				importFilesMongo(bulk, "RatingsGoogle-"+bulk.length+"-"+getDate(0), "\\part", Utils.MONGO_IMPORT_RATINGS_GOOGLE, Integer.parseInt(Utils.getProperties().getProperty("start-ratings-google")), Integer.parseInt(Utils.getProperties().getProperty("end-ratings-google")));
			}

			if(Boolean.parseBoolean(Utils.getProperties().getProperty("process-app-estimates-apple"))){

				bulk = getDataDumps(restTemplate,
						Utils.getProperties().getProperty("apple-data-dumps-service"),
						"daily_app_estimates",
						"",
						"2017-01-01",
						getDate(0));
				log.info("Numero de archivos a descargar en App Estimates Apple " + bulk.length);

				importFilesMongo(bulk, "AppEstimatesApple-"+bulk.length+"-"+getDate(0), "\\part", Utils.MONGO_IMPORT_APP_ESTIMATES_APPLE, Integer.parseInt(Utils.getProperties().getProperty("start-app-estimates-apple")), Integer.parseInt(Utils.getProperties().getProperty("end-app-estimates-apple")));
			}

			if(Boolean.parseBoolean(Utils.getProperties().getProperty("process-app-estimates-google"))){

				bulk = getDataDumps(restTemplate,
						Utils.getProperties().getProperty("google-data-dumps-service"),
						"daily_app_estimates",
						"",
						"2017-01-01",
						getDate(0));
				log.info("Numero de archivos a descargar en App Estimates Google " + bulk.length);

				importFilesMongo(bulk, "AppEstimatesGoogle-"+bulk.length+"-"+getDate(0), "\\part", Utils.MONGO_IMPORT_APP_ESTIMATES_GOOGLE, Integer.parseInt(Utils.getProperties().getProperty("start-app-estimates-google")), Integer.parseInt(Utils.getProperties().getProperty("end-app-estimates-google")));
			}

			if(Boolean.parseBoolean(Utils.getProperties().getProperty("process-publisher-estimates-apple"))){

				bulk = getDataDumps(restTemplate,
						Utils.getProperties().getProperty("apple-data-dumps-service"),
						"daily_publisher_estimates",
						"",
						"2017-01-01",
						getDate(0));
				log.info("Numero de archivos a descargar en Publisher Estimates Apple " + bulk.length);

				importFilesMongo(bulk, "PublisherEstimatesApple-"+bulk.length+"-"+getDate(0), "\\part", Utils.MONGO_IMPORT_PUBLISHER_ESTIMATES_APPLE, Integer.parseInt(Utils.getProperties().getProperty("start-publisher-estimates-apple")), Integer.parseInt(Utils.getProperties().getProperty("end-publisher-estimates-apple")));
			}

			if(Boolean.parseBoolean(Utils.getProperties().getProperty("process-publisher-estimates-google"))){

				bulk = getDataDumps(restTemplate,
						Utils.getProperties().getProperty("google-data-dumps-service"),
						"daily_publisher_estimates",
						"",
						"2017-01-01",
						getDate(0));
				log.info("Numero de archivos a descargar en Publisher Estimates Google " + bulk.length);

				importFilesMongo(bulk, "PublisherEstimatesGoogle-"+bulk.length+"-"+getDate(0), "\\part", Utils.MONGO_IMPORT_PUBLISHER_ESTIMATES_GOOGLE, Integer.parseInt(Utils.getProperties().getProperty("start-publisher-estimates-google")), Integer.parseInt(Utils.getProperties().getProperty("end-publisher-estimates-google")));
			}

			log.info("Import Successful!!");
		};
	}

	private String getDate(int days){

		Calendar c = Calendar.getInstance();

		return c.get(Calendar.YEAR)+"-"+((c.get(Calendar.MONTH)+1) < 10 ? "0" : "")+(c.get(Calendar.MONTH)+1)+"-"+((c.get(Calendar.DATE)-days) < 10 ? "0" : "")+(c.get(Calendar.DATE)-days);
	}

	private void importFilesMongo(Bulk[] bulk, String dir, String file, String command, int start, int end){

		File directory = new File(dir)	;
		directory.mkdir();
		int limit = 0;

		if(end==0){
			limit = bulk.length;
		}
		else{

			limit = end;
			log.info("Limite de descargas configurado: "+ limit);
		}

		for(int i =start; i<limit; i++){

			try {
				URL website = new URL(bulk[i].getUrl());
				ReadableByteChannel rbc = Channels.newChannel(website.openStream());
				FileOutputStream fos = new FileOutputStream(dir + file + i);
				fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
				fos.close();
				rbc.close();
				log.info("File "+ file + i + " downloaded");
			}
			catch (IOException e) {
				log.error("exception happened - here's what I know: ");
				e.printStackTrace();
			}

			executeCommand(command + dir + file + i);

			try {

				Files.delete(Paths.get(dir+file+i));
				log.info("File "+ file + i + " deleted");
			}
			catch (IOException e) {
				log.error("exception happened - here's what I know: ");
				e.printStackTrace();
			}
		}
		if (directory.delete())
			log.info("Carpeta " + directory.toString() + " borrada exitosamente");
		else
			log.error("Carpeta " + directory.toString() + " no se pudo borrar");
	}

	private Bulk[] getDataDumps(RestTemplate restTemplate, String service, String dataset, String interval, String dateFrom, String date_to){

		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", login.getToken());

		HttpEntity<String> entity = new HttpEntity<String>(null,headers);

		ResponseEntity<Bulk []> response = restTemplate.exchange(Utils.getProperties().getProperty("host-apptopia")+
						service+"?dataset="+dataset+
						"&date_from="+dateFrom+
						"&date_to="+date_to+
						(interval.equals("") ? "" : ("&interval="+interval))
				, HttpMethod.GET,
				entity, Bulk[].class);

		return response.getBody();
	}

	private Login login(RestTemplate restTemplate){

		return restTemplate.postForObject(Utils.getProperties().getProperty("host-apptopia")+
						Utils.getProperties().getProperty("login-service")+
						"?client="+Utils.getProperties().getProperty("client-apptopia")+
						"&secret="+Utils.getProperties().getProperty("secret-apptopia"),
				null,
				Login.class);
	}

	private void executeCommand (String command)  {

		String s = null;

		try {
			// run the Unix "ps -ef" command
			// using the Runtime exec method:
			Process p = Runtime.getRuntime().exec(command);

			BufferedReader stdInput = new BufferedReader(new
					InputStreamReader(p.getInputStream()));

			BufferedReader stdError = new BufferedReader(new
					InputStreamReader(p.getErrorStream()));

			// read the output from the command
			log.info("Here is the standard output of the command: " + command);
			while ((s = stdInput.readLine()) != null) {
				log.info(s);
			}

			// read any errors from the attempted command
			log.info("Here is the standard second output of the command (if any): " + command);
			while ((s = stdError.readLine()) != null) {
				log.info(s);
			}
		}
		catch (IOException e) {

			log.error("exception happened - here's what I know: ");
			e.printStackTrace();
		}
	}
}