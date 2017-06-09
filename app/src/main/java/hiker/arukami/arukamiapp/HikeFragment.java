package hiker.arukami.arukamiapp;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.List;

import hiker.arukami.arukamiapp.API.APIClient;
import hiker.arukami.arukamiapp.API.UserAPI;
import hiker.arukami.arukamiapp.Models.HikeRequest;
import hiker.arukami.arukamiapp.Models.SpinnerResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


/**
 * A simple {@link Fragment} subclass.
 */
public class HikeFragment extends android.support.v4.app.Fragment {

    private Spinner _difficulty_spinner;
    private Spinner _district_spinner;
    private Spinner _hike_type_spinner;
    private Spinner _quality_spinner;
    private Spinner _price_spinner;
    private TextView _text_hike_name;
    private static String _start_date_hike;
    private static String _end_date_hike;
    private static String _rout_hike;


    public HikeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_hike, container, false);
        _district_spinner = (Spinner) rootView.findViewById(R.id.spinner_district);
        _difficulty_spinner = (Spinner) rootView.findViewById(R.id.spinner_difficulty);
        _hike_type_spinner = (Spinner) rootView.findViewById(R.id.spinner_hike_type);
        _quality_spinner = (Spinner) rootView.findViewById(R.id.spinner_quality);
        _price_spinner = (Spinner) rootView.findViewById(R.id.spinner_price);

        _text_hike_name = (TextView) rootView.findViewById(R.id.hike_name);

        initSpinners();


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

    public HikeRequest getHike(){
        HikeRequest hike = new HikeRequest();
        hike.setName(_text_hike_name.getText().toString());
        hike.setDistrict(((SpinnerResponse) _district_spinner.getSelectedItem()).getValue());
        hike.setDifficulty(((SpinnerResponse) _difficulty_spinner.getSelectedItem()).getValue());
        hike.setHikeType(((SpinnerResponse) _hike_type_spinner.getSelectedItem()).getValue());
        hike.setQualityLevel(((SpinnerResponse) _quality_spinner.getSelectedItem()).getValue());
        hike.setPriceLevel(((SpinnerResponse) _price_spinner.getSelectedItem()).getValue());
        hike.setStartDate(_start_date_hike);
        return hike;
    }

    public void initSpinners(){
        Retrofit retrofit = APIClient.getClient();
        UserAPI apiService = retrofit.create(UserAPI.class);

        callSpinner(apiService.getDistricts(), _district_spinner);
        callSpinner(apiService.getDifficulties(), _difficulty_spinner);
        callSpinner(apiService.getHikeTypes(), _hike_type_spinner);
        callSpinner(apiService.getQualities(), _quality_spinner);
        callSpinner(apiService.getPrices(), _price_spinner);
    }

    public static void setStartDate(String date){
        _start_date_hike = date;
    }

    public static void setEndDate(String date){
        _start_date_hike = date;
    }

}
