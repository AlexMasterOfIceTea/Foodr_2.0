package com.cheekibreeki.foodr.adapters;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.cheekibreeki.foodr.MyViewModel;
import com.cheekibreeki.foodr.R;
import com.cheekibreeki.foodr.database.Food;
import com.cheekibreeki.foodr.database.Repository;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

public class MyViewHolder extends RecyclerView.ViewHolder{

    TextView tv_title;
    TextView tv_distance;
    RatingBar ratingBar;
    ToggleButton fav_button;
    LinearLayout dir_container;
    TextView tv_attribution;
    ImageView iv_thumbnail;
    public static final int FOOD_CARD = 0;
    public static final int FAV_FOOD_CARD = 1;
    public Food food;

    MyViewHolder(@NonNull final View itemView, final MyViewModel viewModel, int type) {
        super(itemView);
        switch (type){
            case FOOD_CARD:
                tv_distance = itemView.findViewById(R.id.tv_distance);
                tv_title = itemView.findViewById(R.id.tv_title);
                fav_button = itemView.findViewById(R.id.iv_fav);
                ratingBar = itemView.findViewById(R.id.rating);
                tv_attribution = itemView.findViewById(R.id.tv_attribution);
                iv_thumbnail = itemView.findViewById(R.id.iv_thumbnail);
                dir_container = itemView.findViewById(R.id.dir_container);
                break;
            case FAV_FOOD_CARD:
                tv_title = itemView.findViewById(R.id.tv_fav_title);
                ratingBar = itemView.findViewById(R.id.fav_rating);
                fav_button = itemView.findViewById(R.id.btn_fav_in_fav);
                dir_container = itemView.findViewById(R.id.dir_container_fav);
        }

        fav_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isFav = ((ToggleButton)v).isChecked();
                food.setFav(isFav);
                if(isFav)
                    viewModel.queueInsertToDb(food);
                else
                    viewModel.queueDeleteFromDb(food);
            }
        });
        dir_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://www.google.de/maps/@"+food.getCoordinatesAsString()+",16.06z";
                Uri uri = Uri.parse(url);
                // Create an Intent from gmmIntentUri. Set the action to ACTION_VIEW
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, uri);

                // Make the Intent explicit by setting the Google Maps package
                mapIntent.setPackage("com.google.android.apps.maps");

                // Attempt to start an activity that can handle the Intent
                itemView.getContext().startActivity(mapIntent);
            }
        });
    }
}
