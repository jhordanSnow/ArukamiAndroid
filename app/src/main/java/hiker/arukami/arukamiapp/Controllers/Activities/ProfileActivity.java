package hiker.arukami.arukamiapp.Controllers.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import hiker.arukami.arukamiapp.API.APIClient;
import hiker.arukami.arukamiapp.API.AruKamiAPI;
import hiker.arukami.arukamiapp.Controllers.Fragments.ProfileFragment;
import hiker.arukami.arukamiapp.Models.BoolModel;
import hiker.arukami.arukamiapp.Models.JsonResponse;
import hiker.arukami.arukamiapp.Models.User;
import hiker.arukami.arukamiapp.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ProfileActivity extends android.support.v7.app.AppCompatActivity {

    private static Integer USER_ID;
    private static User user;
    private Boolean following;
    Toolbar myToolbar;

    @BindView(R.id.username_textview)
    TextView _usernameText;
    @BindView(R.id.name_textview)
    TextView _fullnameText;
    @BindView(R.id.gender_textview)
    TextView _genderText;
    @BindView(R.id.birthdate_textview)
    TextView _birthdateText;
    @BindView(R.id.nationality_textview)
    TextView _nationalityText;
    @BindView(R.id.account_number_textview)
    TextView _accountNumberText;
    @BindView(R.id.profile_bk)
    ImageView _profilePhoto;
    @BindView(R.id.fab)
    ImageView _myHikesButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_profile);
        USER_ID = (Integer) getIntent().getExtras().get("USER_ID");
        following = false;
        if (USER_ID== null){
            finish();
        }
        ButterKnife.bind(this);
        getUserDetails();

        myToolbar = (Toolbar) findViewById(R.id.profile_toolbar);
        _myHikesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MyHikesActivity.class);
                intent.putExtra("USER_ID",USER_ID);
                startActivity(intent);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.profile_menu, menu);
        if (following){
            menu.findItem(R.id.follow).setIcon(R.drawable.ic_heart_white_24dp);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_sign_out: {
                MainActivity.getInstance().SignOut();
                break;
            }
            case R.id.action_donate: {
                break;
            }
            case R.id.follow: {
                followUnfollow();
                if (following){
                    item.setIcon(R.drawable.ic_heart_outline_white_24dp);
                    following = false;
                }else{
                    item.setIcon(R.drawable.ic_heart_white_24dp);
                    following = true;
                }
                break;
            }
        }
        return false;
    }

    public void followUnfollow(){
        Retrofit retrofit = APIClient.getClient();
        AruKamiAPI apiService = retrofit.create(AruKamiAPI.class);
        Call<JsonResponse> result = apiService.followUnfollow(ProfileFragment.getUserId(),USER_ID);
        result.enqueue(new Callback<JsonResponse>() {
            @Override
            public void onResponse(Call<JsonResponse> call, Response<JsonResponse> response) {
                if (response.body() !=  null && !response.body().getResponse().equals("Success")){
                    Toast.makeText(ProfileActivity.this, response.body().getResponse(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<JsonResponse> call, Throwable t) {
                Log.wtf("failure", t.getMessage());
            }
        });
    }

    public void getUserDetails(){
        Retrofit retrofit = APIClient.getClient();
        AruKamiAPI apiService = retrofit.create(AruKamiAPI.class);
        Call<User> result = apiService.getUserDetails(USER_ID);
        result.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                fillUserDetails(response.body());
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.wtf("failure", t.getMessage());
            }
        });

        Call<BoolModel> followingCall = apiService.isFollowing(ProfileFragment.getUserId(), USER_ID);
        followingCall.enqueue(new Callback<BoolModel>() {
            @Override
            public void onResponse(Call<BoolModel> call, Response<BoolModel> response) {
               if (response.body().getResult()){
                   following = true;
               }
            }

            @Override
            public void onFailure(Call<BoolModel> call, Throwable t) {
                Log.wtf("failure", t.getMessage());
            }
        });
    }

    public void fillUserDetails(User user){
        setSupportActionBar(myToolbar);
        setTitle(user.getFirstName() + " " + user.getLastName());
        _usernameText.setText(user.getUsername());
        _fullnameText.setText(user.toString());
        _genderText.setText(user.getGender());
        _birthdateText.setText(user.getBirthDateFormat("MMMM dd, yyyy"));
        _nationalityText.setText(String.valueOf(user.getNationality()));
        _accountNumberText.setText(String.valueOf((int)user.getAccountNumber()));
        String photoURL = APIClient.getURL();
        photoURL +="User/"+USER_ID+"/Photo";
        Log.wtf("caca",photoURL);
        Picasso.with(this).load(photoURL).into(_profilePhoto);

    }

}
