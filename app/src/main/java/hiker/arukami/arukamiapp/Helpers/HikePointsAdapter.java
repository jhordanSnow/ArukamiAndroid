package hiker.arukami.arukamiapp.Helpers;


import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.util.List;

import hiker.arukami.arukamiapp.API.APIClient;
import hiker.arukami.arukamiapp.API.AruKamiAPI;
import hiker.arukami.arukamiapp.Controllers.Fragments.ProfileFragment;
import hiker.arukami.arukamiapp.Models.GetPointsResponse;
import hiker.arukami.arukamiapp.Models.HikeModel;
import hiker.arukami.arukamiapp.Models.PointModel;
import hiker.arukami.arukamiapp.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


/**
 * Created by Jhordan on 17/6/2017.
 */

public class HikePointsAdapter extends RecyclerView.Adapter<HikePointsAdapter.HikePointsViewHolder>  {


    static class HikePointsViewHolder extends RecyclerView.ViewHolder{

        RoundedImageView image;
        HikePointsViewHolder(View itemView) {
            super(itemView);
            image = (RoundedImageView) itemView.findViewById(R.id.iv);
        }
    }

    private List<GetPointsResponse> points;

    public HikePointsAdapter(List<GetPointsResponse> points){
        this.points = points;

    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }


    @Override
    public HikePointsViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.wall_item, null);
        HikePointsViewHolder hodor = new HikePointsViewHolder(v);
        return hodor;
    }

    @Override
    public void onBindViewHolder(final HikePointsViewHolder hikeViewHolder, int i) {
        String Path = APIClient.getURL()+"Photo";
        Retrofit retrofit = APIClient.getClient();
        AruKamiAPI apiService = retrofit.create(AruKamiAPI.class);
        Call<Void> result = apiService.getPhoto(points.get(i).getPhoto());
        result.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Log.wtf("caca","response.raw().request().url();"+response.raw().request().url());
                Picasso.with(hikeViewHolder.image.getContext()).load(response.raw().request().url().toString()).into(hikeViewHolder.image);
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {

            }
        });


    }

    @Override
    public int getItemCount() {
        return points.size();
    }

}