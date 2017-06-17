package hiker.arukami.arukamiapp.Models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class HikePointRequest implements Parcelable {

    @SerializedName("Latitude")
    @Expose
    private String latitude;
    @SerializedName("Longitude")
    @Expose
    private String longitude;

    public HikePointRequest(String latitude, String longitude){
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.latitude);
        dest.writeString(this.longitude);
    }

    protected HikePointRequest(Parcel in) {
        this.latitude = in.readString();
        this.longitude = in.readString();
    }

    public static final Parcelable.Creator<HikePointRequest> CREATOR = new Parcelable.Creator<HikePointRequest>() {
        @Override
        public HikePointRequest createFromParcel(Parcel source) {
            return new HikePointRequest(source);
        }

        @Override
        public HikePointRequest[] newArray(int size) {
            return new HikePointRequest[size];
        }
    };
}


