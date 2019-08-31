package com.cheekibreeki.foodr.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.cheekibreeki.foodr.MyViewModel;
import com.cheekibreeki.foodr.R;
import com.cheekibreeki.foodr.database.Food;
import com.cheekibreeki.foodr.database.Repository;
import com.cheekibreeki.foodr.fragments.ExploreFragment;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class SwipeAdapter extends BaseAdapter {

    private List<Food> foods;
    private LayoutInflater mInflater;
    private MyViewModel viewModel;

    public SwipeAdapter(Context context, MyViewModel model){
        mInflater = LayoutInflater.from(context);
        viewModel = model;
        this.foods = new ArrayList<Food>();
    }

    public List<Food> getFoods(){
        return foods;
    }

    @Override
    public int getCount() {
        return foods.size();
    }

    @Override
    public Food getItem(int position) {
        return foods.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null)
            convertView = mInflater.inflate(R.layout.food_card, parent, false);
        final MyViewHolder holder = new MyViewHolder(convertView, viewModel, MyViewHolder.FOOD_CARD);

        Food food = foods.get(position);

        holder.food = food;
        holder.tv_distance.setText(food.getDist()+"m");
        holder.tv_title.setText(food.getName());
        holder.ratingBar.setRating(food.getRating());
        holder.fav_button.setChecked(food.isFav());

        food.loadBitmap(new Food.OnLoadingDone() {
            @Override
            public void onLoadingDone(Bitmap bm) {
                holder.iv_thumbnail.setImageBitmap(bm);
            }
        });
        return convertView;
    }

    public void setDataset(List<Food> list) {
        foods = list;
        notifyDataSetChanged();
    }

    public void removeFirstObjectInAdapter() {
        foods.remove(0);
        notifyDataSetChanged();
    }

}
