package hiker.arukami.arukamiapp.Models;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class HikeModel implements Parcelable {

    @SerializedName("IdHike")
    @Expose
    private int idHike;
    @SerializedName("HikeName")
    @Expose
    private String hikeName;
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
    @SerializedName("StartPoint")
    @Expose
    private HikePointRequest startPoint;
    @SerializedName("EndPoint")
    @Expose
    private HikePointRequest endPoint;
    @SerializedName("District")
    @Expose
    private String district;
    @SerializedName("Quality")
    @Expose
    private String quality;
    @SerializedName("Price")
    @Expose
    private String price;
    @SerializedName("Difficulty")
    @Expose
    private String difficulty;
    @SerializedName("HikeType")
    @Expose
    private String hikeType;
    @SerializedName("Creator")
    @Expose
    private String creator;
    @SerializedName("IdCreator")
    @Expose
    private float idCreator;

    @SerializedName("Description")
    @Expose
    private String description;

    @SerializedName("IsLiked")
    @Expose
    private Boolean like;


    public String getHikeName() {
        return hikeName;
    }

    public void setHikeName(String hikeName) {
        this.hikeName = hikeName;
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

    public HikePointRequest getStartPoint() {
        return startPoint;
    }

    public void setStartPoint(HikePointRequest startPoint) {
        this.startPoint = startPoint;
    }

    public HikePointRequest getEndPoint() {
        return endPoint;
    }

    public void setEndPoint(HikePointRequest endPoint) {
        this.endPoint = endPoint;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getQuality() {
        return quality;
    }

    public void setQuality(String quality) {
        this.quality = quality;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public String getHikeType() {
        return hikeType;
    }

    public void setHikeType(String hikeType) {
        this.hikeType = hikeType;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public float getIdCreator() {
        return idCreator;
    }

    public void setIdCreator(float idCreator) {
        this.idCreator = idCreator;
    }

    public int getIdHike() {
        return idHike;
    }

    public void setIdHike(int idHike) {
        this.idHike = idHike;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getLike() {
        return like;
    }

    public void setLike(Boolean like) {
        this.like = like;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.idHike);
        dest.writeString(this.hikeName);
        dest.writeString(this.startDate);
        dest.writeString(this.endDate);
        dest.writeString(this.route);
        dest.writeString(this.photo);
        dest.writeParcelable(this.startPoint, flags);
        dest.writeParcelable(this.endPoint, flags);
        dest.writeString(this.district);
        dest.writeString(this.quality);
        dest.writeString(this.price);
        dest.writeString(this.difficulty);
        dest.writeString(this.hikeType);
        dest.writeString(this.creator);
        dest.writeFloat(this.idCreator);
        dest.writeString(this.description);
        dest.writeValue(this.like);
    }

    public HikeModel() {
    }

    protected HikeModel(Parcel in) {
        this.idHike = in.readInt();
        this.hikeName = in.readString();
        this.startDate = in.readString();
        this.endDate = in.readString();
        this.route = in.readString();
        this.photo = in.readString();
        this.startPoint = in.readParcelable(HikePointRequest.class.getClassLoader());
        this.endPoint = in.readParcelable(HikePointRequest.class.getClassLoader());
        this.district = in.readString();
        this.quality = in.readString();
        this.price = in.readString();
        this.difficulty = in.readString();
        this.hikeType = in.readString();
        this.creator = in.readString();
        this.idCreator = in.readFloat();
        this.description = in.readString();
        this.like = (Boolean) in.readValue(Boolean.class.getClassLoader());
    }

    public static final Creator<HikeModel> CREATOR = new Creator<HikeModel>() {
        @Override
        public HikeModel createFromParcel(Parcel source) {
            return new HikeModel(source);
        }

        @Override
        public HikeModel[] newArray(int size) {
            return new HikeModel[size];
        }
    };
}
