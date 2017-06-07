package hiker.arukami.arukamiapp.API;

import java.util.List;

import hiker.arukami.arukamiapp.Models.JsonResponse;
import hiker.arukami.arukamiapp.Models.LoginRequest;
import hiker.arukami.arukamiapp.Models.LoginResponse;
import hiker.arukami.arukamiapp.Models.SignUpRequest;
import hiker.arukami.arukamiapp.Models.SpinnerResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * Created by Jhordan on 21/5/2017.
 */

public interface UserAPI {

    @POST("Login/Authenticate/")
    Call<LoginResponse> login(@Body LoginRequest user);

    @POST("Register/User")
    Call<JsonResponse> signUp(@Body SignUpRequest user);

    @GET("Catalog/Nationalities")
    Call<List<SpinnerResponse>> getNationalities();

    @GET("Catalog/Difficulties")
    Call<List<SpinnerResponse>> getDifficulties();

    @GET("Catalog/Types")
    Call<List<SpinnerResponse>> getHikeTypes();

    @GET("Catalog/Qualities")
    Call<List<SpinnerResponse>> getQualities();

    @GET("Catalog/Prices")
    Call<List<SpinnerResponse>> getPrices();

}
