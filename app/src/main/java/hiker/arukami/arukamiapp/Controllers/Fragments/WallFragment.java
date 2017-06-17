package hiker.arukami.arukamiapp.Controllers.Fragments;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import hiker.arukami.arukamiapp.API.APIClient;
import hiker.arukami.arukamiapp.API.AruKamiAPI;
import hiker.arukami.arukamiapp.Controllers.Activities.MainActivity;
import hiker.arukami.arukamiapp.Controllers.Activities.MyHikesActivity;
import hiker.arukami.arukamiapp.Helpers.HikeAdapter;
import hiker.arukami.arukamiapp.Helpers.WallAdapter;
import hiker.arukami.arukamiapp.Models.BoolModel;
import hiker.arukami.arukamiapp.Models.HikeModel;
import hiker.arukami.arukamiapp.Models.HikeRequest;
import hiker.arukami.arukamiapp.Models.HikeResponse;
import hiker.arukami.arukamiapp.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * A simple {@link Fragment} subclass.
 */
public class WallFragment extends Fragment {
    protected RecyclerView mRecyclerView;
    protected WallAdapter mAdapter;
    protected List<HikeModel> mDataset;
    private static WallFragment instance;

    public static WallFragment getInstance() {
        if (instance == null) {
            instance = new WallFragment();
        }
        return instance;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDataset  = new ArrayList<>();
        initializeData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_wall, container, false);
        mRecyclerView = (RecyclerView)rootView.findViewById(R.id.wall_rv);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mRecyclerView.setHasFixedSize(true);
        mAdapter = new WallAdapter( mDataset);
        mRecyclerView.setAdapter(mAdapter);
        return rootView;
    }

    private void initializeData(){
        GetWallCall result = new GetWallCall();
        try {
            mDataset = result.execute().get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    private class GetWallCall extends AsyncTask<Call, Void, List<HikeModel>> {

        @Override
        protected List<HikeModel> doInBackground(Call... params) {
            Retrofit retrofit = APIClient.getClient();
            AruKamiAPI apiService = retrofit.create(AruKamiAPI.class);
            List<HikeModel> response = new ArrayList<>();
            Call<List<HikeModel>> result = apiService.getWall(ProfileFragment.getUserId());
            try {
                response = result.execute().body();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return response;
        }

        @Override
        protected void onPostExecute(List<HikeModel> result){
            mDataset = result;
        }

    }
}
