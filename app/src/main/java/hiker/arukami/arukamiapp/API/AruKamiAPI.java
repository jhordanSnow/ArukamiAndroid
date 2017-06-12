package hiker.arukami.arukamiapp.API;

import java.util.List;

import hiker.arukami.arukamiapp.Models.HikePointRequest;
import hiker.arukami.arukamiapp.Models.HikePointRespond;
import hiker.arukami.arukamiapp.Models.HikeRequest;
import hiker.arukami.arukamiapp.Models.JsonResponse;
import hiker.arukami.arukamiapp.Models.LoginRequest;
import hiker.arukami.arukamiapp.Models.LoginResponse;
import hiker.arukami.arukamiapp.Models.SignUpRequest;
import hiker.arukami.arukamiapp.Models.SpinnerResponse;
import hiker.arukami.arukamiapp.Models.User;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
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
    Call<LoginResponse> addHike(@Body HikeRequest hike);
}
