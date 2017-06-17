package hiker.arukami.arukamiapp.Controllers.Activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import hiker.arukami.arukamiapp.API.APIClient;
import hiker.arukami.arukamiapp.API.AruKamiAPI;
import hiker.arukami.arukamiapp.Controllers.Fragments.ProfileFragment;
import hiker.arukami.arukamiapp.Models.JsonResponse;
import hiker.arukami.arukamiapp.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class DonationActivity extends AppCompatActivity {
    Button donateButton;
    EditText ammountText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donation);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Donate");
        donateButton = (Button) findViewById(R.id.donate_button);
        ammountText = (EditText) findViewById(R.id.input_ammount);
        donateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                donate();
            }
        });
    }

    public void donate(){
        if (!ammountText.equals("")){
            Retrofit retrofit = APIClient.getClient();
            AruKamiAPI apiService = retrofit.create(AruKamiAPI.class);
            String text =ammountText.getText().toString();
            double ammount = 0;
            if(!text.isEmpty()) {
                try {
                    ammount = Double.parseDouble(text);
                } catch (Exception e1) {
                    ammountText.setError("Not valid");
                    e1.printStackTrace();
                }
            }
            if (ammount > 0){
                Call<JsonResponse> result = apiService.donate(ProfileFragment.getUserId(),ammount);
                result.enqueue(new Callback<JsonResponse>() {
                    @Override
                    public void onResponse(Call<JsonResponse> call, Response<JsonResponse> response) {
                        Toast.makeText(DonationActivity.this, response.body().getResponse(), Toast.LENGTH_SHORT).show();
                        if (response.body().getResponse().equals("Success")){
                            endActivity();
                        }
                    }

                    @Override
                    public void onFailure(Call<JsonResponse> call, Throwable t) {
                        Log.wtf("failure", t.getMessage());
                    }
                });
            }
            else{
                ammountText.setError("Required");
            }
        }else{
            ammountText.setError("Required");
        }
    }

    public void endActivity(){
        finish();
    }
}
