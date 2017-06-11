package hiker.arukami.arukamiapp.Helpers;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.List;

import hiker.arukami.arukamiapp.Models.SpinnerResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Carlos on 6/10/2017.
 */

public final class FillSpinner {

    private FillSpinner(){

    }

    public static void callSpinner(Call<List<SpinnerResponse>> result, final Spinner spinnerTarget){
        result.enqueue(new Callback<List<SpinnerResponse>>() {
            @Override
            public void onResponse(Call<List<SpinnerResponse>> call, Response<List<SpinnerResponse>> response) {
                fillSpinner(spinnerTarget.getContext(),spinnerTarget, response.body());
            }

            @Override
            public void onFailure(Call<List<SpinnerResponse>> call, Throwable t) {

            }
        });
    }

    public static void fillSpinner(Context context, Spinner spinnerTarget, List<SpinnerResponse> items){
        ArrayAdapter<SpinnerResponse> dataAdapter = new ArrayAdapter<SpinnerResponse>(context,
                android.R.layout.simple_spinner_item, items);

        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTarget.setAdapter(dataAdapter);
    }
}
