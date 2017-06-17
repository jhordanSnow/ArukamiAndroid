package hiker.arukami.arukamiapp.Helpers;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.util.List;

import hiker.arukami.arukamiapp.API.APIClient;
import hiker.arukami.arukamiapp.API.AruKamiAPI;
import hiker.arukami.arukamiapp.Controllers.Activities.HikeDetailsActivity;
import hiker.arukami.arukamiapp.Controllers.Activities.MyHikesActivity;
import hiker.arukami.arukamiapp.Controllers.Activities.ProfileActivity;
import hiker.arukami.arukamiapp.Controllers.Fragments.ProfileFragment;
import hiker.arukami.arukamiapp.Models.BoolModel;
import hiker.arukami.arukamiapp.Models.HikeModel;
import hiker.arukami.arukamiapp.Models.HikeRequest;
import hiker.arukami.arukamiapp.Models.JsonResponse;
import hiker.arukami.arukamiapp.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class HikeAdapter extends RecyclerView.Adapter<HikeAdapter.HikeViewHolder> {

    private static Boolean like = false;
    private static Integer HIKE_ID;
    private static HikeModel hike;

    static class HikeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        CardView cv;
        TextView hikeName;
        TextView hikeDescription;
        RoundedImageView routeImage;
        ImageView detailsButton;
        ImageView likeButton;

        HikeViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cv);
            hikeName = (TextView)itemView.findViewById(R.id.show_hike_name);
            hikeDescription = (TextView)itemView.findViewById(R.id.show_hike_description);
            routeImage = (RoundedImageView)itemView.findViewById(R.id.show_hike_route);
            likeButton= (ImageView) itemView.findViewById(R.id.icon_like);
            detailsButton= (ImageView) itemView.findViewById(R.id.icon_details);
            likeButton.setOnClickListener(this);
            detailsButton.setOnClickListener(this);

        }

        public void setLikeImage(Boolean like){
            if (!like){
                likeButton.setImageResource(R.drawable.ic_heart_black_24dp);
            }else{
                likeButton.setImageResource(R.drawable.ic_heart_outline_black_24dp);
            }
        }

        @Override
        public void onClick(View v) {
            if (v.getId() == likeButton.getId()){
                if (!like){
                    likeButton.setImageResource(R.drawable.ic_heart_outline_black_24dp);
                    like = true;
                }else{
                    likeButton.setImageResource(R.drawable.ic_heart_black_24dp);
                    like = false;
                }
                likeUnlike();
            }else if (v.getId() == detailsButton.getId()){
                openDetails();
            }
        }

        public void openDetails(){
            Intent intent = new Intent(likeButton.getContext(),HikeDetailsActivity.class);
            intent.putExtra("HIKE",hike);
            likeButton.getContext().startActivity(intent);
        }

        public void likeUnlike(){
            Retrofit retrofit = APIClient.getClient();
            AruKamiAPI apiService = retrofit.create(AruKamiAPI.class);
            Call<JsonResponse> result = apiService.likeUnlike(ProfileFragment.getUserId(),HIKE_ID);
            result.enqueue(new Callback<JsonResponse>() {
                @Override
                public void onResponse(Call<JsonResponse> call, Response<JsonResponse> response) {
                    if (response.body() !=  null && !response.body().getResponse().equals("Success")){
                        Toast.makeText(likeButton.getContext(), response.body().getResponse(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<JsonResponse> call, Throwable t) {
                    Log.wtf("failure", t.getMessage());
                }
            });
        }


    }

    private List<HikeModel> hikes;
    private final String BASE_ROUTE_URL = "https://maps.googleapis.com/maps/api/staticmap?size=420x200&key=AIzaSyASEdLL4-lqifDHDZgOjYgUDPKZOoKrKzQ&path=weight:7%7Ccolor:blue%7Cenc:";

    public HikeAdapter(List<HikeModel> hikes){
        this.hikes = hikes;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public HikeViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.my_hike_layout, null);
        HikeViewHolder hodor = new HikeViewHolder(v);
        return hodor;
    }

    @Override
    public void onBindViewHolder(HikeViewHolder hikeViewHolder, int i) {
        hike = hikes.get(i);
        HIKE_ID = hikes.get(i).getIdHike();
        likeCall(hikeViewHolder);
        hikeViewHolder.hikeName.setText(hikes.get(i).getHikeName());
        hikeViewHolder.hikeDescription.setText(hikes.get(i).getDistrict());
        String startMarker  = "&markers=color:blue%7Clabel:S%7C"+hikes.get(i).getStartPoint().getLatitude()+","+hikes.get(i).getStartPoint().getLongitude();
        String endMarker  = "&markers=color:red%7Clabel:E%7C"+hikes.get(i).getEndPoint().getLatitude()+","+hikes.get(i).getEndPoint().getLongitude()+"&";
        String path = BASE_ROUTE_URL + hikes.get(i).getRoute() + startMarker + endMarker;
        Picasso.with(hikeViewHolder.routeImage.getContext()).load(path).into(hikeViewHolder.routeImage);

    }

    public void likeCall(final HikeViewHolder ViewHolder){
        Retrofit retrofit = APIClient.getClient();
        AruKamiAPI apiService = retrofit.create(AruKamiAPI.class);
        Call<BoolModel> result = apiService.isLiking(ProfileFragment.getUserId(), HIKE_ID);
        result.enqueue(new Callback<BoolModel>() {
        @Override
        public void onResponse(Call<BoolModel> call, Response<BoolModel> response) {
            if (response.body() != null && response.body().getResult()){
                ViewHolder.setLikeImage(true);
                like = true;
            }
        }

        @Override
        public void onFailure(Call<BoolModel> call, Throwable t) {
            Log.wtf("failure", t.getMessage());
        }
    });
}

    @Override
    public int getItemCount() {
        return hikes.size();
    }
}
