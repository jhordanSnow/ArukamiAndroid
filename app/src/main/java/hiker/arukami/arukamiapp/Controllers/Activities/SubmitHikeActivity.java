package hiker.arukami.arukamiapp.Controllers.Activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import hiker.arukami.arukamiapp.API.APIClient;
import hiker.arukami.arukamiapp.API.AruKamiAPI;
import hiker.arukami.arukamiapp.Controllers.Fragments.MainHikeFragment;
import hiker.arukami.arukamiapp.Helpers.FillSpinner;
import hiker.arukami.arukamiapp.Models.HikeRequest;
import hiker.arukami.arukamiapp.Models.LoginResponse;
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


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Uploading Hike...", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
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
        insertHike(myHike);
    }

    public void insertHike(HikeRequest hike){

        Retrofit retrofit = APIClient.getClient();
        AruKamiAPI apiService = retrofit.create(AruKamiAPI.class);
        final LoginResponse hikeResponse = new LoginResponse();
        Call<LoginResponse> result = apiService.addHike(hike);

        result.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                hikeResponse.setMessage(response.body().getMessage());
                hikeResponse.setSuccess(response.body().isSuccess());
                Log.wtf("CACA", hikeResponse.getMessage());
                Toast.makeText(getApplicationContext(), hikeResponse.getMessage(), Toast.LENGTH_LONG).show();

            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "SHITS", Toast.LENGTH_LONG).show();
                hikeResponse.setMessage("Something went wrong.");
                hikeResponse.setSuccess(false);
            }
        });

    }

    public void initSpinners(){
        Retrofit retrofit = APIClient.getClient();
        AruKamiAPI apiService = retrofit.create(AruKamiAPI.class);

        FillSpinner.callSpinner(apiService.getDifficulties(), _difficulty_spinner);
        FillSpinner.callSpinner(apiService.getHikeTypes(), _hike_type_spinner);
        FillSpinner.callSpinner(apiService.getQualities(), _quality_spinner);
        FillSpinner.callSpinner(apiService.getPrices(), _price_spinner);
    }
}
