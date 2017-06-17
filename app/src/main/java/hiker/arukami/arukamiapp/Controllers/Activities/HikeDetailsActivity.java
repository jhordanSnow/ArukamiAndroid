package hiker.arukami.arukamiapp.Controllers.Activities;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import butterknife.BindView;
import butterknife.ButterKnife;
import hiker.arukami.arukamiapp.API.APIClient;
import hiker.arukami.arukamiapp.API.AruKamiAPI;
import hiker.arukami.arukamiapp.Controllers.Fragments.ProfileFragment;
import hiker.arukami.arukamiapp.Controllers.Fragments.WallFragment;
import hiker.arukami.arukamiapp.Helpers.FillSpinner;
import hiker.arukami.arukamiapp.Helpers.HikePointsAdapter;
import hiker.arukami.arukamiapp.Helpers.UserSearchAdapter;
import hiker.arukami.arukamiapp.Helpers.WallAdapter;
import hiker.arukami.arukamiapp.Models.GetPointsResponse;
import hiker.arukami.arukamiapp.Models.HikeModel;
import hiker.arukami.arukamiapp.Models.PointModel;
import hiker.arukami.arukamiapp.Models.User;
import hiker.arukami.arukamiapp.R;
import retrofit2.Call;
import retrofit2.Retrofit;

public class HikeDetailsActivity extends AppCompatActivity {
    private static Integer HIKE_ID;
    private static HikeModel hike;
    Toolbar myToolbar;
    protected RecyclerView mRecyclerView;
    protected HikePointsAdapter mAdapter;
    protected List<GetPointsResponse> mDataset;

    @BindView(R.id.hikeName_text)
    TextView hikeName;
    @BindView(R.id.startDate_text)
    TextView startDate_text;
    @BindView(R.id.endDate_text)
    TextView endDate_text;
    @BindView(R.id.spinner_district)
    TextView district;
    @BindView(R.id.spinner_difficulty)
    TextView difficulty;
    @BindView(R.id.spinner_hike_type)
    TextView hikeType;
    @BindView(R.id.spinner_price)
    TextView price;
    @BindView(R.id.spinner_quality)
    TextView quality;
    @BindView(R.id.description_text)
    TextView description;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hike_details);
        HIKE_ID = (Integer) getIntent().getExtras().get("USER_ID");
        hike = getIntent().getExtras().getParcelable("HIKE");
        ButterKnife.bind(this);
        mDataset  = new ArrayList<>();

        hikeName.setText(hike.getHikeName());
        startDate_text.setText(hike.getStartDate());
        endDate_text.setText(hike.getEndDate());
        district.setText(hike.getDistrict());
        difficulty.setText(hike.getDifficulty());
        hikeType.setText(hike.getHikeType());
        price.setText(hike.getPrice());
        quality.setText(hike.getQuality());
        description.setText(hike.getDescription());

        mRecyclerView = (RecyclerView) findViewById(R.id.rv);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mRecyclerView.setHasFixedSize(true);
        mAdapter = new HikePointsAdapter(mDataset);
        mRecyclerView.setAdapter(mAdapter);
        initializeData();


    }

    private void initializeData(){
        GetPointsCall result = new GetPointsCall();
        try {
            mDataset = result.execute().get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }


    private class GetPointsCall extends AsyncTask<Call, Void, List<GetPointsResponse>> {

        @Override
        protected List<GetPointsResponse> doInBackground(Call... params) {
            Retrofit retrofit = APIClient.getClient();
            AruKamiAPI apiService = retrofit.create(AruKamiAPI.class);
            List<GetPointsResponse> response = new ArrayList<>();
            Call<List<GetPointsResponse>> result = apiService.getPoints((int)hike.getIdCreator());
            try {
                response = result.execute().body();

            } catch (IOException e) {
                e.printStackTrace();
            }
            return response;
        }

        @Override
        protected void onPostExecute(List<GetPointsResponse> result){mDataset = result;}

    }

}
