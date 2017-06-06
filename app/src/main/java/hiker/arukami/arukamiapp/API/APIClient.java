package hiker.arukami.arukamiapp.API;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Jhordan on 21/5/2017.
 */

public class APIClient {
    private static final String BASE_URL = "http://192.168.1.142:61442/api/";
    private static Retrofit retrofit = null;

    public static Retrofit getClient(){
        if (retrofit==null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

}
