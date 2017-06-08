package hiker.arukami.arukamiapp;

import android.icu.util.Calendar;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;

public class MainActivity extends AppCompatActivity {
    public MainHikeFragment hikeFragment = MainHikeFragment.getInstance();
    public static boolean walking = false;
    private TabLayout tabLayout;

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

    public String getDateTime(){
        final java.util.Calendar c = java.util.Calendar.getInstance();

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = format.format(c.getTime());

        HikeFragment.setStartDate(date);

        return date;
    }

    public void startHike(){
        TextView startHike = (TextView) findViewById(R.id.start_date_label);
        startHike.setText(getDateTime());

        findViewById(R.id.StartHikeButton).setVisibility(View.GONE);
        findViewById(R.id.EndHikeButton).setVisibility(View.VISIBLE);

        LinearLayout tabStrip = ((LinearLayout)((TabLayout) findViewById(R.id.tabLayout)).getChildAt(0));
        tabStrip.getChildAt(2).setClickable(true);
        walking = true;
    }

    public void endHike(){
        findViewById(R.id.StartHikeButton).setVisibility(View.VISIBLE);
        findViewById(R.id.EndHikeButton).setVisibility(View.GONE);

        LinearLayout tabStrip = ((LinearLayout)((TabLayout) findViewById(R.id.tabLayout)).getChildAt(0));
        tabStrip.setEnabled(false);
        tabStrip.getChildAt(2).setClickable(false);

        walking = false;

        // Todo lo de guardar rikolinamente en la base de datos

        hikeFragment.resetHike();
        Log.wtf("encodeCaca",HikeMapFragment.encodePath());
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

