package hiker.arukami.arukamiapp.Controllers.Fragments;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;

import hiker.arukami.arukamiapp.API.APIClient;
import hiker.arukami.arukamiapp.API.AruKamiAPI;
import hiker.arukami.arukamiapp.Helpers.FillSpinner;
import hiker.arukami.arukamiapp.Models.HikeRequest;
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
    private static final int CAMERA_REQUEST = 1888;
    private ImageView imageView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_hike, container, false);
        _district_spinner = (Spinner) rootView.findViewById(R.id.spinner_district);

        imageView = (ImageView) rootView.findViewById(R.id.imageView);
            _text_hike_name = (EditText) rootView.findViewById(R.id.input_hikeName);


        initSpinners();
        imageView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                takePhoto(v);

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

    public void takePhoto(View view) {
        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, CAMERA_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            photo.compress(Bitmap.CompressFormat.PNG, 100, bos);
            byte[] bArray = bos.toByteArray();
            mCurrentPhotoPath = Base64.encodeToString(bArray, Base64.DEFAULT);
            imageView.setImageBitmap(photo);
        }
    }


}
