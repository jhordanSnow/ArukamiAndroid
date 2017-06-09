package hiker.arukami.arukamiapp;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.ArrayList;

import hiker.arukami.arukamiapp.API.APIClient;
import hiker.arukami.arukamiapp.API.UserAPI;
import hiker.arukami.arukamiapp.HikeFragment;
import hiker.arukami.arukamiapp.HikeMapFragment;
import hiker.arukami.arukamiapp.Models.HikePointRequest;
import hiker.arukami.arukamiapp.Models.HikePointRespond;
import hiker.arukami.arukamiapp.Models.HikeRequest;
import hiker.arukami.arukamiapp.Models.JsonResponse;
import hiker.arukami.arukamiapp.Models.LoginRequest;
import hiker.arukami.arukamiapp.Models.LoginResponse;
import hiker.arukami.arukamiapp.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


/**
 * A simple {@link Fragment} subclass.
 */
public class MainHikeFragment extends Fragment {

    private static MainHikeFragment instance;
    private ViewPager mViewPager;
    private static TabLayout tabLayout;
    private HikeFragment hikeGeneral;
    private HikeMapFragment hikeMap;
    private HikeRequest hike;

    public static MainHikeFragment getInstance() {
        if (instance == null) {
            instance = new MainHikeFragment();
        }
        return instance;
    }

    public void resetHike() {
        instance = null;
    }

    public MainHikeFragment() {
        hikeGeneral = new HikeFragment();
        hikeMap = new HikeMapFragment();
    }

    public HikeRequest getHike() {
        ArrayList<LatLng> points = hikeMap.getPoints();
        if (points.size() > 1) {
            hike = hikeGeneral.getHike();
            insertPoint(points.get(0).latitude, points.get(0).longitude, true);
            insertPoint(points.get(points.size() - 1).latitude, points.get(points.size() - 1).longitude, false);
            hike.setRoute(hikeMap.encodePath());
            return hike;
        }
        return null;
    }

    public void insertPoint(double latitude, double longitude, boolean start) {
        HikePointRequest point = new HikePointRequest();
        point.setLatitude(String.valueOf(latitude));
        point.setLongitude(String.valueOf(longitude));
        PointTask pointTask = new PointTask(point);
        pointTask.execute((Void) null);

    }

    public int setCaca(int id) {
        Log.wtf("adios",String.valueOf(id));

        return id;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        hikeGeneral = new HikeFragment();
        hikeMap = new HikeMapFragment();

        View inflatedView = inflater.inflate(R.layout.fragment_likes, container, false);
        mViewPager = (ViewPager) inflatedView.findViewById(R.id.viewpager);

        mViewPager.addOnPageChangeListener(
                new ViewPager.SimpleOnPageChangeListener() {
                    @Override
                    public void onPageSelected(int position) {
                        TabLayout.Tab tab = tabLayout.getTabAt(position);
                        tab.select();

                    }
                });

        tabLayout = (TabLayout) inflatedView.findViewById(R.id.tabLayout);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                showLayout();
                if (position == 2) {
                    hideLayout();
                }
                mViewPager.setCurrentItem(position);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        tabLayout.addTab(tabLayout.newTab().setText("General"));
        tabLayout.addTab(tabLayout.newTab().setText("Map"));

        View view1 = getActivity().getLayoutInflater().inflate(R.layout.side, null);
        tabLayout.addTab(tabLayout.newTab().setCustomView(view1));

        mViewPager.setAdapter(new PagerAdapter
                (getFragmentManager(), tabLayout.getTabCount()));
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));


        LinearLayout tabStrip = ((LinearLayout) tabLayout.getChildAt(0));
        // tabStrip.getChildAt(2).setClickable(false);
        return inflatedView;

    }

    public void showLayout() {
        tabLayout.setVisibility(View.VISIBLE);
        ((MainActivity) getActivity()).showButton();
        ((MainActivity) getActivity()).showNavBar();
    }

    public void hideLayout() {
        tabLayout.setVisibility(View.GONE);
        ((MainActivity) getActivity()).hideButtons();
        ((MainActivity) getActivity()).hideNavBar();
    }


    public class PagerAdapter extends FragmentStatePagerAdapter {
        int mNumOfTabs;

        public PagerAdapter(FragmentManager fm, int NumOfTabs) {
            super(fm);
            this.mNumOfTabs = NumOfTabs;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return hikeGeneral;
                case 1:
                    return hikeMap;
                case 2:

                    return new AddPointFragment();
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return mNumOfTabs;
        }
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class PointTask extends AsyncTask<Void, Void, Boolean> {

        private final String Latitude;
        private final String Longitude;
        public int id;

        PointTask(HikePointRequest point) {
            Latitude = point.getLatitude();
            Longitude = point.getLongitude();
        }

        public int getId() {
            return id;
        }

        @Override
        protected Boolean doInBackground(Void... params) {

            Retrofit retrofit = APIClient.getClient();
            UserAPI apiService = retrofit.create(UserAPI.class);
            HikePointRespond pointResponse = new HikePointRespond();
            HikePointRequest point = new HikePointRequest();
            point.setLatitude(String.valueOf(Latitude));
            point.setLongitude(String.valueOf(Longitude));
            Call<HikePointRespond> result = apiService.addGeoPoint(point);
            try {
                Log.wtf("hola", "caca");
                pointResponse = result.execute().body();
                Log.wtf("afuera", String.valueOf(pointResponse.getIdPoint()));
            } catch (IOException e) {
                e.printStackTrace();
            }

            Log.wtf("afuera", String.valueOf(pointResponse.getIdPoint()));


            try {
                // Simulate network access.
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                return false;
            }

            // TODO: register the new account here.
            if (pointResponse.isSuccess()) {
                id = pointResponse.getIdPoint();
                Log.wtf("value", String.valueOf(id));
                return true;
            }
            return false;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            setCaca(id);
        }

        @Override
        protected void onCancelled() {

        }
    }
}

