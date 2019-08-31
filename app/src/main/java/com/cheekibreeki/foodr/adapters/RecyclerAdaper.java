package com.cheekibreeki.foodr.adapters;

import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;

import com.cheekibreeki.foodr.MyViewModel;
import com.cheekibreeki.foodr.R;
import com.cheekibreeki.foodr.database.Food;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class RecyclerAdaper extends RecyclerView.Adapter<MyViewHolder> {

    private List<Food> foods;
    private MyViewModel viewModel;

    public RecyclerAdaper(@NonNull List<Food> foods, MyViewModel viewModel){
        this.foods = foods;
        this.viewModel = viewModel;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fav_food_card, parent, false);
        return new MyViewHolder(view, viewModel, MyViewHolder.FAV_FOOD_CARD);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {
        Food food = foods.get(position);

        holder.food = food;
        //holder.tv_distance.setText(food.getDist()+"m");
        holder.tv_title.setText(food.getName());
        holder.ratingBar.setRating(food.getRating());
        holder.fav_button.setChecked(food.isFav());

        /*
        food.loadBitmap(new Food.OnLoadingDone() {
            @Override
            public void onLoadingDone(Bitmap bm) {
                holder.iv_thumbnail.setImageBitmap(bm);
            }
        });
        */
    }

    @Override
    public int getItemCount() {
        return foods.size();
    }
}
