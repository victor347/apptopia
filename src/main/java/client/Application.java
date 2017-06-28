package client;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
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

	private static final Logger log = LogManager.getLogger(Application.class.getName());

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

			Service service = new Service(restTemplate);

			Bulk[] bulk = null;

			if(Boolean.parseBoolean(Utils.getProperties().getProperty("process-apps-apple"))){

				bulk = service.getDataDumps(
						Utils.getProperties().getProperty("apple-data-dumps-service"),
						"app_general_data",
						"",
						getDate(2),
						getDate(2));
				log.info("Files to download from Apps_Apple " + bulk.length);

				importFilesMongo(bulk, "AppsApple-"+bulk.length+"-"+getDate(0), "\\part", Utils.MONGO_IMPORT_APPS_APPLE, Integer.parseInt(Utils.getProperties().getProperty("start-apps-apple")), Integer.parseInt(Utils.getProperties().getProperty("end-apps-apple")));
			}

			if(Boolean.parseBoolean(Utils.getProperties().getProperty("process-apps-google"))){

				bulk = service.getDataDumps(
						Utils.getProperties().getProperty("google-data-dumps-service"),
						"app_general_data",
						"",
						getDate(2),
						getDate(2));
				log.info("Files to download from Apps_Google " + bulk.length);

				importFilesMongo(bulk, "AppsGoogle-"+bulk.length+"-"+getDate(0), "\\part", Utils.MONGO_IMPORT_APPS_GOOGLE, Integer.parseInt(Utils.getProperties().getProperty("start-apps-google")), Integer.parseInt(Utils.getProperties().getProperty("end-apps-google")));
			}

			if(Boolean.parseBoolean(Utils.getProperties().getProperty("process-publishers-apple"))){

				bulk = service.getDataDumps(
						Utils.getProperties().getProperty("apple-data-dumps-service"),
						"publisher_general_data",
						"",
						getDate(2),
						getDate(2));
				log.info("Files to download from Publishers_Apple " + bulk.length);

				importFilesMongo(bulk, "PublishersApple-"+bulk.length+"-"+getDate(0), "\\part", Utils.MONGO_IMPORT_PUBLISHER_APPLE, Integer.parseInt(Utils.getProperties().getProperty("start-publisher-apple")), Integer.parseInt(Utils.getProperties().getProperty("end-publisher-apple")));
			}

			if(Boolean.parseBoolean(Utils.getProperties().getProperty("process-publishers-google"))){

				bulk = service.getDataDumps(
						Utils.getProperties().getProperty("google-data-dumps-service"),
						"publisher_general_data",
						"",
						getDate(2),
						getDate(2));
				log.info("Files to download from Publisher_Google " + bulk.length);

				importFilesMongo(bulk, "PublishersGoogle-"+bulk.length+"-"+getDate(0), "\\part", Utils.MONGO_IMPORT_PUBLISHER_GOOGLE, Integer.parseInt(Utils.getProperties().getProperty("start-publisher-google")), Integer.parseInt(Utils.getProperties().getProperty("end-publisher-google")));
			}

			if(Boolean.parseBoolean(Utils.getProperties().getProperty("process-ratings-apple"))){

				bulk = service.getDataDumps(
						Utils.getProperties().getProperty("apple-data-dumps-service"),
						"daily_app_ratings",
						"",
						"2017-01-01",
						getDate(0));
				log.info("Files to download from Ratings_Apple " + bulk.length);

				importFilesMongo(bulk, "RatingsApple-"+bulk.length+"-"+getDate(0), "\\part", Utils.MONGO_IMPORT_RATINGS_APPLE, Integer.parseInt(Utils.getProperties().getProperty("start-ratings-apple")), Integer.parseInt(Utils.getProperties().getProperty("end-ratings-apple")));
			}

			if(Boolean.parseBoolean(Utils.getProperties().getProperty("process-ratings-google"))){

				bulk = service.getDataDumps(
						Utils.getProperties().getProperty("google-data-dumps-service"),
						"daily_app_ratings",
						"",
						"2017-01-01",
						getDate(0));
				log.info("Files to download from Ratings_Google " + bulk.length);

				importFilesMongo(bulk, "RatingsGoogle-"+bulk.length+"-"+getDate(0), "\\part", Utils.MONGO_IMPORT_RATINGS_GOOGLE, Integer.parseInt(Utils.getProperties().getProperty("start-ratings-google")), Integer.parseInt(Utils.getProperties().getProperty("end-ratings-google")));
			}

			if(Boolean.parseBoolean(Utils.getProperties().getProperty("process-app-estimates-apple"))){

				bulk = service.getDataDumps(
						Utils.getProperties().getProperty("apple-data-dumps-service"),
						"daily_app_estimates",
						"",
						"2017-01-01",
						getDate(0));
				log.info("Files to download from App_Estimates_Apple " + bulk.length);

				importFilesMongo(bulk, "AppEstimatesApple-"+bulk.length+"-"+getDate(0), "\\part", Utils.MONGO_IMPORT_APP_ESTIMATES_APPLE, Integer.parseInt(Utils.getProperties().getProperty("start-app-estimates-apple")), Integer.parseInt(Utils.getProperties().getProperty("end-app-estimates-apple")));
			}

			if(Boolean.parseBoolean(Utils.getProperties().getProperty("process-app-estimates-google"))){

				bulk = service.getDataDumps(
						Utils.getProperties().getProperty("google-data-dumps-service"),
						"daily_app_estimates",
						"",
						"2017-01-01",
						getDate(0));
				log.info("Files to download from App_Estimates_Google " + bulk.length);

				importFilesMongo(bulk, "AppEstimatesGoogle-"+bulk.length+"-"+getDate(0), "\\part", Utils.MONGO_IMPORT_APP_ESTIMATES_GOOGLE, Integer.parseInt(Utils.getProperties().getProperty("start-app-estimates-google")), Integer.parseInt(Utils.getProperties().getProperty("end-app-estimates-google")));
			}

			if(Boolean.parseBoolean(Utils.getProperties().getProperty("process-publisher-estimates-apple"))){

				bulk = service.getDataDumps(
						Utils.getProperties().getProperty("apple-data-dumps-service"),
						"daily_publisher_estimates",
						"",
						"2017-01-01",
						getDate(0));
				log.info("Files to download from Publisher_Estimates_Apple " + bulk.length);

				importFilesMongo(bulk, "PublisherEstimatesApple-"+bulk.length+"-"+getDate(0), "\\part", Utils.MONGO_IMPORT_PUBLISHER_ESTIMATES_APPLE, Integer.parseInt(Utils.getProperties().getProperty("start-publisher-estimates-apple")), Integer.parseInt(Utils.getProperties().getProperty("end-publisher-estimates-apple")));
			}

			if(Boolean.parseBoolean(Utils.getProperties().getProperty("process-publisher-estimates-google"))){

				bulk = service.getDataDumps(
						Utils.getProperties().getProperty("google-data-dumps-service"),
						"daily_publisher_estimates",
						"",
						"2017-01-01",
						getDate(0));
				log.info("Files to download from Publisher_Estimates_Google " + bulk.length);

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
		int limit;

		if(end == 0){

			limit = bulk.length;
		}
		else{

			limit = end;
			log.info("Download limit set: "+ limit);
		}

		for(int i = start; i < limit; i++){

			try {
				log.info("Downloading file " + i);
				URL website = new URL(bulk[i].getUrl());
				ReadableByteChannel rbc = Channels.newChannel(website.openStream());
				FileOutputStream fos = new FileOutputStream(dir + file + i);
				fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
				fos.close();
				rbc.close();
				log.info("File "+ file + i + " downloaded");
			}
			catch (IOException e) {
				log.error("Exception happened - here's what I know: ");
				log.error(e);
				i--;
				Utils.sleep(Integer.parseInt(Utils.getProperties().getProperty("retry-time")));
				continue;
			}

			executeCommand(command + dir + file + i);

			try {

				Files.delete(Paths.get(dir+file+i));
				log.info("File "+ file + i + " deleted");
			}
			catch (IOException e) {
				log.error("Exception happened - here's what I know: " + e);
			}
		}
		if (directory.delete())
			log.info("Directory " + directory.toString() + " deleted sucessfully");
		else
			log.error("Directory " + directory.toString() + " couldn't be deleted");
	}

	private void executeCommand (String command)  {

		String s;

		try {

			Process p = Runtime.getRuntime().exec(command);

			BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));

			BufferedReader stdError = new BufferedReader(new InputStreamReader(p.getErrorStream()));

			log.info("Standard output of the command: " + command);
			while ((s = stdError.readLine()) != null) {
				log.info(s);

				if(s.contains("imported 0 documents")){
					log.info("Retrying command: " + command.substring(command.lastIndexOf("\\") + 1));
					executeCommand(command);
				}
			}

			log.info("Failure output of the command (if any): " + command);
			while ((s = stdInput.readLine()) != null) {
				log.info(s);
			}
		}
		catch (IOException e) {

			log.error("Exception happened - here's what I know: " + e);
		}
	}
}