package hiker.arukami.arukamiapp.Controllers.Activities;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import hiker.arukami.arukamiapp.API.APIClient;
import hiker.arukami.arukamiapp.API.AruKamiAPI;
import hiker.arukami.arukamiapp.Controllers.Fragments.MainHikeFragment;
import hiker.arukami.arukamiapp.Controllers.Fragments.ProfileFragment;
import hiker.arukami.arukamiapp.Helpers.FillSpinner;
import hiker.arukami.arukamiapp.Models.HikeRequest;
import hiker.arukami.arukamiapp.Models.HikeResponse;
import hiker.arukami.arukamiapp.Models.LoginResponse;
import hiker.arukami.arukamiapp.Models.PointModel;
import hiker.arukami.arukamiapp.Models.SpinnerResponse;
import hiker.arukami.arukamiapp.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class SubmitHikeActivity extends AppCompatActivity {
    private HikeRequest myHike;

    private Spinner _difficulty_spinner;
    private Spinner _hike_type_spinner;
    private Spinner _quality_spinner;
    private Spinner _price_spinner;
    private EditText _description_hike;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit_hike);
        TextView _hikeNameText = (TextView) findViewById(R.id.hikeName_text);
        TextView _startDateText = (TextView) findViewById(R.id.startDate_text);
        TextView _endDateText = (TextView) findViewById(R.id.endDate_text);

        _difficulty_spinner = (Spinner) findViewById(R.id.spinner_difficulty);
        _hike_type_spinner = (Spinner) findViewById(R.id.spinner_hike_type);
        _quality_spinner = (Spinner) findViewById(R.id.spinner_quality);
        _price_spinner = (Spinner) findViewById(R.id.spinner_price);
        _description_hike = (EditText) findViewById(R.id.description_text);




        Toolbar toolbar = (Toolbar) findViewById(R.id.addpoint_toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar()!= null){
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadHike();
            }
        });
        myHike = getIntent().getParcelableExtra("MyHike");
        _hikeNameText.setText(myHike.getName());
        _startDateText.setText(myHike.getStartDate());
        _endDateText.setText(myHike.getEndDate());
        initSpinners();
    }


    public void uploadHike(){
        myHike.setDifficulty(((SpinnerResponse) _difficulty_spinner.getSelectedItem()).getValue());
        myHike.setHikeType(((SpinnerResponse) _hike_type_spinner.getSelectedItem()).getValue());
        myHike.setQualityLevel(((SpinnerResponse) _quality_spinner.getSelectedItem()).getValue());
        myHike.setPriceLevel(((SpinnerResponse) _price_spinner.getSelectedItem()).getValue());
        MainHikeFragment frag = MainHikeFragment.getInstance();
        HikeRequest points = frag.getHikePoints();
        myHike.setStartPoint(points.getStartPoint());
        myHike.setEndPoint(points.getEndPoint());
        myHike.setRoute(points.getRoute());
        myHike.setDescription(_description_hike.getText().toString());
        myHike.setIdCard(ProfileFragment.getUserId());
        insertHike(myHike);
    }

    public void insertHike(HikeRequest hike){
        InsertHike result = new InsertHike(hike);
        MainHikeFragment hikeFrag = MainHikeFragment.getInstance();
        Integer idPoint = null;
        try {
            HikeResponse hikeResponse = result.execute().get();
            idPoint = hikeResponse.getIdHike();

        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        Retrofit retrofit = APIClient.getClient();
        AruKamiAPI apiService = retrofit.create(AruKamiAPI.class);
        LoginResponse respuesta = new LoginResponse();
        Log.wtf("punto",String.valueOf(idPoint));
        if (idPoint!= null){
            for (PointModel point: hikeFrag.getHikeSelectedPoints()){
                point.setIdHike(idPoint);
                Call<LoginResponse> resultado = apiService.addHikePoint(hike.getIdCard(), point);
                resultado.enqueue(new Callback<LoginResponse>() {
                    @Override
                    public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                        Toast.makeText(getBaseContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Call<LoginResponse> call, Throwable t) {
                        Toast.makeText(getBaseContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
        finish();
    }

    public void initSpinners(){
        Retrofit retrofit = APIClient.getClient();
        AruKamiAPI apiService = retrofit.create(AruKamiAPI.class);

        FillSpinner.callSpinner(apiService.getDifficulties(), _difficulty_spinner);
        FillSpinner.callSpinner(apiService.getHikeTypes(), _hike_type_spinner);
        FillSpinner.callSpinner(apiService.getQualities(), _quality_spinner);
        FillSpinner.callSpinner(apiService.getPrices(), _price_spinner);
    }

    private class InsertHike extends AsyncTask<Call, Void, HikeResponse> {
        private HikeRequest hike;

        public InsertHike(HikeRequest hike) {
            this.hike = hike;
        }

        @Override
        protected HikeResponse doInBackground(Call... params) {
            MainHikeFragment hikeActivity = MainHikeFragment.getInstance();
            Retrofit retrofit = APIClient.getClient();
            AruKamiAPI apiService = retrofit.create(AruKamiAPI.class);
            HikeResponse hikeResponse = new HikeResponse();
            Call<HikeResponse> result = apiService.addHike(hike);

            try {
                hikeResponse = result.execute().body();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return hikeResponse;
        }

    }
}