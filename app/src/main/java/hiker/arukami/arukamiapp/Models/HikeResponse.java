package hiker.arukami.arukamiapp.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class HikeResponse {

    @SerializedName("Success")
    @Expose
    private boolean success;
    @SerializedName("Msg")
    @Expose
    private String msg;
    @SerializedName("IdHike")
    @Expose
    private int idHike;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return msg;
    }

    public void setMessage(String msg) {
        this.msg = msg;
    }

    public int getIdHike() {
        return idHike;
    }

    public void setIdHike(int idHike) {
        this.idHike = idHike;
    }

}

