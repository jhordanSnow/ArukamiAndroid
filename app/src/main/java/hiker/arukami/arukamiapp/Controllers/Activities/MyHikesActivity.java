package hiker.arukami.arukamiapp.Controllers.Activities;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import hiker.arukami.arukamiapp.API.APIClient;
import hiker.arukami.arukamiapp.API.AruKamiAPI;
import hiker.arukami.arukamiapp.Controllers.Fragments.MainHikeFragment;
import hiker.arukami.arukamiapp.Controllers.Fragments.ProfileFragment;
import hiker.arukami.arukamiapp.Helpers.HikeAdapter;
import hiker.arukami.arukamiapp.Models.HikeModel;
import hiker.arukami.arukamiapp.Models.HikeRequest;
import hiker.arukami.arukamiapp.Models.HikeResponse;
import hiker.arukami.arukamiapp.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MyHikesActivity extends AppCompatActivity {
    private static Integer USER_ID;
    private RecyclerView rv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_hikes);
        USER_ID = (Integer) getIntent().getExtras().get("USER_ID");
        if (USER_ID== null){
            finish();
        }
        rv = (RecyclerView)findViewById(R.id.rv);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setHasFixedSize(true);
        initializeData();
    }

    private void initializeData(){
        Retrofit retrofit = APIClient.getClient();
        AruKamiAPI apiService = retrofit.create(AruKamiAPI.class);
        Call<List<HikeModel>> result = apiService.getHikes(USER_ID);
        result.enqueue(new Callback<List<HikeModel>>() {
            @Override
            public void onResponse(Call<List<HikeModel>> call, Response<List<HikeModel>> response) {
                initializeAdapter(response.body());
                Log.wtf("caca",String.valueOf(response.body().size()));
            }

            @Override
            public void onFailure(Call<List<HikeModel>> call, Throwable t) {
                Toast.makeText(MyHikesActivity.this, "Unable to Fetch Data.", Toast.LENGTH_SHORT).show();
            }
        });



    }

    private void initializeAdapter(List<HikeModel> hikes){
        Log.wtf("caca","la caca entro");
        HikeAdapter adapter = new HikeAdapter(hikes);
        rv.setAdapter(adapter);
    }

}
