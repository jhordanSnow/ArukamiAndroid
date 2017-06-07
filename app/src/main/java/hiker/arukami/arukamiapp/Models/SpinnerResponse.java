package hiker.arukami.arukamiapp.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SpinnerResponse {

    @SerializedName("Id")
    @Expose
    private int value;
    @SerializedName("Name")
    @Expose
    private String name;

    public SpinnerResponse(int value,String name){
        this.value = value;
        this.name = name;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString () {
        return name;
    }
}