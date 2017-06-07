package hiker.arukami.arukamiapp;

import android.app.FragmentManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    //mTextMessage.setText(R.string.title_home);
                    return true;
                case R.id.navigation_search:
                    //mTextMessage.setText(R.string.title_search);
                    return true;
                case R.id.navigation_hike:
                    HikeFragment hikeFragment = new HikeFragment();
                    FragmentManager manager = getFragmentManager();
                    manager.beginTransaction().replace(R.id.contentLayout, hikeFragment, hikeFragment.getTag()).commit();
                    return true;
                case R.id.navigation_likes:
                    //mTextMessage.setText(R.string.title_likes);
                    return true;
                case R.id.navigation_profile:
                    //mTextMessage.setText(R.string.title_profile);
                    return true;
            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        BottomNavigationViewHelper.disableShiftMode(navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

}

