package hiker.arukami.arukamiapp.Helpers;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import hiker.arukami.arukamiapp.API.APIClient;
import hiker.arukami.arukamiapp.API.AruKamiAPI;
import hiker.arukami.arukamiapp.Controllers.Activities.HikeDetailsActivity;
import hiker.arukami.arukamiapp.Controllers.Activities.MainActivity;
import hiker.arukami.arukamiapp.Controllers.Activities.MyHikesActivity;
import hiker.arukami.arukamiapp.Controllers.Activities.ProfileActivity;
import hiker.arukami.arukamiapp.Controllers.Fragments.LikesFragment;
import hiker.arukami.arukamiapp.Controllers.Fragments.MainHikeFragment;
import hiker.arukami.arukamiapp.Controllers.Fragments.ProfileFragment;
import hiker.arukami.arukamiapp.Controllers.Fragments.WallFragment;
import hiker.arukami.arukamiapp.Models.BoolModel;
import hiker.arukami.arukamiapp.Models.HikeModel;
import hiker.arukami.arukamiapp.Models.JsonResponse;
import hiker.arukami.arukamiapp.Models.User;
import hiker.arukami.arukamiapp.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static android.R.attr.fragment;

public class WallAdapter extends RecyclerView.Adapter<WallAdapter.HikeViewHolder> {

    private static Integer USER_ID;
    private static Boolean like ;
    private static Integer HIKE_ID;
    private static HikeModel hike;

    static class HikeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        CardView cv;
        TextView hikeName;
        TextView hikeDescription;
        TextView username;
        RoundedImageView routeImage;
        ImageView likeButton;
        ImageView detailsButton;
        CircleImageView profileIcon;




        HikeViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cv);
            hikeName = (TextView)itemView.findViewById(R.id.show_hike_name);
            hikeDescription = (TextView)itemView.findViewById(R.id.show_hike_description);
            routeImage = (RoundedImageView)itemView.findViewById(R.id.show_hike_route);
            likeButton= (ImageView) itemView.findViewById(R.id.icon_like);
            detailsButton= (ImageView) itemView.findViewById(R.id.icon_details);
            username = (TextView)itemView.findViewById(R.id.username_textview);
            profileIcon = (CircleImageView)itemView.findViewById(R.id.profile_icon);
            likeButton.setOnClickListener(this);
            username.setOnClickListener(this);
            profileIcon.setOnClickListener(this);
            detailsButton.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (v.getId() == likeButton.getId()) {
                if (!like) {
                    likeButton.setImageResource(R.drawable.ic_heart_outline_black_24dp);
                    like = true;
                } else {
                    likeButton.setImageResource(R.drawable.ic_heart_black_24dp);
                    like = false;
                }
                likeUnlike();
            } else if (v.getId() == username.getId() || v.getId() == profileIcon.getId()) {
                Log.wtf("caca", "caca");
                openProfile();
            } else if (v.getId() == detailsButton.getId()){
                openDetails();
            }
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

        public void openProfile(){

            Intent intent = new Intent(likeButton.getContext(),ProfileActivity.class);
            intent.putExtra("USER_ID",USER_ID);
            Log.wtf("USER_ID",USER_ID.toString());
            likeButton.getContext().startActivity(intent);
        }

        public void openDetails(){
            Intent intent = new Intent(likeButton.getContext(),HikeDetailsActivity.class);
            intent.putExtra("HIKE",hike);
            likeButton.getContext().startActivity(intent);
        }


    }

    private List<HikeModel> hikes;
    private final String BASE_ROUTE_URL = "https://maps.googleapis.com/maps/api/staticmap?size=420x200&key=AIzaSyASEdLL4-lqifDHDZgOjYgUDPKZOoKrKzQ&path=weight:7%7Ccolor:blue%7Cenc:";

    public WallAdapter(List<HikeModel> hikes){
        this.hikes = hikes;

    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }


    @Override
    public HikeViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.wall_item, null);
        HikeViewHolder hodor = new HikeViewHolder(v);
        return hodor;
    }

    @Override
    public void onBindViewHolder(final HikeViewHolder hikeViewHolder, int i) {
        hike = hikes.get(i);
        HIKE_ID = hikes.get(i).getIdHike();
        hikeViewHolder.hikeName.setText(hikes.get(i).getHikeName());
        hikeViewHolder.hikeDescription.setText(hikes.get(i).getDistrict());
        hikeViewHolder.username.setText(hikes.get(i).getCreator());
        USER_ID =(int) hikes.get(i).getIdCreator();
        String photoURL = APIClient.getURL();
        Picasso.Builder builder = new Picasso.Builder(hikeViewHolder.routeImage.getContext());
        builder.listener(new Picasso.Listener()
        {
            @Override
            public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception)
            {
                hikeViewHolder.profileIcon.setImageResource(R.drawable.profile_default);
            }
        });
        like = hikes.get(i).getLike();
        Log.wtf("hike"+HIKE_ID,like.toString());
        if (like){
            hikeViewHolder.likeButton.setImageResource(R.drawable.ic_heart_black_24dp);
        }else{
            hikeViewHolder.likeButton.setImageResource(R.drawable.ic_heart_outline_black_24dp);
        }
        photoURL +="User/"+(int) hikes.get(i).getIdCreator()+"/Photo";
        builder.build().load(photoURL).into(hikeViewHolder.profileIcon);
        String startMarker  = "&markers=color:blue%7Clabel:S%7C"+hikes.get(i).getStartPoint().getLatitude()+","+hikes.get(i).getStartPoint().getLongitude();
        String endMarker  = "&markers=color:red%7Clabel:E%7C"+hikes.get(i).getEndPoint().getLatitude()+","+hikes.get(i).getEndPoint().getLongitude()+"&";
        String path = BASE_ROUTE_URL + hikes.get(i).getRoute() + startMarker + endMarker;
        Picasso.with(hikeViewHolder.routeImage.getContext()).load(path).into(hikeViewHolder.routeImage);
    }



    @Override
    public int getItemCount() {
        return hikes.size();
    }

}
