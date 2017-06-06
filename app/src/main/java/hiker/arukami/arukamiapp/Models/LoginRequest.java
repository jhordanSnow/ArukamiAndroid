package hiker.arukami.arukamiapp.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Jhordan on 21/5/2017.
 */

public class LoginRequest {
    @SerializedName("Username")
    @Expose
    private String username;
    @SerializedName("Password")
    @Expose
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public LoginRequest(String username, String password){
        this.username = username;
        this.password = password;
    }
}

