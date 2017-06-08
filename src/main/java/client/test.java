package client;

import java.io.*;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by victo on 07/06/2017.
 */
public class test {

    public static void main (String args []){

        new test();
    }

    public test() {



        String domainName = "google.com";

        //in mac oxs
        String command = "mongoimport -d test -c ratings --file Apps-7-5-2017\\part50";


        String output = executeCommand(command);

        System.out.println(output);

    }



    private String executeCommand(String command) {

        StringBuffer output = new StringBuffer();

        Process p;
        try {
            p = Runtime.getRuntime().exec(command);

            BufferedReader reader =
                    new BufferedReader(new InputStreamReader(p.getInputStream()));

            String line = null;
            while ((line = reader.readLine())!= null) {
                output.append(line + "\n");
                System.out.println(line);
            }
            int exitVal = p.waitFor();
            System.out.println("Exited with error code "+exitVal);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return output.toString();

    }

}
