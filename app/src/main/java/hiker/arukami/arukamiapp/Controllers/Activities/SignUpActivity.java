package hiker.arukami.arukamiapp.Controllers.Activities;


import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.BindView;
import de.hdodenhof.circleimageview.CircleImageView;
import hiker.arukami.arukamiapp.API.APIClient;
import hiker.arukami.arukamiapp.API.AruKamiAPI;
import hiker.arukami.arukamiapp.Models.JsonResponse;
import hiker.arukami.arukamiapp.Models.SignUpRequest;
import hiker.arukami.arukamiapp.Models.SpinnerResponse;
import hiker.arukami.arukamiapp.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class SignUpActivity extends AppCompatActivity {
    private static final String TAG = "SignupActivity";
    private static TextView mEdit;
    private static String SQLBirthDate;

    @BindView(R.id.input_username)
    EditText _usernameText;
    @BindView(R.id.input_password)
    EditText _passwordText;
    @BindView(R.id.input_idCard)
    EditText _idText;
    @BindView(R.id.input_accountNumber)
    EditText _accountNumberText;
    @BindView(R.id.input_firstName)
    EditText _nameText;
    @BindView(R.id.input_middleName)
    EditText _middleText;
    @BindView(R.id.input_lastName)
    EditText _lastText;
    @BindView(R.id.input_secondlastName)
    EditText _secondLastNameText;
    @BindView(R.id.gender_spinner)
    Spinner _genderSpinner;
    @BindView(R.id.input_dateText)
    TextView _dateText;
    @BindView(R.id.nationality_spinner)
    Spinner _nationality_spinner;
    @BindView(R.id.sign_up_icon)
    CircleImageView imageView;

    @BindView(R.id.btn_signup)
    Button _signupButton;
    @BindView(R.id.link_login)
    TextView _loginLink;

    public List<SpinnerResponse> items;
    private static final int CAMERA_REQUEST = 1123;

    String photo;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        ButterKnife.bind(this);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.genders_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        _genderSpinner.setAdapter(adapter);
        _signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
            }
        });

        // Spinner de las nacionalidades
        items = new ArrayList<>();
        Retrofit retrofit = APIClient.getClient();
        AruKamiAPI apiService = retrofit.create(AruKamiAPI.class);
        Call<List<SpinnerResponse>> result = apiService.getNationalities();
        result.enqueue(new Callback<List<SpinnerResponse>>() {
            @Override
            public void onResponse(Call<List<SpinnerResponse>> call, Response<List<SpinnerResponse>> response) {
                setNationalities(response.body());
            }

            @Override
            public void onFailure(Call<List<SpinnerResponse>> call, Throwable t) {

            }
        });
        photo = null;
        mEdit = (TextView) findViewById(R.id.input_dateText);
        mEdit.setClickable(false);
        SQLBirthDate = "";
        imageView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                takePhoto(v,CAMERA_REQUEST);

            }
        });
        _loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish the registration screen and return to the Login activity
                finish();
            }
        });
    }
    public void setNationalities(List<SpinnerResponse> response){
        ArrayAdapter<SpinnerResponse> dataAdapter = new ArrayAdapter<SpinnerResponse>(this,
                android.R.layout.simple_spinner_item, response);

        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        _nationality_spinner.setAdapter(dataAdapter);
    }

    public void signup() {
        Log.d(TAG, "Signup");

        if (!validate()) {
            onSignupFailed("Registration failed.");
            return;
        }

        _signupButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(SignUpActivity.this,
                R.style.AppTheme);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Creating Account...");
        progressDialog.show();

        String firstName = _nameText.getText().toString();
        String middleName = _middleText.getText().toString().equals("") ? null : _middleText.getText().toString();
        String lastName = _lastText.getText().toString();
        String secondLastName = _secondLastNameText.getText().toString().equals("") ? null : _secondLastNameText.getText().toString() ;
        String username = _usernameText.getText().toString();
        String password = _passwordText.getText().toString();
        int idCard = Integer.parseInt(_idText.getText().toString());
        int accountNumber = Integer.parseInt(_accountNumberText.getText().toString());
        String gender = _genderSpinner.getSelectedItem().toString().equals("Male") ? "M" : "F";
        int nationality = ((SpinnerResponse) _nationality_spinner.getSelectedItem()).getValue();

        SignUpRequest user = new SignUpRequest(idCard, accountNumber, username, password, gender, SQLBirthDate, nationality, firstName, middleName, lastName, secondLastName,photo);
        Retrofit retrofit = APIClient.getClient();
        AruKamiAPI apiService = retrofit.create(AruKamiAPI.class);
        final JsonResponse registerResponse = new JsonResponse();
        Call<JsonResponse> result = apiService.signUp(user);

        result.enqueue(new Callback<JsonResponse>() {
            @Override
            public void onResponse(Call<JsonResponse> call, Response<JsonResponse> response) {
                registerResponse.setResponse(response.body().getResponse());

            }

            @Override
            public void onFailure(Call<JsonResponse> call, Throwable t) {
                registerResponse.setResponse("Something went wrong.");
            }
        });

        new Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        if (registerResponse.getResponse().equals("Success")) {
                            onSignupSuccess();
                        } else {
                            onSignupFailed(registerResponse.getResponse());
                        }
                        progressDialog.dismiss();
                    }
                }, 3000);
    }


    public void onSignupSuccess() {
        Toast.makeText(getBaseContext(), "Registration Successful", Toast.LENGTH_LONG).show();
        _signupButton.setEnabled(true);
        setResult(RESULT_OK, null);
        Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    public void onSignupFailed(String message) {
        Toast.makeText(getBaseContext(), message, Toast.LENGTH_LONG).show();

        _signupButton.setEnabled(true);
    }

    public boolean validate() {
        return ( validateUsername() && validatePassword() && validateIdCard() &&
                validateBirtDate() && validateName() );
    }

    public boolean validateUsername(){
        String username = _usernameText.getText().toString();
        if (username.isEmpty() || username.length() > 20 || username.contains(" ")) {
            _usernameText.setError("Invalid Username.");
            _usernameText.requestFocus();
            return false;
        }
        return true;
    }

    public boolean validatePassword(){
        String password = _passwordText.getText().toString();
        if (password.isEmpty() || password.length() > 20) {
            _passwordText.setError("Invalid Password.");
            _passwordText.requestFocus();
            return false;
        }
        return true;
    }

    public boolean validateIdCard(){
        String idCard = _idText.getText().toString();
        if (idCard.isEmpty()) {
            _idText.setError("Invalid IdCard.");
            _idText.requestFocus();
            return false;
        }
        return true;
    }

    public boolean validateBirtDate(){
        if (SQLBirthDate.isEmpty()) {
            Toast.makeText(getBaseContext(), "Please enter a Date.", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    public boolean validateName(){
        String firstName = _nameText.getText().toString();
        String middleName = _middleText.getText().toString();
        String lastName = _lastText.getText().toString();
        String secondLastName = _secondLastNameText.getText().toString();

        if (firstName.isEmpty() || firstName.matches(".*\\d+.*")) {
            _nameText.setError("Not a valid name.");
            return false;
        }

        if (middleName.matches(".*\\d+.*")) {
            _middleText.setError("Not a valid name.");
            return false;
        }
        if (lastName.isEmpty() || lastName.matches(".*\\d+.*")) {
            _lastText.setError("Not a valid name.");
            return false;
        }
        if (secondLastName.matches(".*\\d+.*")) {
            _secondLastNameText.setError("Not a valid name.");
            return false;
        }

        return true;
    }




    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    public static void populateSetDate(int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);

        SimpleDateFormat format = new SimpleDateFormat("MMM dd, yyyy");
        String strDate = format.format(calendar.getTime());
        mEdit.setText(strDate);

        format = new SimpleDateFormat("yyyy-MM-dd");
        SQLBirthDate = format.format(calendar.getTime());


    }

    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), R.style.datepicker, this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            populateSetDate(year, month, day);
        }
    }

    public void takePhoto(View view, int CAMERA_EVENT) {
        Log.wtf("imageview","cacaentra");
        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, CAMERA_EVENT);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.wtf("imageview","caca");
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            Bitmap image = (Bitmap) data.getExtras().get("data");
            imageView.setImageBitmap(image);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            image.compress(Bitmap.CompressFormat.PNG, 100, bos);
            byte[] bArray = bos.toByteArray();
            photo = Base64.encodeToString(bArray, Base64.DEFAULT);
        }
    }


}

