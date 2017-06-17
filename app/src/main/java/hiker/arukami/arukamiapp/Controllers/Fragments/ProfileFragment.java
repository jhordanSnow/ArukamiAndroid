package hiker.arukami.arukamiapp.Controllers.Fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
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

import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import hiker.arukami.arukamiapp.API.APIClient;
import hiker.arukami.arukamiapp.API.AruKamiAPI;
import hiker.arukami.arukamiapp.Controllers.Activities.DonationActivity;
import hiker.arukami.arukamiapp.Controllers.Activities.MainActivity;
import hiker.arukami.arukamiapp.Controllers.Activities.MyHikesActivity;
import hiker.arukami.arukamiapp.Models.User;
import hiker.arukami.arukamiapp.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {

    private static ProfileFragment instance;
    private static Integer USER_ID;
    private static User user;
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

    public static Integer getUserId(){
        return USER_ID;
    }

    public static ProfileFragment getInstance(int id) {
        if (instance == null) {
            USER_ID = id;
            instance = new ProfileFragment();
        }
        return instance;
    }

    public ProfileFragment() {
        // Required empty public constructor
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
    }

    public void fillUserDetails(User user){
        ((AppCompatActivity)getActivity()).setSupportActionBar(myToolbar);
        getActivity().setTitle(user.getFirstName() + " " + user.getLastName());
        _usernameText.setText(user.getUsername());
        _fullnameText.setText(user.toString());
        _genderText.setText(user.getGender());
        _birthdateText.setText(user.getBirthDateFormat("MMMM dd, yyyy"));
        _nationalityText.setText(String.valueOf(user.getNationality()));
        _accountNumberText.setText(String.valueOf((int)user.getAccountNumber()));
        String photoURL = APIClient.getURL();
        photoURL +="User/"+USER_ID+"/Photo";
        Log.wtf("caca",photoURL);

        Picasso.with(getContext()).load(photoURL).into(_profilePhoto);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);
        ButterKnife.bind(this,rootView);
        myToolbar = (Toolbar) rootView.findViewById(R.id.profile_toolbar);

        if (USER_ID != null) {
            getUserDetails();
        }
        _myHikesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MyHikesActivity.class);
                intent.putExtra("USER_ID",USER_ID);
                startActivity(intent);
            }
        });

        return rootView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onDestroy() {
        Log.wtf("caca","me sali");
        super.onDestroy();
        instance = null;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.profile_menu, menu);
        menu.findItem(R.id.follow).setVisible(false);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_sign_out: {
                ((MainActivity)getActivity()).SignOut();
                break;
            }
            case R.id.action_donate: {
                Intent intent = new Intent(getContext(), DonationActivity.class);
                startActivity(intent);
                break;
            }
        }
        return false;
    }

}
