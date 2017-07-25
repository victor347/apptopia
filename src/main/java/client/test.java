package client;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Calendar;
import java.util.Date;
import java.util.stream.Stream;

public class test {

    public static void main(String args[]) {

        new test();
    }

    public test() {

        System.out.println(new Date());
        try (Stream<String> input = Files.lines(Paths.get("part.json"));
             PrintWriter output = new PrintWriter("part2.json", "UTF-8"))
        {
            input.map(s -> s.replaceAll("\"date\":\"","\"date\":ISODate(\"").replaceAll("\",\"iap_revenue\":","T00:00:00Z\"),\"iap_revenue\":"))
                    .forEachOrdered(output::println);
        }
        catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(new Date());
    }


}