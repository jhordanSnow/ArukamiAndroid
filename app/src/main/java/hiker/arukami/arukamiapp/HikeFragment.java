package hiker.arukami.arukamiapp;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

import hiker.arukami.arukamiapp.API.APIClient;
import hiker.arukami.arukamiapp.API.UserAPI;
import hiker.arukami.arukamiapp.Models.SpinnerResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


/**
 * A simple {@link Fragment} subclass.
 */
public class HikeFragment extends android.app.Fragment {

    private Spinner _difficulty_spinner;
    private Spinner _hike_type_spinner;
    private Spinner _quality_spinner;
    private Spinner _price_spinner;


    public HikeFragment() {
        // Required empty public constructor


    }

    MapView mMapView;
    private GoogleMap googleMap;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_hike, container, false);
        _difficulty_spinner = (Spinner) rootView.findViewById(R.id.spinner_difficulty);
        _hike_type_spinner = (Spinner) rootView.findViewById(R.id.spinner_hike_type);
        _quality_spinner = (Spinner) rootView.findViewById(R.id.spinner_quality);
        _price_spinner = (Spinner) rootView.findViewById(R.id.spinner_price);

        initSpinners();

        mMapView = (MapView) rootView.findViewById(R.id.Mapau);
        mMapView.onCreate(savedInstanceState);

        mMapView.onResume(); // needed to get the map to display immediately

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {
                googleMap = mMap;

                //googleMap.setMyLocationEnabled(true);

                // For dropping a marker at a point on the Map
                LatLng sydney = new LatLng(-34, 151);
                googleMap.addMarker(new MarkerOptions().position(sydney).title("Marker Title").snippet("Marker Description"));

                // For zooming automatically to the location of the marker
                CameraPosition cameraPosition = new CameraPosition.Builder().target(sydney).zoom(12).build();
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            }
        });

        return rootView;
    }

    public void fillSpinner(Spinner spinnerTarget, List<SpinnerResponse> items){
        ArrayAdapter<SpinnerResponse> dataAdapter = new ArrayAdapter<SpinnerResponse>(getActivity(),
                android.R.layout.simple_spinner_item, items);

        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTarget.setAdapter(dataAdapter);
    }

    public void callSpinner(Call<List<SpinnerResponse>> result, final Spinner spinnerTarget){
        result.enqueue(new Callback<List<SpinnerResponse>>() {
            @Override
            public void onResponse(Call<List<SpinnerResponse>> call, Response<List<SpinnerResponse>> response) {
                fillSpinner(spinnerTarget, response.body());
            }

            @Override
            public void onFailure(Call<List<SpinnerResponse>> call, Throwable t) {

            }
        });
    }

    public void initSpinners(){
        Retrofit retrofit = APIClient.getClient();
        UserAPI apiService = retrofit.create(UserAPI.class);

        callSpinner(apiService.getDifficulties(), _difficulty_spinner);
        callSpinner(apiService.getHikeTypes(), _hike_type_spinner);
        callSpinner(apiService.getQualities(), _quality_spinner);
        callSpinner(apiService.getPrices(), _price_spinner);
    }


    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

}
