package client;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Calendar;
import java.util.stream.Stream;

public class test {

    public static void main(String args[]) {

        new test();
    }

    public test() {

        try (Stream<String> input = Files.lines(Paths.get("part.json"));
             PrintWriter output = new PrintWriter("part2.json", "UTF-8"))
        {
            input.map(s -> s.replaceAll(",\"id\":\"", ",\"id\":").replaceAll("\",\"breakout\":",",\"breakout\":"))
                    .forEachOrdered(output::println);
        }
        catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getDate(int days){

        Calendar c = Calendar.getInstance();


        return c.get(Calendar.YEAR)+
                "-"+
                ((c.get(Calendar.MONTH)+1) < 10 ? "0" : "")+
                (c.get(Calendar.MONTH)+1)+
                "-"+
                ((c.get(Calendar.DATE)-days) < 10 ? "0" : "")+
                (c.get(Calendar.DATE)-days);
    }

}