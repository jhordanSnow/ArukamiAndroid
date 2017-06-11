package hiker.arukami.arukamiapp.Models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class HikeRequest implements Parcelable {

    @SerializedName("Name")
    @Expose
    private String name;
    @SerializedName("StartDate")
    @Expose
    private String startDate;
    @SerializedName("EndDate")
    @Expose
    private String endDate;
    @SerializedName("Route")
    @Expose
    private String route;
    @SerializedName("Photo")
    @Expose
    private String photo;
    @SerializedName("District")
    @Expose
    private int district;

    @SerializedName("QualityLevel")
    @Expose
    private int qualityLevel;
    @SerializedName("PriceLevel")
    @Expose
    private int priceLevel;
    @SerializedName("Difficulty")
    @Expose
    private int difficulty;
    @SerializedName("HikeType")
    @Expose
    private int hikeType;
    @SerializedName("StartPoint")
    @Expose
    private int startPoint;
    @SerializedName("EndPoint")
    @Expose
    private int endPoint;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getRoute() {
        return route;
    }

    public void setRoute(String route) {
        this.route = route;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public int getDistrict() {
        return district;
    }

    public void setDistrict(int district) {
        this.district = district;
    }

    public int getQualityLevel() {
        return qualityLevel;
    }

    public void setQualityLevel(int qualityLevel) {
        this.qualityLevel = qualityLevel;
    }

    public int getPriceLevel() {
        return priceLevel;
    }

    public void setPriceLevel(int priceLevel) {
        this.priceLevel = priceLevel;
    }

    public int getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }

    public int getHikeType() {
        return hikeType;
    }

    public void setHikeType(int hikeType) {
        this.hikeType = hikeType;
    }

    public int getStartPoint() {
        return startPoint;
    }

    public void setStartPoint(int startPoint) {
        this.startPoint = startPoint;
    }

    public int getEndPoint() {
        return endPoint;
    }

    public void setEndPoint(int endPoint) {
        this.endPoint = endPoint;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.startDate);
        dest.writeString(this.endDate);
        dest.writeString(this.route);
        dest.writeString(this.photo);
        dest.writeInt(this.district);
        dest.writeInt(this.qualityLevel);
        dest.writeInt(this.priceLevel);
        dest.writeInt(this.difficulty);
        dest.writeInt(this.hikeType);
        dest.writeInt(this.startPoint);
        dest.writeInt(this.endPoint);
    }

    public HikeRequest() {
    }

    protected HikeRequest(Parcel in) {
        this.name = in.readString();
        this.startDate = in.readString();
        this.endDate = in.readString();
        this.route = in.readString();
        this.photo = in.readString();
        this.district = in.readInt();
        this.qualityLevel = in.readInt();
        this.priceLevel = in.readInt();
        this.difficulty = in.readInt();
        this.hikeType = in.readInt();
        this.startPoint = in.readInt();
        this.endPoint = in.readInt();
    }

    public static final Parcelable.Creator<HikeRequest> CREATOR = new Parcelable.Creator<HikeRequest>() {
        @Override
        public HikeRequest createFromParcel(Parcel source) {
            return new HikeRequest(source);
        }

        @Override
        public HikeRequest[] newArray(int size) {
            return new HikeRequest[size];
        }
    };
}


