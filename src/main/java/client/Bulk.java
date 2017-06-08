package client;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
/**
 * Created by victo on 07/06/2017.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Bulk {

    private String date;
    private int part;
    private String url;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getPart() {
        return part;
    }

    public void setPart(int part) {
        this.part = part;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "Bulk{" +
                "date='" + date + '\'' +
                "part='" + part + '\'' +
                "url='" + url + '\'' +
                '}';
    }
}
