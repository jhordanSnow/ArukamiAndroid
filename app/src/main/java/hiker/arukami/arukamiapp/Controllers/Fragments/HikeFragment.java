package hiker.arukami.arukamiapp.Controllers.Fragments;


import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;

import butterknife.BindView;
import hiker.arukami.arukamiapp.API.APIClient;
import hiker.arukami.arukamiapp.API.AruKamiAPI;
import hiker.arukami.arukamiapp.Controllers.Activities.AddPointActivity;
import hiker.arukami.arukamiapp.Controllers.Activities.MainActivity;
import hiker.arukami.arukamiapp.Helpers.FillSpinner;
import hiker.arukami.arukamiapp.Models.HikeRequest;
import hiker.arukami.arukamiapp.Models.PointModel;
import hiker.arukami.arukamiapp.Models.SpinnerResponse;
import hiker.arukami.arukamiapp.R;
import retrofit2.Retrofit;


/**
 * A simple {@link Fragment} subclass.
 */
public class HikeFragment extends android.support.v4.app.Fragment {


    private Spinner _district_spinner;
    View rootView;
    private TextView _text_hike_name;
    private static String _start_date_hike;
    private static String _end_date_hike;
    private String mCurrentPhotoPath;
    private static String _route_hike;
    private static final int CAMERA_REQUEST = 132;
    private static final int CAMERA_REQUEST_POINT = 420;
    private ImageView imageView;
    private FloatingActionButton addPoint;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_hike, container, false);
        _district_spinner = (Spinner) rootView.findViewById(R.id.spinner_district);

        imageView = (ImageView) rootView.findViewById(R.id.imageView);
        _text_hike_name = (EditText) rootView.findViewById(R.id.input_hikeName);

        addPoint = (FloatingActionButton) getActivity().findViewById(R.id.addPointButton);

        addPoint.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                takePhoto(v,CAMERA_REQUEST_POINT);

            }
        });

        initSpinners();
        imageView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                takePhoto(v,CAMERA_REQUEST);

            }
        });
        return rootView;
    }

    public boolean validateInputs(){
        if (TextUtils.isEmpty(_text_hike_name.getText().toString())){
            Toast.makeText(getContext(), "Please enter a Name", Toast.LENGTH_SHORT).show();
            _text_hike_name.requestFocus();
            return false;
        }
        return true;
    }

    public HikeRequest getHike(){
        if (!validateInputs()){
            return null;
        }
        HikeRequest hike = new HikeRequest();
        hike.setName(_text_hike_name.getText().toString());
        hike.setDistrict(((SpinnerResponse) _district_spinner.getSelectedItem()).getValue());
        hike.setStartDate(_start_date_hike);
        hike.setEndDate(_end_date_hike);
        if (mCurrentPhotoPath!= null){
            hike.setPhoto(mCurrentPhotoPath);
        }
        return hike;
    }



    public static void setStartDate(String date){
        _start_date_hike = date;
    }

    public static void setEndDate(String date){
        _end_date_hike = date;
    }

    public void initSpinners(){
        Retrofit retrofit = APIClient.getClient();
        AruKamiAPI apiService = retrofit.create(AruKamiAPI.class);

        FillSpinner.callSpinner(apiService.getDistricts(), _district_spinner);
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
            mCurrentPhotoPath = Base64.encodeToString(bArray, Base64.DEFAULT);
        }else if (requestCode == CAMERA_REQUEST_POINT && resultCode == Activity.RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            Intent intent = new Intent(getContext(), AddPointActivity.class);
            intent.putExtra("Image",(Bitmap) data.getExtras().get("data"));
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            photo.compress(Bitmap.CompressFormat.PNG, 100, bos);
            byte[] bArray = bos.toByteArray();
            String photoString = Base64.encodeToString(bArray, Base64.DEFAULT);
            SharedPreferences app_preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
            LatLng geoPoint = ((MainActivity)getActivity()).hikeFragment.getLastPoint();
            PointModel model = new PointModel();
            model.setDate(MainActivity.getDateTime());
            model.setPhoto(photoString);
            model.setLatitude(String.valueOf(geoPoint.latitude));
            model.setLongitude(String.valueOf(geoPoint.longitude));
            model.setIdCard(app_preferences.getInt("IdCard",0));

            intent.putExtra("Model", model);

            startActivity(intent);
        }
    }
}
