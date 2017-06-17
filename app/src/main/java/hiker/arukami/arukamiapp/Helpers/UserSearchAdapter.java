package hiker.arukami.arukamiapp.Helpers;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import hiker.arukami.arukamiapp.API.APIClient;
import hiker.arukami.arukamiapp.Controllers.Activities.ProfileActivity;
import hiker.arukami.arukamiapp.Models.User;
import hiker.arukami.arukamiapp.R;


/**
 * Created by Jhordan on 17/6/2017.
 */

public class UserSearchAdapter extends ArrayAdapter<User> {


    public UserSearchAdapter(Context context, ArrayList<User> users) {
        super(context,  R.layout.user_item, users);
    }

    static class UserSearchViewHolder{

        TextView fullName;
        TextView username;
        RelativeLayout layout;
        CircleImageView profilePicture;
        Integer USER_ID;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        User user = getItem(position);
        final UserSearchViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new UserSearchViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.user_item, parent, false);
            viewHolder.fullName = (TextView) convertView.findViewById(R.id.full_name_textview);
            viewHolder.username = (TextView) convertView.findViewById(R.id.username_textview);
            viewHolder.layout = (RelativeLayout) convertView.findViewById(R.id.search_user_list_view);
            viewHolder.profilePicture = (CircleImageView) convertView.findViewById(R.id.profile_icon);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (UserSearchViewHolder) convertView.getTag();
        }

        viewHolder.username.setText(user.getUsername());
        viewHolder.fullName.setText(user.toString());
        viewHolder.USER_ID =(int) user.getIdCard();
        if (user.getPhoto() != null){
            String photoURL = APIClient.getURL();
            Picasso.Builder builder = new Picasso.Builder(getContext());
            builder.listener(new Picasso.Listener()
            {
                @Override
                public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception)
                {
                    if( viewHolder.profilePicture != null) {
                        ((BitmapDrawable) viewHolder.profilePicture.getDrawable()).getBitmap().recycle();
                    }else {
                        viewHolder.profilePicture.setImageResource(R.drawable.profile_default);
                    }
                }
            });

            photoURL +="User/"+(int) user.getIdCard()+"/Photo";
            builder.build().load(photoURL).into(viewHolder.profilePicture);
        }


        viewHolder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getContext(), viewHolder.USER_ID.toString(), Toast.LENGTH_SHORT).show();
                openProfile(viewHolder.USER_ID);
            }
        });

        return convertView;
    }

    public void openProfile(Integer userId){
        Intent intent = new Intent(getContext(),ProfileActivity.class);
        intent.putExtra("USER_ID",userId);
        getContext().startActivity(intent);
    }

}