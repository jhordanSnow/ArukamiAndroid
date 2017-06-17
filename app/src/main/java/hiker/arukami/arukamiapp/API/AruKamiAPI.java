package hiker.arukami.arukamiapp.API;

import java.util.List;

import hiker.arukami.arukamiapp.Models.BoolModel;
import hiker.arukami.arukamiapp.Models.GetPointsResponse;
import hiker.arukami.arukamiapp.Models.HikeModel;
import hiker.arukami.arukamiapp.Models.HikePointRequest;
import hiker.arukami.arukamiapp.Models.HikePointRespond;
import hiker.arukami.arukamiapp.Models.HikeRequest;
import hiker.arukami.arukamiapp.Models.HikeResponse;
import hiker.arukami.arukamiapp.Models.JsonResponse;
import hiker.arukami.arukamiapp.Models.LoginRequest;
import hiker.arukami.arukamiapp.Models.LoginResponse;
import hiker.arukami.arukamiapp.Models.PointModel;
import hiker.arukami.arukamiapp.Models.SignUpRequest;
import hiker.arukami.arukamiapp.Models.SpinnerResponse;
import hiker.arukami.arukamiapp.Models.User;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by Jhordan on 21/5/2017.
 */

public interface AruKamiAPI {

    @POST("Login/Authenticate/")
    Call<LoginResponse> login(@Body LoginRequest user);

    @POST("Register/User")
    Call<JsonResponse> signUp(@Body SignUpRequest user);

    @GET("User/{idCard}/")
    Call<User> getUserDetails(@Path("idCard") int idCard);

    @GET("Catalog/Nationalities")
    Call<List<SpinnerResponse>> getNationalities();

    @GET("Catalog/Districts")
    Call<List<SpinnerResponse>> getDistricts();

    @GET("Catalog/Difficulties")
    Call<List<SpinnerResponse>> getDifficulties();

    @GET("Catalog/Types")
    Call<List<SpinnerResponse>> getHikeTypes();

    @GET("Catalog/Qualities")
    Call<List<SpinnerResponse>> getQualities();

    @GET("Catalog/Prices")
    Call<List<SpinnerResponse>> getPrices();

    @POST("Hikes/GeoPoint/New/")
    Call<HikePointRespond> addGeoPoint(@Body HikePointRequest point);

    @POST("Hikes/New/")
    Call<HikeResponse> addHike(@Body HikeRequest hike);

    @POST("Hikes/{id}/AddPoints")
    Call<LoginResponse> addHikePoint(@Path("id") int idCard, @Body PointModel detailPoint);

    @GET("User/{id}/Hikes/")
    Call<List<HikeModel>> getHikes(@Path("id") int idCard);

    @GET("User/{id}/Wall/")
    Call<List<HikeModel>> getWall(@Path("id") int idCard);

    @GET("User/Follow/{id}/{idUser}")
    Call<BoolModel> isFollowing(@Path("id") int idCard, @Path("idUser") int idFriend);

    @FormUrlEncoded
    @POST("User/Follow/")
    Call<JsonResponse> followUnfollow(@Field("IdCard") int IdCard, @Field("IdFriend") int IdFriend);

    @GET("User/Search/{Username}")
    Call<List<User>> SearchUser(@Path("Username") String username);

    @GET("Hikes/Like/{id}/{idUser}")
    Call<BoolModel> isLiking(@Path("id") int idCard, @Path("idUser") int idFriend);

    @FormUrlEncoded
    @POST("Hikes/Like/")
    Call<JsonResponse> likeUnlike(@Field("IdCard") int IdCard, @Field("IdHike") int IdHike);

    @FormUrlEncoded
    @POST("User/Donate/")
    Call<JsonResponse> donate(@Field("IdCard") int IdCard, @Field("Ammount") double Ammount);

    @GET("Hikes/Points/{id}")
    Call<List<GetPointsResponse>> getPoints(@Path("id") int IdCard);

    @GET("Photo")
    Call<Void> getPhoto(@Header("Path") String path);
}
