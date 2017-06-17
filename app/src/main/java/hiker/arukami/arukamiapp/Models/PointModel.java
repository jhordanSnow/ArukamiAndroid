package hiker.arukami.arukamiapp.Models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class PointModel implements Parcelable {
    @SerializedName("Latitude")
    @Expose
    private String latitude;
    @SerializedName("Longitude")
    @Expose
    private String longitude;
    @SerializedName("IdHike")
    @Expose
    private int idHike;
    @SerializedName("IdCard")
    @Expose
    private float idCard;
    @SerializedName("Photo")
    @Expose
    private String photo;
    @SerializedName("Description")
    @Expose
    private String description;
    @SerializedName("Date")
    @Expose
    private String date;

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

    public int getIdHike() {
        return idHike;
    }

    public void setIdHike(int idHike) {
        this.idHike = idHike;
    }

    public float getIdCard() {
        return idCard;
    }

    public void setIdCard(float idCard) {
        this.idCard = idCard;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.latitude);
        dest.writeString(this.longitude);
        dest.writeInt(this.idHike);
        dest.writeFloat(this.idCard);
        dest.writeString(this.photo);
        dest.writeString(this.description);
        dest.writeString(this.date);
    }

    public PointModel() {
    }

    protected PointModel(Parcel in) {
        this.latitude = in.readString();
        this.longitude = in.readString();
        this.idHike = in.readInt();
        this.idCard = in.readFloat();
        this.photo = in.readString();
        this.description = in.readString();
        this.date = in.readString();
    }

    public static final Creator<PointModel> CREATOR = new Creator<PointModel>() {
        @Override
        public PointModel createFromParcel(Parcel source) {
            return new PointModel(source);
        }

        @Override
        public PointModel[] newArray(int size) {
            return new PointModel[size];
        }
    };
}


