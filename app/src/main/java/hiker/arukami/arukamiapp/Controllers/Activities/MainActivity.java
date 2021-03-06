package hiker.arukami.arukamiapp.Controllers.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.text.SimpleDateFormat;

import hiker.arukami.arukamiapp.Controllers.Fragments.HikeFragment;
import hiker.arukami.arukamiapp.Controllers.Fragments.MainHikeFragment;
import hiker.arukami.arukamiapp.Controllers.Fragments.ProfileFragment;
import hiker.arukami.arukamiapp.Controllers.Fragments.SearchFragment;
import hiker.arukami.arukamiapp.Controllers.Fragments.WallFragment;
import hiker.arukami.arukamiapp.Helpers.BottomNavigationViewHelper;
import hiker.arukami.arukamiapp.Models.HikeRequest;
import hiker.arukami.arukamiapp.R;

public class MainActivity extends AppCompatActivity {

    public MainHikeFragment hikeFragment;
    public ProfileFragment profileFragment;
    public WallFragment wallFragment;
    public SearchFragment searchFragment;
    public static boolean walking = false;
    private static MainActivity instance;



    public static MainActivity getInstance() {
        if (instance == null) {
            instance = new MainActivity();
        }
        return instance;
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment = null;
            hideButtons();
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    fragment = wallFragment;
                    break;
                case R.id.navigation_search:
                    fragment = searchFragment;
                    break;
                case R.id.navigation_hike:
                    showButton();
                    fragment = hikeFragment;
                    break;
                case R.id.navigation_likes:
                    break;
                case R.id.navigation_profile:
                    fragment = profileFragment;
                    break;
            }
            getSupportFragmentManager().beginTransaction().replace(R.id.contentLayout, fragment, "TAG").commit();
            return true;
        }

    };

    public static boolean getWalking(){
        return walking;
    }

    public void showButton(){
        if (walking){
            findViewById(R.id.EndHikeButton).setVisibility(View.VISIBLE);
            findViewById(R.id.addPointButton).setVisibility(View.VISIBLE);
        }else{
            findViewById(R.id.StartHikeButton).setVisibility(View.VISIBLE);
        }
    }

    public void hideButtons(){
        findViewById(R.id.addPointButton).setVisibility(View.GONE);
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
        findViewById(R.id.addPointButton).setVisibility(View.VISIBLE);
        walking = true;
    }

    public void submitHike(){
        HikeFragment.setEndDate(getDateTime());
        HikeRequest hike = hikeFragment.getHike();
        if (hike!=null){
            Intent intent = new Intent(this, SubmitHikeActivity.class);
            intent.putExtra("MyHike",hike);
            startActivity(intent);
        }
    }

    public void endHike(){

        findViewById(R.id.StartHikeButton).setVisibility(View.VISIBLE);
        findViewById(R.id.EndHikeButton).setVisibility(View.GONE);
        findViewById(R.id.addPointButton).setVisibility(View.GONE);
        //LinearLayout tabStrip = ((LinearLayout)((TabLayout) findViewById(R.id.tabLayout)).getChildAt(0));
        walking = false;

        // Todo lo de guardar rikolinamente en la base de datos

        HikeRequest hike = hikeFragment.getHike();

//        hike.setEndDate(getDateTime());
        //insertHike(hike);

        //hikeFragment.resetHike();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        hikeFragment = MainHikeFragment.getInstance();
        wallFragment = WallFragment.getInstance();
        searchFragment = SearchFragment.getInstance();

        SharedPreferences app_preferences = PreferenceManager.getDefaultSharedPreferences(this);
        boolean logged = app_preferences.getBoolean("Islogin",false);
        if(logged){
            loadUser(app_preferences.getInt("IdCard",0));
        }else{
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }

        FloatingActionButton startHike = (FloatingActionButton) findViewById(R.id.StartHikeButton);
        startHike.setVisibility(View.GONE);
        startHike.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startHike();
            }
        });
        final BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        FloatingActionButton endHike = (FloatingActionButton) findViewById(R.id.EndHikeButton);
        endHike.setVisibility(View.GONE);
        endHike.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                submitHike();
            }
        });
        hideButtons();
        BottomNavigationViewHelper.disableShiftMode(navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    public void loadUser(int idCard){
        profileFragment = ProfileFragment.getInstance(idCard);
    }

    public void SignOut(){
        SharedPreferences mySPrefs =PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = mySPrefs.edit();
        editor.remove("IdCard").remove("Islogin");
        editor.apply();
        getSupportFragmentManager().beginTransaction().remove(profileFragment).commit();
        getSupportFragmentManager().beginTransaction().remove(hikeFragment).commit();

        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

}

