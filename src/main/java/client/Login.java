package client;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
/**
 * Created by victo on 07/06/2017.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Login {

    private String token;

    public Login() {
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public String toString() {
        return "Login{" +
                "token='" + token + '\'' +
                '}';
    }
}
