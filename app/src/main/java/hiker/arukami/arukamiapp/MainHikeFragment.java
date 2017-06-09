package hiker.arukami.arukamiapp;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import hiker.arukami.arukamiapp.API.APIClient;
import hiker.arukami.arukamiapp.API.UserAPI;
import hiker.arukami.arukamiapp.Models.HikePointRequest;
import hiker.arukami.arukamiapp.Models.HikePointRespond;
import hiker.arukami.arukamiapp.Models.HikeRequest;
import retrofit2.Call;
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
        InsertPointCall pointTask = new InsertPointCall();
        try {
            pointTask.execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        hike.setRoute(hikeMap.encodePath());
        return hike;

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


        private class InsertPointCall extends AsyncTask<Call, Void, HikeRequest> {

        private HikePointRequest startPoint;
        private HikePointRequest endPoint;

        @Override
        protected void onPreExecute() {
            ArrayList<LatLng> points = hikeMap.getPoints();
            if (points.size() > 1) {
                hike = hikeGeneral.getHike();
                startPoint = new HikePointRequest(String.valueOf(points.get(0).latitude), String.valueOf(points.get(0).longitude));
                endPoint = new HikePointRequest(String.valueOf(points.get(points.size()-1).latitude), String.valueOf(points.get(points.size()-1).longitude));

            }
        }

        @Override
        protected HikeRequest doInBackground(Call... params) {
            Retrofit retrofit = APIClient.getClient();
            UserAPI apiService = retrofit.create(UserAPI.class);
            HikePointRespond startPointResponse = new HikePointRespond();
            HikePointRespond endPointResponse  = new HikePointRespond();
            Call<HikePointRespond> startPointResult = apiService.addGeoPoint(startPoint);
            Call<HikePointRespond> endPointResult = apiService.addGeoPoint(endPoint);
            try {
                startPointResponse = startPointResult.execute().body();
                endPointResponse = endPointResult.execute().body();

            } catch (IOException e) {
                e.printStackTrace();
            }

            hike.setStartPoint(startPointResponse.getIdPoint());
            hike.setEndPoint(endPointResponse.getIdPoint());
            return hike;
        }

        @Override
        protected void onPostExecute(HikeRequest result) {
        }
    }
}

