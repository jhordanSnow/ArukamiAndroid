package hiker.arukami.arukamiapp.API;

import hiker.arukami.arukamiapp.Models.LoginRequest;
import hiker.arukami.arukamiapp.Models.LoginResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by Jhordan on 21/5/2017.
 */

public interface UserAPI {

    @POST("Login/Authenticate/")
    Call<LoginResponse> login(@Body LoginRequest user);

}
