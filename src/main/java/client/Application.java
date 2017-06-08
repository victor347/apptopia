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

import java.io.*;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.Calendar;

@SpringBootApplication
public class Application {

	private static final Logger log = LoggerFactory.getLogger(Application.class);

	public static void main(String args[]) {
		SpringApplication.run(Application.class);
	}

	@Bean
	public RestTemplate restTemplate(RestTemplateBuilder builder) {
		return builder.build();
	}

	@Bean
	public CommandLineRunner run(RestTemplate restTemplate) throws Exception {
		return args -> {
			Login login = restTemplate.postForObject(
					"https://integrations.apptopia.com/api/login?client=JYxPttnrQakG&secret=xLqvkRYPtrAuZbdHm6caJkeo", null, Login.class);
			log.info(login.toString());

			HttpHeaders headers = new HttpHeaders();
			headers.set("Authorization", login.getToken());

			HttpEntity<String> entity = new HttpEntity<String>(null,headers);

			ResponseEntity<Bulk []> response = restTemplate.exchange(
					"https://integrations.apptopia.com/api/itunes_connect/data_dumps?dataset=app_general_data&date_from=2017-06-06&date_to=2017-06-06", HttpMethod.GET, entity, Bulk[].class);

			Bulk[] bulk = response.getBody();
			log.info(bulk[0].toString());


			Calendar c = Calendar.getInstance();
			String fecha = c.get(Calendar.DATE)+"-"+c.get(Calendar.MONTH)+"-"+c.get(Calendar.YEAR);
			String file = "Apps-" +fecha;
			String toFile = file+"\\part";
			File dir = new File(file)	;
			dir.mkdir();
			String command =null;

			for(int i =0; i<bulk.length; i++){

				try {

					URL website = new URL(bulk[i].getUrl());
					ReadableByteChannel rbc = Channels.newChannel(website.openStream());
					FileOutputStream fos = new FileOutputStream(toFile+i);
					fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
					fos.close();
					rbc.close();
					log.info("Archivo "+toFile+i + " descargado");
					command = "mongoimport -d test -c apps --file " + toFile + i;
					executeCommand(command);

				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		};
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
			log.info("Here is the standard error of the command (if any): " + command);
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