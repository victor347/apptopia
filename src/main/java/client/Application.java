package client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

import javax.rmi.CORBA.Util;
import java.io.*;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.Calendar;
import java.util.Properties;

@SpringBootApplication
public class Application {

	private static final Logger log = LoggerFactory.getLogger(Application.class);
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

			login = login(restTemplate);
			log.info(login.toString());

			//Apps apple
			Bulk[] bulk = getDataDumps(restTemplate,
					Utils.getProperties().getProperty("apple-data-dumps-service"),
					"app_general_data",
					"",
					getDate(1),
					getDate(1));
			log.info("Numero de archivos a descargar en Apps Apple " + bulk.length);

			importFilesMongo(bulk, "AppsApple-"+bulk.length+"-"+getDate(0), "\\part", Utils.MONGO_IMPORT_APPS_APPLE);

			//Apps google
			bulk=null;
			bulk = getDataDumps(restTemplate,
					Utils.getProperties().getProperty("google-data-dumps-service"),
					"app_general_data",
					"",
					getDate(1),
					getDate(1));
			log.info("Numero de archivos a descargar en Apps Google " + bulk.length);

			importFilesMongo(bulk, "AppsGoogle-"+bulk.length+"-"+getDate(0), "\\part", Utils.MONGO_IMPORT_APPS_GOOGLE);

			//Publishers apple
			bulk=null;
			bulk = getDataDumps(restTemplate,
					Utils.getProperties().getProperty("apple-data-dumps-service"),
					"publisher_general_data",
					"",
					getDate(1),
					getDate(1));
			log.info("Numero de archivos a descargar en Publishers Apple " + bulk.length);

			importFilesMongo(bulk, "PublishersApple-"+bulk.length+"-"+getDate(0), "\\part", Utils.MONGO_IMPORT_PUBLISHER_APPLE);

			//Publishers google
			bulk=null;
			bulk = getDataDumps(restTemplate,
					Utils.getProperties().getProperty("google-data-dumps-service"),
					"publisher_general_data",
					"",
					getDate(1),
					getDate(1));
			log.info("Numero de archivos a descargar en Publisher Google " + bulk.length);

			importFilesMongo(bulk, "PublishersGoogle-"+bulk.length+"-"+getDate(0), "\\part", Utils.MONGO_IMPORT_PUBLISHER_GOOGLE);
		};
	}

	private String getDate(int days){

		Calendar c = Calendar.getInstance();

		return c.get(Calendar.YEAR)+"-"+((c.get(Calendar.MONTH)+1) < 10 ? "0" : "")+(c.get(Calendar.MONTH)+1)+"-"+((c.get(Calendar.DATE)-days) < 10 ? "0" : "")+(c.get(Calendar.DATE)-days);
	}

	private void importFilesMongo(Bulk[] bulk,String dir, String file, String command){

		File directory = new File(dir)	;
		directory.mkdir();

		for(int i =0; i<bulk.length; i++){

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
		}
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
			log.info("Here is the standard error output of the command (if any): " + command);
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