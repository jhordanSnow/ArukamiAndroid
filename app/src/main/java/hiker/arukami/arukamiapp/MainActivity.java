package hiker.arukami.arukamiapp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;

import hiker.arukami.arukamiapp.API.APIClient;
import hiker.arukami.arukamiapp.API.UserAPI;
import hiker.arukami.arukamiapp.Models.HikeRequest;
import hiker.arukami.arukamiapp.Models.LoginResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity {
    public MainHikeFragment hikeFragment = MainHikeFragment.getInstance();
    public static boolean walking = false;
    private static TabLayout tabLayout;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            FragmentManager manager = getSupportFragmentManager();
            hideButtons();
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    //mTextMessage.setText(R.string.title_home);
                    return true;
                case R.id.navigation_search:
                    //mTextMessage.setText(R.string.title_search);
                    return true;
                case R.id.navigation_hike:
                    showButton();
                    manager.beginTransaction().replace(R.id.contentLayout, hikeFragment, hikeFragment.getTag()).commit();
                    return true;
                case R.id.navigation_likes:
                    return true;
                case R.id.navigation_profile:
                    //mTextMessage.setText(R.string.title_profile);
                    return true;
            }
            return false;
        }

    };

    public static boolean getWalking(){
        return walking;
    }

    public void showButton(){
        if (walking){
            findViewById(R.id.EndHikeButton).setVisibility(View.VISIBLE);
        }else{
            findViewById(R.id.StartHikeButton).setVisibility(View.VISIBLE);
        }
    }

    public void hideButtons(){
        findViewById(R.id.StartHikeButton).setVisibility(View.GONE);
        findViewById(R.id.EndHikeButton).setVisibility(View.GONE);
    }

    public void hideNavBar(){
        findViewById(R.id.navigation).setVisibility(View.GONE);
    }
    public void showNavBar(){
        findViewById(R.id.navigation).setVisibility(View.VISIBLE);
    }


    public static String getDateTime(){
        final java.util.Calendar c = java.util.Calendar.getInstance();

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = format.format(c.getTime());

        return date;
    }

    public void startHike(){
        TextView startHike = (TextView) findViewById(R.id.start_date_label);
        String date = getDateTime();
        startHike.setText(date);
        HikeFragment.setStartDate(date);

        findViewById(R.id.StartHikeButton).setVisibility(View.GONE);
        findViewById(R.id.EndHikeButton).setVisibility(View.VISIBLE);
//
//        LinearLayout tabStrip = ((LinearLayout)((TabLayout) findViewById(R.id.tabLayout)).getChildAt(0));
//        tabStrip.getChildAt(2).setClickable(true);
        walking = true;
    }

    public void endHike(){

        findViewById(R.id.StartHikeButton).setVisibility(View.VISIBLE);
        findViewById(R.id.EndHikeButton).setVisibility(View.GONE);
        HikeFragment.setEndDate(getDateTime());
        LinearLayout tabStrip = ((LinearLayout)((TabLayout) findViewById(R.id.tabLayout)).getChildAt(0));
        walking = false;

        // Todo lo de guardar rikolinamente en la base de datos

        HikeRequest hike = hikeFragment.getHike();
        hike.setEndDate(getDateTime());
        insertHike(hike);

        hikeFragment.resetHike();
    }

    public void insertHike(HikeRequest hike){

        Retrofit retrofit = APIClient.getClient();
        UserAPI apiService = retrofit.create(UserAPI.class);
        final LoginResponse hikeResponse = new LoginResponse();
        Call<LoginResponse> result = apiService.addHike(hike);

        result.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                hikeResponse.setMessage(response.body().getMessage());
                hikeResponse.setSuccess(response.body().isSuccess());
                Log.wtf("CACA", hikeResponse.getMessage());

            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                hikeResponse.setMessage("Something went wrong.");
                hikeResponse.setSuccess(false);
            }
        });

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FloatingActionButton startHike = (FloatingActionButton) findViewById(R.id.StartHikeButton);
        startHike.setVisibility(View.GONE);
        startHike.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startHike();
            }
        });

        FloatingActionButton endHike = (FloatingActionButton) findViewById(R.id.EndHikeButton);
        endHike.setVisibility(View.GONE);
        endHike.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                endHike();
            }
        });
        findViewById(R.id.EndHikeButton).setVisibility(View.GONE);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        BottomNavigationViewHelper.disableShiftMode(navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
    }

}

