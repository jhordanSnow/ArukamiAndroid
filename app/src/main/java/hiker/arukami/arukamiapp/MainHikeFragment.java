package hiker.arukami.arukamiapp;


import android.content.Intent;
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

import hiker.arukami.arukamiapp.HikeFragment;
import hiker.arukami.arukamiapp.HikeMapFragment;
import hiker.arukami.arukamiapp.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class MainHikeFragment extends Fragment {

    private static MainHikeFragment instance;
    private ViewPager mViewPager;
    private TabLayout tabLayout;

    public static MainHikeFragment getInstance(){
        if (instance == null){
            instance = new MainHikeFragment();
        }
        return instance;
    }

    public void resetHike(){
        instance = null;
    }

    public MainHikeFragment() {
        // Required empty public constructor
    }


//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//
//
//        View inflatedView = inflater.inflate(R.layout.fragment_likes, container, false);
//
//        TabLayout tabLayout = (TabLayout) inflatedView.findViewById(R.id.tabLayout);
//        tabLayout.addTab(tabLayout.newTab().setText("General"));
//        tabLayout.addTab(tabLayout.newTab().setText("Map"));
//        final ViewPager viewPager = (ViewPager) inflatedView.findViewById(R.id.viewpager);
//
//
//        viewPager.setAdapter(new PagerAdapter
//                (getFragmentManager(), tabLayout.getTabCount()));
//        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
//        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
//            @Override
//            public void onTabSelected(TabLayout.Tab tab) {
//                viewPager.setCurrentItem(tab.getPosition());
//            }
//
//            @Override
//            public void onTabUnselected(TabLayout.Tab tab) {
//
//            }
//
//            @Override
//            public void onTabReselected(TabLayout.Tab tab) {
//
//            }
//        });
//
//
//        tabLayout.setupWithViewPager(viewPager);
//        tabLayout.getTabAt(0).setText("General");
//        tabLayout.getTabAt(1).setText("Map");
//
//        return inflatedView;
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

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
                mViewPager.setCurrentItem(tab.getPosition());
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



        LinearLayout tabStrip = ((LinearLayout)tabLayout.getChildAt(0));
        tabStrip.getChildAt(2).setClickable(false);
        return inflatedView;

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
                    return new HikeFragment();
                case 1:
                    return new HikeMapFragment();
                case 2:
                    
                    return new HikeMapFragment();
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return mNumOfTabs;
        }
    }
}

